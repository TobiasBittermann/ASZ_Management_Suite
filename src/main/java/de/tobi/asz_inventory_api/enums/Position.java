package de.tobi.asz_inventory_api.enums;

public enum Position {
    CHAIRMAN("Senior"),
    PLEDGE_MASTER("Fuxmajor"),
    SECRETARY("Schriftwart"),
    TREASURER("Kassenwart"),
    BEER_STEWARD("Bierwart"),
    HOUSE_MANAGER("Hauswart"),
    CABIN_MANAGER("Hüttenwart");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
