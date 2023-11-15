package com.store.gift.security.config;

import com.store.gift.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * Configuration class for web security.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private static final String USER = "ROLE_USER";
    private static final String ADMIN = "ROLE_ADMIN";
    /**
     * Handler for logging out the user.
     */
    private final LogoutHandler logoutHandler;

    /**
     * Handler for handling access denied situations.
     */
    private final AccessDeniedHandler accessDeniedHandler;

    /**
     * Filter for JWT authorization.
     */
    private final JwtAuthorizationFilter authorizationFilter;

    /**
     * Provider for authentication.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Entry point for authentication.
     */
    private final AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @return The security filter chain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            final HttpSecurity http) throws Exception {

        return http
                .csrf(CsrfConfigurer::disable)
                .cors(customizer -> customizer.configurationSource(request -> corsConfigurationSource()))
                .securityContext(customizer -> customizer.requireExplicitSave(false))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/upload/**").permitAll()
                                .requestMatchers(POST, "/signup", "/logout", "/login", "/upload/*").permitAll()
                                .requestMatchers(GET, "/tags/**", "/certificates/**").permitAll()
                                .requestMatchers(GET, "/orders/**", "/token/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(POST, "/orders/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(POST, "/users/**").hasAnyAuthority(ADMIN)
                                .requestMatchers("/**").hasAuthority(ADMIN)
                                .anyRequest()
                                .authenticated())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authenticationProvider(authenticationProvider)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .logout().logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
                .and().build();
    }

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) configuration source.
     *
     * @return The CORS configuration source.
     */
    private CorsConfiguration corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://192.168.31.177:4200",
                "http://localhost:5500",
                "http://localhost:4200",
                "http://127.0.0.1:5500",
                "http://127.0.0.1:8080",
                "http://127.0.0.1:4200",
                "https://gift-store.onrender.com",
                "https://gift-store-angular.netlify.app",
                "https://gift-store-certificate.netlify.app"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(360000L);
        configuration.setExposedHeaders(List.of("Authorization"));
        return configuration;
    }
}
