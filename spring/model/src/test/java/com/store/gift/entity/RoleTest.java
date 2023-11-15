package com.store.gift.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
    @Test
    @DisplayName("Given id, permission, users, and authorities, when Role is built, then id, permission, users, and authorities are set correctly")
    void testRoleBuilder() {
        Role role = Role.builder()
                .id(1L)
                .permission(RoleType.ADMIN)
                .users(List.of(User.builder().username("user").build()))
                .authorities(Set.of(Permission.ADMIN_CREATE, Permission.ADMIN_READ))
                .build();

        assertEquals(1L, role.getId());
        assertEquals(RoleType.ADMIN, role.getPermission());
        assertEquals("user", role.getUsers().get(0).getUsername());
        assertTrue(role.getAuthorities().contains(Permission.ADMIN_CREATE));
        assertTrue(role.getAuthorities().contains(Permission.ADMIN_READ));
    }
}