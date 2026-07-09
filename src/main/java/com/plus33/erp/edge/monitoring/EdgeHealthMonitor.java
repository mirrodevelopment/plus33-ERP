/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Edge Module
 * Package           : com.plus33.erp.edge.monitoring
 * File              : EdgeHealthMonitor.java
 * Purpose           : Business logic service layer for Edge Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EdgeHealthMonitorController
 * Related Service   : EdgeHealthMonitor
 * Related Repository: EdgeHealthMonitorRepository
 * Related Entity    : EdgeHealthMonitor
 * Related DTO       : N/A
 * Related Mapper    : EdgeHealthMonitorMapper
 * Related DB Table  : edge_health_monitors
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : EdgeHealthMonitorController, EdgeHealthMonitorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Edge Module. Implements EdgeHealthMonitorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.edge.monitoring;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Edge Module</b>
 *
 * <p><b>Class  :</b> {@code EdgeHealthMonitor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.edge.monitoring}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Edge Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EdgeHealthMonitorController
 *   --> EdgeHealthMonitor (this)
 *   --> Validate business rules
 *   --> EdgeHealthMonitorRepository (read/write 'edge_health_monitors')
 *   --> EdgeHealthMonitorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code edge_health_monitors}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class EdgeHealthMonitor {
    @Autowired PlatformEdgeHealthMetricRepository metricsRepo;
    /**
     * Performs the recordMetrics operation in this module.
     *
     * @param nodeId the nodeId input value
     * @param cpu the cpu input value
     * @param mem the mem input value
     * @param disk the disk input value
     * @return the PlatformEdgeHealthMetric result
     */
    @Transactional
    public PlatformEdgeHealthMetric recordMetrics(Long nodeId, BigDecimal cpu, BigDecimal mem, BigDecimal disk) {
        PlatformEdgeHealthMetric metric = new PlatformEdgeHealthMetric();
        metric.setNodeId(nodeId);
        metric.setCpuUsage(cpu);
        metric.setMemoryUsage(mem);
        metric.setDiskUsage(disk);
        metric.setTemperature(BigDecimal.valueOf(45.50));
        metric.setNetworkLatencyMs(15);
        metric.setPacketLossRate(BigDecimal.valueOf(0.02));
        metric.setBatteryLevel(BigDecimal.valueOf(98.00));
        metric.setUptimeSeconds(3600L);
        metric.setActiveThreads(25);
        metric.setTelemetryBacklog(5);
        metric.setQueueDepth(10);
        metric.setSyncLagSeconds(1);
        metric.setRecordedAt(LocalDateTime.now());
        return metricsRepo.save(metric);
    }
}