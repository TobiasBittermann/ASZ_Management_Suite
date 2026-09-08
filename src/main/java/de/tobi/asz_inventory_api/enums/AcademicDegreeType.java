package de.tobi.asz_inventory_api.enums;

public enum AcademicDegreeType {
    STUDENT("Student", "Student"),
    BSC("B.Sc.", "Bachelor of Science"),
    MSC("M.Sc.", "Master of Science"),
    BA("B.A.", "Bachelor of Arts"),
    MA("M.A.", "Master of Arts"),
    LLB("LL.B", "Bachelor of Laws"),
    LLM("LL.M", "Master of Laws"),
    BENG("B.ENG", "Bachelor of Engineering"),
    MENG("M.ENG", "Master of Engineering"),
    DIPL_ING("Dipl.-Ing.", "Diplom-Ingenieur"),
    DIPL_KFM("Dipl.-Kfm.", "Diplom-Kaufmann"),
    STAATSEXAMEN("Staatsexamen", "Staatsexamen"),
    DR("Dr.", "Doktor");

    private final String abbreviation;
    private final String fullName;

    AcademicDegreeType(String abbreviation, String fullName) {
        this.abbreviation = abbreviation;
        this.fullName = fullName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getFullName() {
        return fullName;
    }
}