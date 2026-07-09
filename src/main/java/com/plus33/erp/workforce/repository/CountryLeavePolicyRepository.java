package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.CountryLeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CountryLeavePolicyRepository extends JpaRepository<CountryLeavePolicy, Long> {

    @Query("SELECT cp FROM CountryLeavePolicy cp " +
           "WHERE cp.countryCode = :countryCode AND cp.leaveType.id = :leaveTypeId " +
           "AND cp.effectiveFrom <= :date AND (cp.effectiveTo IS NULL OR cp.effectiveTo >= :date) " +
           "ORDER BY cp.version DESC")
    List<CountryLeavePolicy> findEffectivePolicies(
            @Param("countryCode") String countryCode,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("date") LocalDate date);
}
