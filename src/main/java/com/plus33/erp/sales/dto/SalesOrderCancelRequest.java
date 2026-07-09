/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderCancelRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderCancelController
 * Related Service   : SalesOrderCancelService, SalesOrderCancelServiceImpl
 * Related Repository: SalesOrderCancelRepository
 * Related Entity    : SalesOrderCancel
 * Related DTO       : SalesOrderCancelRequest
 * Related Mapper    : SalesOrderCancelMapper
 * Related DB Table  : sales_order_cancels
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderCancelController, SalesOrderCancelService, SalesOrderCancelServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.NotBlank;

public record SalesOrderCancelRequest(
    @NotBlank(message = "Cancellation reason is required")
    String reason
) {}
