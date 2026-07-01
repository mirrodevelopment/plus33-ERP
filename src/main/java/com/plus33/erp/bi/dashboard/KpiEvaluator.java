package com.plus33.erp.bi.dashboard;

import com.plus33.erp.bi.entity.BiKpiDefinition;
import com.plus33.erp.bi.entity.BiKpiFormulaVersion;
import com.plus33.erp.bi.repository.BiKpiDefinitionRepository;
import com.plus33.erp.bi.repository.BiKpiFormulaVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * KpiEvaluator: Resolves KPI definitions and their current active formula version.
 * The actual SQL computation is delegated to the BI layer (views/cube queries).
 */
@Service
public class KpiEvaluator {

    private static final Logger log = LoggerFactory.getLogger(KpiEvaluator.class);
    private final BiKpiDefinitionRepository kpiRepo;
    private final BiKpiFormulaVersionRepository formulaRepo;

    public KpiEvaluator(BiKpiDefinitionRepository kpiRepo, BiKpiFormulaVersionRepository formulaRepo) {
        this.kpiRepo = kpiRepo;
        this.formulaRepo = formulaRepo;
    }

    public Optional<BiKpiDefinition> getActiveKpi(String kpiCode) {
        return kpiRepo.findByKpiCode(kpiCode);
    }

    public Optional<BiKpiFormulaVersion> getActiveFormula(Long kpiId) {
        return formulaRepo.findByKpiIdAndIsCurrentTrue(kpiId);
    }

    public List<BiKpiDefinition> getAllActiveKpis() {
        return kpiRepo.findByStatus("ACTIVE");
    }

    /**
     * Evaluates whether an actual value breaches warning/critical thresholds.
     */
    public KpiHealthStatus evaluateHealth(BiKpiDefinition kpi, BigDecimal actualValue) {
        if (actualValue == null || kpi.getThresholdCritical() == null) {
            return KpiHealthStatus.UNKNOWN;
        }
        boolean higher = "HIGHER".equals(kpi.getDirection());
        if (higher) {
            if (actualValue.compareTo(kpi.getThresholdCritical()) < 0) return KpiHealthStatus.CRITICAL;
            if (kpi.getThresholdWarning() != null && actualValue.compareTo(kpi.getThresholdWarning()) < 0) return KpiHealthStatus.WARNING;
        } else {
            if (actualValue.compareTo(kpi.getThresholdCritical()) > 0) return KpiHealthStatus.CRITICAL;
            if (kpi.getThresholdWarning() != null && actualValue.compareTo(kpi.getThresholdWarning()) > 0) return KpiHealthStatus.WARNING;
        }
        return KpiHealthStatus.HEALTHY;
    }

    public enum KpiHealthStatus { HEALTHY, WARNING, CRITICAL, UNKNOWN }
}
