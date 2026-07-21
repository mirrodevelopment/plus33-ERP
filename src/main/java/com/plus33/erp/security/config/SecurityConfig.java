/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : SecurityConfig.java
 * Path              : src/main/java/com/plus33/erp/security/config/SecurityConfig.java
 * Purpose           : Configures the Spring Security filter chain, HTTP authorization
 *                     rules, stateless session policy, JWT filter integration, and
 *                     authentication provider beans for the entire ERP platform.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Central Spring Security configuration class for PLUS33 Coffee ERP. Annotated
 * with @EnableWebSecurity and @EnableMethodSecurity to activate filter chain
 * and method-level @PreAuthorize enforcement.
 *
 * Security Filter Chain (securityFilterChain):
 *   - Disables CSRF (stateless JWT API — no browser sessions).
 *   - Disables HTTP Basic and form-based login.
 *   - Enforces STATELESS session management — no server-side sessions.
 *   - Permits unauthenticated access to:
 *       /api/v1/auth/login, /actuator/health, /swagger-ui/**, /v3/api-docs/**
 *   - All other requests require a valid Bearer JWT.
 *   - Custom 401 JSON response body for unauthorized requests.
 *   - Inserts JwtAuthFilter before UsernamePasswordAuthenticationFilter.
 *
 * Authentication Provider (authenticationProvider):
 *   - Uses DaoAuthenticationProvider backed by UserDetailsServiceImpl.
 *   - Verifies BCrypt-encoded passwords via the shared PasswordEncoder bean.
 *
 * Authentication Manager (authenticationManager):
 *   - Exposes the Spring AuthenticationManager as a bean, injected into
 *     AuthController to authenticate login requests.
 *
 * Dependencies:
 *   - JwtDecoder (security.config.JwtConfig) — for JwtAuthFilter construction
 *   - UserDetailsService (security.service.UserDetailsServiceImpl) — user lookup
 *   - PasswordEncoder (security.config.SecurityBeansConfig) — BCrypt matching
 ******************************************************************************/
package com.plus33.erp.security.config;

import com.plus33.erp.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code SecurityConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.config}</p>
 * <p><b>Layer  :</b> Spring Configuration: defines and registers beans for Security Module.</p>
 *
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Performs the securityFilterChain operation in this module.
     *
     * @param http the http input value
     * @return the SecurityFilterChain result
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"success\":false,\"message\":\"Unauthorized: " + authException.getMessage() + "\"}");
                        })
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthFilter(jwtDecoder), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Performs the authenticationProvider operation in this module.
     *
     * @return the AuthenticationProvider result
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Spring Security 7.0: UserDetailsService is a required constructor argument
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * Performs the authenticationManager operation in this module.
     *
     * @param config the config input value
     * @return the AuthenticationManager result
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}