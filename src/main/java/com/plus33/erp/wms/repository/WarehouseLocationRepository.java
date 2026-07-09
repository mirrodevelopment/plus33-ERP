/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseLocationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLocationController
 * Related Service   : WarehouseLocationService, WarehouseLocationServiceImpl
 * Related Repository: WarehouseLocationRepository
 * Related Entity    : WarehouseLocation
 * Related DTO       : N/A
 * Related Mapper    : WarehouseLocationMapper
 * Related DB Table  : warehouse_locations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseLocationService, WarehouseLocationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_locations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLocationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_locations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_locations}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {

    Optional<WarehouseLocation> findByWarehouseIdAndLocationCode(Long warehouseId, String locationCode);

    List<WarehouseLocation> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);

    List<WarehouseLocation> findByZone_IdAndActiveTrue(Long zoneId);

    // Empty receivable locations ordered by pick_sequence for NEAREST_EMPTY_BIN strategy
    @Query("""
        SELECT wl FROM WarehouseLocation wl
        WHERE wl.warehouseId = :warehouseId
          AND wl.companyId = :companyId
          AND wl.active = TRUE
          AND wl.isReceivable = TRUE
          AND wl.id NOT IN (
              SELECT ls.location.id FROM LocationStock ls
              WHERE ls.quantity > 0
          )
        ORDER BY wl.pickSequence ASC
        """)
    List<WarehouseLocation> findEmptyReceivableLocations(@Param("warehouseId") Long warehouseId,
                                                          @Param("companyId") Long companyId);

    // Locations with most remaining pallet capacity for CAPACITY_BASED strategy
    @Query("""
        SELECT wl FROM WarehouseLocation wl
        WHERE wl.warehouseId = :warehouseId
          AND wl.companyId = :companyId
          AND wl.active = TRUE
          AND wl.isReceivable = TRUE
        ORDER BY (wl.maxPallets - SIZE(wl.zone.code)) DESC
        """)
    List<WarehouseLocation> findLocationsWithMostCapacity(@Param("warehouseId") Long warehouseId,
                                                           @Param("companyId") Long companyId);

    @Query("""
        SELECT wl FROM WarehouseLocation wl
        JOIN wl.zone z
        WHERE wl.warehouseId = :warehouseId
          AND z.zoneType = :zoneType
          AND wl.active = TRUE
          AND wl.isReceivable = TRUE
        ORDER BY wl.pickSequence ASC
        """)
    List<WarehouseLocation> findByZoneTypeAndWarehouseId(@Param("zoneType") String zoneType,
                                                          @Param("warehouseId") Long warehouseId);

    @Query("""
        SELECT wl FROM WarehouseLocation wl
        WHERE wl.warehouseId = :warehouseId
          AND wl.velocityClass = :velocityClass
          AND wl.active = TRUE
          AND wl.isReceivable = TRUE
        ORDER BY wl.pickSequence ASC
        """)
    List<WarehouseLocation> findByVelocityClassAndWarehouseId(@Param("velocityClass") String velocityClass,
                                                               @Param("warehouseId") Long warehouseId);
}
