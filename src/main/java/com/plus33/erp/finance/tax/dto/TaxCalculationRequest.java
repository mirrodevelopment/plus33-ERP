package com.plus33.erp.finance.tax.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculationRequest {
    private Long companyId;
    private LocalDate transactionDate;
    private String documentType; // SALES_INVOICE, PURCHASE_INVOICE, etc.
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
