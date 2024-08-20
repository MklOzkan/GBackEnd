package com.project.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("Admin"),
    EMPLOYEE("Employee");

    public final String name;

    RoleType(String name) {
        this.name = name;
    }

}
