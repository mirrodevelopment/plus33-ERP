/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountItemCountRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountItemCountController
 * Related Service   : StockCountItemCountService, StockCountItemCountServiceImpl
 * Related Repository: StockCountItemCountRepository
 * Related Entity    : StockCountItemCount
 * Related DTO       : StockCountItemCountRequest
 * Related Mapper    : StockCountItemCountMapper
 * Related DB Table  : stock_count_item_counts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountItemCountController, StockCountItemCountService, StockCountItemCountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record StockCountItemCountRequest(
        @NotNull(message = "Product ID is required")
        Long productId,

        BigDecimal countedQuantity
) {}
