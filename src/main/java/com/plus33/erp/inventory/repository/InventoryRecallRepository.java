/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryRecallRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRecallController
 * Related Service   : InventoryRecallService, InventoryRecallServiceImpl
 * Related Repository: InventoryRecallRepository
 * Related Entity    : InventoryRecall
 * Related DTO       : N/A
 * Related Mapper    : InventoryRecallMapper
 * Related DB Table  : inventory_recalls
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryRecallService, InventoryRecallServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_recalls' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryRecall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryRecallRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_recalls' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_recalls}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryRecallRepository extends JpaRepository<InventoryRecall, Long>, JpaSpecificationExecutor<InventoryRecall> {

    Optional<InventoryRecall> findByRecallNumber(String recallNumber);

    boolean existsByLotIdAndStatus(Long lotId, com.plus33.erp.inventory.entity.InventoryRecallStatus status);

    boolean existsBySerialIdAndStatus(Long serialId, com.plus33.erp.inventory.entity.InventoryRecallStatus status);

    @Query(value = "SELECT nextval('inventory_recall_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
