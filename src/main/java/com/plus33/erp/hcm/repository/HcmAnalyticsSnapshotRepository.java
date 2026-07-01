package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HcmAnalyticsSnapshotRepository extends JpaRepository<HcmAnalyticsSnapshot, Long> {
    List<HcmAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
