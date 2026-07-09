/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.dto
 * File              : TaxComponentResult.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxComponentResultController
 * Related Service   : TaxComponentResultService, TaxComponentResultServiceImpl
 * Related Repository: TaxComponentResultRepository
 * Related Entity    : TaxComponentResult
 * Related DTO       : N/A
 * Related Mapper    : TaxComponentResultMapper
 * Related DB Table  : tax_component_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxComponentResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.dto}</p>
 * <p><b>Layer  :</b> Component of Finance Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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