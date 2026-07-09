/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.repository
 * File              : CompanyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Organization Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyService, CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : N/A
 * Related Mapper    : CompanyMapper
 * Related DB Table  : companys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompanyService, CompanyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Organization Module against the 'companys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code CompanyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'companys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code companys}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    Optional<Company> findByCode(String code);
    boolean existsByCode(String code);
}
