package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectAnalyticsSnapshotRepository extends JpaRepository<ProjectAnalyticsSnapshot, Long> {
    List<ProjectAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
