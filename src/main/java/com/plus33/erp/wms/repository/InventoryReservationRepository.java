/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : InventoryReservationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryReservationController
 * Related Service   : InventoryReservationService, InventoryReservationServiceImpl
 * Related Repository: InventoryReservationRepository
 * Related Entity    : InventoryReservation
 * Related DTO       : N/A
 * Related Mapper    : InventoryReservationMapper
 * Related DB Table  : inventory_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryReservationService, InventoryReservationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'inventory_reservations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryReservationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_reservations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_reservations}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    Optional<InventoryReservation> findByCompanyIdAndIdempotencyKey(Long companyId, String idempotencyKey);
    List<InventoryReservation> findByCompanyIdAndProductIdAndStatus(Long companyId, Long productId, String status);
    List<InventoryReservation> findBySourceTypeAndSourceId(String sourceType, Long sourceId);

    @Query("SELECT ir FROM InventoryReservation ir WHERE ir.status NOT IN ('RELEASED','EXPIRED') AND ir.expiryAt <= :now")
    List<InventoryReservation> findExpiredReservations(@Param("now") LocalDateTime now);
}
