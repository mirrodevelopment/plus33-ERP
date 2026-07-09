/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.config
 * File              : JwtConfig.java
 * Purpose           : Spring Configuration bean for Security Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JwtConfigController
 * Related Service   : JwtConfigService, JwtConfigServiceImpl
 * Related Repository: JwtConfigRepository
 * Related Entity    : JwtConfig
 * Related DTO       : N/A
 * Related Mapper    : JwtConfigMapper
 * Related DB Table  : jwt_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Security Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Security Module within the PLUS33 Coffee ERP platform.
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