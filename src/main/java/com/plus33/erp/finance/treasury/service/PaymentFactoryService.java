package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.PaymentFactoryDtos.*;

import java.util.List;

public interface PaymentFactoryService {
    PaymentBatchResponse createPaymentBatch(PaymentBatchRequest request, String username);
    PaymentBatchResponse getPaymentBatchById(Long id);
    List<PaymentBatchResponse> getPaymentBatchesByCompany(Long companyId);
    void approvePaymentBatch(Long batchId, String remarks, String username);
    void rejectPaymentBatch(Long batchId, String remarks, String username);
    PaymentFileResponse generatePaymentFile(Long batchId, String format);
    void transmitPaymentFile(Long fileId, String method);

    CashTransferResponse createCashTransfer(CashTransferRequest request, String username);
    CashTransferResponse getCashTransferById(Long id);
    List<CashTransferResponse> getCashTransfersByCompany(Long companyId);
    void approveCashTransfer(Long transferId, String remarks, String username);
    void rejectCashTransfer(Long transferId, String remarks, String username);
}
