package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatformAuditLogRepository extends JpaRepository<PlatformAuditLog, Long> {
}