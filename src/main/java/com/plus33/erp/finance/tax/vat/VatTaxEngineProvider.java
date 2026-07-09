/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.vat
 * File              : VatTaxEngineProvider.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: VatTaxEngineProviderController
 * Related Service   : VatTaxEngineProviderService, VatTaxEngineProviderServiceImpl
 * Related Repository: TaxRateRepository
 * Related Entity    : VatTaxEngineProvider
 * Related DTO       : TaxCalculationLineRequest, TaxCalculationRequest
 * Related Mapper    : VatTaxEngineProviderMapper
 * Related DB Table  : vat_tax_engine_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.vat;

import com.plus33.erp.finance.tax.dto.*;
import com.plus33.erp.finance.tax.entity.*;
import com.plus33.erp.finance.tax.repository.TaxRateRepository;
import com.plus33.erp.finance.tax.service.TaxConfigurationCache;
import com.plus33.erp.finance.tax.service.TaxEngineProvider;
import com.plus33.erp.finance.tax.service.TaxPostingProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Default VAT engine provider supporting standard exclusive/inclusive calculations,
 * recoverability, and residual rounding adjustments.
 */
@Component
@RequiredArgsConstructor
public class VatTaxEngineProvider implements TaxEngineProvider {

    private final TaxRateRepository taxRateRepository;
    private final TaxPostingProfileService postingProfileService;
    private final TaxConfigurationCache taxConfigurationCache;

    /**
     * Retrieves tax type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves tax type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getTaxType() {
        return "VAT";
    }

    /**
     * Calculates tax totals including subtotal, tax, discounts, and net amount.
     *
     * @param request the validated request DTO containing input data
     * @param taxGroup the taxGroup input value
     * @param isExempt the isExempt input value
     * @return the TaxCalculationResult result
     */
    /**
     * Calculates tax totals including subtotal, tax, discounts, and net amount.
     *
     * @param request the validated request DTO containing input data
     * @param taxGroup the taxGroup input value
     * @param isExempt the isExempt input value
     * @return the TaxCalculationResult result
     */
    @Override
    public TaxCalculationResult calculateTax(TaxCalculationRequest request, TaxGroup taxGroup, boolean isExempt) {
        Long companyId = request.getCompanyId();
        LocalDate date = request.getTransactionDate();
        String docType = request.getDocumentType();
        boolean isPurchase = isPurchaseDocument(docType);

        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalGross = BigDecimal.ZERO;
        List<TaxCalculationLineResult> lineResults = new ArrayList<>();

        for (TaxCalculationLineRequest lineReq : request.getLines()) {
            List<TaxComponentResult> components = new ArrayList<>();
            BigDecimal totalRatePercent = BigDecimal.ZERO;

            for (TaxGroupLine line : taxGroup.getLines()) {
                TaxRate defaultRate = line.getRate();
                TaxCategory category = defaultRate.getCategory();

                // Dynamic date-versioned rate lookup
                List<TaxRate> activeRates = taxConfigurationCache.getOrLoadRates(category.getId(), date,
                        () -> taxRateRepository.findActiveRatesByCategoryIdAndDate(category.getId(), date));
                BigDecimal ratePercent = activeRates.isEmpty() ? defaultRate.getRatePercent() : activeRates.get(0).getRatePercent();

                if (isExempt) {
                    ratePercent = BigDecimal.ZERO;
                }

                totalRatePercent = totalRatePercent.add(ratePercent);

                // Fetch Posting Profile to get GL Accounts
                TaxPostingProfile profile = postingProfileService.getPostingProfile(companyId, category.getId());

                // Check recoverability
                boolean isRecoverable = false;
                if (isPurchase) {
                    String prodCat = lineReq.getProductTaxCategory();
                    boolean isNonRecProd = prodCat != null && (prodCat.toUpperCase().contains("NONREC") || prodCat.toUpperCase().contains("NON_RECOVERABLE"));
                    if (!isNonRecProd && profile.getRecoverableAccount() != null) {
                        isRecoverable = true;
                    }
                }

                components.add(TaxComponentResult.builder()
                        .taxCategoryId(category.getId())
                        .taxCategoryCode(category.getCode())
                        .taxCategoryName(category.getName())
                        .ratePercent(ratePercent)
                        .isRecoverable(isRecoverable)
                        .inputTaxAccountId(profile.getInputTaxAccount() != null ? profile.getInputTaxAccount().getId() : null)
                        .outputTaxAccountId(profile.getOutputTaxAccount() != null ? profile.getOutputTaxAccount().getId() : null)
                        .reverseChargeAccountId(profile.getReverseChargeAccount() != null ? profile.getReverseChargeAccount().getId() : null)
                        .recoverableAccountId(profile.getRecoverableAccount() != null ? profile.getRecoverableAccount().getId() : null)
                        .nonRecoverableAccountId(profile.getNonRecoverableAccount() != null ? profile.getNonRecoverableAccount().getId() : null)
                        .build());
            }

            // Calculate Net, Tax, and Gross
            BigDecimal lineAmount = lineReq.getAmount();
            BigDecimal netAmount;
            BigDecimal lineTaxAmount;
            BigDecimal grossAmount;

            if (lineReq.isTaxInclusive()) {
                BigDecimal divisor = BigDecimal.ONE.add(totalRatePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_EVEN));
                netAmount = lineAmount.divide(divisor, 2, RoundingMode.HALF_EVEN);
                lineTaxAmount = lineAmount.subtract(netAmount);
                grossAmount = lineAmount;
            } else {
                netAmount = lineAmount;
                lineTaxAmount = lineAmount.multiply(totalRatePercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
                grossAmount = lineAmount.add(lineTaxAmount);
            }

            // Apportion Tax to Components and Adjust Rounding Differences
            BigDecimal calculatedComponentsSum = BigDecimal.ZERO;
            for (int i = 0; i < components.size(); i++) {
                TaxComponentResult comp = components.get(i);
                BigDecimal compTax;
                if (i == components.size() - 1) {
                    compTax = lineTaxAmount.subtract(calculatedComponentsSum);
                } else {
                    compTax = netAmount.multiply(comp.getRatePercent())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
                    calculatedComponentsSum = calculatedComponentsSum.add(compTax);
                }
                comp.setTaxAmount(compTax);

                if (comp.isRecoverable()) {
                    comp.setRecoverableAmount(compTax);
                    comp.setNonRecoverableAmount(BigDecimal.ZERO);
                } else {
                    comp.setRecoverableAmount(BigDecimal.ZERO);
                    comp.setNonRecoverableAmount(isPurchase ? compTax : BigDecimal.ZERO);
                }
            }

            lineResults.add(TaxCalculationLineResult.builder()
                    .lineId(lineReq.getLineId())
                    .netAmount(netAmount)
                    .taxAmount(lineTaxAmount)
                    .grossAmount(grossAmount)
                    .taxComponents(components)
                    .build());

            totalNet = totalNet.add(netAmount);
            totalTax = totalTax.add(lineTaxAmount);
            totalGross = totalGross.add(grossAmount);
        }

        return TaxCalculationResult.builder()
                .totalNetAmount(totalNet)
                .totalTaxAmount(totalTax)
                .totalGrossAmount(totalGross)
                .lines(lineResults)
                .providerName("VAT")
                .build();
    }

    private boolean isPurchaseDocument(String documentType) {
        if (documentType == null) return false;
        String doc = documentType.toUpperCase();
        return doc.contains("PURCHASE") || doc.contains("IMPORT") || doc.contains("SELF") || doc.contains("REVERSE_CHARGE");
    }
}