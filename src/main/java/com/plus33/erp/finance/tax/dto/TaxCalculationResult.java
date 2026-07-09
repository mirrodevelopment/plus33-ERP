/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.dto
 * File              : TaxCalculationResult.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationResultController
 * Related Service   : TaxCalculationResultService, TaxCalculationResultServiceImpl
 * Related Repository: TaxCalculationResultRepository
 * Related Entity    : TaxCalculationResult
 * Related DTO       : N/A
 * Related Mapper    : TaxCalculationResultMapper
 * Related DB Table  : tax_calculation_results
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
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalculationResult}</p>
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