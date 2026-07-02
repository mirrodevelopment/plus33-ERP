package com.plus33.erp.edge.controller;

import com.plus33.erp.edge.registry.EdgeNodeRegistry;
import com.plus33.erp.edge.registry.ProvisioningService;
import com.plus33.erp.edge.sync.StoreAndForwardQueue;
import com.plus33.erp.edge.monitoring.EdgeHealthMonitor;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/edge")
public class EdgeController {
    @Autowired EdgeNodeRegistry nodeRegistry;
    @Autowired ProvisioningService provisioningService;
    @Autowired StoreAndForwardQueue syncQueue;
    @Autowired EdgeHealthMonitor healthMonitor;

    @PostMapping("/nodes")
    public ResponseEntity<Void> registerNode(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String cluster) {
        nodeRegistry.registerNode(code, name, cluster);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/certificates/rotate")
    public ResponseEntity<Void> rotateCertificate(
            @RequestParam Long nodeId,
            @RequestParam String serial) {
        provisioningService.rotateCertificate(nodeId, serial);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/queue/enqueue")
    public ResponseEntity<Void> enqueuePayload(
            @RequestParam Long nodeId,
            @RequestParam String payload,
            @RequestParam String payloadType,
            @RequestParam Long seqNumber) {
        syncQueue.enqueuePayload(nodeId, payload, payloadType, seqNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/health/metrics")
    public ResponseEntity<Void> recordMetrics(
            @RequestParam Long nodeId,
            @RequestParam BigDecimal cpu,
            @RequestParam BigDecimal memory,
            @RequestParam BigDecimal disk) {
        healthMonitor.recordMetrics(nodeId, cpu, memory, disk);
        return ResponseEntity.ok().build();
    }
}