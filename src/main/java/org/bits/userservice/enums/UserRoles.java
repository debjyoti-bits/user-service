package org.bits.userservice.enums;

public enum UserRoles {
    ROLE_CUSTOMER("ROLE_CUSTOMER"),
    ROLE_RESTAURANT_OWNER("ROLE_RESTAURANT_OWNER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}