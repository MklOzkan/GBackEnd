package com.project.domain.enums;

public enum RoleType {
    ADMIN("Admin"),
    TALASLI_IMALAT_AMIRI("Talasli imalat amiri"),
    POLISAJ_AMIRI("Polisaj amiri"),
    LIFT_MONTAJ_AMIRI("Lift montaj amiri"),
    BL_MONTAJ_AMIRI("BL montaj amiri"),

    KALITE_KONTROL("Kalite kontrol"),

    URETIM_PLANLAMA_AMIRI("Uretim planlama amiri"),
    BOYAMA_VE_PAKETLEME_AMIRI("Boyama ve paketleme amiri");


    public String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

//    public static RoleType fromString(String roleName) {
//        for (RoleType role : RoleType.values()) {
//            if (role.name.equalsIgnoreCase(roleName)) {
//                return role;
//            }
//        }
//        throw new IllegalArgumentException(String.format("Role '%s' not found", roleName));
//    }

}
