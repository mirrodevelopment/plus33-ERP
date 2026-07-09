/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.metrics
 * File              : PrometheusExporterService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PrometheusExporterController
 * Related Service   : PrometheusExporterService
 * Related Repository: PrometheusExporterRepository
 * Related Entity    : PrometheusExporter
 * Related DTO       : N/A
 * Related Mapper    : PrometheusExporterMapper
 * Related DB Table  : prometheus_exporters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PrometheusExporterController, PrometheusExporterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PrometheusExporterService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PrometheusExporterService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.metrics}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PrometheusExporterController
 *   --> PrometheusExporterService (this)
 *   --> Validate business rules
 *   --> PrometheusExporterRepository (read/write 'prometheus_exporters')
 *   --> PrometheusExporterMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code prometheus_exporters}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PrometheusExporterService {
    @Autowired PlatformTelemetryMetricRepository metricRepo;
    /**
     * Performs the recordMetric operation in this module.
     *
     * @param name the name input value
     * @param value the value input value
     * @param dimensionsJson the dimensionsJson input value
     */
    @Transactional
    public void recordMetric(String name, double value, String dimensionsJson) {
        PlatformTelemetryMetric metric = new PlatformTelemetryMetric();
        metric.setMetricName(name);
        metric.setMetricValue(BigDecimal.valueOf(value));
        metric.setDimensionsJson(dimensionsJson);
        metric.setTimestamp(LocalDateTime.now());
        metricRepo.save(metric);
    }

    /**
     * Exports prometheus format data as a report or downloadable file.
     *
     * @return the result string value
     */
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