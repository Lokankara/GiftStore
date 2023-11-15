package com.store.gift.controller.auth;

import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static com.store.gift.controller.TestConfig.getCrudHttpRequests;
import static com.store.gift.controller.TestConfig.withAuthorities;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class AuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final Long id = 1100L;
    private final String username = "bob";
    private final String email = "user@i.ua";
    private final String password = "bob";
    private final String userRead = "ROLE_USER";
    private final String userContent = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    User userDetails;

    @BeforeEach
    void logoutBeforeTest() {
        SecurityContextHolder.clearContext();
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER)
                .build()).email(email).password(password).username(username).build();
    }

    @Test
    @DisplayName("Given user, when accessing Http CRUD users, then return 302 Redirect")
    void anonymousWhenAccessOrderThenRedirection() throws Exception {
        for (String host : Arrays.asList("/api/users", "/api/users/1")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().is4xxClientError());
            }
        }
    }

    @Test
    @DisplayName("Anonymous user should be redirected from Http CRUD accessing orders")
    void anonymousWhenAccessUsersThenForbidden() throws Exception {
        List<String> hosts = Arrays.asList("/api/orders", "/api/orders/1", "/api/orders/users/1", "/api/orders/1/users/1", "/api/orders/users/1/most");
        for (String host : hosts) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().is4xxClientError());
            }
        }
    }

    @Test
    @DisplayName("Given user, when accessing Http CUD certificates, then return 302 Redirect")
    void anonymousWhenAccessCertificatesThenRedirection() throws Exception {
        for (String host : Arrays.asList("/api/certificates", "/api/certificates/1")) {
            List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
            for (int i = 1; i < httpRequests.size(); i++) {
                MockHttpServletRequestBuilder mock = httpRequests.get(i);
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().is4xxClientError());
            }
        }
    }

    @Test
    @DisplayName("Given user should be redirected from accessing Http CUD tags, then return 401 Unauthorized")
    void anonymousWhenAccessTagsThenUnauthorized() throws Exception {
        for (String host : Arrays.asList("/api/tags", "/api/tags/1")) {
            @NotNull List<MockHttpServletRequestBuilder> httpRequests = getCrudHttpRequests(host);
            for (int i = 1; i < httpRequests.size(); i++) {
                MockHttpServletRequestBuilder mock = httpRequests.get(i);
                mockMvc.perform(mock.content(userContent))
                        .andExpect(status().is4xxClientError());
            }
        }
    }

    @Test
    @DisplayName("User with read scope should be authorized, when accessing /api/users, then return HTTP 403 forbidden to access specific user and all users")
    void userWhenReadScopeUserThenForbidden() throws Exception {
        for (String host : Arrays.asList("/api/users", "/api/users/1")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(withAuthorities(mock, userRead))
                        .andExpect(status().is4xxClientError())
                        .andReturn();
            }
        }
    }

    @Test
    @DisplayName("Given user with USER_READ permission, when accessing /api/orders, then return HTTP 403 Forbidden")
    void userWhenHasUserReadPermissionThenForbidden() throws Exception {
        for (String host : Arrays.asList("/api/orders", "/api/orders/2")) {
            for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
                mockMvc.perform(withAuthorities(mock, userRead))
                        .andExpect(status().is4xxClientError());
            }
        }
    }
}
