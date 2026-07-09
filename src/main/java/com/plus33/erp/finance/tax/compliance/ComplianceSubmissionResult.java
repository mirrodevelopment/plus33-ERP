/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : ComplianceSubmissionResult.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceSubmissionResultController
 * Related Service   : ComplianceSubmissionResultService, ComplianceSubmissionResultServiceImpl
 * Related Repository: ComplianceSubmissionResultRepository
 * Related Entity    : ComplianceSubmissionResult
 * Related DTO       : N/A
 * Related Mapper    : ComplianceSubmissionResultMapper
 * Related DB Table  : compliance_submission_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

import lombok.Builder;
import lombok.Value;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceSubmissionResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.compliance}</p>
 * <p><b>Layer  :</b> Component of Finance Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Value
@Builder
public class ComplianceSubmissionResult {
    boolean success;
    String status; // ACCEPTED, REJECTED, WARNINGS
    String errorDetails;
    String governmentUuid;
}