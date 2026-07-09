/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerInvoiceSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceSearchController
 * Related Service   : CustomerInvoiceSearchService, CustomerInvoiceSearchServiceImpl
 * Related Repository: CustomerInvoiceSearchRepository
 * Related Entity    : CustomerInvoiceSearch
 * Related DTO       : CustomerInvoiceSearchRequest
 * Related Mapper    : CustomerInvoiceSearchMapper
 * Related DB Table  : customer_invoice_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceSearchController, CustomerInvoiceSearchService, CustomerInvoiceSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Builder
public class CustomerInvoiceSearchRequest {
    private String invoiceNumber;
    private Long companyId;
    private Long customerId;
    private Long salesOrderId;
    private CustomerInvoiceStatus status;
    private LocalDate invoiceDateFrom;
    private LocalDate invoiceDateTo;
}