/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.repository
 * File              : AccountRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AccountController
 * Related Service   : AccountService, AccountServiceImpl
 * Related Repository: AccountRepository
 * Related Entity    : Account
 * Related DTO       : N/A
 * Related Mapper    : AccountMapper
 * Related DB Table  : accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AccountService, AccountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'accounts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code AccountRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'accounts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code accounts}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCompanyIdAndAccountCode(Long companyId, String accountCode);
    List<Account> findByCompanyId(Long companyId);
}
