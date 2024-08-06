package com.project.domain.enums;

public enum RoleType {
    ADMIN("Admin"),
    TALASLI_IMALAT_AMIRI("Talasli imalat amiri"),
    POLISAJ_AMIRI("Polisaj amiri"),
    LIFT_MONTAJ_AMIRI("Lift montaj amiri"),
    BL_MONTAJ_AMIRI("BL montaj amiri"),
    BOYAMA_VE_PAKETLEME_AMIRI("Boyama ve paketleme amiri");


    public String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
