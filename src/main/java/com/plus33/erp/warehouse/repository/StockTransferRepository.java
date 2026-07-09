/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Module
 * Package           : com.plus33.erp.warehouse.repository
 * File              : StockTransferRepository.java
 * Purpose           : JPA Repository providing database CRUD for Warehouse Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockTransferController
 * Related Service   : StockTransferService, StockTransferServiceImpl
 * Related Repository: StockTransferRepository
 * Related Entity    : StockTransfer
 * Related DTO       : N/A
 * Related Mapper    : StockTransferMapper
 * Related DB Table  : stock_transfers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockTransferService, StockTransferServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Warehouse Module against the 'stock_transfers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.warehouse.repository;

import com.plus33.erp.warehouse.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Warehouse Module</b>
 *
 * <p><b>Class  :</b> {@code StockTransferRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.warehouse.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'stock_transfers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_transfers}</p>
 * <p><b>Module Deps      :</b> Warehouse</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    Optional<StockTransfer> findByTransferNumber(String transferNumber);
}
