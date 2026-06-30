package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    Optional<InventoryReservation> findByCompanyIdAndIdempotencyKey(Long companyId, String idempotencyKey);
    List<InventoryReservation> findByCompanyIdAndProductIdAndStatus(Long companyId, Long productId, String status);
    List<InventoryReservation> findBySourceTypeAndSourceId(String sourceType, Long sourceId);

    @Query("SELECT ir FROM InventoryReservation ir WHERE ir.status NOT IN ('RELEASED','EXPIRED') AND ir.expiryAt <= :now")
    List<InventoryReservation> findExpiredReservations(@Param("now") LocalDateTime now);
}
