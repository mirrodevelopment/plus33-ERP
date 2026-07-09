/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankStatementLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankStatementLineController
 * Related Service   : BankStatementLineService, BankStatementLineServiceImpl
 * Related Repository: BankStatementLineRepository
 * Related Entity    : BankStatementLine
 * Related DTO       : N/A
 * Related Mapper    : BankStatementLineMapper
 * Related DB Table  : bank_statement_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankStatementLineService, BankStatementLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_statement_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankStatementLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankStatementLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_statement_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_statement_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankStatementLineRepository extends JpaRepository<BankStatementLine, Long> {
    List<BankStatementLine> findByStatementId(Long statementId);
    List<BankStatementLine> findByStatementIdAndReconciled(Long statementId, Boolean reconciled);
}