package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record APStatusBucket(
        String status,
        Long billCount,
        BigDecimal totalAmount,
        BigDecimal outstandingAmount
) {}
