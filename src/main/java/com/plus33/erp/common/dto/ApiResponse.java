/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.dto
 * File              : ApiResponse.java
 * Purpose           : Data Transfer Object for request/response in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ApiController
 * Related Service   : ApiService, ApiServiceImpl
 * Related Repository: ApiRepository
 * Related Entity    : Api
 * Related DTO       : ApiResponse
 * Related Mapper    : ApiMapper
 * Related DB Table  : apis
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ApiController, ApiService, ApiServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Common Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
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
