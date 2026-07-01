package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAnalyticsSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAnalyticsSnapshotRepository extends JpaRepository<BiAnalyticsSnapshot, Long> {
    java.util.List<BiAnalyticsSnapshot> findByCompanyIdAndKpiCodeOrderBySnapshotDateDesc(Long companyId, String kpiCode);
    java.util.List<BiAnalyticsSnapshot> findByCompanyIdAndSnapshotDateBetween(Long companyId, java.time.LocalDate from, java.time.LocalDate to);
}
