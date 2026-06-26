package com.plus33.erp.finance.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.dto.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

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
