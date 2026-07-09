/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.controller
 * File              : BiIntegrationController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiIntegrationController
 * Related Service   : BiIntegrationControllerService, BiIntegrationControllerServiceImpl
 * Related Repository: BiIntegrationControllerRepository
 * Related Entity    : BiIntegrationController
 * Related DTO       : N/A
 * Related Mapper    : BiIntegrationControllerMapper
 * Related DB Table  : bi_integration_controllers
 * Related REST APIs : GET /api/integration/dead-letter, GET /api/integration/connectors, GET /api/integration/gateway/usage
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Integration Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/integration/dead-letter, GET /api/integration/connectors, GET /api/integration/gateway/usage
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code BiIntegrationController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiIntegrationService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiIntegrationController.endpoint()
 *   --> BiIntegrationService.method()
 *   --> BiIntegrationRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/integration/dead-letter, GET /api/integration/connectors, GET /api/integration/gateway/usage</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/integration")
public class BiIntegrationController {

    @Autowired EventMeshDeadLetterRepository deadLetterRepo;
    @Autowired IntegrationConnectorRepository connectorRepo;
    @Autowired IntegrationGatewayUsageLogRepository usageLogRepo;
    /**
     * Retrieves dead letters data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dead-letter")
    public ResponseEntity<List<EventMeshDeadLetter>> getDeadLetters() {
        return ResponseEntity.ok(deadLetterRepo.findAll());
    }

    /**
     * Retrieves connectors data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/connectors")
    public ResponseEntity<List<IntegrationConnector>> getConnectors() {
        return ResponseEntity.ok(connectorRepo.findAll());
    }

    /**
     * Retrieves gateway usage logs data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/gateway/usage")
    public ResponseEntity<List<IntegrationGatewayUsageLog>> getGatewayUsageLogs() {
        return ResponseEntity.ok(usageLogRepo.findAll());
    }
}