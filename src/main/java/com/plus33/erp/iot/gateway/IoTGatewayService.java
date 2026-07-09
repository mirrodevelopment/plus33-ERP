/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Iot Module
 * Package           : com.plus33.erp.iot.gateway
 * File              : IoTGatewayService.java
 * Purpose           : Business logic service layer for Iot Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IoTGatewayController
 * Related Service   : IoTGatewayService
 * Related Repository: IoTGatewayRepository
 * Related Entity    : IoTGateway
 * Related DTO       : N/A
 * Related Mapper    : IoTGatewayMapper
 * Related DB Table  : io_t_gateways
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : IoTGatewayController, IoTGatewayServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Iot Module. Implements IoTGatewayService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.iot.gateway;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Iot Module</b>
 *
 * <p><b>Class  :</b> {@code IoTGatewayService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.iot.gateway}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Iot Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * IoTGatewayController
 *   --> IoTGatewayService (this)
 *   --> Validate business rules
 *   --> IoTGatewayRepository (read/write 'io_t_gateways')
 *   --> IoTGatewayMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code io_t_gateways}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class IoTGatewayService {
    @Autowired PlatformIotGatewayRepository gatewayRepo;
    @Autowired PlatformGatewayHeartbeatRepository heartbeatRepo;
    /**
     * Creates a new gateway and persists it to the database.
     *
     * @param code the code input value
     * @param version the version input value
     * @param cert the cert input value
     * @param cluster the cluster input value
     * @param mqtt the mqtt input value
     * @return the PlatformIotGateway result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformIotGateway registerGateway(String code, String version, String cert, String cluster, String mqtt) {
        PlatformIotGateway gateway = new PlatformIotGateway();
        gateway.setGatewayCode(code);
        gateway.setGatewayStatus("ONLINE");
        gateway.setFirmwareVersion(version);
        gateway.setCertificateThumbprint(cert);
        gateway.setEdgeCluster(cluster);
        gateway.setMqttClientId(mqtt);
        gateway.setLastSeen(LocalDateTime.now());
        return gatewayRepo.save(gateway);
    }

    /**
     * Performs the recordHeartbeat operation in this module.
     *
     * @param gatewayId the gatewayId input value
     */
    @Transactional
    public void recordHeartbeat(Long gatewayId) {
        PlatformGatewayHeartbeat heartbeat = new PlatformGatewayHeartbeat();
        heartbeat.setGatewayId(gatewayId);
        heartbeat.setRecordedAt(LocalDateTime.now());
        heartbeatRepo.save(heartbeat);

        gatewayRepo.findById(gatewayId).ifPresent(g -> {
            g.setGatewayStatus("ONLINE");
            g.setLastSeen(LocalDateTime.now());
            gatewayRepo.save(g);
        });
    }
}