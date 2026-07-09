/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxGroupRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxGroupController
 * Related Service   : TaxGroupService, TaxGroupServiceImpl
 * Related Repository: TaxGroupRepository
 * Related Entity    : TaxGroup
 * Related DTO       : N/A
 * Related Mapper    : TaxGroupMapper
 * Related DB Table  : tax_groups
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxGroupService, TaxGroupServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_groups' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxGroupRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_groups' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_groups}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxGroupRepository extends JpaRepository<TaxGroup, Long> {
    Optional<TaxGroup> findByCode(String code);
}