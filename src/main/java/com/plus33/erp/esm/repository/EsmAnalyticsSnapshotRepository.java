package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EsmAnalyticsSnapshotRepository extends JpaRepository<EsmAnalyticsSnapshot, Long> {
    List<EsmAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
