package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.TaxCalculationRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;

public interface TaxCalculationEngine {
    TaxCalculationResult calculateTax(TaxCalculationRequest request);
}
