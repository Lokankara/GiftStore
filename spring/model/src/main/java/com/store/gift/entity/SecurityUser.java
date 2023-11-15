package com.store.gift.entity;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
public class SecurityUser implements UserDetails {
    private User user;

    /**
     * Retrieves the authorities granted to the user.
     *
     * @return A collection of GrantedAuthority
     * objects representing the user's authorities.
     */
    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Hibernate.initialize(user.getRole());
        Hibernate.initialize(user.getRole().getPermission());
        return user.getRole().getPermission().getGrantedAuthorities();
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return the password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     *
     * @return the username (never <code>null</code>)
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Checks if the user's account is not expired.
     *
     * @return true if the account is not expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if the user's account is not locked.
     *
     * @return true if the account is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if the user's credentials are not expired.
     *
     * @return true if the credentials are not expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if the user is enabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
