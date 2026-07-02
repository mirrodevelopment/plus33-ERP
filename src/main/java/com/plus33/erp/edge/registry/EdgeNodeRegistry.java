package com.plus33.erp.edge.registry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class EdgeNodeRegistry {
    @Autowired PlatformEdgeNodeRepository nodeRepo;
    @Autowired PlatformEdgeAuditLogRepository auditRepo;

    @Transactional
    public PlatformEdgeNode registerNode(String code, String name, String cluster) {
        PlatformEdgeNode n = new PlatformEdgeNode();
        n.setNodeCode(code);
        n.setNodeName(name);
        n.setEdgeCluster(cluster);
        n.setStatus("ACTIVE");
        n.setLastSeen(LocalDateTime.now());
        n.setProvisionedAt(LocalDateTime.now());
        n = nodeRepo.save(n);

        PlatformEdgeAuditLog audit = new PlatformEdgeAuditLog();
        audit.setNodeId(n.getId());
        audit.setOperator("edge-admin");
        audit.setActionType("UPDATE_CONFIG");
        audit.setNewConfig("{ \"cluster\": \"" + cluster + "\" }");
        audit.setTraceId("TRACE-ID-EDGE-INIT");
        auditRepo.save(audit);

        return n;
    }
}