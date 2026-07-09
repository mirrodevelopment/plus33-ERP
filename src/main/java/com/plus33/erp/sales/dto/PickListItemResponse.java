/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : PickListItemResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListItemController
 * Related Service   : PickListItemService, PickListItemServiceImpl
 * Related Repository: PickListItemRepository
 * Related Entity    : PickListItem
 * Related DTO       : PickListItemResponse
 * Related Mapper    : PickListItemMapper
 * Related DB Table  : pick_list_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickListItemController, PickListItemService, PickListItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import java.math.BigDecimal;

public record PickListItemResponse(
    Long id,
    Long salesOrderItemId,
    Long productId,
    String productName,
    String productSku,
    BigDecimal orderedQuantity,
    BigDecimal allocatedQuantity,
    BigDecimal pickedQuantity,
    BigDecimal shippedQuantity
) {}
