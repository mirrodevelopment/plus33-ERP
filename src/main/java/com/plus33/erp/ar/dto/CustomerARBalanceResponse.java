package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * Current AR position for a single customer.
 */
public record CustomerARBalanceResponse(
        Long customerId,
        String customerName,
        Long companyId,
        BigDecimal creditLimit,
        BigDecimal totalOutstanding,
        BigDecimal totalOverdue,
        BigDecimal totalPaid,
        BigDecimal totalCredited
) {}
