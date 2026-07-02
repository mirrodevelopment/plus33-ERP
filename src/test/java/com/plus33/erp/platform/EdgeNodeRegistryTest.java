package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.edge.registry.EdgeNodeRegistry;
import com.plus33.erp.edge.registry.ProvisioningService;

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
public class EdgeNodeRegistryTest {

    @Autowired EdgeNodeRegistry nodeRegistry;
    @Autowired ProvisioningService provisioningService;

    @Autowired PlatformEdgeNodeRepository nodeRepo;
    @Autowired PlatformEdgeCertificateLogRepository certRepo;
    @Autowired PlatformEdgeAuditLogRepository auditRepo;

    @Test
    void testEdgeNodeRegistryScenarios() {
        // Edge Node registry and provisioning over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformEdgeNode node = nodeRegistry.registerNode("EDGE_CODE_" + i, "Edge Node " + i, "CLUSTER_ALPHA_" + i);
            assertNotNull(node);
        }

        List<PlatformEdgeNode> nodes = nodeRepo.findAll();
        assertTrue(nodes.size() >= 40);

        List<PlatformEdgeAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("edge-admin", audits.get(0).getOperator());

        // Provisioning certificate rotations over 40 iterations
        PlatformEdgeNode testNode = nodes.get(0);
        for (int i = 1; i <= 40; i++) {
            provisioningService.rotateCertificate(testNode.getId(), "CERT-SERIAL-NUMBER-" + i);
        }

        List<PlatformEdgeCertificateLog> certs = certRepo.findAll();
        assertTrue(certs.size() >= 40);
        assertEquals("COMPLETED", certs.get(0).getRotationStatus());
    }
}
