package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomHeader;
import com.plus33.erp.manufacturing.entity.BomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BomHeaderRepository extends JpaRepository<BomHeader, Long> {

    List<BomHeader> findByCompanyIdAndStatus(Long companyId, BomStatus status);

    List<BomHeader> findByCompanyIdAndProductId(Long companyId, Long productId);

    Optional<BomHeader> findByCompanyIdAndBomNumber(Long companyId, String bomNumber);

    boolean existsByCompanyIdAndBomNumber(Long companyId, String bomNumber);

    @Query("""
        SELECT b FROM BomHeader b
        WHERE b.companyId = :companyId
          AND b.product.id = :productId
          AND b.status = com.plus33.erp.manufacturing.entity.BomStatus.ACTIVE
          AND b.effectiveFrom <= :date
          AND (b.effectiveTo IS NULL OR b.effectiveTo >= :date)
        ORDER BY b.effectiveFrom DESC
    """)
    List<BomHeader> findActiveByProductAndDate(
            @Param("companyId") Long companyId,
            @Param("productId") Long productId,
            @Param("date") LocalDate date);

    default List<BomHeader> findActiveBomForProduct(Long companyId, Long productId, LocalDate date) {
        return findActiveByProductAndDate(companyId, productId, date);
    }

    @Query("SELECT b FROM BomHeader b WHERE b.companyId = :companyId ORDER BY b.createdAt DESC")
    List<BomHeader> findAllByCompany(@Param("companyId") Long companyId);
}
