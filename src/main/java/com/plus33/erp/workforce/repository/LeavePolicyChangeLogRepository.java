package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeavePolicyChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeavePolicyChangeLogRepository extends JpaRepository<LeavePolicyChangeLog, Long> {
    List<LeavePolicyChangeLog> findByPolicyGroupCodeOrderByChangedAtDesc(String policyGroupCode);
}
