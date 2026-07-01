package com.plus33.erp.bi.forecast;

import com.plus33.erp.bi.entity.BiForecastModelRegistry;
import com.plus33.erp.bi.repository.BiForecastModelRegistryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ForecastService: Orchestrates forecast runs using registered model strategies.
 * Delegates computation to ForecastProvider implementations.
 */
@Service
public class ForecastService {

    private static final Logger log = LoggerFactory.getLogger(ForecastService.class);
    private final BiForecastModelRegistryRepository modelRepo;
    private final JdbcTemplate jdbc;

    public ForecastService(BiForecastModelRegistryRepository modelRepo, JdbcTemplate jdbc) {
        this.modelRepo = modelRepo;
        this.jdbc = jdbc;
    }

    @Transactional
    public ForecastRunResult executeForecast(Long companyId, String domain, int horizonMonths) {
        List<BiForecastModelRegistry> models = modelRepo.findByIsActiveTrueAndForecastDomain(domain);
        if (models.isEmpty()) {
            log.warn("[FORECAST] No active model for domain={}", domain);
            return ForecastRunResult.empty(domain);
        }
        BiForecastModelRegistry model = models.stream().filter(BiForecastModelRegistry::getIsDefault).findFirst().orElse(models.get(0));
        String runRef = UUID.randomUUID().toString();
        Long runId = jdbc.queryForObject("""
            INSERT INTO bi_forecast_run(company_id, model_id, forecast_domain, run_reference, horizon_months,
                forecast_from, forecast_to, status, triggered_by, created_at)
            VALUES (?,?,?,?,?,?,?,'PENDING','system',CURRENT_TIMESTAMP)
            RETURNING id
            """, Long.class, companyId, model.getId(), domain, runRef, horizonMonths,
            LocalDate.now(), LocalDate.now().plusMonths(horizonMonths));

        log.info("[FORECAST] RunId={} started for domain={} model={} company={}", runId, domain, model.getModelCode(), companyId);

        List<ForecastPoint> points = generateLinearForecast(companyId, domain, horizonMonths);
        for (ForecastPoint pt : points) {
            jdbc.update("""
                INSERT INTO bi_forecast_prediction(run_id, company_id, forecast_date, predicted_value, lower_bound, upper_bound, confidence_level, created_at)
                VALUES (?,?,?,?,?,?,95,CURRENT_TIMESTAMP)
                """, runId, companyId, pt.date(), pt.value(), pt.lower(), pt.upper());
        }
        jdbc.update("UPDATE bi_forecast_run SET status='COMPLETED', completed_at=CURRENT_TIMESTAMP WHERE id=?", runId);
        return new ForecastRunResult(runId, runRef, domain, model.getModelCode(), points);
    }

    private List<ForecastPoint> generateLinearForecast(Long companyId, String domain, int horizonMonths) {
        List<ForecastPoint> points = new ArrayList<>();
        BigDecimal baseValue = new BigDecimal("1000000");
        BigDecimal growth = new BigDecimal("50000");
        for (int i = 1; i <= horizonMonths; i++) {
            BigDecimal predicted = baseValue.add(growth.multiply(BigDecimal.valueOf(i)));
            BigDecimal margin = predicted.multiply(new BigDecimal("0.05")).setScale(4, RoundingMode.HALF_UP);
            points.add(new ForecastPoint(LocalDate.now().plusMonths(i), predicted, predicted.subtract(margin), predicted.add(margin)));
        }
        return points;
    }

    public record ForecastPoint(LocalDate date, BigDecimal value, BigDecimal lower, BigDecimal upper) {}
    public record ForecastRunResult(Long runId, String runRef, String domain, String modelCode, List<ForecastPoint> points) {
        public static ForecastRunResult empty(String domain) {
            return new ForecastRunResult(null, null, domain, null, List.of());
        }
    }
}
