/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : ApiResponse.java
 * Path              : src/main/java/com/plus33/erp/common/dto/ApiResponse.java
 * Purpose           : Universal generic HTTP response wrapper returned by all REST
 *                     controllers in the ERP, carrying success status, message, typed
 *                     data payload, optional metadata, and response timestamp.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Immutable generic Java record used as the standard envelope for every JSON
 * response returned by the PLUS33 Coffee ERP REST API. All controllers return
 * ResponseEntity<ApiResponse<T>> ensuring consistent response structure for
 * the frontend SPA and API consumers.
 *
 * Fields:
 *   success   — true on normal completion, false on error.
 *   message   — human-readable status message surfaced to the frontend.
 *   data      — typed payload of generic type T (entity DTO, list, map, etc.)
 *   metadata  — optional supplementary data (e.g. pagination totals, cursors).
 *   timestamp — server-side LocalDateTime stamped at construction.
 *
 * Static factory methods:
 *   success(data)                  — wraps data with default "Operation successful" message.
 *   success(message, data)         — wraps data with a custom message.
 *   success(message, data, metadata) — wraps data with custom message and metadata.
 *   error(message)                 — sets success=false, null data, error message.
 *
 * Used by every controller in the ERP: AuthController, workforce, inventory,
 * procurement, sales, finance, and all domain module controllers.
 * Deserialized by the frontend apiClient.js to check response.success.
 ******************************************************************************/
package com.plus33.erp.common.dto;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Object metadata,
        LocalDateTime timestamp
) {
    /**
     * Performs the success operation in this module.
     *
     * @param data the data input value
     * @return the  result
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful", data, null, LocalDateTime.now());
    }

    /**
     * Performs the success operation in this module.
     *
     * @param message the message input value
     * @param data the data input value
     * @return the  result
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, LocalDateTime.now());
    }

    /**
     * Performs the success operation in this module.
     *
     * @param message the message input value
     * @param data the data input value
     * @param metadata the metadata input value
     * @return the  result
     */
    public static <T> ApiResponse<T> success(String message, T data, Object metadata) {
        return new ApiResponse<>(true, message, data, metadata, LocalDateTime.now());
    }

    /**
     * Performs the error operation in this module.
     *
     * @param message the message input value
     * @return the  result
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null, LocalDateTime.now());
    }
}
