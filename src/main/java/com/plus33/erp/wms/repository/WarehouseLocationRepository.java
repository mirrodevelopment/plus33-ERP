package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
