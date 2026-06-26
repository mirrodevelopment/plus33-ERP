package com.plus33.erp.paymentrun.repository;

import com.plus33.erp.paymentrun.entity.PaymentRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRunRepository extends JpaRepository<PaymentRun, Long>, JpaSpecificationExecutor<PaymentRun> {
    Optional<PaymentRun> findByRunNumber(String runNumber);
    Optional<PaymentRun> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('payment_run_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}

