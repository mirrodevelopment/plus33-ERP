package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
