package de.tobi.asz_management_suite_api.Login.admin;

import de.tobi.asz_management_suite_api.Login.credential.CredentialService;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInvite;
import de.tobi.asz_management_suite_api.Login.registrationInvite.RegistrationInviteService;
import de.tobi.asz_management_suite_api.member.Member;
import de.tobi.asz_management_suite_api.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);

    private final CredentialService credentialService;
    private final MemberService memberService;
    private final RegistrationInviteService inviteService;

    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.first-name}")
    private String adminFirstName;
    @Value("${app.admin.last-name}")
    private String adminLastName;
    @Value("${app.frontend.origin}")
    private String frontendOrigin;

    public AdminBootstrap(CredentialService credentialService, MemberService memberService, RegistrationInviteService inviteService) {
        this.credentialService = credentialService;
        this.memberService = memberService;
        this.inviteService = inviteService;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (credentialService.hasAnyCredential()) {
            return;
        }

        Member admin = memberService.findMemberByEmail(adminEmail).orElseGet(() -> {
            Member member = new Member();
            member.setEmail(adminEmail);
            member.setFirstName(adminFirstName);
            member.setLastName(adminLastName);
            memberService.addMember(member);
            return member;
        });

        RegistrationInvite invite = inviteService.createInvite(admin);
        log.warn("First-Invite. No passkey existing yet. Register under {}/register?token={}",frontendOrigin, invite.getToken());
    }
}
