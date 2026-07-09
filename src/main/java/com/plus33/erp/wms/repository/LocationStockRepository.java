/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : LocationStockRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LocationStockController
 * Related Service   : LocationStockService, LocationStockServiceImpl
 * Related Repository: LocationStockRepository
 * Related Entity    : LocationStock
 * Related DTO       : N/A
 * Related Mapper    : LocationStockMapper
 * Related DB Table  : location_stocks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LocationStockService, LocationStockServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'location_stocks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.LocationStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code LocationStockRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'location_stocks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code location_stocks}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface LocationStockRepository extends JpaRepository<LocationStock, Long> {

    @Query("SELECT ls FROM LocationStock ls WHERE ls.location.id = :locationId AND ls.productId = :productId AND ls.quantity > 0")
    List<LocationStock> findByLocationIdAndProductId(@Param("locationId") Long locationId,
                                                      @Param("productId") Long productId);

    List<LocationStock> findByCompanyIdAndProductId(Long companyId, Long productId);

    List<LocationStock> findByCompanyIdAndProductIdAndSerialNumber(Long companyId, Long productId, String serialNumber);

    @Query("SELECT ls FROM LocationStock ls WHERE ls.location.id = :locationId AND ls.quantity > 0")
    List<LocationStock> findByLocationId(@Param("locationId") Long locationId);

    /**
     * Pessimistic WRITE lock for bin moves — prevents concurrent quantity updates.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ls FROM LocationStock ls WHERE ls.location.id = :locationId AND ls.productId = :productId AND ls.lotNumber = :lotNumber")
    Optional<LocationStock> findForUpdateByLocationAndProductAndLot(@Param("locationId") Long locationId,
                                                                    @Param("productId") Long productId,
                                                                    @Param("lotNumber") String lotNumber);

    // FEFO: earliest expiry first (null expiry sorts last)
    @Query("""
        SELECT ls FROM LocationStock ls
        JOIN ls.location wl
        WHERE ls.companyId = :companyId
          AND ls.productId = :productId
          AND wl.warehouseId = :warehouseId
          AND wl.isPickable = TRUE
          AND wl.active = TRUE
          AND (ls.quantity - ls.reservedQuantity) > 0
        ORDER BY CASE WHEN ls.expiryDate IS NULL THEN 1 ELSE 0 END ASC,
                 ls.expiryDate ASC,
                 ls.receiptDate ASC
        """)
    List<LocationStock> findPickableByProductFefo(@Param("companyId") Long companyId,
                                                   @Param("productId") Long productId,
                                                   @Param("warehouseId") Long warehouseId);

    // FIFO: earliest receipt date first
    @Query("""
        SELECT ls FROM LocationStock ls
        JOIN ls.location wl
        WHERE ls.companyId = :companyId
          AND ls.productId = :productId
          AND wl.warehouseId = :warehouseId
          AND wl.isPickable = TRUE
          AND wl.active = TRUE
          AND (ls.quantity - ls.reservedQuantity) > 0
        ORDER BY ls.receiptDate ASC
        """)
    List<LocationStock> findPickableByProductFifo(@Param("companyId") Long companyId,
                                                   @Param("productId") Long productId,
                                                   @Param("warehouseId") Long warehouseId);

    // LIFO: latest receipt date first
    @Query("""
        SELECT ls FROM LocationStock ls
        JOIN ls.location wl
        WHERE ls.companyId = :companyId
          AND ls.productId = :productId
          AND wl.warehouseId = :warehouseId
          AND wl.isPickable = TRUE
          AND wl.active = TRUE
          AND (ls.quantity - ls.reservedQuantity) > 0
        ORDER BY ls.receiptDate DESC
        """)
    List<LocationStock> findPickableByProductLifo(@Param("companyId") Long companyId,
                                                   @Param("productId") Long productId,
                                                   @Param("warehouseId") Long warehouseId);

    // Batch/Cluster: aisle walk order (pick_sequence)
    @Query("""
        SELECT ls FROM LocationStock ls
        JOIN ls.location wl
        WHERE ls.companyId = :companyId
          AND ls.productId = :productId
          AND wl.warehouseId = :warehouseId
          AND wl.isPickable = TRUE
          AND wl.active = TRUE
          AND (ls.quantity - ls.reservedQuantity) > 0
        ORDER BY wl.pickSequence ASC
        """)
    List<LocationStock> findPickableByProductByPickSequence(@Param("companyId") Long companyId,
                                                             @Param("productId") Long productId,
                                                             @Param("warehouseId") Long warehouseId);

    // Zone picking: PICKING zone first, then fallback to BULK
    @Query("""
        SELECT ls FROM LocationStock ls
        JOIN ls.location wl
        JOIN wl.zone z
        WHERE ls.companyId = :companyId
          AND ls.productId = :productId
          AND wl.warehouseId = :warehouseId
          AND wl.isPickable = TRUE
          AND wl.active = TRUE
          AND z.zoneType IN ('PICKING', 'BULK')
          AND (ls.quantity - ls.reservedQuantity) > 0
        ORDER BY CASE z.zoneType WHEN 'PICKING' THEN 0 ELSE 1 END ASC,
                 ls.expiryDate ASC NULLS LAST
        """)
    List<LocationStock> findPickableByProductInPickingZone(@Param("companyId") Long companyId,
                                                            @Param("productId") Long productId,
                                                            @Param("warehouseId") Long warehouseId);

    // Total available stock across all bins for a product
    @Query("SELECT COALESCE(SUM(ls.quantity - ls.reservedQuantity), 0) FROM LocationStock ls WHERE ls.companyId = :companyId AND ls.productId = :productId")
    java.math.BigDecimal sumAvailableByProduct(@Param("companyId") Long companyId,
                                                @Param("productId") Long productId);
}
