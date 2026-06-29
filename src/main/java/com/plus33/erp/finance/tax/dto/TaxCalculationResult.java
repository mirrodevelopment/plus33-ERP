package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculationResult {
    private BigDecimal totalNetAmount;
    private BigDecimal totalTaxAmount;
    private BigDecimal totalGrossAmount;
    private List<TaxCalculationLineResult> lines;

    // Rule resolution diagnostics
    private Long matchedRuleId;
    private String matchedJurisdictionId;
    private String providerName;
    private Integer configurationVersion;
    private boolean overrideApplied;
    private String overrideReason;
}
