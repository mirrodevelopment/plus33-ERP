/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiAnalyticsRoleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAnalyticsRoleController
 * Related Service   : BiAnalyticsRoleService, BiAnalyticsRoleServiceImpl
 * Related Repository: BiAnalyticsRoleRepository
 * Related Entity    : BiAnalyticsRole
 * Related DTO       : N/A
 * Related Mapper    : BiAnalyticsRoleMapper
 * Related DB Table  : bi_analytics_roles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAnalyticsRoleService, BiAnalyticsRoleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_analytics_roles' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAnalyticsRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAnalyticsRoleRepository extends JpaRepository<BiAnalyticsRole, Long> {
    java.util.Optional<BiAnalyticsRole> findByRoleCode(String roleCode);
}
