package com.store.gift.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;

class TokenTest {

    @Test
    @DisplayName("Given id, tokenType, accessToken, accessTokenTTL, revoked, expired, and user, when Token is built, then id, tokenType, accessToken, accessTokenTTL, revoked, expired, and user are set correctly")
    void testTokenBuilderWithUser() {
        User user = User.builder()
                .id(1L)
                .role(Role.builder().build())
                .username("user")
                .password("password")
                .email("email@i.ua")
                .tokens(new HashSet<>(Collections.singleton(new Token())))
                .orders(new HashSet<>())
                .build();

        Token token = Token.builder()
                .id(1)
                .tokenType(TokenType.BEARER)
                .accessToken("AccessToken")
                .accessTokenTTL(3600000L)
                .revoked(false)
                .expired(false)
                .user(user)
                .build();

        assertEquals(1, token.getId());
        assertEquals(TokenType.BEARER, token.getTokenType());
        assertEquals("AccessToken", token.getAccessToken());
        assertEquals(3600000L, token.getAccessTokenTTL());
        assertFalse(token.isRevoked());
        assertFalse(token.isExpired());

        assertEquals(1L, token.getUser().getId());
        assertEquals("user", token.getUser().getUsername());
        assertEquals("password", token.getUser().getPassword());
        assertEquals("user", token.getUser().getUsername());
        assertEquals("email@i.ua", token.getUser().getEmail());
        assertEquals("email@i.ua", token.getUser().getEmail());
        assertEquals(1, token.getUser().getTokens().size());
        assertEquals(0, token.getUser().getOrders().size());
    }
}
