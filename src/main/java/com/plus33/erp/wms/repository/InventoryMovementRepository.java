/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : InventoryMovementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryMovementController
 * Related Service   : InventoryMovementService, InventoryMovementServiceImpl
 * Related Repository: InventoryMovementRepository
 * Related Entity    : InventoryMovement
 * Related DTO       : N/A
 * Related Mapper    : InventoryMovementMapper
 * Related DB Table  : inventory_movements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryMovementService, InventoryMovementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'inventory_movements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryMovementRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_movements' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_movements}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {

    Optional<InventoryMovement> findByCompanyIdAndIdempotencyKey(Long companyId, String idempotencyKey);

    List<InventoryMovement> findByCompanyIdAndProductIdOrderByMovementAtDesc(Long companyId, Long productId);

    @Query("SELECT im FROM InventoryMovement im WHERE im.companyId = :companyId AND im.productId = :productId AND im.movementAt BETWEEN :from AND :to ORDER BY im.movementAt ASC")
    List<InventoryMovement> findByProductAndDateRange(@Param("companyId") Long companyId,
                                                       @Param("productId") Long productId,
                                                       @Param("from") LocalDateTime from,
                                                       @Param("to") LocalDateTime to);

    @Query("SELECT im FROM InventoryMovement im WHERE im.sourceType = :sourceType AND im.sourceId = :sourceId ORDER BY im.movementAt ASC")
    List<InventoryMovement> findBySourceDocument(@Param("sourceType") String sourceType,
                                                  @Param("sourceId") Long sourceId);
}
