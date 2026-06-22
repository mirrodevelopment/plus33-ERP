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
