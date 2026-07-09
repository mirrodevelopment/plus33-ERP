/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : ReturnApprovalRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReturnApprovalController
 * Related Service   : ReturnApprovalService, ReturnApprovalServiceImpl
 * Related Repository: ReturnApprovalRepository
 * Related Entity    : ReturnApproval
 * Related DTO       : ReturnApprovalRequest
 * Related Mapper    : ReturnApprovalMapper
 * Related DB Table  : return_approvals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReturnApprovalController, ReturnApprovalService, ReturnApprovalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

public record ReturnApprovalRequest(
    String remarks
) {}
