package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxDeterminationRule;
import com.plus33.erp.finance.tax.entity.TaxGroup;
import com.plus33.erp.finance.tax.repository.TaxDeterminationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxDeterminationRuleEngineImpl implements TaxDeterminationRuleEngine {

    private final TaxDeterminationRuleRepository determinationRuleRepository;

    @Override
    public TaxGroup determineTaxGroup(
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
    ) {
        List<TaxDeterminationRule> rules = determinationRuleRepository.findMatchingRules(
                companyId,
                documentType,
                customerTaxProfile,
                supplierTaxProfile,
                productTaxCategory,
                originCountry,
                originState,
                destCountry,
                destState,
                incoterms,
                date
        );

        if (rules.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "No matching tax determination rule found for company: %d, documentType: %s, " +
                    "customerTaxProfile: %s, supplierTaxProfile: %s, productTaxCategory: %s, date: %s",
                    companyId, documentType, customerTaxProfile, supplierTaxProfile, productTaxCategory, date
            ));
        }

        // The query returns rules sorted by priority ASC.
        // Therefore, the first element represents the rule with the highest priority/specificity.
        return rules.get(0).getTaxGroup();
    }
}
