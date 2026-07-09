/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxRateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxRateController
 * Related Service   : TaxRateService, TaxRateServiceImpl
 * Related Repository: TaxRateRepository
 * Related Entity    : TaxRate
 * Related DTO       : N/A
 * Related Mapper    : TaxRateMapper
 * Related DB Table  : tax_rates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxRateService, TaxRateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_rates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxRateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_rates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_rates}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {
    @Query("SELECT r FROM TaxRate r WHERE r.category.id = :categoryId AND r.active = true " +
           "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)")
    List<TaxRate> findActiveRatesByCategoryIdAndDate(@Param("categoryId") Long categoryId, @Param("date") LocalDate date);

    @Query("SELECT r FROM TaxRate r WHERE r.category.code = :categoryCode AND r.active = true " +
           "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)")
    List<TaxRate> findActiveRatesByCategoryCodeAndDate(@Param("categoryCode") String categoryCode, @Param("date") LocalDate date);
}