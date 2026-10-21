package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import de.tobi.asz_management_suite_api.Login.credential.Credential;
import de.tobi.asz_management_suite_api.Login.credential.CredentialService;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInvite;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInviteService;
import de.tobi.asz_management_suite_api.member.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/webauthn")
public class WebAuthnController {
    private final RelyingParty relyingParty;
    private final RegistrationInviteService inviteService;
    private final CredentialService credentialService;
    private final Map<String, PublicKeyCredentialCreationOptions> pendingRegistrations = new ConcurrentHashMap<>();
    private final Map<String, AssertionRequest> pendingAssertions = new ConcurrentHashMap<>();
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public WebAuthnController(RelyingParty relyingParty, RegistrationInviteService inviteService, CredentialService credentialService) {
        this.relyingParty = relyingParty;
        this.inviteService = inviteService;
        this.credentialService = credentialService;
    }

    @PostMapping("/register/start")
    public PublicKeyCredentialCreationOptions startRegistration(@RequestBody StartRegistrationRequest request) {
        RegistrationInvite invite = inviteService.validateInvite(request.token());
        Member member = invite.getMember();

        UserIdentity userIdentity = UserIdentity.builder()
                .name(member.getEmail())
                .displayName(String.format("%s %s", member.getFirstName(), member.getLastName()))
                .id(new ByteArray(member.getUserHandle()))
                .build();

        PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(
                StartRegistrationOptions.builder()
                        .user(userIdentity)
                        .build()
        );

        pendingRegistrations.put(request.token(), options);
        return options;
    }

    @PostMapping("/register/finish")
    public void finishRegistration(@RequestBody FinishRegistrationRequest request) throws RegistrationFailedException, IOException {
        PublicKeyCredentialCreationOptions options = pendingRegistrations.remove(request.token());

        if (options == null) {
            throw new IllegalStateException("No pending registration found");
        }

        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> response;
        response = PublicKeyCredential.parseRegistrationResponseJson(request.response());
        RegistrationResult result = relyingParty.finishRegistration(FinishRegistrationOptions.builder().request(options).response(response).build());

        Member member = inviteService.validateInvite(request.token()).getMember();
        Credential credential = new Credential();

        credential.setCredentialId(result.getKeyId().getId().getBytes());
        credential.setPublicKey(result.getPublicKeyCose().getBytes());
        credential.setSignatureCount(result.getSignatureCount());
        credential.setMember(member);
        credential.setCreatedAt(LocalDateTime.now());

        credentialService.addCredential(credential);
        inviteService.redeemInvite(request.token());
    }

    @PostMapping("/login/start")
    public LoginStartResponse startLogin() throws JsonProcessingException {
        AssertionRequest request = relyingParty.startAssertion(StartAssertionOptions.builder().userVerification(UserVerificationRequirement.REQUIRED).build());

        String requestId = UUID.randomUUID().toString();
        pendingAssertions.put(requestId, request);

        return new LoginStartResponse(requestId, request.toCredentialsGetJson());
    }

    @PostMapping("/login/finish")
    public String finishLogin(@RequestBody LoginFinishRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, AssertionFailedException{
        AssertionRequest assertionRequest = pendingAssertions.remove(request.requestId());

        if(assertionRequest == null){
            throw new IllegalStateException("No pending login found");
        }

        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> response = PublicKeyCredential.parseAssertionResponseJson(request.response());

        AssertionResult result = relyingParty.finishAssertion(FinishAssertionOptions.builder().request(assertionRequest).response(response).build());

        if(!result.isSuccess()){
            throw new IllegalStateException("Login failed");
        }

        Credential credential = credentialService.getCredentialById(result.getCredential().getCredentialId().getBytes());

        if (credential.getRevokedAt() != null){
            throw new IllegalStateException("Credential revoked");
        }

        credential.setSignatureCount(result.getSignatureCount());
        credential.setLastUsedAt(LocalDateTime.now());
        credentialService.updateCredential(credential.getId(), credential);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(result.getUsername(), null, List.of());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        if(httpRequest.getSession(false) != null){
            httpRequest.changeSessionId();
        }
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        return result.getUsername();
    }

    public record StartRegistrationRequest(String token) {
    }

    public record FinishRegistrationRequest(String token, String response) {
    }

    public record LoginStartResponse(String requestId, @JsonRawValue String options) {
    }

    public record LoginFinishRequest (String requestId, String response){

    }
}
