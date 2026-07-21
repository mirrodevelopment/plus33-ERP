/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : ErrorResponse.java
 * Path              : src/main/java/com/plus33/erp/common/dto/ErrorResponse.java
 * Purpose           : Structured error envelope returned by GlobalExceptionHandler for
 *                     all failed API requests, carrying HTTP status, error type, message,
 *                     request path, and optional field-level validation errors.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Immutable Java record representing the standardized error JSON response body
 * produced by GlobalExceptionHandler for every exception scenario in the ERP.
 * Returned as ResponseEntity<ErrorResponse> with the matching HTTP status code.
 *
 * Fields:
 *   success     — always false for error responses (set via compact constructor).
 *   status      — HTTP status code integer (e.g. 400, 404, 409, 500).
 *   error       — HTTP reason phrase (e.g. "Bad Request", "Not Found", "Conflict").
 *   message     — human-readable error description surfaced to the frontend.
 *                 For validation errors this is the first failing field message.
 *   path        — the request URI that triggered the error (from HttpServletRequest).
 *   fieldErrors — nullable Map<fieldName, message> populated for
 *                 MethodArgumentNotValidException and ConstraintViolationException.
 *                 Used by frontend forms to highlight specific invalid fields.
 *   timestamp   — server-side LocalDateTime stamped at error construction.
 *
 * The compact constructor (without 'success') delegates to the canonical
 * constructor always setting success = false, ensuring error responses
 * never accidentally have success = true.
 *
 * Consumed by the frontend apiClient.js: when response.success is false,
 * the error message is extracted and displayed as a toast notification.
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
