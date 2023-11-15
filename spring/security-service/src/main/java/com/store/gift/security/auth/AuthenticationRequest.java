package com.store.gift.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a response to an authentication request.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    /**
     * The username for authentication.
     */
    private String username;
    /**
     * The password for authentication.
     */
    private String password;
}
