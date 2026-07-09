/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.telemetry
 * File              : TelemetryValidator.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TelemetryValidatorController
 * Related Service   : TelemetryValidator
 * Related Repository: TelemetryValidatorRepository
 * Related Entity    : TelemetryValidator
 * Related DTO       : N/A
 * Related Mapper    : TelemetryValidatorMapper
 * Related DB Table  : telemetry_validators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TelemetryValidatorController, TelemetryValidatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements TelemetryValidatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.telemetry;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code TelemetryValidator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.telemetry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TelemetryValidatorController
 *   --> TelemetryValidator (this)
 *   --> Validate business rules
 *   --> TelemetryValidatorRepository (read/write 'telemetry_validators')
 *   --> TelemetryValidatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code telemetry_validators}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TelemetryValidator {
    /**
     * Performs the isValid operation in this module.
     *
     * @param name the name input value
     * @param val the val input value
     * @return true if operation succeeded, false otherwise
     */
    public boolean isValid(String name, BigDecimal val) {
        return name != null && val != null && val.compareTo(BigDecimal.ZERO) >= 0;
    }
}