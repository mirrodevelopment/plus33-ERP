package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentBatchRepository extends JpaRepository<PaymentBatch, Long> {
    List<PaymentBatch> findByCompanyId(Long companyId);
    Optional<PaymentBatch> findByCompanyIdAndBatchNumber(Long companyId, String batchNumber);
}
