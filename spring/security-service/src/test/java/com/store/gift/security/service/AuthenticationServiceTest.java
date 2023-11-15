package com.store.gift.security.service;

import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.Token;
import com.store.gift.entity.TokenType;
import com.store.gift.entity.User;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.repository.TokenRepository;
import com.store.gift.repository.UserRepository;
import com.store.gift.security.auth.AuthenticationResponse;
import com.store.gift.security.auth.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService service;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    RegisterRequest mockRequest;
    User mockUser;
    AuthenticationResponse mockResponse;
    SecurityUser securityUser;

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        mockRequest = mock(RegisterRequest.class);
        mockUser = User.builder().id(1L).username("test").build();
        mockResponse = mock(AuthenticationResponse.class);
        securityUser = SecurityUser.builder().user(mockUser).build();
        tokenRepository = mock(TokenRepository.class);

    }

    @Test
    @DisplayName("Given a valid username, when testFindUser is called, it should return an User")
    void testFindUser() {
        String username = "test";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(securityUser.getUser()));
        SecurityUser securityUser = service.findUser(username);
        assertEquals(securityUser.getUser().getUsername(), username);
    }

    @Test
    void testFindUserNotFound() {
        String username = "nonExistentUser";
        assertThrows(UserNotFoundException.class,
                () -> service.findUser(username));
    }

    @Test
    void testGetToken() {
        String accessToken = "access_token";

        Token token = Token.builder()
                .user(mockUser)
                .expired(false)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .accessToken(accessToken)
                .build();

        when(jwtTokenProvider.getToken(any(User.class), anyString())).thenReturn(token);
        Token resultToken = jwtTokenProvider.getToken(mockUser, accessToken);
        assertEquals(mockUser, resultToken.getUser());
        Assertions.assertFalse(resultToken.isExpired());
        Assertions.assertFalse(resultToken.isRevoked());
        assertEquals(TokenType.BEARER, resultToken.getTokenType());
        assertEquals(accessToken, resultToken.getAccessToken());
    }

//    @Test
//    void testFindAllValidToken() {
//        Token token1 = Token.builder().accessToken("access_token_1").build();
//        Token token2 = Token.builder().accessToken("access_token_2").build();
//        List<Token> expectedTokens = Arrays.asList(token1, token2);
//        when(tokenRepository.findAllValidAccessTokenByUserId(mockUser.getId())).thenReturn(expectedTokens);
//        when(jwtTokenProvider.findAllValidToken(mockUser)).thenReturn(expectedTokens);
//        List<Token> resultTokens = jwtTokenProvider.findAllValidToken(mockUser);
//        assertEquals(expectedTokens, resultTokens);
//        verify(tokenRepository, times(1)).findAllValidAccessTokenByUserId(mockUser.getId());
//    }

//    @Test
//    void testSave() {
//        Token token = new Token();
//        when(tokenRepository.save(token)).thenReturn(token);
//        Token savedToken = jwtTokenProvider.save(token);
//        assertEquals(token, savedToken);
//        verify(tokenRepository, times(1)).save(token);
//    }
}
