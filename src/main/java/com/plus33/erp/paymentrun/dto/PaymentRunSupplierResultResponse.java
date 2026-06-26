package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRunSupplierResultResponse(
        Long id,
        Long supplierId,
        String supplierName,
        Long paymentId,
        String paymentNumber,
        String status,
        BigDecimal amount,
        String errorMessage,
        LocalDateTime startedAt,
        LocalDateTime completedAt
) {}
