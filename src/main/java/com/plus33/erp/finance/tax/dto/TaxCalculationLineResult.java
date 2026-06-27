package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculationLineResult {
    private Long lineId;
    private BigDecimal netAmount;
    private BigDecimal taxAmount;
    private BigDecimal grossAmount;
    private List<TaxComponentResult> taxComponents;
}
