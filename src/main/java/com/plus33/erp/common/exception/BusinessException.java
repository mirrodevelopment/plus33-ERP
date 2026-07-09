/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.exception
 * File              : BusinessException.java
 * Purpose           : Custom exception for domain error handling in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BusinessExceptionController
 * Related Service   : BusinessExceptionService, BusinessExceptionServiceImpl
 * Related Repository: BusinessExceptionRepository
 * Related Entity    : BusinessException
 * Related DTO       : N/A
 * Related Mapper    : BusinessExceptionMapper
 * Related DB Table  : business_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.exception;

// Business rule exception
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
