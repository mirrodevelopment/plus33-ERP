/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : DuplicateResourceException.java
 * Path              : src/main/java/com/plus33/erp/common/exception/DuplicateResourceException.java
 * Purpose           : Custom unchecked exception thrown when a create or update operation
 *                     would produce a duplicate record, causing a 409 Conflict HTTP
 *                     response via GlobalExceptionHandler across all ERP modules.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * RuntimeException subclass representing an HTTP 409 Conflict condition caused
 * by uniqueness constraint violations at the business logic layer — distinct from
 * DataIntegrityViolationException which is thrown at the database level.
 *
 * Common throw sites (examples):
 *   - EmployeeService: "Employee code {code} already exists"
 *   - StoreService: "Store code {code} is already registered"
 *   - ProductService: "SKU {sku} is already in use"
 *   - UserService: "Email {email} is already registered"
 *
 * Intercepted by GlobalExceptionHandler.handleDuplicateResource() which
 * builds an ErrorResponse with HTTP 409 and the exception message, returning
 * it as structured JSON to the frontend for display in form validation errors.
 *
 * Prefer throwing this before the database insert to give a meaningful message
 * rather than relying on DataIntegrityViolationException from the DB layer.
 ******************************************************************************/
package com.plus33.erp.common.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
