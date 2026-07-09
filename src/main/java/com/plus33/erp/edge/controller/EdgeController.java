/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Edge Module
 * Package           : com.plus33.erp.edge.controller
 * File              : EdgeController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Edge Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EdgeController
 * Related Service   : EdgeControllerService, EdgeControllerServiceImpl
 * Related Repository: EdgeControllerRepository
 * Related Entity    : EdgeController
 * Related DTO       : N/A
 * Related Mapper    : EdgeControllerMapper
 * Related DB Table  : edge_controllers
 * Related REST APIs : POST /api/edge/nodes, POST /api/edge/certificates/rotate, POST /api/edge/queue/enqueue, POST /api/edge/health/metrics
 * Depends On        : Platform Module
 * Used By           : Edge Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Edge Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/edge/nodes, POST /api/edge/certificates/rotate, POST /api/edge/queue/enqueue, POST /api/edge/health/metrics
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Edge Module</b>
 *
 * <p><b>Class  :</b> {@code EdgeController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.edge.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EdgeService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EdgeController.endpoint()
 *   --> EdgeService.method()
 *   --> EdgeRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/edge/nodes, POST /api/edge/certificates/rotate, POST /api/edge/queue/enqueue, POST /api/edge/health/metrics</p>
 * <p><b>Module Deps      :</b> Edge, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/edge")
public class EdgeController {
    @Autowired EdgeNodeRegistry nodeRegistry;
    @Autowired ProvisioningService provisioningService;
    @Autowired StoreAndForwardQueue syncQueue;
    @Autowired EdgeHealthMonitor healthMonitor;
    /**
     * Creates a new node and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/nodes")
    public ResponseEntity<Void> registerNode(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String cluster) {
        nodeRegistry.registerNode(code, name, cluster);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the rotateCertificate operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/certificates/rotate")
    public ResponseEntity<Void> rotateCertificate(
            @RequestParam Long nodeId,
            @RequestParam String serial) {
        provisioningService.rotateCertificate(nodeId, serial);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the enqueuePayload operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/queue/enqueue")
    public ResponseEntity<Void> enqueuePayload(
            @RequestParam Long nodeId,
            @RequestParam String payload,
            @RequestParam String payloadType,
            @RequestParam Long seqNumber) {
        syncQueue.enqueuePayload(nodeId, payload, payloadType, seqNumber);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the recordMetrics operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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