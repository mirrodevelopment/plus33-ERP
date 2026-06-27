package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxComponentResult {
    private Long taxCategoryId;
    private String taxCategoryCode;
    private String taxCategoryName;
    private BigDecimal ratePercent;
    private BigDecimal taxAmount;
    private boolean isRecoverable;
    private BigDecimal recoverableAmount;
    private BigDecimal nonRecoverableAmount;

    // GL Account IDs mapped from posting profile
    private Long inputTaxAccountId;
    private Long outputTaxAccountId;
    private Long reverseChargeAccountId;
    private Long recoverableAccountId;
    private Long nonRecoverableAccountId;
}
