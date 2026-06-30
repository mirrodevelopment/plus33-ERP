package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
