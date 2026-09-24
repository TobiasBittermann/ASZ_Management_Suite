package de.tobi.asz_management_suite_api.Login.credential;

import de.tobi.asz_management_suite_api.member.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialRepository repository;
    private static final Logger log = LoggerFactory.getLogger(CredentialService.class);

    public CredentialService(CredentialRepository repository) {
        this.repository = repository;
    }

    public Credential getCredentialById(byte[] id) {
        return repository.findByCredentialId(id).orElseThrow();
    }

    public List<Credential> getCredentialByMember(Member member) {
        return repository.findByMember(member);
    }

    public void addCredential(Credential credential) {
        repository.save(credential);
        log.info("CredentialService added credential with id {}", credential.getId());
    }

    public void updateCredential(long id, Credential credential) {
        credential.setId(id);
        repository.save(credential);
        log.info("CredentialService updated credential with id {}", credential.getId());
    }

    public void deleteCredential(long id) {
        repository.deleteById(id);
        log.info("CredentialService deleted credential with id {}", id);
    }
}
