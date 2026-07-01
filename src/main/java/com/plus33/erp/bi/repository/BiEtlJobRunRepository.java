package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEtlJobRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEtlJobRunRepository extends JpaRepository<BiEtlJobRun, Long> {
    java.util.List<BiEtlJobRun> findByJobIdOrderByCreatedAtDesc(Long jobId);
    java.util.Optional<BiEtlJobRun> findTopByJobIdAndStatusOrderByCreatedAtDesc(Long jobId, String status);
}
