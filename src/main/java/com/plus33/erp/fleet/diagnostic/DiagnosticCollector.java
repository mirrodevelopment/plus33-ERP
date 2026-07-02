package com.plus33.erp.fleet.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DiagnosticCollector {
    @Autowired PlatformDeviceDiagnosticRepository diagnosticRepo;

    @Transactional
    public PlatformDeviceDiagnostic collectDiagnostic(Long nodeId, BigDecimal cpu, BigDecimal mem) {
        PlatformDeviceDiagnostic d = new PlatformDeviceDiagnostic();
        d.setNodeId(nodeId);
        d.setCpuUsage(cpu);
        d.setMemoryUsage(mem);
        d.setDiskUsage(BigDecimal.valueOf(45.00));
        d.setTemperature(BigDecimal.valueOf(39.00));
        d.setRunningServices("edge-agent, timeseries-buffer");
        d.setFirmwareVersion("v1.5.0");
        d.setUptimeSeconds(86400L);
        d.setNetworkQuality("EXCELLENT");
        d.setLogs("System started correctly. Online status achieved.");
        d.setReportedAt(LocalDateTime.now());
        return diagnosticRepo.save(d);
    }
}