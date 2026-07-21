/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : BusinessException.java
 * Path              : src/main/java/com/plus33/erp/common/exception/BusinessException.java
 * Purpose           : Custom unchecked exception thrown when a business domain rule is
 *                     violated in any ERP service layer, causing a 400 Bad Request
 *                     response via GlobalExceptionHandler.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * RuntimeException subclass representing a business logic constraint violation.
 * Thrown by service layer implementations throughout all ERP modules when
 * an operation cannot proceed due to domain-specific rules.
 *
 * Common throw sites (examples):
 *   - InventoryService: "Insufficient stock for item {sku}"
 *   - LeaveService: "Insufficient leave balance for employee {id}"
 *   - PayrollService: "Payroll already processed for period {periodId}"
 *   - ShiftService: "Employee already has an active shift"
 *
 * Intercepted by GlobalExceptionHandler.handleBusiness() which builds an
 * ErrorResponse with HTTP 400 and the business rule violation message,
 * returning it to the API caller and consequently to the frontend as
 * structured JSON for display in toast notifications or error dialogs.
 *
 * Does not carry HTTP status code — that is determined by GlobalExceptionHandler.
 * Does not include stack trace in response — only the message is surfaced.
 ******************************************************************************/
package com.plus33.erp.common.exception;

// Business rule exception
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
