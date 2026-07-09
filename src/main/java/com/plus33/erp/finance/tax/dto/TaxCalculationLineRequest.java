/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.dto
 * File              : TaxCalculationLineRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationLineController
 * Related Service   : TaxCalculationLineService, TaxCalculationLineServiceImpl
 * Related Repository: TaxCalculationLineRepository
 * Related Entity    : TaxCalculationLine
 * Related DTO       : TaxCalculationLineRequest
 * Related Mapper    : TaxCalculationLineMapper
 * Related DB Table  : tax_calculation_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxCalculationLineController, TaxCalculationLineService, TaxCalculationLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalculationLineRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
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
public class TaxCalculationLineRequest {
    private Long lineId;
    private String productTaxCategory;
    private BigDecimal amount;
    private boolean taxInclusive;
}