package de.tobi.asz_management_suite_api.Login.credential;

import de.tobi.asz_management_suite_api.member.MemberService;
import com.yubico.webauthn.CredentialRepository;
import org.springframework.stereotype.Component;

@Component
public class WebAuthnCredentialRepository implements CredentialRepository {
    private final CredentialService credentialService;
    private final MemberService memberService;

    public WebAuthnCredentialRepository(CredentialService credentialService, MemberService memberService) {
        this.credentialService = credentialService;
        this.memberService = memberService;
    }
}
