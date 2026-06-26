package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * One row from mv_customer_aging.
 */
public record ARAgingResponse(
        Long companyId,
        Long customerId,
        String customerName,
        BigDecimal totalOutstanding,
        BigDecimal agingCurrent,
        BigDecimal aging1to30,
        BigDecimal aging31to60,
        BigDecimal aging61to90,
        BigDecimal aging90Plus
) {}
