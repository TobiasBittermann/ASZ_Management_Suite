package de.tobi.asz_management_suite_api.enums;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class EnumController {
    public record EnumOption(String value, String label) {
    }

    @GetMapping("/enums/status")
    public List<EnumOption> getStatusValue() {
        return Arrays.stream(Status.values())
                .map(s -> new EnumOption(s.name(), s.getDisplayName()))
                .toList();
    }
}
