package com.store.gift;

import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.Token;
import com.store.gift.entity.User;
import com.store.gift.repository.TokenRepository;
import com.store.gift.repository.UserRepository;
import com.store.gift.security.exception.InvalidJwtAuthenticationException;
import com.store.gift.security.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JwtTokenProvider.class)
class JwtTokenProviderTest {
    HttpServletRequest request;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;
    private final String username = "Bob";
    private User user;
    private SecurityUser securityUser;
    private final String invalidToken = "invalidToken";
    public String jwtToken;

    @BeforeEach
    void init() {
        request = new MockHttpServletRequest();

        user = User.builder()
                .username(username)
                .email("bob@i.ua")
                .password(invalidToken)
                .role(Role.builder().permission(RoleType.USER).build())
                .build();

        securityUser = SecurityUser.builder().user(user).build();

        jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void testGetUsernameTokenNotFoundException() {
        String token = "JWT strings must contain exactly 2 period characters. Found: 1";
        Function<Claims, String> mockClaimsFunction = claims -> {
            throw new MalformedJwtException("");
        };
        Exception exception = assertThrows(Exception.class,
                () -> jwtTokenProvider.getClaim(token, mockClaimsFunction));
        assertEquals(InvalidJwtAuthenticationException.class, exception.getClass());
        assertEquals(String.format("%s", token), exception.getMessage());
    }

    @Test
    @DisplayName("UserDetails object with a scope, when buildToken is called, then return a valid JWT token with the scope")
    void testValidUserDetailsWithScopeWhenBuildTokenThenReturnValidTokenWithScope() {
        String jwt = jwtTokenProvider.generateToken(securityUser);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("JWT token, when getUsername is called, then return the username from the token")
    void testValidJwtTokenWhenGetUsernameThenReturnUsernameFromToken() {
        String result = jwtTokenProvider.getUsername(jwtToken);
        assertEquals(username, result);
    }

    @Test
    @DisplayName("JWT token and a valid claim function, when getClaim is called, then return the claim from the token")
    void testValidJwtTokenAndValidClaimFunctionWhenGetClaimThenReturnClaimFromToken() {
        String result = jwtTokenProvider.getClaim(jwtToken, Claims::getSubject);
        assertEquals(username, result);
    }

    @Test
    @DisplayName("UserDetails object, when buildRefreshToken is called, then return a valid refresh JWT token")
    void testValidUserWhenBuildRefreshTokenThenReturnValidRefreshToken() {
        String token = jwtTokenProvider.generateRefreshToken(securityUser);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(token);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("UserDetails object, when buildToken is called, then return a valid JWT token")
    void testValidUserWhenBuildTokenThenReturnValidToken() {
        String jwt = jwtTokenProvider.generateToken(securityUser);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
    }

    @Test
    @DisplayName("UserDetails object and valid claims, when buildToken is called with claims and userDetails arguments, then return a valid JWT token with the claims")
    void testValidUserAndValidClaimsWhenBuildTokenWithClaimsAndUserThenReturnValidTokenWithClaims() {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("key", "value");
        String jwt = jwtTokenProvider.generateToken(claimsMap, securityUser);
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .build()
                .parseClaimsJws(jwt);
        Assertions.assertEquals(username, claims.getBody().getSubject());
        Assertions.assertTrue(claims.getBody().getExpiration().after(new Date()));
        Assertions.assertEquals("value", claims.getBody().get("key"));
    }

    @Test
    @DisplayName("JWT token and a valid UserDetails object, when validateToken is called, then return true")
    void validatedJwtTokenAndValidUserWhenValidateTokenThenReturnTrue() {
        boolean result = jwtTokenProvider.isTokenValid(jwtToken, securityUser);
        assertTrue(result);
    }

    @Test
    @DisplayName("Given an invalid JWT token, when getUsername is called, then throw TokenNotFoundException")
    void validatedUsernameWithInvalidToken() {
        assertThrows(InvalidJwtAuthenticationException.class, () ->
                jwtTokenProvider.getUsername(invalidToken));
    }

    @Test
    @DisplayName("Given an invalid JWT token and a valid claim function, when getClaim is called, then throw TokenNotFoundException")
    void validatedClaimWithInvalidToken() {
        assertThrows(InvalidJwtAuthenticationException.class, () ->
                jwtTokenProvider.getClaim(invalidToken, Claims::getSubject));
    }

    @Test
    @DisplayName("Given an expired JWT token and a valid UserDetails object, when validatedToken is called, then return false")
    void validatedTokenWithExpiredToken() {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)),
                        SignatureAlgorithm.HS256)
                .compact();
        boolean result = jwtTokenProvider.isTokenValid(token, securityUser);
        assertFalse(result);
    }

