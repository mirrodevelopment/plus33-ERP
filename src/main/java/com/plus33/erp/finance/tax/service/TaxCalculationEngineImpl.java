package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.*;
import com.plus33.erp.finance.tax.entity.*;
import com.plus33.erp.finance.tax.repository.TaxExemptionCertificateRepository;
import com.plus33.erp.finance.tax.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxCalculationEngineImpl implements TaxCalculationEngine {

    private final TaxDeterminationRuleEngine determinationRuleEngine;
    private final TaxPostingProfileService postingProfileService;
    private final TaxRateRepository taxRateRepository;
    private final TaxExemptionCertificateRepository exemptionCertificateRepository;

    @Override
    public TaxCalculationResult calculateTax(TaxCalculationRequest request) {
        Long companyId = request.getCompanyId();
        LocalDate date = request.getTransactionDate();
        String docType = request.getDocumentType();

        // 1. Exemption Certificate Check
        boolean isExempt = false;
        if (request.getCustomerId() != null) {
            List<TaxExemptionCertificate> certificates = exemptionCertificateRepository
                    .findActiveCertificates(companyId, request.getCustomerId(), date);
            if (!certificates.isEmpty()) {
                isExempt = true;
            }
        }

        BigDecimal totalNet = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalGross = BigDecimal.ZERO;
        List<TaxCalculationLineResult> lineResults = new ArrayList<>();

        boolean isPurchase = isPurchaseDocument(docType);

        for (TaxCalculationLineRequest lineReq : request.getLines()) {
            // 2. Rule Determination
            TaxGroup taxGroup = determinationRuleEngine.determineTaxGroup(
                    companyId,
                    docType,
                    request.getCustomerTaxProfile(),
                    request.getSupplierTaxProfile(),
                    lineReq.getProductTaxCategory(),
                    request.getOriginCountry(),
                    request.getOriginState(),
                    request.getDestCountry(),
                    request.getDestState(),
                    request.getIncoterms(),
                    date
            );

            // 3. Resolve active rate percentages for the group
            List<TaxComponentResult> components = new ArrayList<>();
            BigDecimal totalRatePercent = BigDecimal.ZERO;

            for (TaxGroupLine line : taxGroup.getLines()) {
                TaxRate defaultRate = line.getRate();
                TaxCategory category = defaultRate.getCategory();

                // Dynamic date-versioned rate lookup
                List<TaxRate> activeRates = taxRateRepository
                        .findActiveRatesByCategoryIdAndDate(category.getId(), date);
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

            // 4. Calculate Net, Tax, and Gross
            BigDecimal lineAmount = lineReq.getAmount();
            BigDecimal netAmount;
            BigDecimal lineTaxAmount;
            BigDecimal grossAmount;

            if (lineReq.isTaxInclusive()) {
                // netAmount = lineAmount / (1 + totalRatePercent / 100)
                BigDecimal divisor = BigDecimal.ONE.add(totalRatePercent.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_EVEN));
                netAmount = lineAmount.divide(divisor, 2, RoundingMode.HALF_EVEN);
                lineTaxAmount = lineAmount.subtract(netAmount);
                grossAmount = lineAmount;
            } else {
                netAmount = lineAmount;
                lineTaxAmount = lineAmount.multiply(totalRatePercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
                grossAmount = lineAmount.add(lineTaxAmount);
            }

            // 5. Apportion Tax to Components and Adjust Rounding Differences
            BigDecimal calculatedComponentsSum = BigDecimal.ZERO;
            for (int i = 0; i < components.size(); i++) {
                TaxComponentResult comp = components.get(i);
                BigDecimal compTax;
                if (i == components.size() - 1) {
                    // Last component gets the residual amount to guarantee sum equality
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
                    if (isPurchase) {
                        comp.setNonRecoverableAmount(compTax);
                    } else {
                        comp.setNonRecoverableAmount(BigDecimal.ZERO);
                    }
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
                .build();
    }

    private boolean isPurchaseDocument(String documentType) {
        if (documentType == null) return false;
        String doc = documentType.toUpperCase();
        return doc.contains("PURCHASE") || doc.contains("IMPORT") || doc.contains("SELF") || doc.contains("REVERSE_CHARGE");
    }
}
