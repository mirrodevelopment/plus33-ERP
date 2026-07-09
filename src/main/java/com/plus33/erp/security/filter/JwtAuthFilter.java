/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.filter
 * File              : JwtAuthFilter.java
 * Purpose           : Spring Security filter for JWT authentication and authorization
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JwtAuthFilterController
 * Related Service   : JwtAuthFilterService, JwtAuthFilterServiceImpl
 * Related Repository: JwtAuthFilterRepository
 * Related Entity    : JwtAuthFilter
 * Related DTO       : HttpServletRequest, HttpServletResponse
 * Related Mapper    : JwtAuthFilterMapper
 * Related DB Table  : jwt_auth_filters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Security Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Security Module within the PLUS33 Coffee ERP platform.
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