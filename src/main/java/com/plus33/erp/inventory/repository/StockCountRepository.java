/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : StockCountRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : N/A
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountService, StockCountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'stock_counts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.StockCount;
import com.plus33.erp.inventory.entity.StockCountStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'stock_counts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_counts}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StockCountRepository extends JpaRepository<StockCount, Long>, JpaSpecificationExecutor<StockCount> {

    Optional<StockCount> findByClientReferenceId(UUID clientReferenceId);

    Optional<StockCount> findByCountNumber(String countNumber);

    @Query(value = "SELECT nextval('stock_count_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sc FROM StockCount sc WHERE sc.status IN :statuses AND " +
           "((:warehouseId IS NOT NULL AND sc.warehouse.id = :warehouseId) OR " +
           "(:storeId IS NOT NULL AND sc.store.id = :storeId))")
    List<StockCount> findActiveCountsForLocationWithLock(
            @Param("statuses") List<StockCountStatus> statuses,
            @Param("warehouseId") Long warehouseId,
            @Param("storeId") Long storeId
    );
}
