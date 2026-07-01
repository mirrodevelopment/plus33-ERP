package com.plus33.erp.intelligence.causal;

import org.springframework.stereotype.Service;

@Service
public class ExplainabilityService {
    public String generateExplanation(String node, String probs) {
        return "Causal path verified anomaly event caused by " + node + " with probabilities: " + probs;
    }
}