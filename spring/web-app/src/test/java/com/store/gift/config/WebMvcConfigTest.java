package com.store.gift.config;

import com.store.gift.entity.SecurityUser;
import com.store.gift.entity.User;
import com.store.gift.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
class WebMvcConfigTest {

    private User user;
    private SecurityUser securityUser;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);

    @Autowired
    private WebApplicationContext context;

    String username = "test";

    @BeforeEach
    public void setup() {
        user = User.builder().id(1L).username("test")
                .email("email@email.ua").password("password").build();
        securityUser = SecurityUser.builder().user(user).build();

    }

    @Test
    void testLoadUserByUsername() {
        user.setUsername(username);
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void userDetailsServiceShouldReturnUserDetails() {
        userRepository.save(securityUser.getUser());
        Optional<User> byUsername = userRepository.findByUsername("test");
        assertNotNull(userDetailsService.loadUserByUsername("test"));
        assertTrue(byUsername.isPresent());
        userRepository.delete(securityUser.getUser());
    }

    @Test
    void testUserDetailsServiceUserNotFound() {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        UserDetailsService userDetailsService = context.getBean(UserDetailsService.class);
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
    }
    @Test
    void testAccessDeniedHandler() throws IOException, ServletException {
        AccessDeniedException deniedException = mock(AccessDeniedException.class);
        doAnswer(invocation -> {
            int status = invocation.getArgument(0);
            when(response.getStatus()).thenReturn(status);
            return null;
        }).when(response).setStatus(anyInt());

        doAnswer(invocation -> {
            String contentType = invocation.getArgument(0);
            when(response.getContentType()).thenReturn(contentType);
            return null;
        }).when(response).setContentType(anyString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new WebMvcConfig().accessDeniedHandler()
                .handle(request, response, deniedException);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertEquals("{\"statusCode\":\"FORBIDDEN\",\"errorMessage\":\"Access Denied\"}",
                stringWriter.toString());
    }

    @Test
    void testAuthenticationEntryPoint() throws IOException, ServletException {
        AuthenticationException exception = mock(AuthenticationException.class);

        doAnswer(invocation -> {
            int status = invocation.getArgument(0);
            when(response.getStatus()).thenReturn(status);
            return null;
        }).when(response).setStatus(anyInt());

        doAnswer(invocation -> {
            String contentType = invocation.getArgument(0);
            when(response.getContentType()).thenReturn(contentType);
            return null;
        }).when(response).setContentType(anyString());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new WebMvcConfig().authenticationEntryPoint()
                .commence(request, response, exception);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertEquals("{\"statusCode\":\"UNAUTHORIZED\",\"errorMessage\":\"Unauthorized\"}",
                stringWriter.toString());
    }
}
