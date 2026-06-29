package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TaxConfigurationVersionRepository extends JpaRepository<TaxConfigurationVersion, Long> {
    
    @Query("SELECT v FROM TaxConfigurationVersion v WHERE v.company.id = :companyId AND v.active = true " +
           "AND v.effectiveFrom <= :dateTime AND (v.effectiveTo IS NULL OR v.effectiveTo >= :dateTime)")
    Optional<TaxConfigurationVersion> findActiveVersionAt(
            @Param("companyId") Long companyId,
            @Param("dateTime") LocalDateTime dateTime
    );
}
