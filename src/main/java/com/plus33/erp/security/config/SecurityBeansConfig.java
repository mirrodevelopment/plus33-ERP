/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : SecurityBeansConfig.java
 * Path              : src/main/java/com/plus33/erp/security/config/SecurityBeansConfig.java
 * Purpose           : Registers the BCrypt PasswordEncoder Spring bean used for
 *                     password hashing during user creation and credential verification
 *                     throughout the ERP authentication flow.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Minimal Spring @Configuration class that exposes the PasswordEncoder bean.
 * Separated from SecurityConfig to avoid circular dependency issues between
 * security configuration beans.
 *
 * passwordEncoder():
 *   Returns a BCryptPasswordEncoder instance. BCrypt is an adaptive one-way
 *   hashing algorithm with salting built in. Used in:
 *     - SecurityConfig / DaoAuthenticationProvider — verifies login credentials
 *       by comparing BCrypt hash of submitted password against stored hash.
 *     - AuthController.changePassword() — hashes new password before save.
 *
 * No application.properties configuration needed — BCrypt uses default strength.
 * Injected as a shared singleton across all Spring context consumers.
 ******************************************************************************/
package com.plus33.erp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code SecurityBeansConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.config}</p>
 * <p><b>Layer  :</b> Spring Configuration: defines and registers beans for Security Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
public class SecurityBeansConfig {

    /**
     * Performs the passwordEncoder operation in this module.
     *
     * @return the PasswordEncoder result
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}