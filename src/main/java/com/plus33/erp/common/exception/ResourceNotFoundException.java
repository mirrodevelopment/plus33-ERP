/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.exception
 * File              : ResourceNotFoundException.java
 * Purpose           : Custom exception for domain error handling in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ResourceNotFoundExceptionController
 * Related Service   : ResourceNotFoundExceptionService, ResourceNotFoundExceptionServiceImpl
 * Related Repository: ResourceNotFoundExceptionRepository
 * Related Entity    : ResourceNotFoundException
 * Related DTO       : N/A
 * Related Mapper    : ResourceNotFoundExceptionMapper
 * Related DB Table  : resource_not_found_exceptions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Common Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Common Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
