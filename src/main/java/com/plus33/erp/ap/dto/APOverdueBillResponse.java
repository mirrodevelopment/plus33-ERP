package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record APOverdueBillResponse(
        Long billId,
        String billNumber,
        Long supplierId,
        String supplierName,
        Long companyId,
        LocalDate billDate,
        LocalDate dueDate,
        Long daysOverdue,
        BigDecimal totalAmount,
        BigDecimal outstandingBalance,
        String status
) {}
