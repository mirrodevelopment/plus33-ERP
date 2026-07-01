package com.plus33.erp.grc.repository;

import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EnterpriseRiskRepository extends JpaRepository<EnterpriseRisk, Long> {
    Optional<EnterpriseRisk> findByRiskNumber(String riskNumber);
    List<EnterpriseRisk> findByCompanyId(Long companyId);
    List<EnterpriseRisk> findByCompanyIdAndStatus(Long companyId, String status);
    List<EnterpriseRisk> findByCompanyIdAndCategory(Long companyId, String category);
}
