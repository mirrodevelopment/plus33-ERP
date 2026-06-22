package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventorySlowDeadResponse(
        Long companyId,
        Long productId,
        Long warehouseId,
        Long storeId,
        BigDecimal onHandQuantity,
        LocalDateTime lastMovementAt,
        BigDecimal usage90Days,
        Boolean isDead,
        Boolean isSlow
) {}
