package com.plus33.erp.bi.mdm;

import java.util.HashMap;
import java.util.Map;

public class GoldenRecordSnapshot {
    private Map<String, String> attributes = new HashMap<>();
    
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }
    
    public String getAttribute(String name) {
        return attributes.get(name);
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
}