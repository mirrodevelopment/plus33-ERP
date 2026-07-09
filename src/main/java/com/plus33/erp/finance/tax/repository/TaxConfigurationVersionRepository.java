/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxConfigurationVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxConfigurationVersionController
 * Related Service   : TaxConfigurationVersionService, TaxConfigurationVersionServiceImpl
 * Related Repository: TaxConfigurationVersionRepository
 * Related Entity    : TaxConfigurationVersion
 * Related DTO       : N/A
 * Related Mapper    : TaxConfigurationVersionMapper
 * Related DB Table  : tax_configuration_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxConfigurationVersionService, TaxConfigurationVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_configuration_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxConfigurationVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxConfigurationVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_configuration_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_configuration_versions}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxConfigurationVersionRepository extends JpaRepository<TaxConfigurationVersion, Long> {
    
    @Query("SELECT v FROM TaxConfigurationVersion v WHERE v.company.id = :companyId AND v.active = true " +
           "AND v.effectiveFrom <= :dateTime AND (v.effectiveTo IS NULL OR v.effectiveTo >= :dateTime)")
    Optional<TaxConfigurationVersion> findActiveVersionAt(
            @Param("companyId") Long companyId,
            @Param("dateTime") LocalDateTime dateTime
    );
}