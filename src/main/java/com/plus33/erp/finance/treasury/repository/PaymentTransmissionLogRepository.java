package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentTransmissionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentTransmissionLogRepository extends JpaRepository<PaymentTransmissionLog, Long> {
    List<PaymentTransmissionLog> findByFileId(Long fileId);
}
