/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerReturnItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnItemController
 * Related Service   : CustomerReturnItemService, CustomerReturnItemServiceImpl
 * Related Repository: CustomerReturnItemRepository
 * Related Entity    : CustomerReturnItem
 * Related DTO       : CustomerReturnItemResponse
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

import com.plus33.erp.sales.entity.InspectionResult;
import java.math.BigDecimal;

public record CustomerReturnItemResponse(
    Long id,
    Long salesOrderItemId,
    Long customerInvoiceItemId,
    Long productId,
    String productName,
    String productCode,
    BigDecimal quantity,
    InspectionResult inspectionResult,
    String inspectionNotes,
    Long lotId,
    String lotNumber,
    Long serialId,
    String serialNumber
) {}
