package com.store.gift.controller.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.store.gift.controller.TestConfig.getCrudHttpRequests;
import static com.store.gift.controller.TestConfig.withAuthorities;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class GuestPermissionIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final Long id = 1100L;
    private final String guest = "ROLE_GUEST";
    private final String forbidden = "{\"statusCode\":\"FORBIDDEN\",\"errorMessage\":\"Access Denied\"}";
    private final String unauthorized = "{\"statusCode\":\"UNAUTHORIZED\",\"errorMessage\":\"Unauthorized\"}";

    @BeforeEach
    void logoutBeforeTest() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("User with wrong scope should be forbidden")
    void userWhenWrongScopeThenForbidden() throws Exception {
        String host = "/api/users";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(mock.with(jwt().jwt(t -> t.claim("user_id", this.id.toString())
                                    .claim("scope", guest))
                            .authorities(new SimpleGrantedAuthority(guest))))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    @DisplayName("Given guest user with guest permission, when accessing orders, then return HTTP 403 Forbidden")
    void guestWhenHasGuestReadPermissionThenForbidden() throws Exception {
        String host = "/api/users/1";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, guest))
                    .andExpect(status().isForbidden())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/certificates")
    void guestWhenAccessCertificatesThenForbidden() throws Exception {
        String host = "/api/certificates";
        List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
        for (int i = 1; i < httpRequests.size(); i++) {
            mockMvc.perform(httpRequests.get(i)).andExpect(status().is4xxClientError())
                    .andExpect(content().json(unauthorized));
        }
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/tags")
    void guestWhenAccessTagsThenForbidden() throws Exception {
        String host = "/api/tags";
        List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
        for (int i = 1; i < httpRequests.size(); i++) {
            mockMvc.perform(httpRequests.get(i)).andExpect(status().is4xxClientError())
                    .andExpect(content().json(unauthorized));
        }
    }

    @Test
    @DisplayName("Guest user should be forbidden from accessing /api/users")
    void guestWhenAccessUsersThenForbidden() throws Exception {
        String host = "/api/users";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(mock.with(jwt().authorities(new SimpleGrantedAuthority(guest))))
                    .andExpect(status().isForbidden());
        }
    }
}
