package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmCommissionRepository extends JpaRepository<CrmCommission, Long> {
    List<CrmCommission> findByCompanyIdAndSalesRepId(Long companyId, Long salesRepId);
}
