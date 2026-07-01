package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiKpiFormulaVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiKpiFormulaVersionRepository extends JpaRepository<BiKpiFormulaVersion, Long> {
    java.util.Optional<BiKpiFormulaVersion> findByKpiIdAndIsCurrentTrue(Long kpiId);
    java.util.List<BiKpiFormulaVersion> findByKpiIdOrderByVersionNumberDesc(Long kpiId);
}
