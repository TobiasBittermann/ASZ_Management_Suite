package de.tobi.asz_management_suite_api.Login.registrationInvite;

import de.tobi.asz_management_suite_api.member.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RegistrationInviteService {
    private final RegistrationInviteRepository repository;
    private final static Logger log = LoggerFactory.getLogger(RegistrationInviteService.class);
    private final static SecureRandom RANDOM = new SecureRandom();

    public RegistrationInviteService(RegistrationInviteRepository repository) {
        this.repository = repository;
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public RegistrationInvite createInvite(Member member) {
        RegistrationInvite invite = new RegistrationInvite();
        invite.setMember(member);
        invite.setToken(generateToken());
        invite.setExpiresAt(LocalDateTime.now().plusHours(24));
        invite.setUsed(false);
        repository.save(invite);

        log.info("RegistrationInviteService created invite with id {} for member {} {}.",
                invite.getId(), member.getFirstName(), member.getLastName());
        return invite;
    }

    @Transactional
    public RegistrationInvite redeemInvite(String token) {
        RegistrationInvite invite = repository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid invite token"));
        if(invite.isUsed()){
            throw new IllegalStateException("Invite already used");
        }
        if(invite.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Invite expired");
        }
        invite.setUsed(true);
        repository.save(invite);
        log.info("RegistrationInviteService redeemed invite with id {}.", invite.getId());
        return invite;
    }
}
