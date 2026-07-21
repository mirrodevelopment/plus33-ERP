/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : JacksonConfig.java
 * Path              : src/main/java/com/plus33/erp/common/config/JacksonConfig.java
 * Purpose           : Configures the Jackson ObjectMapper bean with JavaTimeModule to
 *                     ensure proper ISO-8601 JSON serialization and deserialization of
 *                     Java 8 date/time types (LocalDateTime, LocalDate) across all REST APIs.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Configuration class providing custom JSON serialization behavior.
 * Registers JavaTimeModule with Jackson's ObjectMapper to handle JSR-310 types
 * (such as LocalDateTime used in ApiResponse, ErrorResponse, and entities).
 *
 * Primary Bean:
 *   objectMapper() — instantiates and configures a Spring-managed ObjectMapper
 *   instance with JavaTimeModule registered. Prevents serialization errors when
 *   converting Java 8 temporal objects to JSON format.
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