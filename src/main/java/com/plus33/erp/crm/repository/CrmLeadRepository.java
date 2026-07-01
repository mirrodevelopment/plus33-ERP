package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmLead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmLeadRepository extends JpaRepository<CrmLead, Long> {
    List<CrmLead> findByCompanyId(Long companyId);
}
