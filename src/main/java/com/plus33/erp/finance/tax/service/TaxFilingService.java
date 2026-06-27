package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.PreFilingValidationResult;
import com.plus33.erp.finance.tax.entity.TaxFiling;

public interface TaxFilingService {
    PreFilingValidationResult validateFiling(Long calendarId);
    TaxFiling calculateFiling(Long calendarId, String filedBy);
    TaxFiling submitFiling(Long filingId, String filedBy);
}
