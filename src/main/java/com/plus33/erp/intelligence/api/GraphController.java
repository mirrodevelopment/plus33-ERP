package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.graph.KnowledgeGraphService;
import com.plus33.erp.intelligence.semantic.TwinRelationshipService;
import com.plus33.erp.intelligence.semantic.SemanticTraversalService;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/intelligence/graph")
public class GraphController {
    @Autowired KnowledgeGraphService graphService;
    @Autowired TwinRelationshipService twinRelationshipService;
    @Autowired SemanticTraversalService semanticTraversalService;

    @PostMapping("/node")
    public ResponseEntity<Void> addNode(
            @RequestParam String type,
            @RequestParam String key,
            @RequestParam String name,
            @RequestParam String module,
            @RequestParam(required = false) String metadata) {
        graphService.addNode(type, key, name, module, metadata);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edge")
    public ResponseEntity<Void> addEdge(
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam String type) {
        graphService.addEdge(source, target, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/relation")
    public ResponseEntity<Void> addRelation(
            @RequestParam Long source,
            @RequestParam Long target,
            @RequestParam String type) {
        twinRelationshipService.relate(source, target, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/traverse")
    public ResponseEntity<List<PlatformTwinRelation>> traverse(
            @RequestParam Long instanceId) {
        return ResponseEntity.ok(semanticTraversalService.findDependencies(instanceId));
    }
}