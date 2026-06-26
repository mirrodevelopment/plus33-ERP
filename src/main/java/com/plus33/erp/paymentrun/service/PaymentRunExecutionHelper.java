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

@Component
public class PaymentRunExecutionHelper {

    private final PaymentService paymentService;

    public PaymentRunExecutionHelper(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

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
