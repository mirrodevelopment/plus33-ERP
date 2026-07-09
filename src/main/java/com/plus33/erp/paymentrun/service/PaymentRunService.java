/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service
 * File              : PaymentRunService.java
 * Purpose           : Service interface contract defining the API for Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunService, PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository
 * Related Entity    : PaymentRun
 * Related DTO       : PaymentRunDashboardResponse, PaymentRunInvoiceRequest, PaymentRunRequest, PaymentRunResponse
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Paymentrun Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.paymentrun.service;

import com.plus33.erp.paymentrun.dto.*;
import java.util.List;

public interface PaymentRunService {
    PaymentRunResponse createPaymentRun(PaymentRunRequest request);
    PaymentRunResponse calculatePaymentRun(Long id);
    PaymentRunResponse updatePaymentRunInvoices(Long id, List<PaymentRunInvoiceRequest> requests);
    PaymentRunResponse approvePaymentRun(Long id);
    PaymentRunResponse executePaymentRun(Long id);
    PaymentRunResponse cancelPaymentRun(Long id);
    PaymentRunDashboardResponse getPaymentRunDashboard(Long companyId);
    PaymentRunResponse getPaymentRunById(Long id);
    List<PaymentRunResponse> searchPaymentRuns(Long companyId, String status);
}
