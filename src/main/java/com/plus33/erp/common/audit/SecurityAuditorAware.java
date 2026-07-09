/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.audit
 * File              : SecurityAuditorAware.java
 * Purpose           : Component of Common Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SecurityAuditorAwareController
 * Related Service   : SecurityAuditorAwareService, SecurityAuditorAwareServiceImpl
 * Related Repository: SecurityAuditorAwareRepository
 * Related Entity    : SecurityAuditorAware
 * Related DTO       : N/A
 * Related Mapper    : SecurityAuditorAwareMapper
 * Related DB Table  : security_auditor_awares
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code SecurityAuditorAware}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.audit}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Common Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component("auditorProvider")
public class SecurityAuditorAware implements AuditorAware<Long> {

    /**
     * Retrieves current auditor data from the database.
     *
     * @return Optional containing the entity if found, empty if not the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves current auditor data from the database.
     *
     * @return Optional containing the entity if found, empty if not the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            try {
                return Optional.of(Long.parseLong((String) principal));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}