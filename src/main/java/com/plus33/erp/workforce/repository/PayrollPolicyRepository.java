package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PayrollPolicyRepository extends JpaRepository<PayrollPolicy, Long> {
    Optional<PayrollPolicy> findByCompanyIdAndCode(Long companyId, String code);
    List<PayrollPolicy> findByCompanyId(Long companyId);
}
