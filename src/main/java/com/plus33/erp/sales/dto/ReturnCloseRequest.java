/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : ReturnCloseRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReturnCloseController
 * Related Service   : ReturnCloseService, ReturnCloseServiceImpl
 * Related Repository: ReturnCloseRepository
 * Related Entity    : ReturnClose
 * Related DTO       : ReturnCloseRequest
 * Related Mapper    : ReturnCloseMapper
 * Related DB Table  : return_closes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReturnCloseController, ReturnCloseService, ReturnCloseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

public record ReturnCloseRequest(
    String remarks
) {}
