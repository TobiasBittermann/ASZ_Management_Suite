package de.tobi.asz_inventory_api.credential;

import de.tobi.asz_inventory_api.member.Member;

import java.time.LocalDateTime;

public class Credential {
    private long id;
    private byte[] credentialId;
    private byte[] publicKey;
    private long signatureCount;
    private Member member;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;
    private LocalDateTime revokedAt;

    public Credential() {
    }

    public Credential(Credential other) {
        this.id = other.id;
        this.credentialId = other.credentialId;
        this.publicKey = other.publicKey;
        this.signatureCount = other.signatureCount;
        this.member = other.member;
        this.createdAt = other.createdAt;
        this.lastUsedAt = other.lastUsedAt;
        this.revokedAt = other.revokedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(byte[] credentialId) {
        this.credentialId = credentialId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public long getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(long signatureCount) {
        this.signatureCount = signatureCount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(LocalDateTime lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public void updateFrom(Credential credential){
        this.credentialId = credential.credentialId;
        this.publicKey = credential.publicKey;
        this.signatureCount = credential.signatureCount;
        this.member = credential.member;
        this.createdAt = credential.createdAt;
        this.lastUsedAt = credential.lastUsedAt;
        this.revokedAt = credential.revokedAt;
    }
}
