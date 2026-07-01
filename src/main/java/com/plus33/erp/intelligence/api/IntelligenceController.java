package com.plus33.erp.intelligence.api;

import com.plus33.erp.intelligence.causal.CausalInferenceEngine;
import com.plus33.erp.intelligence.causal.RootCauseAnalyzer;
import com.plus33.erp.intelligence.query.NaturalLanguageQueryService;
import com.plus33.erp.intelligence.query.DecisionLineageTracker;
import com.plus33.erp.platform.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/intelligence")
public class IntelligenceController {
    @Autowired CausalInferenceEngine causalEngine;
    @Autowired RootCauseAnalyzer rootCauseAnalyzer;
    @Autowired NaturalLanguageQueryService queryService;
    @Autowired DecisionLineageTracker lineageTracker;

    @PostMapping("/causal/model")
    public ResponseEntity<Void> registerCausalModel(
            @RequestParam String code,
            @RequestParam String name,
            @RequestParam String structure) {
        causalEngine.registerModel(code, name, structure);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/root-cause")
    public ResponseEntity<Void> runRootCauseAnalysis(
            @RequestParam Long modelId,
            @RequestParam String anomaly) {
        rootCauseAnalyzer.runAnalysis(modelId, anomaly);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/query")
    public ResponseEntity<Void> runOperationalQuery(
            @RequestParam String queryText) {
        queryService.executeQuery(queryText);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/xai")
    public ResponseEntity<Void> recordXaiLineage(
            @RequestParam String key,
            @RequestParam String factors,
            @RequestParam String version) {
        lineageTracker.recordLineage(key, factors, version);
        return ResponseEntity.ok().build();
    }
}