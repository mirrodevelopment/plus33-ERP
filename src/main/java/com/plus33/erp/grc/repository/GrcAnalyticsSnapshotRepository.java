package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface GrcAnalyticsSnapshotRepository extends JpaRepository<GrcAnalyticsSnapshot, Long> {
    List<GrcAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
