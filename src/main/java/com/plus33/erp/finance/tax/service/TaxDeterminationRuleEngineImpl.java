/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxDeterminationRuleEngineImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxDeterminationRuleEngineController
 * Related Service   : TaxDeterminationRuleEngineImpl
 * Related Repository: TaxDeterminationRuleRepository
 * Related Entity    : TaxDeterminationRuleEngine
 * Related DTO       : N/A
 * Related Mapper    : TaxDeterminationRuleEngineMapper
 * Related DB Table  : tax_determination_rule_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxDeterminationRuleEngineController, TaxDeterminationRuleEngineImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TaxDeterminationRuleEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxDeterminationRule;
import com.plus33.erp.finance.tax.entity.TaxGroup;
import com.plus33.erp.finance.tax.repository.TaxDeterminationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Determines the applicable TaxGroup based on the transaction context.
 * Rules are matched and then sorted by explicit specificity hierarchy:
 *
 * <ol>
 *   <li>Customer/Supplier Tax Profile matches (highest precedence)</li>
 *   <li>Product Tax Category matches</li>
 *   <li>Place of Supply matches (origin/dest country+state)</li>
 *   <li>Company default fallback (all null fields, lowest precedence)</li>
 * </ol>
 *
 * Within the same specificity tier, the configured priority (ASC) breaks ties.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxDeterminationRuleEngineImpl implements TaxDeterminationRuleEngine {

    private final TaxDeterminationRuleRepository determinationRuleRepository;
    private final TaxConfigurationCache taxConfigurationCache;

    /**
     * Performs the determineTaxGroup operation in this module.
     *
     * @return the TaxGroup result
     */
    /**
     * Performs the determineTaxGroup operation in this module.
     *
     * @return the TaxGroup result
     */
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
        return taxConfigurationCache.getOrLoadRule(
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
                date,
                () -> {
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

                    // Sort rules by specificity hierarchy (descending) then by priority (ascending)
                    rules.sort(Comparator.comparingInt(this::calculateSpecificity).reversed()
                            .thenComparingInt(TaxDeterminationRule::getPriority));

                    return rules.get(0).getTaxGroup();
                }
        );
    }

    /**
     * Calculate a specificity score for a rule.
     * Higher score means more specific and therefore higher precedence.
     *
     * Scoring:
     *   +8 for Profile match (customer or supplier tax profile is non-null)
     *   +4 for Product Tax Category match
     *   +2 for Place of Supply match (any of origin/dest country/state)
     *   +1 for Incoterms match
     *   +0 for Company default (all null)
     */
    private int calculateSpecificity(TaxDeterminationRule rule) {
        int score = 0;

        // Tier 1: Profile match (highest specificity)
        if (rule.getCustomerTaxProfile() != null || rule.getSupplierTaxProfile() != null) {
            score += 8;
        }

        // Tier 2: Product category match
        if (rule.getProductTaxCategory() != null) {
            score += 4;
        }

        // Tier 3: Place of supply match
        if (rule.getOriginCountry() != null || rule.getOriginState() != null
                || rule.getDestCountry() != null || rule.getDestState() != null) {
            score += 2;
        }

        // Tier 4: Incoterms match
        if (rule.getIncoterms() != null) {
            score += 1;
        }

        return score;
    }
}