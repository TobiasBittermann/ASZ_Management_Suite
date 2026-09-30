package de.tobi.asz_management_suite_api.enums;

public enum Status {
    GUEST("Hausgast"),
    PROVISIONAL_MEMBER("Vorläufig aufgenommen"),
    ACTIVE_MEMBER("Bundesbruder"),
    ACTIVE_FLOATING_MEMBER("Bundesbruder in der Schwebe"),
    NON_RESIDENT_ACTIVE_MEMBER("Auswärtiger Aktiver"),
    ALUMNUS ("Alter Herr"),
    RESIGNED("Ausgetreten"),
    EXPELLED("Ausgeschlossen"),
    HONORARY_LADY("Dame der Verbindung"),
    ASSOCIATE_MEMBER("Konkneipant");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
