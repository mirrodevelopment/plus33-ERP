/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : InHouseBankAccountRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InHouseBankAccountController
 * Related Service   : InHouseBankAccountService, InHouseBankAccountServiceImpl
 * Related Repository: InHouseBankAccountRepository
 * Related Entity    : InHouseBankAccount
 * Related DTO       : N/A
 * Related Mapper    : InHouseBankAccountMapper
 * Related DB Table  : in_house_bank_accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InHouseBankAccountService, InHouseBankAccountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'in_house_bank_accounts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.InHouseBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InHouseBankAccountRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'in_house_bank_accounts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code in_house_bank_accounts}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface InHouseBankAccountRepository extends JpaRepository<InHouseBankAccount, Long> {
    Optional<InHouseBankAccount> findByAccountNumber(String accountNumber);
    Optional<InHouseBankAccount> findBySubsidiaryCompanyId(Long subsidiaryCompanyId);
}