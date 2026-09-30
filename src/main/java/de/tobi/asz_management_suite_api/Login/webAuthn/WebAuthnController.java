package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.RegistrationFailedException;
import de.tobi.asz_management_suite_api.Login.credential.CredentialService;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInvite;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInviteService;
import de.tobi.asz_management_suite_api.member.Member;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class WebAuthnController {
    private final RelyingParty relyingParty;
    private final RegistrationInviteService inviteService;
    private final CredentialService credentialService;
    private final Map<String, PublicKeyCredentialCreationOptions> pendingRegistrations = new ConcurrentHashMap<>();

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
    public void finishRegistration(@RequestBody FinishRegistrationRequest request) throws RegistrationFailedException {
        PublicKeyCredentialCreationOptions options = pendingRegistrations.get(request.token());

        if(options == null){
            throw new IllegalStateException("No pending registration found");
        }

        PublicKeyCredential<AuthenticatorAttestationResponse,ClientRegistrationExtensionOutputs> response;

        try{
            response = PublicKeyCredential.parseRegistrationResponseJson(request.response());
        }
        catch (Exception e){
            throw new IllegalArgumentException("Invalid registration response", e);
        }

        RegistrationResult result = relyingParty.finishRegistration(FinishRegistrationOptions.builder().request(options).response(response).build());
    }

    public record StartRegistrationRequest(String token) {
    }

    public record FinishRegistrationRequest(String token, String response){}
}
