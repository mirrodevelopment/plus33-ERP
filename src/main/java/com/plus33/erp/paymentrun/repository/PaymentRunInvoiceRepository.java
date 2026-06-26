package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRunInvoiceRepository extends JpaRepository<PaymentRunInvoice, Long> {
    List<PaymentRunInvoice> findByPaymentRunId(Long paymentRunId);
}
