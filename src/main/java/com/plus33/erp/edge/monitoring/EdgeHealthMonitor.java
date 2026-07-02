package com.plus33.erp.edge.monitoring;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EdgeHealthMonitor {
    @Autowired PlatformEdgeHealthMetricRepository metricsRepo;

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