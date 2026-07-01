package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcurementAnalyticsSnapshotRepository extends JpaRepository<ProcurementAnalyticsSnapshot, Long> {
    List<ProcurementAnalyticsSnapshot> findByCompanyIdAndMetricName(Long companyId, String metricName);
}
