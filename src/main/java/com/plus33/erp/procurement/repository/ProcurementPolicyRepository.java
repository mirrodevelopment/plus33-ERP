package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcurementPolicyRepository extends JpaRepository<ProcurementPolicy, Long> {
    List<ProcurementPolicy> findByCompanyIdAndPolicyTypeAndActive(Long companyId, String policyType, Boolean active);
}
