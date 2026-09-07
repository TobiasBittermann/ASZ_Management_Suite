package de.tobi.asz_inventory_api.enums;

public enum Status {
    GUEST("Hausgast"),
    PROVISIONAL_MEMBER("Vorläufig aufgenommen"),
    ACTIVE_MEMBER("Bundesbruder"),
    ALUMNUS ("Alter Herr"),
    RESIGNED("Ausgetreten"),
    EXPELLED("Ausgeschlossen"),
    HONORARY_LADY("Dame der Verbindung");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
