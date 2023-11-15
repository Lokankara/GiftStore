package com.store.gift.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntitySecurityTest {
    RegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
    }

    @Test
    void getUsername() {
        request.setUsername("user");
        assertEquals("user", request.getUsername());
    }

    @Test
    void userGettersAndSetters() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("user");
        request.setEmail("user@example.com");
        request.setPassword("pass");

        assertEquals("user", request.getUsername());
        assertEquals("user@example.com", request.getEmail());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void userBuilder() {
        RegisterRequest request = RegisterRequest.builder()
                .username("user")
                .email("user@example.com")
                .password("pass")
                .build();

        assertEquals("user", request.getUsername());
        assertEquals("user@example.com", request.getEmail());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void userNoArgsConstructor() {
        RegisterRequest request = new RegisterRequest();
        assertNotNull(request);
    }

    @Test
    void userAllArgsConstructor() {
        RegisterRequest request = new RegisterRequest(
                "user", "user@example.com", "pass");
        assertEquals("user", request.getUsername());
        assertEquals("user@example.com", request.getEmail());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void testGettersAndSetters() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("user");
        request.setPassword("pass");

        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void testBuilder() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("user")
                .password("pass")
                .build();

        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        AuthenticationRequest request = new AuthenticationRequest();
        assertNotNull(request);
    }

    @Test
    void testAllArgsConstructor() {
        AuthenticationRequest request = new AuthenticationRequest("user", "pass");
        assertEquals("user", request.getUsername());
        assertEquals("pass", request.getPassword());
    }

    @Test
    void testGettersAndSettersAuthentication() {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setUsername("user");
        response.setAccessToken("accessToken");
        response.setRefreshToken("refreshToken");
        Timestamp timestamp = Timestamp.valueOf("2023-08-10 21:39:46.123");
        response.setExpiresAt(timestamp);

        assertEquals("user", response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(timestamp, response.getExpiresAt());
    }

    @Test
    void testBuilderAuthentication() {
        Timestamp timestamp = Timestamp.valueOf("2023-08-10 21:39:46.123");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .username("user")
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .expiresAt(timestamp)
                .build();

        assertEquals("user", response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(timestamp, response.getExpiresAt());
    }

    @Test
    void testNoArgsConstructorAuthentication() {
        AuthenticationResponse response = new AuthenticationResponse();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructorAuthentication() {
        Timestamp timestamp = Timestamp.valueOf("2023-08-10 21:39:46.123");
        AuthenticationResponse response = new AuthenticationResponse(
                0L,
                "user",
                "accessToken",
                "refreshToken",
                timestamp
        );

        assertEquals("user", response.getUsername());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(timestamp, response.getExpiresAt());
    }
}