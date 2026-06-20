package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByCompanyIdAndPaymentNumber(Long companyId, String paymentNumber);
    List<Payment> findByCompanyId(Long companyId);
}
