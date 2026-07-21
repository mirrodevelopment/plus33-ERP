/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : ResourceNotFoundException.java
 * Path              : src/main/java/com/plus33/erp/common/exception/ResourceNotFoundException.java
 * Purpose           : Custom unchecked exception thrown when a requested entity
 *                     cannot be found in the database, causing a 404 HTTP response
 *                     via GlobalExceptionHandler across all ERP modules.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * RuntimeException subclass representing an HTTP 404 Not Found condition.
 * Thrown by service and repository layers throughout all ERP modules when
 * an entity is looked up by ID or unique key and does not exist.
 *
 * Common throw sites (examples):
 *   - EmployeeService: "Employee not found with id: {id}"
 *   - PurchaseOrderService: "Purchase order {id} not found"
 *   - StoreService: "Store not found: {code}"
 *
 * Intercepted by GlobalExceptionHandler.handleResourceNotFound() which
 * builds an ErrorResponse with HTTP 404 and the exception message,
 * returning it to the API caller as structured JSON.
 *
 * Does not log internally — logging is handled at the service call site.
 * Does not carry additional metadata — use the message string for context.
 ******************************************************************************/
package com.plus33.erp.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
