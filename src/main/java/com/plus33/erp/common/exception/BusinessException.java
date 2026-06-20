package com.plus33.erp.common.exception;

// Business rule exception
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
