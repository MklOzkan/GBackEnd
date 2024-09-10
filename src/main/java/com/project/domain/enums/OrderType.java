package com.project.domain.enums;

public enum OrderType {
    LIFT ("Lift"),
    DAMPER ("Damper"),
    BLOKLIFT ("Blok Lift"),
    PASLANMAZ ("Paslanmaz"),;

    public final String name;

    OrderType(String name) {
        this.name = name;
    }
}

