package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculationLineRequest {
    private Long lineId;
    private String productTaxCategory;
    private BigDecimal amount;
    private boolean taxInclusive;
}
