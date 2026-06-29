package com.plus33.erp.finance.tax.gst;

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
 * GST engine provider stub supporting India-style CGST/SGST/IGST component separation.
 * In a full implementation, this would split inter-state (IGST) vs intra-state (CGST+SGST) based
 * on origin/destination state matching.
 */
@Component
@RequiredArgsConstructor
public class GstTaxEngineProvider implements TaxEngineProvider {

    private final TaxRateRepository taxRateRepository;
    private final TaxPostingProfileService postingProfileService;
    private final TaxConfigurationCache taxConfigurationCache;

    @Override
    public String getTaxType() {
        return "GST";
    }

    @Override
    public TaxCalculationResult calculateTax(TaxCalculationRequest request, TaxGroup taxGroup, boolean isExempt) {
        Long companyId = request.getCompanyId();
        LocalDate date = request.getTransactionDate();
        boolean isPurchase = isPurchaseDocument(request.getDocumentType());

        // Determine if inter-state or intra-state based on origin vs destination
        boolean isInterState = request.getOriginState() != null && request.getDestState() != null
                && !request.getOriginState().equalsIgnoreCase(request.getDestState());

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

                List<TaxRate> activeRates = taxConfigurationCache.getOrLoadRates(category.getId(), date,
                        () -> taxRateRepository.findActiveRatesByCategoryIdAndDate(category.getId(), date));
                BigDecimal ratePercent = activeRates.isEmpty() ? defaultRate.getRatePercent() : activeRates.get(0).getRatePercent();

                if (isExempt) {
                    ratePercent = BigDecimal.ZERO;
                }

                TaxPostingProfile profile = postingProfileService.getPostingProfile(companyId, category.getId());

                boolean isRecoverable = false;
                if (isPurchase && profile.getRecoverableAccount() != null) {
                    isRecoverable = true;
                }

                if (isInterState) {
                    // Full rate goes to IGST
                    totalRatePercent = totalRatePercent.add(ratePercent);
                    components.add(buildComponent(category, "IGST", ratePercent, isRecoverable, profile));
                } else {
                    // Split evenly between CGST and SGST
                    BigDecimal halfRate = ratePercent.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_EVEN);
                    totalRatePercent = totalRatePercent.add(ratePercent);
                    components.add(buildComponent(category, "CGST", halfRate, isRecoverable, profile));
                    components.add(buildComponent(category, "SGST", halfRate, isRecoverable, profile));
                }
            }

            BigDecimal lineAmount = lineReq.getAmount();
            BigDecimal netAmount = lineAmount;
            BigDecimal lineTaxAmount = lineAmount.multiply(totalRatePercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
            BigDecimal grossAmount = lineAmount.add(lineTaxAmount);

            // Apportion tax to components
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
                .providerName("GST")
                .build();
    }

    private TaxComponentResult buildComponent(TaxCategory category, String componentLabel,
                                               BigDecimal ratePercent, boolean isRecoverable,
                                               TaxPostingProfile profile) {
        return TaxComponentResult.builder()
                .taxCategoryId(category.getId())
                .taxCategoryCode(componentLabel)
                .taxCategoryName(category.getName() + " - " + componentLabel)
                .ratePercent(ratePercent)
                .isRecoverable(isRecoverable)
                .inputTaxAccountId(profile.getInputTaxAccount() != null ? profile.getInputTaxAccount().getId() : null)
                .outputTaxAccountId(profile.getOutputTaxAccount() != null ? profile.getOutputTaxAccount().getId() : null)
                .reverseChargeAccountId(profile.getReverseChargeAccount() != null ? profile.getReverseChargeAccount().getId() : null)
                .recoverableAccountId(profile.getRecoverableAccount() != null ? profile.getRecoverableAccount().getId() : null)
                .nonRecoverableAccountId(profile.getNonRecoverableAccount() != null ? profile.getNonRecoverableAccount().getId() : null)
                .build();
    }

    private boolean isPurchaseDocument(String documentType) {
        if (documentType == null) return false;
        String doc = documentType.toUpperCase();
        return doc.contains("PURCHASE") || doc.contains("IMPORT") || doc.contains("SELF") || doc.contains("REVERSE_CHARGE");
    }
}
