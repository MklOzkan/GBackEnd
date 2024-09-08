package com.project.domain.enums;

import lombok.Getter;

@Getter
public enum StatusType {
    ISLENMEYI_BEKLIYOR("İşlenmeyi Bekliyor"),
    ISLENMEKTE("İşlenmekte"),
    BEKLEMEDE("Beklemede"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi");

    public final String name;

    StatusType(String name) {
        this.name = name;
    }

}
