package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APAgingResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        BigDecimal totalOutstanding,
        List<APAgingBucket> buckets
) {}
