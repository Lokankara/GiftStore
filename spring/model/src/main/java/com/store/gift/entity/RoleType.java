package com.store.gift.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Enum representing the roles of users in the system.
 */
@RequiredArgsConstructor
public enum RoleType {
    /**
     * Represents the guest role with read-only access.
     */
    GUEST(new HashSet<>(singletonList(Permission.GUEST_READ))),
    /**
     * Represents the user role with create and read access.
     */
    USER(new HashSet<>(asList(
            Permission.USER_CREATE,
            Permission.USER_READ))),
    /**
     * Represents the admin role with create, read, update, and delete access.
     */
    ADMIN(new HashSet<>(asList(
            Permission.ADMIN_CREATE,
            Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE)));
    /**
     * The set of permissions associated with the role.
     */
    @Getter
    private final Set<Permission> authorities;

    /**
     * Retrieves the granted authorities for this user.
     * The authorities are derived from the user's permissions.
     * The user's name is also included as a ROLE_ authority.
     *
     * @return List of SimpleGrantedAuthority objects representing the granted authorities.
     */
    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities =
                getAuthorities().stream()
                        .map(permission -> new SimpleGrantedAuthority(
                                permission.getAuthority()))
                        .collect(toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
