/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.dto
 * File              : PreFilingValidationResult.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PreFilingValidationResultController
 * Related Service   : PreFilingValidationResultService, PreFilingValidationResultServiceImpl
 * Related Repository: PreFilingValidationResultRepository
 * Related Entity    : PreFilingValidationResult
 * Related DTO       : N/A
 * Related Mapper    : PreFilingValidationResultMapper
 * Related DB Table  : pre_filing_validation_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.dto;

import lombok.Value;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PreFilingValidationResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.dto}</p>
 * <p><b>Layer  :</b> Component of Finance Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Value
public class PreFilingValidationResult {
    List<String> errors;
    List<String> warnings;

    /**
     * Performs the isValid operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isValid() {
        return errors.isEmpty();
    }
}