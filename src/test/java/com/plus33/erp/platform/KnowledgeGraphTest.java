package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.intelligence.graph.KnowledgeGraphService;
import com.plus33.erp.intelligence.semantic.TwinRelationshipService;
import com.plus33.erp.intelligence.semantic.SemanticTraversalService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class KnowledgeGraphTest {

    @Autowired KnowledgeGraphService graphService;
    @Autowired TwinRelationshipService twinRelationshipService;
    @Autowired SemanticTraversalService traversalService;

    @Autowired PlatformGraphNodeRepository nodeRepo;
    @Autowired PlatformGraphEdgeRepository edgeRepo;
    @Autowired PlatformTwinRelationRepository relationRepo;

    @Test
    void testKnowledgeGraphScenarios() {
        // Node CRUD & Edge CRUD over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformGraphNode nodeA = graphService.addNode("PUMP_NODE", "pump-key-" + i, "Pump Node " + i, "INVENTORY", "{}");
            PlatformGraphNode nodeB = graphService.addNode("MOTOR_NODE", "motor-key-" + i, "Motor Node " + i, "INVENTORY", "{}");
            assertNotNull(nodeA);
            assertNotNull(nodeB);

            PlatformGraphEdge edge = graphService.addEdge(nodeA.getId(), nodeB.getId(), "DependsOn");
            assertNotNull(edge);
        }

        List<PlatformGraphNode> nodes = nodeRepo.findAll();
        assertTrue(nodes.size() >= 80);

        List<PlatformGraphEdge> edges = edgeRepo.findAll();
        assertTrue(edges.size() >= 40);

        // Multi-hop traversal & Relationship creation over 40 semantic relationships
        for (int i = 1; i <= 40; i++) {
            twinRelationshipService.relate(1L, (long) i, "DependsOn");
        }

        List<PlatformTwinRelation> traversals = traversalService.findDependencies(1L);
        assertEquals(40, traversals.size());
    }
}
