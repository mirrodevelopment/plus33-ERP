package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * One bucket from mv_receivables_dashboard (per status).
 */
public record ARStatusBucket(
        String status,
        Long invoiceCount,
        BigDecimal totalAmount,
        BigDecimal outstandingAmount
) {}
