package com.store.gift.controller.auth;

import com.store.gift.entity.Role;
import com.store.gift.entity.RoleType;
import com.store.gift.entity.User;
import com.store.gift.security.auth.AuthenticationRequest;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.store.gift.controller.TestConfig.getCrudHttpRequests;
import static com.store.gift.controller.TestConfig.withAuthorities;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class AdminPermissionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String username = "Admin";
    private final String password = "1";
    private final String admin = "ROLE_ADMIN";
    private final String adminContent = "{\"username\":\"" + admin + "\",\"password\":\"" + password + "\"}";
    User userDetails;

    @BeforeEach
    void logoutBeforeTest() {
        SecurityContextHolder.clearContext();
    }

    @BeforeEach
    void clearDatabase() {
        Long id = 1100L;
        String email = "admin@i.ua";
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER)
                .build()).email(email).password(password).username(username).build();
    }

    @Test
    @DisplayName("Given a user with 'admin' permission, when accessing Certificate CRUD endpoints, then should receive an 200 response")
    void givenUserHasPermissionAdminWhenAccessCertificatesEndpoints() throws Exception {
        String host = "/api/certificates";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("Given a user with 'admin' permission, should be able to access tags, then should receive an 200 response")
    void administratorWhenAccessTagsThenAllowedEndpoints() throws Exception {
        String host = "/api/tags";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin).content(adminContent))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("Given a user with 'admin' permission, when accessing Order CRUD endpoints, then should receive an 200 response")
    void givenUserWithAdminPermissionWhenAccessingOrdersEndpoints() throws Exception {
        String host = "/api/orders";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("Given admin user, when accessing User CRUD endpoints, then should receive an 200 response")
    void administratorWhenAccessUsersThenAllowed() throws Exception {
        String host = "/api/users";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("Given a user with 'admin' permission, when accessing to access specific certificate CRUD endpoints, then should receive an 200 response")
    void givenUserHasPermissionAdminWhenAccessCertificateEndpoints() throws Exception {
        String host = "/api/certificates/1";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin)
                            .content(adminContent)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("Given User with CRUD scope and admin role should be authorized to access specific endpoints, then should receive an 200 response")
    void givenAdminWhenCrudScopeAccessTagThenAuthorized() throws Exception {
        String host = "/api/tags/1";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin)
                            .content(adminContent)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("Given a user with 'admin' permission, when accessing specific Order CRUD endpoints, then should receive an 200 response")
    void givenUserWithAdminPermissionWhenAccessingOrderEndpoints() throws Exception {
        String host = "/api/orders/1";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("Given User with CRUD scope and admin role should be authorized to access specific user, then return HTTP 200 OK")
    void givenAdminWhenCrudScopeAccessUsersThenAuthorized() throws Exception {
        String host = "/api/users/1";
        for (MockHttpServletRequestBuilder mock : getCrudHttpRequests(host)) {
            mockMvc.perform(withAuthorities(mock, admin)
                            .content(adminContent)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("Given valid request, when registering a new user as admin, then return HTTP 200 OK")
    void administratorWhenRegisterThenAllowed() throws Exception {
        String host = "/api/token/register";
        mockMvc.perform(withAuthorities(post(host)
                        .content(adminContent)
                        .contentType(APPLICATION_JSON), admin))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Administrator should be able to authenticate, then return HTTP 200 OK")
    void administratorWhenAuthenticateThenAllowed() throws Exception {
        String host = "/api/token/authenticate";
        mockMvc.perform(withAuthorities(post(host)
                        .content(adminContent)
                        .contentType(APPLICATION_JSON), admin))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given valid authentication request, when authenticating a user, then return HTTP 200 OK with token")
    void givenValidAuthenticationRequestWhenAuthenticatingUserThenReturnsToken() throws Exception {
        String host = "/api/token/refresh";
        MvcResult result = mockMvc.perform(withAuthorities(post(host), admin)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AuthenticationRequest(username, password))))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }
}
