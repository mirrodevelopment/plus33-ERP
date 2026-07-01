package com.plus33.erp.integration.controller;

import com.plus33.erp.integration.entity.EventMeshDeadLetter;
import com.plus33.erp.integration.entity.IntegrationConnector;
import com.plus33.erp.integration.entity.IntegrationGatewayUsageLog;
import com.plus33.erp.integration.repository.EventMeshDeadLetterRepository;
import com.plus33.erp.integration.repository.IntegrationConnectorRepository;
import com.plus33.erp.integration.repository.IntegrationGatewayUsageLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/integration")
public class BiIntegrationController {

    @Autowired EventMeshDeadLetterRepository deadLetterRepo;
    @Autowired IntegrationConnectorRepository connectorRepo;
    @Autowired IntegrationGatewayUsageLogRepository usageLogRepo;

    @GetMapping("/dead-letter")
    public ResponseEntity<List<EventMeshDeadLetter>> getDeadLetters() {
        return ResponseEntity.ok(deadLetterRepo.findAll());
    }

    @GetMapping("/connectors")
    public ResponseEntity<List<IntegrationConnector>> getConnectors() {
        return ResponseEntity.ok(connectorRepo.findAll());
    }

    @GetMapping("/gateway/usage")
    public ResponseEntity<List<IntegrationGatewayUsageLog>> getGatewayUsageLogs() {
        return ResponseEntity.ok(usageLogRepo.findAll());
    }
}
