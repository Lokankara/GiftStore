package com.store.gift.security.service;

import com.store.gift.entity.Role;
import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.Token;
import com.store.gift.entity.User;
import com.store.gift.exception.UserAlreadyExistsException;
import com.store.gift.exception.UserNotFoundException;
import com.store.gift.repository.UserRepository;
import com.store.gift.security.auth.AuthenticationRequest;
import com.store.gift.security.auth.AuthenticationResponse;
import com.store.gift.security.auth.RegisterRequest;
import com.store.gift.security.exception.InvalidJwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

import static com.store.gift.entity.RoleType.USER;
import static java.lang.String.format;
import static java.time.Instant.now;

/**
 * Service class that handles user authentication and registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class AuthenticationService implements AuthService {
    private final PasswordEncoder encoder;
    private final JwtTokenProvider provider;
    private final AuthenticationManager manager;
    private final UserRepository userRepository;

    /**
     * Registers a new user with the provided registration request.
     * <p>
     * Generate a token for the saved user and create a Token object
     *
     * @param request The registration request containing user details.
     * @return The authentication response containing the generated token.
     */
    @Transactional
    public AuthenticationResponse signup(
            final RegisterRequest request) {
        if (findByUsername(request.getUsername())
                .isPresent()) {
            throw new UserAlreadyExistsException(
                    "User already exists with name "
                            + request.getUsername());
        }
        SecurityUser user = SecurityUser.builder()
                .user(saveUserWithRole(request))
                .build();
        log.info("signup service: " + user);
        return getAuthenticationResponse(user);
    }

    /**
     * Performs the login operation for the given authentication request.
     *
     * @param request the authentication request containing the username and password
     * @return the authentication response containing the JWT token and other details
     */
    @Transactional
    public AuthenticationResponse login(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        return getAuthenticationResponse(user);
    }

    /**
     * Authenticates a user with the provided credentials.
     * <p>
     * Find the user in the database based on the username
     *
     * @param request The authentication request containing the username and password.
     * @return An AuthenticationResponse object containing the generated token.
     * @throws UserNotFoundException If the user is not found in the database.
     */
    @Transactional
    public AuthenticationResponse authenticate(
            final AuthenticationRequest request) {
        setAuthenticationToken(request);
        SecurityUser user = findUser(request.getUsername());
        provider.revokeAllUserTokens(user.getUser());
        return getAuthenticationResponse(user);
    }

    /**
     * Refreshes the access token for the current user.
     *
     * @param authorizationHeader The HttpServletRequest object.
     * @param response            The HttpServletResponse object.
     * @return AuthenticationResponse containing the refreshed access token.
     */
    @Transactional
    public AuthenticationResponse refresh(
            final String authorizationHeader,
            final HttpServletResponse response) {
        if (provider.isBearerToken(authorizationHeader)) {
            String refreshToken = authorizationHeader.substring(7);
            String username = provider.getUsername(refreshToken);
            if (username != null) {
                SecurityUser user = findUser(username);
                if (provider.isTokenValid(refreshToken, user)) {
                    String accessToken = provider.generateToken(user);
                    Token token = provider.updateUserTokens(user.getUser(), accessToken);
                    return AuthenticationResponse.builder()
                            .username(username)
                            .accessToken(token.getAccessToken())
                            .refreshToken(refreshToken)
                            .expiresAt(new Timestamp(token.getAccessTokenTTL()))
                            .build();
                }
            }
        }
        throw new InvalidJwtAuthenticationException("Invalid Jwt Authentication");
    }

    /**
     * Logs out the current user by invalidating the access token and clearing any associated session data.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     */
    public void logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Token token = provider.findByToken(jwt).orElse(null);
            if (token != null) {
                token.setExpired(true);
                token.setRevoked(true);
                provider.save(token);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write("Logout successful");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Sets the authentication token for the provided username and password.
     *
     * @param request The AuthenticationRequest containing the username and password.
     */
    private void setAuthenticationToken(
            final AuthenticationRequest request) {
        Authentication authenticate = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext()
                .setAuthentication(authenticate);
    }

    /**
     * Retrieves the user with the given username.
     *
     * @param username The username of the user to find.
     * @return The User object.
     * @throws UserNotFoundException if the user is not found.
     */
    @Transactional
    public SecurityUser findUser(final String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        format("User not found: %s", username)));
        return SecurityUser.builder().user(user).build();
    }

    /**
     * Saves a new user with the provided registration details and default role.
     *
     * @param request The RegisterRequest containing the user registration details.
     * @return The saved User object.
     */
    @Transactional
    public User saveUserWithRole(
            final RegisterRequest request) {
        User user = User.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .role(getRole())
                .build();
        log.info("saveUserWithRole: " + user);
        return userRepository.save(user);
    }

    /**
     * Finds the user with the given username.
     *
     * @param username The username of the user to find.
     * @return Optional containing the User object, or empty if not found.
     */
    @Transactional
    public Optional<User> findByUsername(
            final String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Returns the Role object representing the default role for a user.
     *
     * @return The default Role object.
     */
    private static Role getRole() {
        return Role.builder()
                .permission(USER)
                .build();
    }

    /**
     * Generates the authentication response for the given user.
     * It generates a JWT token using the specified provider and updates the user's tokens.
     *
     * @param user the user for which the authentication response is generated
     * @return the authentication response containing the JWT token and other details
     */
    @Transactional
    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user) {
        String jwtToken = provider.generateToken(user);
        Token token = provider.updateUserTokens(user.getUser(), jwtToken);
        return getAuthenticationResponse(user, jwtToken,
                token.getAccessTokenTTL());
    }

    /**
     * Generates the authentication response for the given user with the provided access token.
     *
     * @param user        the user for which the authentication response is generated
     * @param accessToken the access token to include in the authentication response
     * @return the authentication response containing the user, access token, and expiration time
     */
    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user, final String accessToken) {
        return getAuthenticationResponse(
                user, accessToken,
                provider.getExpiration());
    }

    /**
     * Constructs the AuthenticationResponse object with the user details, JWT token, and access token expiration.
     *
     * @param user        The User object.
     * @param jwtToken    The JWT token.
     * @param accessToken The access token expiration time.
     * @return The AuthenticationResponse object.
     */
    public AuthenticationResponse getAuthenticationResponse(
            final SecurityUser user,
            final String jwtToken,
            final Long accessToken) {
        return AuthenticationResponse.builder()
                .id(user.getUser().getId())
                .username(user.getUsername())
                .expiresAt(Timestamp.from(now()
                        .plusMillis(accessToken)))
                .refreshToken(provider.generateRefreshToken(user))
                .accessToken(jwtToken)
                .build();
    }
}
