package com.plus33.erp.intelligence.graph;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KnowledgeGraphService {
    @Autowired PlatformGraphNodeRepository nodeRepo;
    @Autowired PlatformGraphEdgeRepository edgeRepo;

    @Transactional
    public PlatformGraphNode addNode(String type, String key, String name, String mod, String meta) {
        PlatformGraphNode node = new PlatformGraphNode();
        node.setNodeType(type);
        node.setBusinessKey(key);
        node.setDisplayName(name);
        node.setModule(mod);
        node.setMetadataJson(meta);
        return nodeRepo.save(node);
    }

    @Transactional
    public PlatformGraphEdge addEdge(Long src, Long target, String type) {
        PlatformGraphEdge edge = new PlatformGraphEdge();
        edge.setSourceNode(src);
        edge.setTargetNode(target);
        edge.setRelationshipType(type);
        return edgeRepo.save(edge);
    }
}