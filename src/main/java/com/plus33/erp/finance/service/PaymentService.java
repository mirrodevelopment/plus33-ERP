package com.plus33.erp.finance.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.dto.*;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PageResponse<PaymentResponse> searchPayments(PaymentSearchRequest searchRequest, Pageable pageable);
    PaymentResponse cancelPayment(Long id, PaymentCancelRequest request);
}
