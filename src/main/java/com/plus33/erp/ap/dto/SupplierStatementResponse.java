package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SupplierStatementResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<SupplierStatementEntry> entries
) {}
