package de.tobi.asz_management_suite_api.Login.webAuthn;

import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import org.hibernate.AssertionFailure;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    @ExceptionHandler(AssertionFailedException.class)
    public ResponseEntity<Void> handleAssertionFailed(AssertionFailedException exception){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
