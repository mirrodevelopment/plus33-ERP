package com.plus33.erp.bi.mdm;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SurvivorshipEngine {

    public String resolveAttribute(
            String attributeName, 
            List<AttributeCandidate> candidates, 
            AttributeResolutionRule rule, 
            String manualOverrideValue) {
        
        if (rule == AttributeResolutionRule.MANUAL_OVERRIDE && manualOverrideValue != null) {
            return manualOverrideValue;
        }
        
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        
        AttributeCandidate best = null;
        for (AttributeCandidate c : candidates) {
            if (c.getValue() == null || c.getValue().trim().isEmpty()) {
                continue;
            }
            if (best == null) {
                best = c;
                continue;
            }
            
            switch (rule) {
                case HIGHEST_CONFIDENCE:
                    if (c.getConfidence() > best.getConfidence()) {
                        best = c;
                    }
                    break;
                case MOST_RECENT:
                    if (c.getUpdatedAt().isAfter(best.getUpdatedAt())) {
                        best = c;
                    }
                    break;
                case TRUSTED_SOURCE_PRIORITY:
                    if (c.getSourcePriority() < best.getSourcePriority()) {
                        best = c;
                    }
                    break;
                case LONGEST_HISTORY:
                    if (c.getCreatedAt().isBefore(best.getCreatedAt())) {
                        best = c;
                    }
                    break;
                default:
                    break;
            }
        }
        return best != null ? best.getValue() : null;
    }

    public static class AttributeCandidate {
        private String value;
        private double confidence;
        private java.time.LocalDateTime updatedAt;
        private java.time.LocalDateTime createdAt;
        private int sourcePriority;

        public AttributeCandidate(String value, double confidence, java.time.LocalDateTime updatedAt, java.time.LocalDateTime createdAt, int sourcePriority) {
            this.value = value;
            this.confidence = confidence;
            this.updatedAt = updatedAt;
            this.createdAt = createdAt;
            this.sourcePriority = sourcePriority;
        }

        public String getValue() { return value; }
        public double getConfidence() { return confidence; }
        public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
        public int getSourcePriority() { return sourcePriority; }
    }
}