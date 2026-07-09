/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.controller
 * File              : IoTController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Iot Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IoTController
 * Related Service   : IoTControllerService, IoTControllerServiceImpl
 * Related Repository: IoTControllerRepository
 * Related Entity    : IoTController
 * Related DTO       : N/A
 * Related Mapper    : IoTControllerMapper
 * Related DB Table  : io_t_controllers
 * Related REST APIs : POST /api/iot/gateways, POST /api/iot/gateways/heartbeat, POST /api/iot/devices, POST /api/iot/signals
 * Depends On        : Platform Module
 * Used By           : Iot Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Iot Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/iot/gateways, POST /api/iot/gateways/heartbeat, POST /api/iot/devices, POST /api/iot/signals
 ******************************************************************************/
package com.plus33.erp.iot.controller;

import com.plus33.erp.iot.gateway.IoTGatewayService;
import com.plus33.erp.iot.scada.ScadaDeviceManager;
import com.plus33.erp.iot.telemetry.MachineryTelemetryService;
import com.plus33.erp.iot.control.ScadaControlService;
import com.plus33.erp.iot.alarm.AlarmOrchestrator;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Iot Module</b>
 *
 * <p><b>Class  :</b> {@code IoTController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to IoTService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> IoTController.endpoint()
 *   --> IoTService.method()
 *   --> IoTRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/iot/gateways, POST /api/iot/gateways/heartbeat, POST /api/iot/devices, POST /api/iot/signals, POST /api/iot/telemetry</p>
 * <p><b>Module Deps      :</b> Iot, Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/iot")
public class IoTController {
    @Autowired IoTGatewayService gatewayService;
    @Autowired ScadaDeviceManager deviceManager;
    @Autowired MachineryTelemetryService telemetryService;
    @Autowired ScadaControlService controlService;
    @Autowired AlarmOrchestrator alarmOrchestrator;
    /**
     * Creates a new gateway and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/gateways")
    public ResponseEntity<Void> registerGateway(
            @RequestParam String code,
            @RequestParam String version,
            @RequestParam String cert,
            @RequestParam String cluster,
            @RequestParam String mqtt) {
        gatewayService.registerGateway(code, version, cert, cluster, mqtt);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the heartbeat operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param gatewayId the gatewayId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/gateways/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestParam Long gatewayId) {
        gatewayService.recordHeartbeat(gatewayId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new device and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/devices")
    public ResponseEntity<Void> registerDevice(
            @RequestParam String code,
            @RequestParam String type,
            @RequestParam(required = false) String ns,
            @RequestParam(required = false) String nodeId,
            @RequestParam(required = false) String plc,
            @RequestParam(required = false) Integer unitId) {
        deviceManager.registerDevice(code, type, ns, nodeId, plc, unitId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new signal and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/signals")
    public ResponseEntity<Void> registerSignal(
            @RequestParam Long deviceId,
            @RequestParam String code,
            @RequestParam String type) {
        deviceManager.registerSignal(deviceId, code, type);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the ingestTelemetry operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/telemetry")
    public ResponseEntity<Void> ingestTelemetry(
            @RequestParam Long deviceId,
            @RequestParam Long signalId,
            @RequestParam BigDecimal value,
            @RequestParam String unit,
            @RequestParam Long sequence) {
        telemetryService.ingest(deviceId, signalId, value, unit, sequence);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the executeWrite operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/control/write")
    public ResponseEntity<Void> executeWrite(
            @RequestParam Long deviceId,
            @RequestParam Long registerId,
            @RequestParam BigDecimal value,
            @RequestParam String operator,
            @RequestParam String signature) {
        controlService.executeWrite(deviceId, registerId, value, operator, signature);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/alarms/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String severity,
            @RequestParam BigDecimal threshold) {
        alarmOrchestrator.createPolicy(code, severity, threshold);
        return ResponseEntity.ok().build();
    }

    /**
     * Performs the triggerAlarm operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/alarms/trigger")
    public ResponseEntity<Void> triggerAlarm(
            @RequestParam Long deviceId,
            @RequestParam Long policyId,
            @RequestParam String message) {
        alarmOrchestrator.triggerAlarm(deviceId, policyId, message);
        return ResponseEntity.ok().build();
    }
}