package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PoFulfilmentResponse(
        Long companyId,
        Long purchaseOrderId,
        String orderNumber,
        String supplierName,
        String status,
        BigDecimal totalAmount,
        LocalDate expectedDeliveryDate,
        Long totalItemsOrdered,
        BigDecimal totalQuantityOrdered,
        BigDecimal totalQuantityReceived,
        BigDecimal fulfillmentRate
) {}
