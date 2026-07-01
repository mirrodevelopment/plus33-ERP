package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiFactLoadAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiFactLoadAuditRepository extends JpaRepository<BiFactLoadAudit, Long> {
    List<BiFactLoadAudit> findByFactTableAndStatus(String factTable, String status);
    List<BiFactLoadAudit> findByBatchId(String batchId);
}
