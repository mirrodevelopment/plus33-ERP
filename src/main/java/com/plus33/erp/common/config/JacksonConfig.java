/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.config
 * File              : JacksonConfig.java
 * Purpose           : Spring Configuration bean for Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JacksonConfigController
 * Related Service   : JacksonConfigService, JacksonConfigServiceImpl
 * Related Repository: JacksonConfigRepository
 * Related Entity    : JacksonConfig
 * Related DTO       : N/A
 * Related Mapper    : JacksonConfigMapper
 * Related DB Table  : jackson_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code JacksonConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.config}</p>
 * <p><b>Layer  :</b> Spring Configuration: defines and registers beans for Common Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
public class JacksonConfig {

    /**
     * Performs the objectMapper operation in this module.
     *
     * @return the ObjectMapper result
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}