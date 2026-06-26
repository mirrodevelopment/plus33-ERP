package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record TopSupplierDTO(
        Long supplierId,
        String supplierName,
        BigDecimal outstandingBalance
) {}
