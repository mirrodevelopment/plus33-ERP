package com.plus33.erp.bi.mdm;

public class SourcePriority {
    private String sourceSystem;
    private int priority;
    
    public SourcePriority(String sourceSystem, int priority) {
        this.sourceSystem = sourceSystem;
        this.priority = priority;
    }
    
    public String getSourceSystem() { return sourceSystem; }
    public int getPriority() { return priority; }
}