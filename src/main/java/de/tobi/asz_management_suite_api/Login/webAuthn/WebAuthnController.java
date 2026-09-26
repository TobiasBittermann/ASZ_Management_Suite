package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.UserIdentity;
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
    private final Map<String, PublicKeyCredentialCreationOptions> pendingRegistrations = new ConcurrentHashMap<>();

    public WebAuthnController(RelyingParty relyingParty, RegistrationInviteService inviteService) {
        this.relyingParty = relyingParty;
        this.inviteService = inviteService;
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

    public record StartRegistrationRequest(String token) {
    }
}
