package de.tobi.asz_management_suite_api.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserHandle(byte[] userHandle);
}
