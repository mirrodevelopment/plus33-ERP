package com.plus33.erp.analytics.repository;

import com.plus33.erp.analytics.entity.DashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, Long> {
    Optional<DashboardConfig> findByCompanyIdAndDashboardCodeAndRoleCode(Long companyId, String dashboardCode, String roleCode);
    List<DashboardConfig> findByCompanyIdAndRoleCode(Long companyId, String roleCode);
}
