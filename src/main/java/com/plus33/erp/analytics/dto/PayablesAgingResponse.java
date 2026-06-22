package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record PayablesAgingResponse(
        Long companyId,
        Long supplierId,
        String supplierName,
        BigDecimal totalOutstanding,
        BigDecimal agingCurrent,
        BigDecimal aging1To30,
        BigDecimal aging31To60,
        BigDecimal aging61To90,
        BigDecimal aging90Plus
) {}
