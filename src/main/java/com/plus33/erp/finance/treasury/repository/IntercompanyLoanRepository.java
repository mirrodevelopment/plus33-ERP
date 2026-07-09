/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : IntercompanyLoanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntercompanyLoanController
 * Related Service   : IntercompanyLoanService, IntercompanyLoanServiceImpl
 * Related Repository: IntercompanyLoanRepository
 * Related Entity    : IntercompanyLoan
 * Related DTO       : N/A
 * Related Mapper    : IntercompanyLoanMapper
 * Related DB Table  : intercompany_loans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntercompanyLoanService, IntercompanyLoanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'intercompany_loans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.IntercompanyLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code IntercompanyLoanRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'intercompany_loans' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code intercompany_loans}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface IntercompanyLoanRepository extends JpaRepository<IntercompanyLoan, Long> {
    List<IntercompanyLoan> findByLenderCompanyId(Long lenderCompanyId);
    List<IntercompanyLoan> findByBorrowerCompanyId(Long borrowerCompanyId);
    List<IntercompanyLoan> findByStatus(String status);
}