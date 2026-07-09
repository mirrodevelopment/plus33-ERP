/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : DepreciationBookRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepreciationBookController
 * Related Service   : DepreciationBookService, DepreciationBookServiceImpl
 * Related Repository: DepreciationBookRepository
 * Related Entity    : DepreciationBook
 * Related DTO       : N/A
 * Related Mapper    : DepreciationBookMapper
 * Related DB Table  : depreciation_books
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DepreciationBookService, DepreciationBookServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'depreciation_books' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.DepreciationBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DepreciationBookRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'depreciation_books' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code depreciation_books}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface DepreciationBookRepository extends JpaRepository<DepreciationBook, Long> {
    Optional<DepreciationBook> findByCompanyIdAndCode(Long companyId, String code);
    List<DepreciationBook> findAllByCompanyId(Long companyId);
}