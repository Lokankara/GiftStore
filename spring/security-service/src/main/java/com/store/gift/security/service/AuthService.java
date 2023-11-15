package com.store.gift.security.service;

import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.User;
import com.store.gift.security.auth.AuthenticationRequest;
import com.store.gift.security.auth.AuthenticationResponse;
import com.store.gift.security.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service interface for authentication and authorization operations.
 */
public interface AuthService {

    /**
     * Handles user signup and returns the authentication response.
     *
     * @param request the register request
     * @return the authentication response
     */
    @Transactional
    AuthenticationResponse signup(
            RegisterRequest request);

    /**
     * Handles user authentication and returns the authentication response.
     *
     * @param request the authentication request
     * @return the authentication response
     */
    @Transactional
    AuthenticationResponse authenticate(
            AuthenticationRequest request);

    /**
     * Handles token refresh and returns the updated authentication response.
     *
     * @param authorizationHeader the authorization header
     * @param response            the HTTP response
     * @return the updated authentication response
     */
    @Transactional
    AuthenticationResponse refresh(
            String authorizationHeader,
            HttpServletResponse response);

    /**
     * Handles user logout.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     */
    void logout(
            HttpServletRequest request,
            HttpServletResponse response);

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the user object if found, otherwise null
     */
    @Transactional
    SecurityUser findUser(String username);

    /**
     * Saves a user with the specified role and returns the updated user object.
     *
     * @param request the register request
     * @return the updated user object
     */
    @Transactional
    User saveUserWithRole(
            RegisterRequest request);

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return the optional user object
     */
    @Transactional
    Optional<User> findByUsername(
            String username);
}
