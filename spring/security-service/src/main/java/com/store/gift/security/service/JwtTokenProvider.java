package com.store.gift.security.service;

import com.store.gift.entity.Token;
import com.store.gift.entity.User;
import com.store.gift.exception.TokenNotFoundException;
import com.store.gift.repository.TokenRepository;
import com.store.gift.security.exception.InvalidJwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.store.gift.entity.TokenType.BEARER;

/**
 * Service class that provides various JWT token operations.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshExpiration;
    private final TokenRepository tokenRepository;

    /**
     * Retrieves the jwtExpiration time
     *
     * @return The expiration time
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Retrieves the username from the provided token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     * @throws TokenNotFoundException If the token is malformed or not found.
     */
    public String getUsername(
            final String token) {
        try {
            return getClaim(token, Claims::getSubject);
        } catch (MalformedJwtException e) {
            throw new TokenNotFoundException(
                    String.format("Token not found: %s", token));
        }
    }

    /**
     * Retrieves a specific claim from the provided token using the given function.
     *
     * @param token  The JWT token.
     * @param claims The function to extract the desired claim from the token.
     * @param <T>    The type of the desired claim.
     * @return The extracted claim.
     */
    public <T> T getClaim(
            final String token,
            final Function<Claims, T> claims) {
        return claims.apply(getAllClaims(token));
    }

    /**
     * Builds a refresh token for the specified user.
     *
     * @param user The UserDetails object.
     * @return The generated refresh token.
     */
    public String generateRefreshToken(
            final UserDetails user) {
        return generateToken(new HashMap<>(),
                user, refreshExpiration);
    }

    /**
     * Builds a token for the provided UserDetails.
     *
     * @param userDetails The UserDetails object.
     * @return The generated token.
     */
    public String generateToken(
            final UserDetails userDetails) {
        return generateToken(new HashMap<>(),
                userDetails);
    }

    /**
     * Builds a token for the provided UserDetails with additional claims.
     *
     * @param claims Additional claims to include in the token.
     * @param user   The UserDetails object.
     * @return The generated token.
     */
    public String generateToken(
            final Map<String, Object> claims,
            final @NotNull UserDetails user) {
        return generateToken(claims, user, expiration);
    }

    /**
     * Builds a JWT token for the specified user with additional claims.
     *
     * @param claims      Additional claims to include in the token.
     * @param userDetails The user details.
     * @param expiration  The time expiration.
     * @return The generated JWT token.
     */
    private String generateToken(
            final Map<String, Object> claims,
            final UserDetails userDetails,
            final Long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(Date.from(Instant.now()))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                    .compact();
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException(
                    "Invalid jwt authentication");
        }
    }

    /**
     * Validates a JWT token against the specified user details.
     *
     * @param token       The JWT token to validate.
     * @param userDetails The user details to compare against.
     * @return {@code true} if the token is valid for the user,
     * {@code false} otherwise.
     */
    public boolean isTokenValid(
            final String token,
            final UserDetails userDetails) {
        try {
            return getUsername(token)
                    .equals(userDetails.getUsername())
                    && !getClaim(token, Claims::getExpiration)
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the signing key used for JWT token verification.
     *
     * @return The signing key.
     */
    public Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders
                .BASE64.decode(secretKey));
    }

    /**
     * Utility methods and repository operations related to JWT tokens.
     *
     * @param token The jwt token
     * @return The claims extracted from the token.
     */
    public Claims getAllClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (MalformedJwtException
                 | ExpiredJwtException
                 | UnsupportedJwtException
                 | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException(e.getMessage());
        }
    }

    /**
     * Checks if the provided HttpServletRequest contains a valid Bearer token.
     *
     * @param authorizationHeader The HttpServletRequest object.
     * @return true if a valid Bearer token is present in the request, false otherwise.
     */
    public boolean isBearerToken(
            final String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ");
    }

    /**
     * Retrieves a token from the database based on the provided JWT.
     *
     * @param jwt The JWT token to search for.
     * @return An Optional containing the Token if found, or empty if not found.
     */
    @Transactional
    public Optional<Token> findByToken(final String jwt) {
        return tokenRepository.findByAccessToken(jwt);
    }

    /**
     * Retrieves a list of all valid tokens associated with the given user.
     *
     * @param user The user for whom to retrieve the tokens.
     * @return A list of valid tokens.
     */
    @Transactional
    public List<Token> findAllValidToken(final User user) {
        return tokenRepository
                .findAllValidAccessTokenByUserId(user.getId());
    }

    /**
     * Saves a token in the database.
     *
     * @param token The token to be saved.
     * @return The saved token.
     */
    @Transactional
    public Token save(final Token token) {
        return tokenRepository.save(token);
    }

    /**
     * Updates the user's tokens with the new access token.
     *
     * @param user        The user for whom to update the tokens.
     * @param accessToken The new access token to be updated.
     * @return The updated token.
     */
    @Transactional
    public Token updateUserTokens(
            final User user,
            final String accessToken) {
        Token token = getToken(user, accessToken);
        return save(token);
    }

    /**
     * Revokes all tokens associated with the given user.
     *
     * @param user The user whose tokens should be revoked.
     */
    @Transactional
    public void revokeAllUserTokens(final User user) {
        List<Token> tokens = findAllValidToken(user);
        if (!tokens.isEmpty()) {
            tokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
        }
        tokenRepository.saveAll(tokens);
    }

    /**
     * Creates a new token for the given user with the provided access token.
     *
     * @param user        The user for whom to create the token.
     * @param accessToken The access token to be associated with the token.
     * @return The created token.
     */
    public Token getToken(
            final User user,
            final String accessToken) {
        return Token.builder()
                .user(user)
                .expired(false)
                .revoked(false)
                .tokenType(BEARER)
                .accessToken(accessToken)
                .accessTokenTTL(getExpiration())
                .build();
    }
}
