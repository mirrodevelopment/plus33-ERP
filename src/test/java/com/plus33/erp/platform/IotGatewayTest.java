package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.iot.gateway.IoTGatewayService;
import com.plus33.erp.iot.scada.ScadaDeviceManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IotGatewayTest {

    @Autowired IoTGatewayService gatewayService;
    @Autowired ScadaDeviceManager deviceManager;

    @Autowired PlatformIotGatewayRepository gatewayRepo;
    @Autowired PlatformGatewayHeartbeatRepository heartbeatRepo;
    @Autowired PlatformScadaDeviceRepository deviceRepo;
    @Autowired PlatformScadaSignalRegisterRepository registerRepo;

    @Test
    void testIotGatewayScenarios() {
        // Register IoT gateways & Gateway heartbeat updates over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformIotGateway gateway = gatewayService.registerGateway("GW_CODE_" + i, "v1.0." + i, "CERT_THUMB_" + i, "edge-cluster-us", "mqtt-client-" + i);
            assertNotNull(gateway);
            gatewayService.recordHeartbeat(gateway.getId());
        }

        List<PlatformIotGateway> gateways = gatewayRepo.findAll();
        assertTrue(gateways.size() >= 40);
        assertEquals("ONLINE", gateways.get(0).getGatewayStatus());

        List<PlatformGatewayHeartbeat> heartbeats = heartbeatRepo.findAll();
        assertTrue(heartbeats.size() >= 40);

        // OPC-UA device signals and Modbus unit IDs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformScadaDevice dev = deviceManager.registerDevice("DEV_CODE_" + i, "PLC_CONTROLLER", "ns=2", "s=Device_" + i, "192.168.1." + i, i);
            assertNotNull(dev);

            PlatformScadaSignalRegister reg = deviceManager.registerSignal(dev.getId(), "REG_CODE_" + i, "Holding Register");
            assertNotNull(reg);
        }

        List<PlatformScadaDevice> devices = deviceRepo.findAll();
        assertTrue(devices.size() >= 40);

        List<PlatformScadaSignalRegister> registers = registerRepo.findAll();
        assertTrue(registers.size() >= 40);
    }
}
