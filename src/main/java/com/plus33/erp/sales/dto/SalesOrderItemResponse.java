/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderItemController
 * Related Service   : SalesOrderItemService, SalesOrderItemServiceImpl
 * Related Repository: SalesOrderItemRepository
 * Related Entity    : SalesOrderItem
 * Related DTO       : SalesOrderItemResponse
 * Related Mapper    : SalesOrderItemMapper
 * Related DB Table  : sales_order_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderItemController, SalesOrderItemService, SalesOrderItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record SalesOrderItemResponse(
    Long id,
    Long productId,
    String productCode,
    String productName,
    BigDecimal orderedQuantity,
    BigDecimal unitPrice,
    BigDecimal discountPercentage,
    BigDecimal taxPercentage,
    BigDecimal lineTotal,
    Long version
) {}
