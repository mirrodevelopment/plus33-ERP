package com.plus33.erp.iot.gateway;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class IoTGatewayService {
    @Autowired PlatformIotGatewayRepository gatewayRepo;
    @Autowired PlatformGatewayHeartbeatRepository heartbeatRepo;

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