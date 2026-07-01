package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAnalyticsRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAnalyticsRoleRepository extends JpaRepository<BiAnalyticsRole, Long> {
    java.util.Optional<BiAnalyticsRole> findByRoleCode(String roleCode);
}
