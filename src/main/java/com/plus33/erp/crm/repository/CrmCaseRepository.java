package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmCaseRepository extends JpaRepository<CrmCase, Long> {
    List<CrmCase> findByCompanyId(Long companyId);
    List<CrmCase> findByCompanyIdAndCustomerId(Long companyId, Long customerId);
}