    @Test
    @DisplayName("null UserDetails object, when buildToken is called, then throw InvalidJwtAuthenticationException")
    void buildTokenWithNullUserDetails() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.generateToken(null));
    }

    @Test
    @DisplayName("null UserDetails object and valid claims, when buildToken is called with claims and userDetails arguments, then throw InvalidJwtAuthenticationException")
    void buildTokenWithNullUserDetailsAndValidClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("key", "value");
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.generateToken(claims, null));
    }

    @Test
    @DisplayName("JWT token and a null UserDetails object, when validatedToken is called, then return false")
    void validatedTokenWithNullUserDetails() {
        assertFalse(jwtTokenProvider.isTokenValid(jwtToken, null));
    }

    @Test
    @DisplayName("UserDetails object and invalid claims, when buildToken is called with claims and userDetails arguments, then throw InvalidJwtAuthenticationException")
    void buildTokenWithInvalidClaims() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.generateToken(null, securityUser));
    }

    @DisplayName("JWT token, when findByToken is called, then return an Optional containing the Token")
    @Test
    void testFindByTokenWithValidToken() {
        User save = userRepository.save(user);
        String accessToken = jwtTokenProvider.generateToken(securityUser);
        Token token = Token.builder().user(save).accessToken(accessToken).expired(false).revoked(false).build();
        token = tokenRepository.save(token);
        Optional<Token> result = jwtTokenProvider.findByToken(accessToken);
        assertTrue(result.isPresent());
        assertEquals(token.getId(), result.get().getId());
    }

    @DisplayName("Given an invalid JWT token, when findByToken is called, then return an empty Optional")
    @Test
    void testFindByTokenWithInvalidToken() {
        Optional<Token> result = jwtTokenProvider.findByToken("invalidToken");
        assertFalse(result.isPresent());
    }

    @DisplayName("User without valid tokens, when findAllValidToken is called, then return an empty list")
    @Test
    void testFindAllValidTokenWithoutValidTokens() {
        userRepository.save(user);
        List<Token> result = jwtTokenProvider.findAllValidToken(user);
        assertTrue(result.isEmpty());
    }

    @DisplayName("User and an access token, when updateUserTokens is called, then delete all tokens associated with the user and create and save a new token for the user")
    @Test
    void testUpdateUserTokens() {

        User save = userRepository.save(user);
        Token token1 = Token.builder().user(save).expired(false).revoked(false).build();
        Token token2 = Token.builder().user(save).expired(false).revoked(false).build();

        tokenRepository.saveAll(Arrays.asList(token1, token2));

        jwtTokenProvider.updateUserTokens(save, invalidToken);

        List<Token> tokens = tokenRepository.findAll();
        assertEquals(3, tokens.size());
        Token newToken = tokens.get(0);
        assertEquals(user, newToken.getUser());
        assertFalse(newToken.isExpired());
        assertFalse(newToken.isRevoked());
    }


    @DisplayName("User with valid tokens, when findAllValidToken is called, then return a list of the user's valid tokens")
    @Test
    void testFindAllValidTokenWithValidTokens() {
        User save = userRepository.save(user);
        Token token1 = Token.builder().user(save).expired(false).revoked(false).build();
        Token token2 = Token.builder().user(save).expired(false).revoked(false).build();
        tokenRepository.saveAll(Arrays.asList(token1, token2));
        List<Token> result = jwtTokenProvider.findAllValidToken(save);
        assertEquals(2, result.size());
        assertTrue(result.contains(token1));
        assertTrue(result.contains(token2));
    }

    @Test
    @DisplayName("JWT token, when getAllClaims is called, then return the claims extracted from the token")
    void testGetAllClaimsWithValidToken() {
        String token = jwtTokenProvider.generateToken(securityUser);
        Claims claims = jwtTokenProvider.getAllClaims(token);
        assertNotNull(claims);
    }

    @Test
    @DisplayName("Given an expired JWT token, when getAllClaims is called, then throw an InvalidJwtAuthenticationException")
    void testGetAllClaimsWithExpiredToken() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.getAllClaims(invalidToken));
    }

    @Test
    @DisplayName("malformed JWT token, when getAllClaims is called, then throw a MalformedJwtException")
    void testGetAllClaimsWithMalformedToken() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.getAllClaims(null));
    }

    @Test
    @DisplayName("Given an empty JWT token, when getAllClaims is called, then throw an InvalidJwtAuthenticationException")
    void testGetAllClaimsWithEmptyToken() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.getAllClaims(""));
    }

    @Test
    @DisplayName("request without a Bearer token in the Authorization header")
    void testIsBearerTokenWithoutToken() {
        boolean result = jwtTokenProvider.isBearerToken(
                request.getHeader(HttpHeaders.AUTHORIZATION));
        assertFalse(result);
    }

    @Test
    @DisplayName("Given an generateToken JWT token, when isTokenValid is called, then return true")
    void testIsTokenExpiredWithExpiredToken() {
        String expiredToken = jwtTokenProvider.generateToken(securityUser);
        boolean result = jwtTokenProvider.isTokenValid(expiredToken, securityUser);
        assertTrue(result);
    }

    @Test
    @DisplayName("expired JWT token, when isTokenValid is called, then return false")
    void testIsTokenExpiredWithExpiredTokens() {
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtExpiration))
                .signWith(jwtTokenProvider.getSignInKey())
                .compact();
        boolean result = jwtTokenProvider.isTokenValid(jwtToken, securityUser);
        assertFalse(result);
    }

    @Test
    @DisplayName("JWT token, when isTokenValid is called, then return true")
    void testIsTokenExpiredWithValidToken() {
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(jwtTokenProvider.getSignInKey())
                .compact();
        assertTrue(jwtTokenProvider.isTokenValid(jwtToken, securityUser));
    }

    @Test
    @DisplayName("Given an expired JWT token, when isTokenValid is called with ignoreExpired set to true, then return false")
    void testIsTokenExpiredWithExpiredTokenAndIgnoreExpired() {

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - jwtExpiration))
                .signWith(jwtTokenProvider.getSignInKey())
                .compact();

        assertFalse(jwtTokenProvider.isTokenValid(jwtToken, securityUser));
    }

    @Test
    void updateUserTokensShouldSaveNewToken() {

        Token savedToken = jwtTokenProvider.updateUserTokens(user, jwtToken);
        Token expectedToken = tokenRepository.findById(savedToken.getId()).orElse(null);
        assertNotNull(expectedToken);
        assertEquals(expectedToken.getId(), savedToken.getId());
        assertEquals(expectedToken.getAccessToken(), savedToken.getAccessToken());
        assertEquals(expectedToken.getAccessTokenTTL(), savedToken.getAccessTokenTTL());
        assertEquals(expectedToken.isRevoked(), savedToken.isRevoked());
        assertEquals(expectedToken.isExpired(), savedToken.isExpired());
        assertEquals(expectedToken.getUser().getUsername(), savedToken.getUser().getUsername());
        assertEquals(expectedToken.getUser().getEmail(), savedToken.getUser().getEmail());
        assertEquals(expectedToken.getUser().getPassword(), savedToken.getUser().getPassword());

        tokenRepository.deleteById(savedToken.getId());
    }

    @Test
    @DisplayName("Test revoking all user tokens")
    void revokeAllUserTokensShouldRevokeAllTokens() {

        userRepository.save(user);
        Token token1 = new Token();
        token1.setUser(user);
        Token token2 = new Token();
        token2.setUser(user);
        List<Token> entities = new java.util.ArrayList<>();
        entities.add(token1);
        entities.add(token2);
        tokenRepository.saveAll(entities);

        jwtTokenProvider.revokeAllUserTokens(user);
        Token revokedToken1 = tokenRepository.findById(token1.getId()).orElse(null);
        Token revokedToken2 = tokenRepository.findById(token2.getId()).orElse(null);

        assertTrue(revokedToken1.isExpired());
        assertTrue(revokedToken1.isRevoked());
        assertTrue(revokedToken2.isExpired());
        assertTrue(revokedToken2.isRevoked());

        tokenRepository.deleteAll();
        userRepository.delete(user);
    }
}
