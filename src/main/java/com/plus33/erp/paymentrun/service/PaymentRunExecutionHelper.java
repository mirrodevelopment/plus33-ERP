/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.service
 * File              : PaymentRunExecutionHelper.java
 * Purpose           : Component of Paymentrun Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunExecutionHelperController
 * Related Service   : PaymentRunExecutionHelperService, PaymentRunExecutionHelperServiceImpl
 * Related Repository: PaymentRunExecutionHelperRepository
 * Related Entity    : PaymentRunExecutionHelper
 * Related DTO       : PaymentAllocationRequest, PaymentRequest, PaymentResponse
 * Related Mapper    : PaymentRunExecutionHelperMapper
 * Related DB Table  : payment_run_execution_helpers
 * Related REST APIs : N/A
 * Depends On        : Finance Module
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Paymentrun Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.paymentrun.service;

import com.plus33.erp.finance.dto.PaymentAllocationRequest;
import com.plus33.erp.finance.dto.PaymentRequest;
import com.plus33.erp.finance.dto.PaymentResponse;
import com.plus33.erp.finance.service.PaymentService;
import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunExecutionHelper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance, Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class PaymentRunExecutionHelper {

    private final PaymentService paymentService;

    public PaymentRunExecutionHelper(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Performs the executeSupplierPayment operation in this module.
     *
     * @return the PaymentResponse result
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentResponse executeSupplierPayment(
            Long supplierId,
            List<PaymentRunInvoice> items,
            PaymentRun run) {
        
        BigDecimal totalAmount = items.stream()
                .map(PaymentRunInvoice::getPaymentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<PaymentAllocationRequest> allocations = items.stream()
                .map(item -> PaymentAllocationRequest.builder()
                        .supplierInvoiceId(item.getSupplierInvoice().getId())
                        .allocatedAmount(item.getPaymentAmount())
                        .build())
                .collect(Collectors.toList());

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .companyId(run.getCompany().getId())
                .supplierId(supplierId)
                .paymentDate(run.getPaymentDate())
                .paymentMethod(run.getPaymentMethod())
                .amount(totalAmount)
                .referenceNumber(run.getRunNumber() + "-" + supplierId)
                .currencyCode(run.getCurrencyCode())
                .allocations(allocations)
                .build();

        return paymentService.createPayment(paymentRequest);
    }
}