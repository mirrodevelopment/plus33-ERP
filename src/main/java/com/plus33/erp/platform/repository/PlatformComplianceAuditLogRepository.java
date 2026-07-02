package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformComplianceAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformComplianceAuditLogRepository extends JpaRepository<PlatformComplianceAuditLog, Long> {
}