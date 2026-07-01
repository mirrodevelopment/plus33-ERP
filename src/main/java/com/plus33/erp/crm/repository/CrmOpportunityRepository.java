package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmOpportunityRepository extends JpaRepository<CrmOpportunity, Long> {
    List<CrmOpportunity> findByCompanyId(Long companyId);
    List<CrmOpportunity> findByCompanyIdAndCustomerId(Long companyId, Long customerId);
}
