package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByCompanyIdAndPaymentNumber(Long companyId, String paymentNumber);
    List<Payment> findByCompanyId(Long companyId);
    Optional<Payment> findByCompanyIdAndReferenceNumber(Long companyId, String referenceNumber);

    @Query(value = "SELECT nextval('payment_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
