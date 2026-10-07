package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.exception.RegistrationFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class WebAuthnExceptionHandler {

    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<Void> handleRegistrationFailed(RegistrationFailedException exception){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleIOException(IOException exception){
        return ResponseEntity.badRequest().build();
    }
}
