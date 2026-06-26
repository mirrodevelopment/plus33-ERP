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
