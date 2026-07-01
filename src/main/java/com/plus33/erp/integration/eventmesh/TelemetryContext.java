package com.plus33.erp.integration.eventmesh;

import java.util.UUID;

public class TelemetryContext {
    public static class TraceInfo {
        public String traceId;
        public String spanId;
        
        public TraceInfo(String traceId, String spanId) {
            this.traceId = traceId;
            this.spanId = spanId;
        }
    }

    public static TraceInfo extract(String traceparent) {
        if (traceparent == null || traceparent.trim().isEmpty() || !traceparent.startsWith("00-")) {
            return new TraceInfo(UUID.randomUUID().toString().replace("-", ""), UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        }
        String[] parts = traceparent.split("-");
        if (parts.length >= 3) {
            return new TraceInfo(parts[1], parts[2]);
        }
        return new TraceInfo(UUID.randomUUID().toString().replace("-", ""), UUID.randomUUID().toString().replace("-", "").substring(0, 16));
    }

    public static String inject(String traceId, String spanId) {
        return "00-" + traceId + "-" + spanId + "-01";
    }
}
