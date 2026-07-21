/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : JwtService.java
 * Path              : src/main/java/com/plus33/erp/security/jwt/JwtService.java
 * Purpose           : Generates signed HMAC-SHA256 JWT access tokens and provides
 *                     token expiry duration for the PLUS33 ERP authentication flow.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Service component responsible for JWT token generation used by
 * AuthController.login() after successful credential verification.
 *
 * generateToken(UserDetails):
 *   Builds a JwtClaimsSet with the following claims:
 *     issuer    — "plus33-erp" (identifies this ERP as token issuer)
 *     issuedAt  — current Instant
 *     expiresAt — current Instant + expirationMinutes (configurable via
 *                 app.jwt.expiration-minutes, default 60)
 *     subject   — the user's email address (from UserDetails.getUsername())
 *     authorities — list of granted authority strings (ROLE_* + permissions)
 *   Signs with MacAlgorithm.HS256 using the shared JwtEncoder bean
 *   configured with the HMAC secret from application properties.
 *
 * getExpirationSeconds():
 *   Converts expirationMinutes to seconds. Returned inside TokenResponse
 *   so the frontend SPA can manage session expiry timers.
 *
 * Does not validate or decode tokens. Token decoding is performed by
 * JwtDecoder (configured in JwtConfig) and used by JwtAuthFilter.
 *
 * Dependencies:
 *   - JwtEncoder (security.config.JwtConfig) — HMAC signing key encoder
 *   - app.jwt.expiration-minutes (application.properties) — token validity
 ******************************************************************************/
package com.plus33.erp.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code JwtService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.jwt}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Security Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * JwtController
 *   --> JwtService (this)
 *   --> Validate business rules
 *   --> JwtRepository (read/write 'jwts')
 *   --> JwtMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code jwts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.expiration-minutes:60}")
    private long expirationMinutes;

    /**
     * Generates the token based on input parameters and business rules.
     *
     * @param userDetails the userDetails input value
     * @return the result string value
     */
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("plus33-erp")
                .issuedAt(now)
                .expiresAt(now.plus(expirationMinutes, ChronoUnit.MINUTES))
                .subject(userDetails.getUsername())
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .toList())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Retrieves expiration seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getExpirationSeconds() {
        return expirationMinutes * 60;
    }
}