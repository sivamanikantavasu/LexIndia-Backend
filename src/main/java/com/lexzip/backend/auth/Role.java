package com.lexzip.backend.auth;

public enum Role {
    ADMIN("admin"),
    EDUCATOR("educator"),
    LEGAL_EXPERT("legal-expert"),
    CITIZEN("citizen");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role fromValue(String value) {
        for (Role role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return CITIZEN;
    }
}
