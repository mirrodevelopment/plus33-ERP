/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerReturnItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnItemController
 * Related Service   : CustomerReturnItemService, CustomerReturnItemServiceImpl
 * Related Repository: CustomerReturnItemRepository
 * Related Entity    : CustomerReturnItem
 * Related DTO       : CustomerReturnItemRequest
 * Related Mapper    : CustomerReturnItemMapper
 * Related DB Table  : customer_return_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerReturnItemController, CustomerReturnItemService, CustomerReturnItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record CustomerReturnItemRequest(
    Long salesOrderItemId,
    Long customerInvoiceItemId,
    Long productId,
    BigDecimal quantity,
    Long lotId,
    Long serialId
) {}
