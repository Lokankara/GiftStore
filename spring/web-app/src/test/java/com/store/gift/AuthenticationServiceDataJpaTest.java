package com.store.gift;

import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.User;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.repository.UserRepository;
import com.store.gift.security.auth.AuthenticationRequest;
import com.store.gift.security.auth.AuthenticationResponse;
import com.store.gift.security.auth.RegisterRequest;
import com.store.gift.security.exception.InvalidJwtAuthenticationException;
import com.store.gift.security.service.AuthenticationService;
import com.store.gift.security.service.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import({AuthenticationService.class, JwtTokenProvider.class})
class AuthenticationServiceDataJpaTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private AuthenticationManager authenticationManager;
    private AuthenticationService service;
    private User user;
    private final String username = "Bob";
    private final String bob = "Feta";
    private final String email = "bob@i.ua";
    private final String password = "password";
    Long expired = 1000L;
    RegisterRequest registerRequest;
    SecurityUser securityUser;

    @BeforeEach
    public void setUp() {
        registerRequest = RegisterRequest.builder().email(email).password(password).username(username).build();
        service = new AuthenticationService(encoder, jwtTokenProvider, authenticationManager, userRepository);
        Mockito.when(encoder.encode(anyString())).thenReturn("encodedPassword");
        user = User.builder().email(email).password(password).username(username).build();
        userRepository.save(user);
        securityUser = SecurityUser.builder().user(user).build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findUserWhenUserExistsReturnsUser() {
        User save = userRepository.save(user);
        SecurityUser foundUser = service.findUser(username);
        assertEquals(foundUser.getUser(), save);
    }

    @Test
    void findUserWhenUserDoesNotExistThrowsException() {
        String username = "noneBob";
        assertThrows(UserNotFoundException.class, () -> service.findUser(username));
    }

    @Test
    void testAuthenticateSuccess() {
        AuthenticationRequest authenticationRequest =
                AuthenticationRequest.builder().username(username).password(password).build();
        AuthenticationResponse authenticationResponse = service.authenticate(authenticationRequest);
        assertNotNull(authenticationResponse);
        assertNotNull(authenticationResponse.getAccessToken());
        assertEquals(username, authenticationResponse.getUsername());
    }

    @Test
    void testAuthenticateUserNotFound() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("InvalidUsername");
        authenticationRequest.setPassword(password);
        assertThrows(UserNotFoundException.class, () -> service.authenticate(authenticationRequest));
    }

    @Test
    void testFindByUsername() {
        Optional<User> result = service.findByUsername(username);
        assertEquals(Optional.of(user), result);
    }

    @Test
    @DisplayName("Given a RegisterRequest, when saveUserWithRole is called, then return a User with the expected values")
    void testSaveUserWithRole() {
        RegisterRequest request = new RegisterRequest(bob, email, password);
        AuthenticationService authService =
                new AuthenticationService(encoder, jwtTokenProvider, null, userRepository);
        User user = authService.saveUserWithRole(request);
        assertEquals(bob, user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(email, user.getEmail());
    }

    @Test
    @DisplayName("Given a RegisterRequest with a new username, when signup is called, then return an AuthenticationResponse with the expected values")
    void testSignupWithNewUsername() {
        RegisterRequest request = new RegisterRequest("username", email, password);
        when(encoder.encode(password)).thenReturn(password);
        AuthenticationResponse response = service.signup(request);
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getExpiresAt());
        assertEquals("username", response.getUsername());
    }

    @Test
    @DisplayName("Given a valid authorization header and response, when refresh is called, then an AuthenticationResponse is returned")
    void refreshGivenValidAuthorizationHeaderAndResponseReturnsAuthenticationResponse() {
        String refreshToken = jwtTokenProvider.generateRefreshToken(securityUser);
        String authorizationHeader = "Bearer " + refreshToken;
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        AuthenticationResponse response = service.refresh(authorizationHeader, httpServletResponse);
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getExpiresAt());
    }

    @Test
    @DisplayName("Given an invalid authorization header and response, when refresh is called, then an InvalidJwtAuthenticationException is thrown")
    void refreshGivenInvalidAuthorizationHeaderAndResponseThrowsInvalidJwtAuthenticationException() {
        String invalidAuthorizationHeader = "Bearer" + bob;
        HttpServletResponse response = new MockHttpServletResponse();
        assertThrows(InvalidJwtAuthenticationException.class,
                () -> service.refresh(invalidAuthorizationHeader, response));
    }

    @Test
    @DisplayName("Given a RegisterRequest for a new user, when signup is called, then an AuthenticationResponse is returned")
    void signupGivenRegisterRequestForNewUserReturnsAuthenticationResponse() {
        RegisterRequest registerRequest = RegisterRequest.builder().username(bob).email(email).password(password).build();
        AuthenticationResponse authenticationResponse = service.signup(registerRequest);
        assertNotNull(authenticationResponse);
        assertNotNull(authenticationResponse.getAccessToken());
    }

    @Test
    @DisplayName("Given a user and a refresh token, when getAuthenticationResponse is called, then an AuthenticationResponse is returned with the correct values")
    void testGetAuthenticationResponses() {
        AuthenticationResponse response = service.getAuthenticationResponse(securityUser);
        assertEquals(username, response.getUsername());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getExpiresAt());
    }

    @Test
    @DisplayName("Given an AuthenticationRequest, when authenticate is called, then return an AuthenticationResponse")
    void testAuthenticate() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(password);
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.getExpiration()).thenReturn(1000L);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        AuthenticationResponse response = service.getAuthenticationResponse(securityUser);
        assertEquals(username, response.getUsername());
        assertNotNull(response.getAccessToken());
    }

    @Test
    @DisplayName("Given a user, jwtToken, and accessToken, when getResponse is called, then return an AuthenticationResponse with the expected values")
    void testGetResponse() {
        JwtTokenProvider provider = mock(JwtTokenProvider.class);
        when(provider.getExpiration()).thenReturn(expired);
        AuthenticationResponse response = service.getAuthenticationResponse(securityUser);
        assertEquals(username, response.getUsername());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getExpiresAt());
    }

    @Test
    @DisplayName("Given a user and a refresh token, when getAuthenticationResponse is called, then an AuthenticationResponse is returned with the correct values")
    void testGetAuthenticationResponse() {
        AuthenticationResponse response = service.getAuthenticationResponse(securityUser, "testRefreshToken");
        assertEquals(username, response.getUsername());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNotNull(response.getExpiresAt());
    }

    @Test
    @DisplayName("Given an AuthenticationRequest with a valid username, when login is called, then return an AuthenticationResponse with the expected values")
    void testLoginWithValidUsername() {
        AuthenticationRequest request = new AuthenticationRequest(username, password);
        AuthenticationResponse result = service.login(request);
        assertNotNull(result.getAccessToken());
        assertEquals(username, result.getUsername());
    }

    @Test
    @DisplayName("Given a valid HttpServletRequest with an Authorization header, when logout is called, then set the response status to SC_OK and write 'Logout successful' to the response writer")
    void testLogoutWithValidAuthorizationHeader() throws IOException {

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(response.getWriter()).thenReturn(printWriter);
        service.logout(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        assertEquals("Logout successful", stringWriter.toString());
    }
}
