/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankAccountRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankAccountController
 * Related Service   : BankAccountService, BankAccountServiceImpl
 * Related Repository: BankAccountRepository
 * Related Entity    : BankAccount
 * Related DTO       : N/A
 * Related Mapper    : BankAccountMapper
 * Related DB Table  : bank_accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankAccountService, BankAccountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_accounts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankAccountRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_accounts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_accounts}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByCompanyId(Long companyId);
    Optional<BankAccount> findByCompanyIdAndAccountNumber(Long companyId, String accountNumber);
    Optional<BankAccount> findByGlAccountId(Long glAccountId);
}