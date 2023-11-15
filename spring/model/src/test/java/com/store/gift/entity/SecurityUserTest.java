package com.store.gift.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUserTest {

    private User user;
    private SecurityUser securityUser;
    @BeforeEach
    public void setup() {
        user = User.builder().id(1L).username("user").email("email@email.ua").password("password").build();
        securityUser = SecurityUser.builder().user(user).build();
    }

    @Test
    void testGetAuthorities() {
        User user = new User();
        Role role = new Role();
        role.setPermission(RoleType.USER);
        user.setRole(role);
        SecurityUser securityUser = SecurityUser.builder().user(user).build();
        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
        assertEquals(3, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertEquals(user, securityUser.getUser());
    }

    @Test
    @DisplayName("Given a valid User entity, getPassword should return the user's password")
    void testGetPassword() {
        user.setPassword("password");
        SecurityUser securityUser = new SecurityUser(user);
        String result = securityUser.getPassword();
        assertEquals("password", result);
    }

    @Test
    @DisplayName("Given a valid User entity, getUsername should return the user's username")
    void testGetUsername() {
        user.setUsername("username");
        SecurityUser securityUser = new SecurityUser(user);
        String result = securityUser.getUsername();
        assertEquals("username", result);
    }

    @Test
    @DisplayName("isAccountNonExpired should always return true")
    void testIsAccountNonExpired() {
        assertTrue(securityUser.isAccountNonExpired());
    }

    @Test
    @DisplayName("Given a valid User entity, isAccountNonExpired should always return true")
    void testIsAccountNonLocked() {
        assertTrue(securityUser.isAccountNonLocked());
    }

    @Test
    @DisplayName("Given a valid SecurityUser entity, isCredentialsNonExpired should always return true")
    void testIsCredentialsNonExpired() {
        assertTrue(securityUser.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Given a valid SecurityUser entity, isEnabled should always return true")
    void testIsEnabled() {
        SecurityUser securityUser = new SecurityUser(user);
        assertTrue(securityUser.isEnabled());
    }
}
