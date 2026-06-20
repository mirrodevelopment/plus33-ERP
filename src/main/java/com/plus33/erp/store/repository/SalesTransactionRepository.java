package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    Optional<SalesTransaction> findByTransactionNumber(String transactionNumber);
}
