package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.PaymentAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentAllocationRepository extends JpaRepository<PaymentAllocation, Long> {
    List<PaymentAllocation> findByPaymentId(Long paymentId);
    List<PaymentAllocation> findBySupplierInvoiceId(Long supplierInvoiceId);
}
