/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : SecurityAuditorAware.java
 * Path              : src/main/java/com/plus33/erp/common/audit/SecurityAuditorAware.java
 * Purpose           : Supplies the currently authenticated user's ID to Spring Data JPA
 *                     auditing so that createdBy and updatedBy fields in AuditableEntity
 *                     subclasses are automatically populated on every persist/merge.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Component implementing AuditorAware<Long> — the hook Spring Data JPA
 * calls during @CreatedBy and @LastModifiedBy field population for entities
 * that extend AuditableEntity.
 *
 * getCurrentAuditor():
 *   1. Reads the current Authentication from SecurityContextHolder.
 *   2. Returns Optional.empty() if authentication is null or not authenticated
 *      (e.g. system-triggered background jobs or anonymous requests).
 *   3. If the principal is a String, attempts to parse it as Long user ID.
 *      Returns Optional.empty() on NumberFormatException (e.g. email principals
 *      that are not numeric IDs).
 *   4. Returns Optional.empty() for any non-String principal type.
 *
 * Registration:
 *   The bean is named "auditorProvider" and referenced by @EnableJpaAuditing
 *   in JpaAuditingConfig via auditorAwareRef = "auditorProvider".
 *
 * Note: In the current implementation, the JWT principal is the user's email
 * string (not a numeric ID), so getCurrentAuditor() typically returns
 * Optional.empty() unless the email string happens to be numeric. The
 * createdBy/updatedBy fields may remain null for most operations.
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