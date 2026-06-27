package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxGroup;
import java.time.LocalDate;

public interface TaxDeterminationRuleEngine {
    TaxGroup determineTaxGroup(
            Long companyId,
            String documentType,
            String customerTaxProfile,
            String supplierTaxProfile,
            String productTaxCategory,
            String originCountry,
            String originState,
            String destCountry,
            String destState,
            String incoterms,
            LocalDate date
    );
}
