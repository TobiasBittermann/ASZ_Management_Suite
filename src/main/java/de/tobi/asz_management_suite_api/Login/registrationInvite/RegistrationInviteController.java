package de.tobi.asz_management_suite_api.Login.registrationInvite;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationInviteController {
    private final RegistrationInviteService inviteService;

    public RegistrationInviteController(RegistrationInviteService inviteService){
        this.inviteService = inviteService;
    }


}
