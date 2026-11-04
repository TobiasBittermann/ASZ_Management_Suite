package de.tobi.asz_management_suite_api.Login.registrationInvite;

import de.tobi.asz_management_suite_api.member.Member;
import de.tobi.asz_management_suite_api.member.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationInviteController {
    private final RegistrationInviteService inviteService;
    private final MemberService memberService;
    private final String frontendOrigin;

    public RegistrationInviteController(RegistrationInviteService inviteService,
                                        MemberService memberService,
                                        @Value("${app.frontend.origin}") String frontendOrigin) {
        this.inviteService = inviteService;
        this.memberService = memberService;
        this.frontendOrigin = frontendOrigin;
    }

    @PostMapping("/members/{memberId}/invite")
    public InviteResponse createInvite(@PathVariable long memberId) {
        Member member = memberService.getMemberById(memberId);
        RegistrationInvite invite = inviteService.createInvite(member);
        return new InviteResponse(frontendOrigin + "/register?token=" + invite.getToken());
    }

    public record InviteResponse(String link) {
    }


}
