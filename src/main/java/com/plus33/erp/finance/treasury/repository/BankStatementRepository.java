/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankStatementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankStatementController
 * Related Service   : BankStatementService, BankStatementServiceImpl
 * Related Repository: BankStatementRepository
 * Related Entity    : BankStatement
 * Related DTO       : N/A
 * Related Mapper    : BankStatementMapper
 * Related DB Table  : bank_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankStatementService, BankStatementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_statements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankStatementRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_statements' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_statements}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    List<BankStatement> findByBankAccountCompanyId(Long companyId);
    List<BankStatement> findByBankAccountId(Long bankAccountId);
    Optional<BankStatement> findByBankAccountIdAndStatementNumber(Long bankAccountId, String statementNumber);
}