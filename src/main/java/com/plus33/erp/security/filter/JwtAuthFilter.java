/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : JwtAuthFilter.java
 * Path              : src/main/java/com/plus33/erp/security/filter/JwtAuthFilter.java
 * Purpose           : Intercepts every HTTP request, validates the Bearer JWT token,
 *                     extracts user identity and authorities, and populates the
 *                     Spring SecurityContext for downstream @PreAuthorize enforcement.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * OncePerRequestFilter that forms the backbone of stateless JWT authentication
 * for the PLUS33 Coffee ERP API. Registered in SecurityConfig before
 * UsernamePasswordAuthenticationFilter in the Spring Security filter chain.
 *
 * doFilterInternal flow:
 *   1. Short-circuits for public paths: /api/v1/auth/login, /swagger-ui/**,
 *      /v3/api-docs/**, /api-docs/**. Passes directly to filter chain.
 *   2. Reads Authorization header. If absent or not "Bearer " prefixed,
 *      passes through without authentication (SecurityConfig will reject
 *      protected paths with 401).
 *   3. Strips "Bearer " prefix and decodes the JWT using the injected
 *      JwtDecoder (HMAC-SHA256 symmetric key).
 *   4. If SecurityContext has no existing authentication, extracts the
 *      "authorities" claim as List<String>, converts to SimpleGrantedAuthority
 *      list, builds UsernamePasswordAuthenticationToken from the JWT subject
 *      (email) and authority list, and sets it in SecurityContextHolder.
 *   5. Invalid/expired/tampered tokens throw JwtException which is caught and
 *      logged at WARN level. The request continues without authentication,
 *      causing 401 from SecurityConfig authenticationEntryPoint.
 *   6. Always invokes filterChain.doFilter() to allow the request to proceed.
 *
 * Dependencies:
 *   - JwtDecoder (security.config.JwtConfig) — decodes and verifies JWT
 *   - SecurityContextHolder — sets authenticated principal per request
 ******************************************************************************/
package com.plus33.erp.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code JwtAuthFilter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.filter}</p>
 * <p><b>Layer  :</b> Spring Security Filter: validates JWT Bearer token on every HTTP request.</p>
 *
 * <p><b>JWT Security Flow:</b></p>
 * <pre>
 * Every HTTP Request
 *   --> JwtAuthFilter.doFilterInternal()
 *   --> Extract Authorization: Bearer token
 *   --> Decode and verify HMAC-SHA256 signature
 *   --> Extract subject (email) + authorities (permissions)
 *   --> SecurityContextHolder.setAuthentication(...)
 *   --> @PreAuthorize checks succeed --> Controller executes
 * </pre>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Performs the doFilterInternal operation in this module.
     *
     */
    /**
     * Performs the doFilterInternal operation in this module.
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip filter for public endpoints
        String path = request.getServletPath();
        log.debug("JwtAuthFilter invoked for path: {}", path);
        if (path.equals("/api/v1/auth/login")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            var jwt = jwtDecoder.decode(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                List<String> authorities = jwt.getClaimAsStringList("authorities");

                var grantedAuthorities = (authorities == null ? List.<String>of() : authorities)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                var authentication = new UsernamePasswordAuthenticationToken(
                        jwt.getSubject(), null, grantedAuthorities);
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}