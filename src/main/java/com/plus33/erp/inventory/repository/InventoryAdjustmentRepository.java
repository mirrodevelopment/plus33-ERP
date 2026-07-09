/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryAdjustmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentController
 * Related Service   : InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 * Related Repository: InventoryAdjustmentRepository
 * Related Entity    : InventoryAdjustment
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentMapper
 * Related DB Table  : inventory_adjustments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAdjustmentService, InventoryAdjustmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_adjustments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAdjustmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_adjustments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_adjustments}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustment, Long>, JpaSpecificationExecutor<InventoryAdjustment> {

    Optional<InventoryAdjustment> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('inventory_adjustment_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
