package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record APAgingBucket(
        String bucketName,
        BigDecimal outstandingAmount
) {}
