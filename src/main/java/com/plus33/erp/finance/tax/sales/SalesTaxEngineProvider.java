package com.plus33.erp.finance.tax.sales;

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
 * US-style Sales Tax engine provider stub.
 * Calculates tax based on destination jurisdiction only (destination-based sourcing).
 * Supports tax-exclusive calculations only as US sales tax is always exclusive.
 */
@Component
@RequiredArgsConstructor
public class SalesTaxEngineProvider implements TaxEngineProvider {

    private final TaxRateRepository taxRateRepository;
    private final TaxPostingProfileService postingProfileService;
    private final TaxConfigurationCache taxConfigurationCache;

    @Override
    public String getTaxType() {
        return "SALES_TAX";
    }

    @Override
    public TaxCalculationResult calculateTax(TaxCalculationRequest request, TaxGroup taxGroup, boolean isExempt) {
        Long companyId = request.getCompanyId();
        LocalDate date = request.getTransactionDate();

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

                totalRatePercent = totalRatePercent.add(ratePercent);

                TaxPostingProfile profile = postingProfileService.getPostingProfile(companyId, category.getId());

                components.add(TaxComponentResult.builder()
                        .taxCategoryId(category.getId())
                        .taxCategoryCode(category.getCode())
                        .taxCategoryName(category.getName())
                        .ratePercent(ratePercent)
                        .isRecoverable(false) // US sales tax is never recoverable for the seller
                        .inputTaxAccountId(profile.getInputTaxAccount() != null ? profile.getInputTaxAccount().getId() : null)
                        .outputTaxAccountId(profile.getOutputTaxAccount() != null ? profile.getOutputTaxAccount().getId() : null)
                        .reverseChargeAccountId(null)
                        .recoverableAccountId(null)
                        .nonRecoverableAccountId(null)
                        .build());
            }

            // US sales tax is always exclusive
            BigDecimal netAmount = lineReq.getAmount();
            BigDecimal lineTaxAmount = netAmount.multiply(totalRatePercent)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
            BigDecimal grossAmount = netAmount.add(lineTaxAmount);

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
                comp.setRecoverableAmount(BigDecimal.ZERO);
                comp.setNonRecoverableAmount(BigDecimal.ZERO);
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
                .providerName("SALES_TAX")
                .build();
    }
}
