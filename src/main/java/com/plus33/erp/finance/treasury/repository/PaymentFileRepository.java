package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentFileRepository extends JpaRepository<PaymentFile, Long> {
    Optional<PaymentFile> findByBatchId(Long batchId);
}
