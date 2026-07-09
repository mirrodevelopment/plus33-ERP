/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : BankFeeRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankFeeRuleController
 * Related Service   : BankFeeRuleService, BankFeeRuleServiceImpl
 * Related Repository: BankFeeRuleRepository
 * Related Entity    : BankFeeRule
 * Related DTO       : N/A
 * Related Mapper    : BankFeeRuleMapper
 * Related DB Table  : bank_fee_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankFeeRuleService, BankFeeRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'bank_fee_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankFeeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankFeeRuleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bank_fee_rules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_fee_rules}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BankFeeRuleRepository extends JpaRepository<BankFeeRule, Long> {
    List<BankFeeRule> findByBankAccountId(Long bankAccountId);
    List<BankFeeRule> findByBankAccountIdAndActiveTrue(Long bankAccountId);
}