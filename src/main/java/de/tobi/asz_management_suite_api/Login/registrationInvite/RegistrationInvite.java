package de.tobi.asz_management_suite_api.Login.registrationInvite;

import de.tobi.asz_management_suite_api.member.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class RegistrationInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Column(unique = true)
    private String token;
    private LocalDateTime expiresAt;
    private boolean used;

    public RegistrationInvite(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void UpdateFrom(RegistrationInvite invite){
        this.member = invite.member;
        this.token = invite.token;
        this.expiresAt = invite.expiresAt;
        this.used = invite.used;
    }
}
