/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : ReportWarning.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReportWarningController
 * Related Service   : ReportWarningService, ReportWarningServiceImpl
 * Related Repository: ReportWarningRepository
 * Related Entity    : ReportWarning
 * Related DTO       : N/A
 * Related Mapper    : ReportWarningMapper
 * Related DB Table  : report_warnings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

public record ReportWarning(
    String severity, // INFO, WARNING, ERROR
    String message,
    String code
) {}
