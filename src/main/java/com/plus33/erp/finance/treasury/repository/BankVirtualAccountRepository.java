/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankVirtualAccountRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankVirtualAccountController
 * Related Service   : BankVirtualAccountService, BankVirtualAccountServiceImpl
 * Related Repository: BankVirtualAccountRepository
 * Related Entity    : BankVirtualAccount
 * Related DTO       : N/A
 * Related Mapper    : BankVirtualAccountMapper
 * Related DB Table  : bank_virtual_accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankVirtualAccountService, BankVirtualAccountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_virtual_accounts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankVirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankVirtualAccountRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_virtual_accounts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_virtual_accounts}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankVirtualAccountRepository extends JpaRepository<BankVirtualAccount, Long> {
    Optional<BankVirtualAccount> findByVirtualAccountNumber(String virtualAccountNumber);
    List<BankVirtualAccount> findByParentAccountId(Long parentAccountId);
}