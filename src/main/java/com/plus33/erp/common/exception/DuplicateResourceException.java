/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.exception
 * File              : DuplicateResourceException.java
 * Purpose           : Custom exception for domain error handling in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DuplicateResourceExceptionController
 * Related Service   : DuplicateResourceExceptionService, DuplicateResourceExceptionServiceImpl
 * Related Repository: DuplicateResourceExceptionRepository
 * Related Entity    : DuplicateResourceException
 * Related DTO       : N/A
 * Related Mapper    : DuplicateResourceExceptionMapper
 * Related DB Table  : duplicate_resource_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
