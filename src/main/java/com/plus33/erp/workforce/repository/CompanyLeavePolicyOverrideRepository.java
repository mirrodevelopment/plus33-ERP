package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.CompanyLeavePolicyOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CompanyLeavePolicyOverrideRepository extends JpaRepository<CompanyLeavePolicyOverride, Long> {

    @Query("SELECT o FROM CompanyLeavePolicyOverride o " +
           "WHERE o.companyId = :companyId AND o.leaveType.id = :leaveTypeId AND o.countryCode = :countryCode " +
           "AND o.effectiveFrom <= :date AND (o.effectiveTo IS NULL OR o.effectiveTo >= :date) " +
           "ORDER BY o.version DESC")
    List<CompanyLeavePolicyOverride> findEffectiveOverrides(
            @Param("companyId") Long companyId,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("countryCode") String countryCode,
            @Param("date") LocalDate date);
}
