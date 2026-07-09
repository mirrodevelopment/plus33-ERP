/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : SupplierInvoiceSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceSearchController
 * Related Service   : SupplierInvoiceSearchService, SupplierInvoiceSearchServiceImpl
 * Related Repository: SupplierInvoiceSearchRepository
 * Related Entity    : SupplierInvoiceSearch
 * Related DTO       : SupplierInvoiceSearchRequest
 * Related Mapper    : SupplierInvoiceSearchMapper
 * Related DB Table  : supplier_invoice_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierInvoiceSearchController, SupplierInvoiceSearchService, SupplierInvoiceSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import com.plus33.erp.finance.entity.SupplierInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInvoiceSearchRequest {

    private String invoiceNumber;
    private Long companyId;
    private Long supplierId;
    private Long purchaseOrderId;
    private SupplierInvoiceStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDateTo;
}