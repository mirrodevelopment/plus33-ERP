/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.repository
 * File              : SalesTransactionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Store Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesTransactionController
 * Related Service   : SalesTransactionService, SalesTransactionServiceImpl
 * Related Repository: SalesTransactionRepository
 * Related Entity    : SalesTransaction
 * Related DTO       : N/A
 * Related Mapper    : SalesTransactionMapper
 * Related DB Table  : sales_transactions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesTransactionService, SalesTransactionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Store Module against the 'sales_transactions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code SalesTransactionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'sales_transactions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_transactions}</p>
 * <p><b>Module Deps      :</b> Store</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    Optional<SalesTransaction> findByTransactionNumber(String transactionNumber);
}
