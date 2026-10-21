package de.tobi.asz_management_suite_api.member;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository repository;
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public Member getMemberById(long id) {
        return repository.findById(id).orElseThrow();
    }

    public Member getMemberByUserHandle(byte[] userHandle){return repository.findByUserHandle(userHandle).orElseThrow();}

    public Member getMemberByEmail(String email){return repository.findByEmail(email).orElseThrow();}

    public List<Member> getAllMembers() {
        List<Member> members = repository.findAll();
        log.debug("MemberService loaded {} members.", members.size());
        return members;
    }

    public void addMember(Member member) {
        byte[] userHandle = new byte[32];
        RANDOM.nextBytes(userHandle);
        member.setUserHandle(userHandle);

        repository.save(member);
        log.info("MemberService added member {} {} with id {}.", member.getFirstName(), member.getLastName(), member.getId());
    }

    public void updateMember(long id, Member member) {
        Member existing = repository.findById(id).orElseThrow();
        member.setId(id);
        member.setUserHandle(existing.getUserHandle());
        repository.save(member);
        log.info("MemberService updated member {} {} with id {}.", member.getFirstName(), member.getLastName(), member.getId());
    }

    public void deleteMember(long id) {
        Member member = repository.findById(id).orElseThrow();
        repository.deleteById(id);
        log.info("MemberService deleted member {} {} with id {}.", member.getFirstName(), member.getLastName(), id);
    }

    public Optional<Member> findMemberByEmail(String email){
        return repository.findByEmail(email);
    }
}
