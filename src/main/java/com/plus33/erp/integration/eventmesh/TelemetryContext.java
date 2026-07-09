/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : TelemetryContext.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TelemetryContextController
 * Related Service   : TelemetryContextService, TelemetryContextServiceImpl
 * Related Repository: TelemetryContextRepository
 * Related Entity    : TelemetryContext
 * Related DTO       : N/A
 * Related Mapper    : TelemetryContextMapper
 * Related DB Table  : telemetry_contexts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

    /**
     * Performs the extract operation in this module.
     *
     * @param traceparent the traceparent input value
     * @return the TraceInfo result
     */
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

    /**
     * Performs the inject operation in this module.
     *
     * @param traceId the traceId input value
     * @param spanId the spanId input value
     * @return the result string value
     */
    public static String inject(String traceId, String spanId) {
        return "00-" + traceId + "-" + spanId + "-01";
    }
}
