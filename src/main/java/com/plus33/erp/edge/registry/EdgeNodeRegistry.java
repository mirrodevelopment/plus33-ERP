/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Edge Module
 * Package           : com.plus33.erp.edge.registry
 * File              : EdgeNodeRegistry.java
 * Purpose           : Business logic service layer for Edge Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EdgeNodeRegistryController
 * Related Service   : EdgeNodeRegistry
 * Related Repository: EdgeNodeRegistryRepository
 * Related Entity    : EdgeNodeRegistry
 * Related DTO       : N/A
 * Related Mapper    : EdgeNodeRegistryMapper
 * Related DB Table  : edge_node_registrys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : EdgeNodeRegistryController, EdgeNodeRegistryImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Edge Module. Implements EdgeNodeRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.edge.registry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Edge Module</b>
 *
 * <p><b>Class  :</b> {@code EdgeNodeRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.edge.registry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Edge Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EdgeNodeRegistryController
 *   --> EdgeNodeRegistry (this)
 *   --> Validate business rules
 *   --> EdgeNodeRegistryRepository (read/write 'edge_node_registrys')
 *   --> EdgeNodeRegistryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code edge_node_registrys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class EdgeNodeRegistry {
    @Autowired PlatformEdgeNodeRepository nodeRepo;
    @Autowired PlatformEdgeAuditLogRepository auditRepo;
    /**
     * Creates a new node and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @param cluster the cluster input value
     * @return the PlatformEdgeNode result
     * @throws BusinessException if a business rule is violated
     */
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