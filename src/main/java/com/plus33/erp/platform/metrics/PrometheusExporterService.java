package com.plus33.erp.platform.metrics;

import com.plus33.erp.platform.entity.PlatformTelemetryMetric;
import com.plus33.erp.platform.repository.PlatformTelemetryMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PrometheusExporterService {
    @Autowired PlatformTelemetryMetricRepository metricRepo;

    @Transactional
    public void recordMetric(String name, double value, String dimensionsJson) {
        PlatformTelemetryMetric metric = new PlatformTelemetryMetric();
        metric.setMetricName(name);
        metric.setMetricValue(BigDecimal.valueOf(value));
        metric.setDimensionsJson(dimensionsJson);
        metric.setTimestamp(LocalDateTime.now());
        metricRepo.save(metric);
    }

    public String exportPrometheusFormat() {
        StringBuilder sb = new StringBuilder();
        metricRepo.findAll().forEach(m -> {
            sb.append("# HELP ").append(m.getMetricName()).append(" Platform runtime metrics\n");
            sb.append("# TYPE ").append(m.getMetricName()).append(" gauge\n");
            sb.append(m.getMetricName()).append("{dimensions=\"").append(m.getDimensionsJson() != null ? m.getDimensionsJson().replace("\"", "'") : "").append("\"} ")
              .append(m.getMetricValue().doubleValue()).append("\n");
        });
        return sb.toString();
    }
}