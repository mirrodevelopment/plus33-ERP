package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

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
