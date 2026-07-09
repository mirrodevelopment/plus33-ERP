/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Exception Module
 * Package           : com.plus33.erp.exception
 * File              : package-info.java
 * Purpose           : REST Controller exposing HTTP endpoints for Exception Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: package-info
 * Related Service   : package-infoService, package-infoServiceImpl
 * Related Repository: package-infoRepository
 * Related Entity    : package-info
 * Related DTO       : N/A
 * Related Mapper    : package-infoMapper
 * Related DB Table  : package-infos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Exception Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Exception Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: N/A
 ******************************************************************************/
/**
 * Global exception handling via @ControllerAdvice, custom domain
 * exception types, and standardized error response construction.
 */
package com.plus33.erp.exception;
