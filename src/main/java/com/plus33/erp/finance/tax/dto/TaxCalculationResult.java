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
}
