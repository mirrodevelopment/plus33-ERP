package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    @Query("SELECT r FROM TaxRate r WHERE r.category.id = :categoryId AND r.active = true " +
           "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)")
    List<TaxRate> findActiveRatesByCategoryIdAndDate(@Param("categoryId") Long categoryId, @Param("date") LocalDate date);

    @Query("SELECT r FROM TaxRate r WHERE r.category.code = :categoryCode AND r.active = true " +
           "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)")
    List<TaxRate> findActiveRatesByCategoryCodeAndDate(@Param("categoryCode") String categoryCode, @Param("date") LocalDate date);
}
