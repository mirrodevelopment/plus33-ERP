/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.repository
 * File              : SalesTransactionItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Store Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesTransactionItemController
 * Related Service   : SalesTransactionItemService, SalesTransactionItemServiceImpl
 * Related Repository: SalesTransactionItemRepository
 * Related Entity    : SalesTransactionItem
 * Related DTO       : N/A
 * Related Mapper    : SalesTransactionItemMapper
 * Related DB Table  : sales_transaction_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesTransactionItemService, SalesTransactionItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Store Module against the 'sales_transaction_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.SalesTransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code SalesTransactionItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'sales_transaction_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_transaction_items}</p>
 * <p><b>Module Deps      :</b> Store</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SalesTransactionItemRepository extends JpaRepository<SalesTransactionItem, Long> {
    List<SalesTransactionItem> findBySalesTransactionId(Long salesTransactionId);
}
