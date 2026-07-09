/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : PaymentService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentController
 * Related Service   : PaymentService, PaymentServiceImpl
 * Related Repository: PaymentRepository
 * Related Entity    : Payment
 * Related DTO       : PageResponse, PaymentCancelRequest, PaymentRequest, PaymentResponse, PaymentSearchRequest
 * Related Mapper    : PaymentMapper
 * Related DB Table  : payments
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PageResponse<PaymentResponse> searchPayments(PaymentSearchRequest searchRequest, Pageable pageable);
    PaymentResponse cancelPayment(Long id, PaymentCancelRequest request);
}
