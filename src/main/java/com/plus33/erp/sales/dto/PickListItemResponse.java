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
