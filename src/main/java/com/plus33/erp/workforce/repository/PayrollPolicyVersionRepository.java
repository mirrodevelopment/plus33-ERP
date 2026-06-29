package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollPolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public interface PayrollPolicyVersionRepository extends JpaRepository<PayrollPolicyVersion, Long> {
    Optional<PayrollPolicyVersion> findByPolicyIdAndVersionNumber(Long policyId, Integer versionNumber);
    List<PayrollPolicyVersion> findByPolicyIdOrderByVersionNumberDesc(Long policyId);
}
