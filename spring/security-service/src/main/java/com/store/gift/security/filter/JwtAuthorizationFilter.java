package com.store.gift.security.filter;

import com.store.gift.security.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Custom JWT authorization filter that extends OncePerRequestFilter.
 * <p>
 * This filter is responsible for handling JWT authorization for each incoming request.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider provider;
    private final UserDetailsService userDetailsService;

    /**
     * Method that gets executed for each incoming request to perform JWT authorization.
     * <p>
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>
     * Provides HttpServletRequest and HttpServletResponse arguments
     * instead of the default ServletRequest and ServletResponse ones.
     *
     * @param request     The incoming HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain for invoking the next filter.
     * @throws ServletException if any servlet-related errors occur.
     * @throws IOException      if any I/O errors occur.
     */
    @Override
    protected void doFilterInternal(
            final @NonNull HttpServletRequest request,
            final @NonNull HttpServletResponse response,
            final @NonNull FilterChain filterChain)
            throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                SecurityContext securityContext = SecurityContextHolder.getContext();
                UserDetails userDetails = userDetailsService
                        .loadUserByUsername(provider.getUsername(jwt));
                boolean isValid = provider.findByToken(jwt)
                        .map(token -> !token.isExpired()
                                && !token.isRevoked())
                        .orElse(false);
                if (isValid && provider.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null,
                                    userDetails.getAuthorities());
                    log.info("security Context: " + securityContext);
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    securityContext.setAuthentication(authToken);
                }
            }
        filterChain.doFilter(request, response);
    }
}
