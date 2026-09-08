package de.tobi.asz_inventory_api.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository repository;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member getMemberById(long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Member> getAllMembers() {
        List<Member> members = repository.findAll();
        log.debug("MemberService loaded {} members.", members.size());
        return members;
    }

    public void addMember(Member member) {
        repository.save(member);
        log.info("MemberService added member {} {} with id {}.", member.getFirstName(), member.getLastName(), member.getId());
    }

    public void updateMember(long id, Member member) {
        member.setId(id);
        repository.save(member);
        log.info("MemberService updated member {} {} with id {}.", member.getFirstName(), member.getLastName(), member.getId());
    }

    public void deleteMember(long id) {
        Member member = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        log.info("MemberService deleted member {} {} with id {}.", member.getFirstName(), member.getLastName(), id);
    }
}
