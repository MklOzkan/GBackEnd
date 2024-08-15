package com.project.domain.enums;

public enum RoleType {
    ADMIN("Admin"),
    TALASLI_IMALAT_AMIRI("TalasliImalatAmiri"),
    POLISAJ_AMIRI("PolisajAmiri"),
    LIFT_MONTAJ_AMIRI("LiftMontajAmiri"),
    KALITE_KONTROL("KaliteKontrol"),
    BL_MONTAJ_AMIRI("BLMontajAmiri"),
    URUETIM_PLANLAMA("UretimPlanlama"),
    BOYAMA_VE_PAKETLEME_AMIRI("BoyamaVePaketlemeAmiri");


    public String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
