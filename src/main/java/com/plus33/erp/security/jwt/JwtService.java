/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.jwt
 * File              : JwtService.java
 * Purpose           : Business logic service layer for Security Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JwtController
 * Related Service   : JwtService
 * Related Repository: JwtRepository
 * Related Entity    : Jwt
 * Related DTO       : N/A
 * Related Mapper    : JwtMapper
 * Related DB Table  : jwts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JwtController, JwtServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Security Module. Implements JwtService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
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