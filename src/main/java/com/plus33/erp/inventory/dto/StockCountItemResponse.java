/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountItemController
 * Related Service   : StockCountItemService, StockCountItemServiceImpl
 * Related Repository: StockCountItemRepository
 * Related Entity    : StockCountItem
 * Related DTO       : StockCountItemResponse
 * Related Mapper    : StockCountItemMapper
 * Related DB Table  : stock_count_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountItemController, StockCountItemService, StockCountItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;

public record StockCountItemResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        BigDecimal systemQuantity,
        BigDecimal reservedQuantity,
        BigDecimal availableQuantity,
        BigDecimal countedQuantity,
        BigDecimal variance,
        Long version
) {}
