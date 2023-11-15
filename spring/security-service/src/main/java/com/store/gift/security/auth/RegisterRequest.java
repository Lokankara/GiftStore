package com.store.gift.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The RegisterRequest class represents the request payload for user registration.
 * It contains the user's username, email, and password.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    /**
     * The username for registration.
     */
    private String username;

    /**
     * The email for registration.
     */
    private String email;

    /**
     * The password for registration.
     */
    private String password;
}
