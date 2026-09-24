package de.tobi.asz_management_suite_api.Login.credential;

import de.tobi.asz_management_suite_api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByCredentialId(byte[] credentialId);
    List<Credential> findByMember(Member member);
}
