/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CustomerInvoiceService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceService, CustomerInvoiceServiceImpl
 * Related Repository: CustomerInvoiceRepository
 * Related Entity    : CustomerInvoice
 * Related DTO       : CustomerInvoiceRequest, CustomerInvoiceResponse, CustomerInvoiceSearchRequest, CustomerInvoiceUpdateRequest, PageResponse
 * Related Mapper    : CustomerInvoiceMapper
 * Related DB Table  : customer_invoices
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CustomerInvoiceService {
    CustomerInvoiceResponse createInvoice(CustomerInvoiceRequest request);
    CustomerInvoiceResponse updateInvoice(Long id, CustomerInvoiceUpdateRequest request);
    CustomerInvoiceResponse getInvoiceById(Long id);
    PageResponse<CustomerInvoiceResponse> searchInvoices(CustomerInvoiceSearchRequest searchRequest, Pageable pageable);
    CustomerInvoiceResponse submitInvoice(Long id);
    CustomerInvoiceResponse approveInvoice(Long id);
    CustomerInvoiceResponse cancelInvoice(Long id, String reason);
    CustomerInvoiceResponse voidInvoice(Long id);
    CustomerInvoiceResponse allocatePayment(Long id, BigDecimal amount);
    CustomerInvoiceResponse deallocatePayment(Long id, BigDecimal amount);
}
