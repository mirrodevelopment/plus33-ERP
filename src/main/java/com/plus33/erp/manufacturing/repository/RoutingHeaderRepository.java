package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.RoutingHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutingHeaderRepository extends JpaRepository<RoutingHeader, Long> {

    List<RoutingHeader> findByCompanyIdAndProductId(Long companyId, Long productId);

    Optional<RoutingHeader> findByCompanyIdAndRoutingNumber(Long companyId, String routingNumber);

    @Query("""
        SELECT r FROM RoutingHeader r
        WHERE r.companyId = :companyId
          AND r.product.id = :productId
          AND r.status = 'ACTIVE'
          AND r.effectiveFrom <= :date
          AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
        ORDER BY r.effectiveFrom DESC
    """)
    List<RoutingHeader> findActiveByProductAndDate(
            @Param("companyId") Long companyId,
            @Param("productId") Long productId,
            @Param("date") LocalDate date);
}
