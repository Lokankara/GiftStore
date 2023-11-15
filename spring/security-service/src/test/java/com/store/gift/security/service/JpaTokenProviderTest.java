package com.store.gift.security.service;

import com.store.gift.entity.Token;
import com.store.gift.entity.User;
import com.store.gift.repository.TokenRepository;
import com.store.gift.security.exception.InvalidJwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.store.gift.entity.TokenType.BEARER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTokenProviderTest {
    private final String secretKey = "e7bc37d4ea2aac8dc945dc06e0ea48e46a38a1669eced79d0a8432b8ffdcfcb4";

    @Mock
    private UserDetails userDetails;

    @Mock
    private Claims claims;

    @Mock
    private JwtParserBuilder mockJwtParserBuilder;

    @Mock
    private JwtParser mockJwtParser;

    @Mock
    private Jws<Claims> mockJws;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private Jws<Claims> jws;
    Token token;

    @BeforeEach
    void setUp() {
        token = new Token();
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider(tokenRepository);
    }

    @Test
    void testGetAllClaimsValidToken() {
        String validToken = Jwts.builder()
                .setSubject("testSubject")
                .signWith(Keys.hmacShaKeyFor(Decoders
                                .BASE64.decode(secretKey)),
                        SignatureAlgorithm.HS256)
                .compact();
        Mockito.when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.of(new Token()));
        Claims expectedClaims = Jwts.claims();
        expectedClaims.setSubject("testSubject");
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        Optional<Token> token = tokenRepository.findByAccessToken(validToken);
        assertNotNull(token);
        Claims claims = jwtTokenProvider.getAllClaims(validToken);
        assertNotNull(claims);
        assertEquals("testSubject", claims.getSubject());
    }

    @Test
    void testGetSignInKey() {
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        Key key = jwtTokenProvider.getSignInKey();
        assertNotNull(key);
        assertEquals("HmacSHA384", key.getAlgorithm());
    }

    @Test
    void testSaveTokens() {
        Token token = new Token();
        Token save = new Token();
        when(tokenRepository.save(save)).thenReturn(token);
        Token result = jwtTokenProvider.save(token);
        assertNotNull(result);
        verify(tokenRepository).save(token);
    }

    @Test
    void testGetToken() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        String testAccessToken = "testAccessToken";
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 3600L);
        Token token = jwtTokenProvider.getToken(testUser, testAccessToken);
        assertNotNull(token);
        assertEquals(testUser, token.getUser());
        assertFalse(token.isExpired());
        assertFalse(token.isRevoked());
        assertEquals(BEARER, token.getTokenType());
        assertEquals(testAccessToken, token.getAccessToken());
        assertEquals(jwtTokenProvider.getExpiration(), token.getAccessTokenTTL());
    }

    @Test
    void testUpdateUserTokens() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        String testAccessToken = "testAccessToken";
        token.setUser(testUser);
        token.setAccessToken(testAccessToken);
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 3600L);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);
        Token updatedToken = jwtTokenProvider.updateUserTokens(testUser, testAccessToken);
        assertNotNull(updatedToken);
        assertEquals(testUser, updatedToken.getUser());
        assertEquals(testAccessToken, updatedToken.getAccessToken());
    }

    @Test
    void testSaveToken() {
        Token save = new Token();
        when(tokenRepository.save(save)).thenReturn(token);
        Token result = jwtTokenProvider.save(token);
        assertNotNull(result);
        verify(tokenRepository).save(token);
    }

    @Test
    void testSave() {
        token.setId(1);
        token.setAccessToken("testAccessToken");
        when(tokenRepository.save(token)).thenReturn(token);
        Token savedToken = jwtTokenProvider.save(token);
        assertNotNull(savedToken);
        assertEquals(token, savedToken);
    }

    @Test
    void testFindAllValidToken() {
        User testUser = new User();
        testUser.setId(1L);
        Token testToken1 = new Token();
        testToken1.setId(1);
        testToken1.setAccessToken("testAccessToken1");
        Token testToken2 = new Token();
        testToken2.setId(2);
        testToken2.setAccessToken("testAccessToken2");
        List<Token> testTokens = Arrays.asList(testToken1, testToken2);
        when(tokenRepository.findAllValidAccessTokenByUserId(testUser.getId())).thenReturn(testTokens);
        List<Token> validTokens = jwtTokenProvider.findAllValidToken(testUser);
        assertNotNull(validTokens);
        assertEquals(2, validTokens.size());
        assertTrue(validTokens.contains(testToken1));
        assertTrue(validTokens.contains(testToken2));
    }

    @Test
    void testIsBearerToken() {
        String validAuthorizationHeader = "Bearer testToken";
        String invalidAuthorizationHeader = "Basic testToken";
        String nullAuthorizationHeader = null;
        boolean isValid = jwtTokenProvider.isBearerToken(validAuthorizationHeader);
        assertTrue(isValid);
        boolean isInvalid = jwtTokenProvider.isBearerToken(invalidAuthorizationHeader);
        assertFalse(isInvalid);
        boolean isNull = jwtTokenProvider.isBearerToken(nullAuthorizationHeader);
        assertFalse(isNull);
    }

    @Test
    void testGetClaim() {
        Claims mockClaims = mock(Claims.class);
        when(mockClaims.get("claimName", String.class)).thenReturn("claimValue");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(tokenRepository) {
            @Override
            public Claims getAllClaims(String token) {
                return mockClaims;
            }
        };
        String result = jwtTokenProvider.getClaim("token", claims -> claims.get("claimName", String.class));
        assertEquals("claimValue", result);
    }

    @Test
    void testGetUsernameThrowsTokenNotFoundException() {
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> jwtTokenProvider.getUsername("malformedToken"));
    }

    @Test
    void testGetUsername() {
        JwtTokenProvider jwtTokenProvider = Mockito.spy(new JwtTokenProvider(tokenRepository));
        doReturn("username").when(jwtTokenProvider).getClaim(eq("token"), any());
        String result = jwtTokenProvider.getUsername("token");
        assertEquals("username", result);
    }

    @Test
    void testRevokeAllUserTokens() {
        User mockUser = new User();
        mockUser.setId(1L);
        List<Token> mockTokens = new ArrayList<>();
        mockTokens.add(new Token());
        mockTokens.add(new Token());
        when(tokenRepository.findAllValidAccessTokenByUserId(any(Long.class))).thenReturn(mockTokens);
        jwtTokenProvider.revokeAllUserTokens(mockUser);
        verify(tokenRepository, times(1)).findAllValidAccessTokenByUserId(mockUser.getId());
        verify(tokenRepository, times(1)).saveAll(mockTokens);
        assertTrue(mockTokens.stream().allMatch(Token::isExpired));
        assertTrue(mockTokens.stream().allMatch(Token::isRevoked));
    }

    @Test
    void testFindByToken() {
        String jwt = "jwt";
        Token mockToken = new Token();
        mockToken.setId(1);
        when(tokenRepository.findByAccessToken(jwt)).thenReturn(Optional.of(mockToken));
        Optional<Token> result = jwtTokenProvider.findByToken(jwt);
        assertTrue(result.isPresent());
        assertEquals(mockToken, result.get());
    }

    @Test
    void testFindByTokenNotFound() {
        String jwt = "nonexistent_jwt";
        when(tokenRepository.findByAccessToken(jwt)).thenReturn(Optional.empty());
        Optional<Token> result = jwtTokenProvider.findByToken(jwt);
        assertFalse(result.isPresent());
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);

        when(jwtTokenProvider.getSignInKey())
                .thenReturn(Keys.hmacShaKeyFor(Decoders
                        .BASE64.decode(secretKey)));
        when(userDetails.getUsername()).thenReturn("testUser");

        Map<String, Object> claims = new HashMap<>();
        claims.put("claim1", "value1");
        claims.put("claim2", "value2");
        Long expiration = 10000L;
        String testToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
                .setExpiration(Date.from(Instant.now().plusMillis(expiration)))
                .compact();

        when(jwtTokenProvider.generateToken(any(), any())).thenReturn(testToken);

        String token = jwtTokenProvider.generateToken(claims, userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(jwtTokenProvider.getSignInKey())
                .build()
                .parseClaimsJws(token);
        Claims parsedClaims = jws.getBody();
        assertEquals("testUser", parsedClaims.getSubject());
        assertEquals("value1", parsedClaims.get("claim1"));
        assertEquals("value2", parsedClaims.get("claim2"));
        Date now = new Date();
        assertTrue(parsedClaims.getExpiration().getTime() - now.getTime() <= expiration);
    }

    @Test
    void testGenerateTokens() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("key1", "value1");
        claims.put("key2", "value2");
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.generateToken(any(), any())).thenReturn("sample_token");

        String token = jwtTokenProvider.generateToken(claims, userDetails);
        assertNotNull(token);
        assertEquals("sample_token", token);
    }

    @Test
    void testGenerateTokenUserDetails() {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.generateToken(any())).thenReturn("sample_token");
        String token = jwtTokenProvider.generateToken(userDetails);
        assertNotNull(token);
        assertEquals("sample_token", token);
    }

    @Test
    void testGenerateRefreshTokens() {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("sample_refresh_token");
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);
        assertNotNull(refreshToken);
        assertEquals("sample_refresh_token", refreshToken);
    }
}
