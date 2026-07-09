/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : PaymentFactoryService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentFactoryController
 * Related Service   : PaymentFactoryService, PaymentFactoryServiceImpl
 * Related Repository: PaymentFactoryRepository
 * Related Entity    : PaymentFactory
 * Related DTO       : CashTransferRequest, CashTransferResponse, PaymentBatchRequest, PaymentBatchResponse, PaymentFileResponse
 * Related Mapper    : PaymentFactoryMapper
 * Related DB Table  : payment_factorys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.PaymentFactoryDtos.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentFactoryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
