/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : SupplierInvoiceService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceService, SupplierInvoiceServiceImpl
 * Related Repository: SupplierInvoiceRepository
 * Related Entity    : SupplierInvoice
 * Related DTO       : PageResponse, searchRequest, SupplierInvoiceRequest, SupplierInvoiceResponse, SupplierInvoiceSearchRequest
 * Related Mapper    : SupplierInvoiceMapper
 * Related DB Table  : supplier_invoices
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.dto.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierInvoiceService {
    SupplierInvoiceResponse createInvoice(SupplierInvoiceRequest request);
    SupplierInvoiceResponse updateInvoice(Long id, SupplierInvoiceUpdateRequest request);
    SupplierInvoiceResponse getInvoiceById(Long id);
    PageResponse<SupplierInvoiceResponse> searchInvoices(SupplierInvoiceSearchRequest searchRequest, Pageable pageable);
    SupplierInvoiceResponse approveInvoice(Long id);
    SupplierInvoiceResponse cancelInvoice(Long id);
    SupplierInvoiceResponse allocatePayment(Long id, BigDecimal amount);
    SupplierInvoiceResponse deallocatePayment(Long id, BigDecimal amount);
    SupplierInvoiceResponse submitInvoice(Long id);
    SupplierInvoiceResponse voidInvoice(Long id);
}
