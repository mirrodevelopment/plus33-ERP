package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.TaxCalculationRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.finance.tax.entity.TaxGroup;

public interface TaxEngineProvider {
    String getTaxType(); // e.g., "VAT", "GST", "SALES_TAX"
    TaxCalculationResult calculateTax(TaxCalculationRequest request, TaxGroup taxGroup, boolean isExempt);
}
