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

@Service
public class ServiceDiscoveryCoordinator {
    @Autowired PlatformDiscoveryNodeRepository nodeRepo;

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