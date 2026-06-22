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
