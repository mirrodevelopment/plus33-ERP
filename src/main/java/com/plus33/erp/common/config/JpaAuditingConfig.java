/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.config
 * File              : JpaAuditingConfig.java
 * Purpose           : Spring Configuration bean for Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JpaAuditingConfigController
 * Related Service   : JpaAuditingConfigService, JpaAuditingConfigServiceImpl
 * Related Repository: JpaAuditingConfigRepository
 * Related Entity    : JpaAuditingConfig
 * Related DTO       : N/A
 * Related Mapper    : JpaAuditingConfigMapper
 * Related DB Table  : jpa_auditing_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code JpaAuditingConfig}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.config}</p>
 * <p><b>Layer  :</b> Spring Configuration: defines and registers beans for Common Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
}