/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryRiskPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryRiskPolicyController
 * Related Service   : TreasuryRiskPolicyService, TreasuryRiskPolicyServiceImpl
 * Related Repository: TreasuryRiskPolicyRepository
 * Related Entity    : TreasuryRiskPolicy
 * Related DTO       : N/A
 * Related Mapper    : TreasuryRiskPolicyMapper
 * Related DB Table  : treasury_risk_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryRiskPolicyService, TreasuryRiskPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_risk_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryRiskPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_risk_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_risk_policys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryRiskPolicyRepository extends JpaRepository<TreasuryRiskPolicy, Long> {

    @Query("""
        SELECT p FROM TreasuryRiskPolicy p
        WHERE p.company.id = :companyId
          AND p.policyCategory = :category
          AND p.active = true
          AND p.effectiveFrom <= :date
          AND (p.effectiveTo IS NULL OR p.effectiveTo >= :date)
        ORDER BY p.policyName
        """)
    List<TreasuryRiskPolicy> findActivePolicies(
            @Param("companyId") Long companyId,
            @Param("category") String category,
            @Param("date") LocalDate date);
    @Query("""
        SELECT p FROM TreasuryRiskPolicy p
        WHERE p.company.id = :companyId
          AND p.policyCategory = :category
          AND p.policyName = :name
          AND (p.currencyCode IS NULL OR p.currencyCode = :currencyCode)
          AND p.active = true
          AND p.effectiveFrom <= :date
          AND (p.effectiveTo IS NULL OR p.effectiveTo >= :date)
        ORDER BY p.currencyCode DESC NULLS LAST
        """)
    List<TreasuryRiskPolicy> findMatchingPolicies(
            @Param("companyId") Long companyId,
            @Param("category") String category,
            @Param("name") String name,
            @Param("currencyCode") String currencyCode,
            @Param("date") LocalDate date);
}