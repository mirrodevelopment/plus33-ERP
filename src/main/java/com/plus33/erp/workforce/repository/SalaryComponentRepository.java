/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : SalaryComponentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalaryComponentController
 * Related Service   : SalaryComponentService, SalaryComponentServiceImpl
 * Related Repository: SalaryComponentRepository
 * Related Entity    : SalaryComponent
 * Related DTO       : N/A
 * Related Mapper    : SalaryComponentMapper
 * Related DB Table  : salary_components
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalaryComponentService, SalaryComponentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'salary_components' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.SalaryComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code SalaryComponentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'salary_components' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code salary_components}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface SalaryComponentRepository extends JpaRepository<SalaryComponent, Long> {
    Optional<SalaryComponent> findByCompanyIdAndCode(Long companyId, String code);
    List<SalaryComponent> findByCompanyId(Long companyId);
}