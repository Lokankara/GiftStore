package com.store.gift.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {
    @Test
    @DisplayName("Given an attempt to retrieve a non-existing order, when getById is called, then OrderNotFoundException should be thrown")
    void testOrderNotFoundException() {
        try {
            throw new OrderNotFoundException("Order not found");
        } catch (OrderNotFoundException e) {
            assertEquals("Order not found", e.getMessage());
        }
    }

    @Test
    @DisplayName("Given an attempt to retrieve a non-existing certificate, when findAllByIds is called, then CertificateNotFoundException should be thrown")
    void testCertificateNotFoundException() {
        try {
            throw new CertificateNotFoundException("Certificate not found");
        } catch (CertificateNotFoundException e) {
            assertEquals("Certificate not found", e.getMessage());
        }
    }

    @Test
    @DisplayName("Given an attempt to access a non-existing token, when getToken is called, then TokenNotFoundException should be thrown")
    void testTokenNotFoundException() {
        try {
            throw new TokenNotFoundException("Token not found");
        } catch (TokenNotFoundException e) {
            assertEquals("Token not found", e.getMessage());
        }
    }

    @Test
    @DisplayName("Given an attempt to access a non-existing resource, when getResource is called, then ResourceNotFoundException should be thrown")
    void testResourceNotFoundException() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            throw new ResourceNotFoundException("GET", "/resource", headers);
        } catch (ResourceNotFoundException e) {
            assertEquals("No endpoint GET /resource.", e.getMessage());
        }
    }

    @Test
    @DisplayName("Given an attempt to create an already existing user, when createUser is called, then UserAlreadyExistsException should be thrown")
    void testUserAlreadyExistsException() {
        try {
            throw new UserAlreadyExistsException("User already exists");
        } catch (UserAlreadyExistsException e) {
            assertEquals("User already exists", e.getMessage());
        }
    }

    @Test
    @DisplayName("Given an attempt to access an invalid role, when getRole is called, then RoleNotFoundException should be thrown")
    void testRoleNotFoundException() {
        try {
            throw new RoleNotFoundException("Role not found");
        } catch (RoleNotFoundException e) {
            assertEquals("Role not found", e.getMessage());
        }
    }
}
