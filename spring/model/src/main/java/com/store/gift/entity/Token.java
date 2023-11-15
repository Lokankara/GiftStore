package com.store.gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents a token used for authentication and authorization.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
public class Token implements Serializable {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "token_sequence")
    private Integer id;
    /**
     * The type of the token (e.g., Bearer).
     */
    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;
    /**
     * The actual token value.
     */
    @Column(unique = true)
    private String accessToken;
    /**
     * The time-to-live (TTL) in milliseconds for the access token.
     */
    @Column(name = "access_token_ttl")
    private Long accessTokenTTL;
    /**
     * Flag indicating if the token has been revoked.
     */
    private boolean revoked;
    /**
     * Flag indicating if the token has expired.
     */
    private boolean expired;
    /**
     * The user associated with the token.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
