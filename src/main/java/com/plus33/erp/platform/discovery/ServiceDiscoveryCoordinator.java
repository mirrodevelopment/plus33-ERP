/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.discovery
 * File              : ServiceDiscoveryCoordinator.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceDiscoveryCoordinatorController
 * Related Service   : ServiceDiscoveryCoordinator
 * Related Repository: ServiceDiscoveryCoordinatorRepository
 * Related Entity    : ServiceDiscoveryCoordinator
 * Related DTO       : N/A
 * Related Mapper    : ServiceDiscoveryCoordinatorMapper
 * Related DB Table  : service_discovery_coordinators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceDiscoveryCoordinatorController, ServiceDiscoveryCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ServiceDiscoveryCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.discovery;

import com.plus33.erp.platform.entity.PlatformDiscoveryNode;
import com.plus33.erp.platform.repository.PlatformDiscoveryNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceDiscoveryCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.discovery}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ServiceDiscoveryCoordinatorController
 *   --> ServiceDiscoveryCoordinator (this)
 *   --> Validate business rules
 *   --> ServiceDiscoveryCoordinatorRepository (read/write 'service_discovery_coordinators')
 *   --> ServiceDiscoveryCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code service_discovery_coordinators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ServiceDiscoveryCoordinator {
    @Autowired PlatformDiscoveryNodeRepository nodeRepo;
    /**
     * Creates a new node and persists it to the database.
     *
     * @param nodeCode the nodeCode input value
     * @param ipAddress the ipAddress input value
     * @param port the port input value
     * @return the PlatformDiscoveryNode result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformDiscoveryNode registerNode(String nodeCode, String ipAddress, int port) {
        PlatformDiscoveryNode node = nodeRepo.findAll().stream()
                .filter(n -> n.getNodeCode().equals(nodeCode))
                .findFirst().orElse(null);

        if (node == null) {
            node = new PlatformDiscoveryNode();
            node.setNodeCode(nodeCode);
            node.setIpAddress(ipAddress);
            node.setPort(port);
            node.setStatus("STARTING");
            node.setClusterRole("FOLLOWER");
        }
        node.setLastHeartbeat(LocalDateTime.now());
        node = nodeRepo.save(node);

        electLeader();
        return node;
    }

    /**
     * Performs the pingHeartbeat operation in this module.
     *
     */
    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void pingHeartbeat() {
        // Keep active nodes updated and clean/expire dead ones
        List<PlatformDiscoveryNode> nodes = nodeRepo.findAll();
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(10);

        for (PlatformDiscoveryNode n : nodes) {
            if (n.getLastHeartbeat().isBefore(threshold)) {
                n.setStatus("OFFLINE");
            }
        }
        nodeRepo.saveAll(nodes);
        electLeader();
    }

    /**
     * Performs the electLeader operation in this module.
     *
     */
    @Transactional
    public void electLeader() {
        List<PlatformDiscoveryNode> active = nodeRepo.findAll().stream()
                .filter(n -> "STARTING".equals(n.getStatus()) || "UP".equals(n.getStatus()) || "LEADER".equals(n.getStatus()) || "FOLLOWER".equals(n.getStatus()))
                .sorted(Comparator.comparing(PlatformDiscoveryNode::getCreatedAt))
                .toList();

        if (active.isEmpty()) return;

        boolean leaderElected = false;
        for (int i = 0; i < active.size(); i++) {
            PlatformDiscoveryNode node = active.get(i);
            if (i == 0) {
                node.setClusterRole("LEADER");
                node.setStatus("UP");
                leaderElected = true;
            } else {
                node.setClusterRole("FOLLOWER");
                node.setStatus("UP");
            }
        }
        nodeRepo.saveAll(active);
    }
}