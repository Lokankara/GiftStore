package com.store.gift.controller;

import com.store.gift.security.auth.AuthenticationRequest;
import com.store.gift.security.auth.AuthenticationResponse;
import com.store.gift.security.auth.RegisterRequest;
import com.store.gift.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling authentication-related requests.
 * This class is responsible for handling registration and authentication requests.
 */
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    /**
     * Handles the signup request and returns a ResponseEntity with the authentication response.
     *
     * @param request The RegisterRequest object containing the signup details.
     * @return A ResponseEntity containing the AuthenticationResponse.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(
            final @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.signup(request));
    }

    /**
     * Endpoint for user authentication.
     *
     * @param request The authentication request containing user credentials.
     * @return ResponseEntity containing the authentication response.
     */
    @PostMapping("/token/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            final @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Refreshes the access token and returns a new AuthenticationResponse.
     *
     * @param authorizationHeader The RequestHeader authorization.
     * @param response            The HttpServletResponse object.
     * @return ResponseEntity containing the refreshed AuthenticationResponse.
     */
    @PostMapping("/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshTokens(
            final @RequestHeader("Authorization") String authorizationHeader,
            final HttpServletResponse response) {
        return ResponseEntity.ok(service.refresh(
                authorizationHeader, response));
    }

    /**
     * Authenticates a user by processing a login request.
     *
     * @param loginRequest The login request containing the user's credentials.
     * @return A ResponseEntity containing the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            final @RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }

    /**
     * Logs out the current user by invalidating the access token.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return ResponseEntity indicating a successful logout.
     */
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        service.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
