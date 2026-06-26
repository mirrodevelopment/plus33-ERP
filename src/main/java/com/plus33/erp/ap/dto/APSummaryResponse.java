package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APSummaryResponse(
        Long companyId,
        Long totalBills,
        BigDecimal totalBilledAmount,
        BigDecimal totalPaidAmount,
        BigDecimal totalOutstandingAmount,
        List<APStatusBucket> billsByStatus
) {}
