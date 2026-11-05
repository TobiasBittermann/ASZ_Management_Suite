package de.tobi.asz_management_suite_api.enums;

public enum Status {
    OTHER_GUEST("Gast"),
    GUEST("Hausgast"),
    PROVISIONAL_MEMBER("Vorläufig aufgenommen"),
    ACTIVE_MEMBER("Bundesbruder"),
    ALUMNUS ("Alter Herr"),
    RESIGNED("Ausgetreten"),
    EXPELLED("Ausgeschlossen"),
    HONORARY_LADY("Dame der Verbindung"),
    ELSE("Sonstiges");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
