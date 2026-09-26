package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class WebAuthnConfig {
    @Bean
    public RelyingPartyIdentity relyingPartyIdentity() {
        return RelyingPartyIdentity.builder()
                .id("localhost")
                .name("ASZ Management Suite")
                .build();
    }

    @Bean
    public RelyingParty relyingParty(RelyingPartyIdentity relyingPartyIdentity, WebAuthnCredentialRepository webAuthnCredentialRepository){
        return RelyingParty.builder()
                .identity(relyingPartyIdentity)
                .credentialRepository(webAuthnCredentialRepository)
                .origins(Set.of("http://localhost:5173"))
                .build();
    }
}
