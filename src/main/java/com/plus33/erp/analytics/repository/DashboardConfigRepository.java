/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.repository
 * File              : DashboardConfigRepository.java
 * Purpose           : JPA Repository providing database CRUD for Analytics Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardConfigController
 * Related Service   : DashboardConfigService, DashboardConfigServiceImpl
 * Related Repository: DashboardConfigRepository
 * Related Entity    : DashboardConfig
 * Related DTO       : N/A
 * Related Mapper    : DashboardConfigMapper
 * Related DB Table  : dashboard_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DashboardConfigService, DashboardConfigServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Analytics Module against the 'dashboard_configs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.analytics.repository;

import com.plus33.erp.analytics.entity.DashboardConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code DashboardConfigRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'dashboard_configs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code dashboard_configs}</p>
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface DashboardConfigRepository extends JpaRepository<DashboardConfig, Long> {
    Optional<DashboardConfig> findByCompanyIdAndDashboardCodeAndRoleCode(Long companyId, String dashboardCode, String roleCode);
    List<DashboardConfig> findByCompanyIdAndRoleCode(Long companyId, String roleCode);
}
