/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.dto
 * File              : TaxCalculationRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationController
 * Related Service   : TaxCalculationService, TaxCalculationServiceImpl
 * Related Repository: TaxCalculationRepository
 * Related Entity    : TaxCalculation
 * Related DTO       : TaxCalculationLineRequest, TaxCalculationRequest
 * Related Mapper    : TaxCalculationMapper
 * Related DB Table  : tax_calculations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxCalculationController, TaxCalculationService, TaxCalculationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalculationRequest}</p>
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
public class TaxCalculationRequest {
    private Long companyId;
    private LocalDate transactionDate;
    private String documentType; // SALES_INVOICE, PURCHASE_INVOICE, etc.
    private Long documentId; // For override lookup; null during draft calculation
    private Long customerId;
    private Long supplierId;
    private String customerTaxProfile;
    private String supplierTaxProfile;
    private String originCountry;
    private String originState;
    private String destCountry;
    private String destState;
    private String incoterms;
    private List<TaxCalculationLineRequest> lines;
}