/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : JpaAuditingConfig.java
 * Path              : src/main/java/com/plus33/erp/common/config/JpaAuditingConfig.java
 * Purpose           : Enables Spring Data JPA entity auditing and wires the custom
 *                     AuditorAware bean (auditorProvider) to track entity creation and modification.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Configuration class that enables automatic audit metadata population
 * across all JPA entities extending AuditableEntity.
 *
 * Configuration:
 *   @EnableJpaAuditing(auditorAwareRef = "auditorProvider")
 *   Tells Spring Data JPA to use the SecurityAuditorAware bean named "auditorProvider"
 *   to resolve the current auditor ID for @CreatedBy and @LastModifiedBy annotations.
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