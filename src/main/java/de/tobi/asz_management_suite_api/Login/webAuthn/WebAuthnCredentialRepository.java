package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.ByteArray;
import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import de.tobi.asz_management_suite_api.Login.credential.Credential;
import de.tobi.asz_management_suite_api.Login.credential.CredentialService;
import de.tobi.asz_management_suite_api.member.Member;
import de.tobi.asz_management_suite_api.member.MemberService;
import com.yubico.webauthn.CredentialRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebAuthnCredentialRepository implements CredentialRepository {
    private final CredentialService credentialService;
    private final MemberService memberService;

    public WebAuthnCredentialRepository(CredentialService credentialService, MemberService memberService) {
        this.credentialService = credentialService;
        this.memberService = memberService;
    }

    @Override
    public Optional<ByteArray> getUserHandleForUsername(String username) {
        try {
            Member member = memberService.getMemberByEmail(username);
            return Optional.of(new ByteArray(member.getUserHandle()));
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
        try {
            Member member = memberService.getMemberByUserHandle(userHandle.getBytes());
            return Optional.of(member.getEmail());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username) {
        try {
            Member member = memberService.getMemberByEmail(username);

            return credentialService.getCredentialByMember(member)
                    .stream()
                    .map(credential -> PublicKeyCredentialDescriptor
                            .builder()
                            .id(new ByteArray(credential.getCredentialId()))
                            .build())
                    .collect(Collectors.toSet());
        } catch (NoSuchElementException e) {
            return Set.of();
        }
    }

    @Override
    public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
        try {
            Credential credential = credentialService.getCredentialById(credentialId.getBytes());
            if (!Arrays.equals(credential.getMember().getUserHandle(), userHandle.getBytes())) {
                return Optional.empty();
            }
            return Optional.of(RegisteredCredential.builder()
                    .credentialId(new ByteArray(credential.getCredentialId()))
                    .userHandle(new ByteArray(credential.getMember().getUserHandle()))
                    .publicKeyCose(new ByteArray(credential.getPublicKey()))
                    .signatureCount(credential.getSignatureCount())
                    .build());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
        try {
            Credential credential = credentialService.getCredentialById(credentialId.getBytes());
            return Set.of(RegisteredCredential.builder()
                    .credentialId(new ByteArray(credential.getCredentialId()))
                    .userHandle(new ByteArray(credential.getMember().getUserHandle()))
                    .publicKeyCose(new ByteArray(credential.getPublicKey()))
                    .signatureCount(credential.getSignatureCount())
                    .build());
        } catch (NoSuchElementException e) {
            return Set.of();
        }
    }
}

