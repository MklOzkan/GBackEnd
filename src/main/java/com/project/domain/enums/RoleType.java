package com.project.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("Admin"),
    EMPLOYEE("Employee");

//    TALASLI_IMALAT_AMIRI("Talasli imalat amiri"),
//    POLISAJ_AMIRI("Polisaj amiri"),
//    LIFT_MONTAJ_AMIRI("Lift montaj amiri"),
//    BL_MONTAJ_AMIRI("BL montaj amiri"),
//
//    KALITE_KONTROL("Kalite kontrol"),
//
//    URETIM_PLANLAMA_AMIRI("Uretim planlama amiri"),
//    BOYAMA_VE_PAKETLEME_AMIRI("Boyama ve paketleme amiri");

    public final String name;

    RoleType(String name) {
        this.name = name;
    }

}
