package de.tobi.asz_management_suite_api.Login.registrationInvite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationInviteRepository extends JpaRepository<RegistrationInvite, Long> {
    Optional<RegistrationInvite> findByToken(String token);
}
