package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRunSupplierResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRunSupplierResultRepository extends JpaRepository<PaymentRunSupplierResult, Long> {
    List<PaymentRunSupplierResult> findByPaymentRunId(Long paymentRunId);
}
