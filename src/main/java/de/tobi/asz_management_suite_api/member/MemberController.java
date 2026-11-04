package de.tobi.asz_management_suite_api.member;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<Member> getAllMembers() throws IOException {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/me")
    public CurrentMemberResponse getCurrentMember(Authentication authentication) {
        Member member = memberService.findMemberByEmail(authentication.getName()).orElseThrow();
        return new CurrentMemberResponse(member.getFirstName(), member.getLastName());
    }

    @PostMapping("/members")
    public void addMember(@RequestBody Member member) throws IOException {
        memberService.addMember(member);
    }

    @PutMapping("/members/{id}")
    public void updateMember(@PathVariable long id, @RequestBody Member member) throws IOException {
        memberService.updateMember(id, member);
    }

    @DeleteMapping("/members/{id}")
    public void deleteMember(@PathVariable long id) throws IOException {
        memberService.deleteMember(id);
    }

    public record CurrentMemberResponse(String firstName, String lastName) {
    }
}
