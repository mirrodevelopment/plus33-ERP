/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : JwtConfig.java
 * Path              : src/main/java/com/plus33/erp/security/config/JwtConfig.java
 * Purpose           : Provides the JwtEncoder and JwtDecoder Spring beans backed by
 *                     a shared HMAC-SHA256 symmetric secret key for JWT signing
 *                     and verification throughout the ERP platform.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Configuration class that wires the JWT cryptographic infrastructure
 * for PLUS33 Coffee ERP using the Nimbus JOSE library.
 *
 * Secret Key:
 *   Read from the 'jwt.secret' application property (application.properties).
 *   Converted to a SecretKeySpec with HmacSHA256 algorithm.
 *   The same key is used for both signing (encoder) and verification (decoder),
 *   implementing a symmetric HMAC approach.
 *
 * jwtEncoder():
 *   Returns a NimbusJwtEncoder backed by ImmutableSecret<OctetSequenceKey>.
 *   Used exclusively by JwtService.generateToken() to produce signed JWTs
 *   after successful login.
 *
 * jwtDecoder():
 *   Returns a NimbusJwtDecoder configured with the same SecretKeySpec.
 *   Used by JwtAuthFilter.doFilterInternal() to decode and verify Bearer tokens
 *   on every authenticated API request, and by SecurityConfig as its
 *   OAuth2 resource server JWT decoder.
 *
 * Changing jwt.secret invalidates all existing tokens immediately (no revocation
 * list). Used by: JwtService (encoding), JwtAuthFilter (decoding), SecurityConfig.
 ******************************************************************************/
package com.plus33.erp.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code JwtConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.config}</p>
 * <p><b>Layer  :</b> Spring Configuration: defines and registers beans for Security Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Performs the jwtEncoder operation in this module.
     *
     * @return the JwtEncoder result
     */
    @Bean
    public JwtEncoder jwtEncoder() {

        var secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));
    }

    /**
     * Performs the jwtDecoder operation in this module.
     *
     * @return the JwtDecoder result
     */
    @Bean
    public JwtDecoder jwtDecoder() {

        var secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}