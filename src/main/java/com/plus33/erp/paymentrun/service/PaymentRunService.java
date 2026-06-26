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
