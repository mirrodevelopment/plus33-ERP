/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : CashPoolMemberRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPoolMemberController
 * Related Service   : CashPoolMemberService, CashPoolMemberServiceImpl
 * Related Repository: CashPoolMemberRepository
 * Related Entity    : CashPoolMember
 * Related DTO       : N/A
 * Related Mapper    : CashPoolMemberMapper
 * Related DB Table  : cash_pool_members
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashPoolMemberService, CashPoolMemberServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'cash_pool_members' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.CashPoolMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPoolMemberRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'cash_pool_members' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code cash_pool_members}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CashPoolMemberRepository extends JpaRepository<CashPoolMember, Long> {
    List<CashPoolMember> findByPoolId(Long poolId);
    Optional<CashPoolMember> findByBankAccountId(Long bankAccountId);
}