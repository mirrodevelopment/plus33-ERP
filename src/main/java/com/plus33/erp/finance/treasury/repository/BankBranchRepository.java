/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankBranchRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankBranchController
 * Related Service   : BankBranchService, BankBranchServiceImpl
 * Related Repository: BankBranchRepository
 * Related Entity    : BankBranch
 * Related DTO       : N/A
 * Related Mapper    : BankBranchMapper
 * Related DB Table  : bank_branchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankBranchService, BankBranchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_branchs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankBranchRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_branchs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_branchs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, Long> {
    List<BankBranch> findByBankId(Long bankId);
    Optional<BankBranch> findByBankIdAndCode(Long bankId, String code);
}