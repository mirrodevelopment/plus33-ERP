/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : SurvivorshipEngine.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SurvivorshipEngineController
 * Related Service   : SurvivorshipEngineService, SurvivorshipEngineServiceImpl
 * Related Repository: SurvivorshipEngineRepository
 * Related Entity    : SurvivorshipEngine
 * Related DTO       : N/A
 * Related Mapper    : SurvivorshipEngineMapper
 * Related DB Table  : survivorship_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

import org.springframework.stereotype.Component;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code SurvivorshipEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.mdm}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Bi Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class SurvivorshipEngine {

    /**
     * Performs the resolveAttribute operation in this module.
     *
     * @return the result string value
     */
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