package com.store.gift.config;

import com.store.gift.entity.SecurityUser;
import com.store.gift.handler.ResponseMessage;
import com.store.gift.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Configuration class for Spring Web MVC.
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * The context path of the server servlet.
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * Adds view controllers for specific paths.
     *
     * @param registry the view controller registry
     */
    @Override
    public void addViewControllers(
            final ViewControllerRegistry registry) {
        registry.addViewController(contextPath)
                .setViewName("forward:/index.html");
    }

    /**
     * Configures default servlet handling.
     *
     * @param configurer the default servlet handler configurer
     */
    @Override
    public void configureDefaultServletHandling(
            final DefaultServletHandlerConfigurer configurer) {
        configurer.enable("default");
    }

    /**
     * Configures content negotiation.
     *
     * @param configurer the content negotiation configurer
     */
    @Override
    public void configureContentNegotiation(
            final ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(APPLICATION_JSON);
    }

    /**
     * Creates a handler mapping for serving the favicon.ico file.
     *
     * @return the favicon handler mapping
     */
    @Bean
    public SimpleUrlHandlerMapping faviconHandlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("/favicon.ico",
                faviconRequestHandler()));
        return mapping;
    }

    /**
     * Creates a request handler for serving the favicon.ico file.
     *
     * @return the favicon request handler
     */
    @Bean
    public ResourceHttpRequestHandler faviconRequestHandler() {
        ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
        requestHandler.setLocations(Collections.singletonList(new ClassPathResource("/")));
        requestHandler.setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS));
        return requestHandler;
    }

    /**
     * Creates an instance of the authentication provider used for user authentication.
     *
     * @param userDetailsService The UserDetailsService implementation.
     * @param passwordEncoder    The PasswordEncoder implementation.
     * @return An AuthenticationProvider instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            final UserDetailsService userDetailsService,
            final PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Creates an instance of the UserDetailsService.
     *
     * @param userRepository The UserRepository implementation.
     * @return A UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepository) {
        return username -> SecurityUser.builder()
                .user(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with name %s", username))))
                .build();
    }

    /**
     * Creates an instance of the PasswordEncoder.
     *
     * @return A PasswordEncoder instance.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an instance of the AuthenticationManager.
     *
     * @param config The AuthenticationConfiguration.
     * @return An AuthenticationManager instance.
     * @throws Exception If an error occurs during the creation of the AuthenticationManager.
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Creates a SecurityContextLogoutHandler bean.
     *
     * @return The configured SecurityContextLogoutHandler bean.
     */
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler =
                new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);
        logoutHandler.setClearAuthentication(true);
        return logoutHandler;
    }

    /**
     * Creates and configures an AuthenticationEntryPoint bean.
     * The authentication entry point handles unauthorized access.
     *
     * @return AuthenticationEntryPoint bean
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            ResponseMessage errorResponse = new ResponseMessage(
                    HttpStatus.UNAUTHORIZED, "Unauthorized");
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ObjectMapper()
                    .writeValueAsString(errorResponse));
        };
    }

    /**
     * Creates and configures an AccessDeniedHandler bean.
     * The access denied handler handles forbidden access.
     *
     * @return AccessDeniedHandler bean
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            ResponseMessage errorResponse = new ResponseMessage(
                    HttpStatus.FORBIDDEN, "Access Denied");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(new ObjectMapper()
                    .writeValueAsString(errorResponse));
        };
    }
}
