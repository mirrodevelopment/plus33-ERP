package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxAdjustmentEntry;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TaxAdjustmentService {
    TaxAdjustmentEntry createAdjustment(
            Long companyId,
            LocalDate date,
            Long taxCategoryId,
            Long glAccountId,
            BigDecimal debitAmount,
            BigDecimal creditAmount,
            String description,
            String reasonCode
    );

    TaxAdjustmentEntry approveAdjustment(Long adjustmentId, String approvedBy);

    List<TaxAdjustmentEntry> getAdjustmentsByCompany(Long companyId);
}
