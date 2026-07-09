/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.config
 * File              : SecurityBeansConfig.java
 * Purpose           : Spring Configuration bean for Security Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SecurityBeansConfigController
 * Related Service   : SecurityBeansConfigService, SecurityBeansConfigServiceImpl
 * Related Repository: SecurityBeansConfigRepository
 * Related Entity    : SecurityBeansConfig
 * Related DTO       : N/A
 * Related Mapper    : SecurityBeansConfigMapper
 * Related DB Table  : security_beans_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Security Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Security Module within the PLUS33 Coffee ERP platform.
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