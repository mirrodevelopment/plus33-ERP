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

@RestController
@RequestMapping("/api/iot")
public class IoTController {
    @Autowired IoTGatewayService gatewayService;
    @Autowired ScadaDeviceManager deviceManager;
    @Autowired MachineryTelemetryService telemetryService;
    @Autowired ScadaControlService controlService;
    @Autowired AlarmOrchestrator alarmOrchestrator;

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

    @PostMapping("/gateways/heartbeat")
    public ResponseEntity<Void> heartbeat(@RequestParam Long gatewayId) {
        gatewayService.recordHeartbeat(gatewayId);
        return ResponseEntity.ok().build();
    }

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

    @PostMapping("/signals")
    public ResponseEntity<Void> registerSignal(
            @RequestParam Long deviceId,
            @RequestParam String code,
            @RequestParam String type) {
        deviceManager.registerSignal(deviceId, code, type);
        return ResponseEntity.ok().build();
    }

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

    @PostMapping("/alarms/policies")
    public ResponseEntity<Void> createPolicy(
            @RequestParam String code,
            @RequestParam String severity,
            @RequestParam BigDecimal threshold) {
        alarmOrchestrator.createPolicy(code, severity, threshold);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/alarms/trigger")
    public ResponseEntity<Void> triggerAlarm(
            @RequestParam Long deviceId,
            @RequestParam Long policyId,
            @RequestParam String message) {
        alarmOrchestrator.triggerAlarm(deviceId, policyId, message);
        return ResponseEntity.ok().build();
    }
}