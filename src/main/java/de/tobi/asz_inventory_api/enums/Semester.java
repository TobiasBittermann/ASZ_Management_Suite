package de.tobi.asz_inventory_api.enums;

public enum Semester {
    WINTERSEMESTER("Wintersemester"),
    SUMMERSEMESTER("Sommersemester");

    private final String displayName;

    Semester(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
