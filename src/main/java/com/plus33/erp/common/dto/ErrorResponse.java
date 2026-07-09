/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.dto
 * File              : ErrorResponse.java
 * Purpose           : Data Transfer Object for request/response in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ErrorController
 * Related Service   : ErrorService, ErrorServiceImpl
 * Related Repository: ErrorRepository
 * Related Entity    : Error
 * Related DTO       : ErrorResponse
 * Related Mapper    : ErrorMapper
 * Related DB Table  : errors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ErrorController, ErrorService, ErrorServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Common Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String error, String message, String path, Map<String, String> fieldErrors, LocalDateTime timestamp) {
        this(false, status, error, message, path, fieldErrors, timestamp);
    }
}
