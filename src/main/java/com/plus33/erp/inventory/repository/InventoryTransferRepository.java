/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryTransferRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferController
 * Related Service   : InventoryTransferService, InventoryTransferServiceImpl
 * Related Repository: InventoryTransferRepository
 * Related Entity    : InventoryTransfer
 * Related DTO       : N/A
 * Related Mapper    : InventoryTransferMapper
 * Related DB Table  : inventory_transfers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTransferService, InventoryTransferServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_transfers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTransferRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_transfers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_transfers}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long>, JpaSpecificationExecutor<InventoryTransfer> {

    Optional<InventoryTransfer> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('inventory_transfer_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
