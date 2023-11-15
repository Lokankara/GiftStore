package com.store.gift.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents different types of permissions
 * and their corresponding authorities.
 */
@RequiredArgsConstructor
public enum Permission {
    /**
     * Represents the permission for creating an admin.
     */
    ADMIN_CREATE("admin:create"),
    /**
     * Represents the permission for reading/administering an admin.
     */
    ADMIN_READ("admin:read"),
    /**
     * Represents the permission for updating an admin.
     */
    ADMIN_UPDATE("admin:update"),
    /**
     * Represents the permission for deleting an admin.
     */
    ADMIN_DELETE("admin:delete"),
    /**
     * Represents the permission for reading a user.
     */
    USER_READ("user:read"),
    /**
     * Represents the permission for creating a user.
     */
    USER_CREATE("user:create"),
    /**
     * Represents the permission for reading as a guest.
     */
    GUEST_READ("guest:read");
    /**
     * The authority associated with the permission.
     */
    @Getter
    private final String authority;
}
