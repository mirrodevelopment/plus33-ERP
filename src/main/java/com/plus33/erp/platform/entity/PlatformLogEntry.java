package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_log_entry")
public class PlatformLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id")
    @Size(max = 100)
    private String traceId;

    @Column(name = "span_id")
    @Size(max = 100)
    private String spanId;

    @Column(name = "service_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String serviceName;

    @Column(name = "node_id", nullable = false)
    @NotNull
    @Size(max = 100)
    private String nodeId;

    @Column(name = "log_level", nullable = false)
    @NotNull
    @Size(max = 50)
    private String logLevel;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String logger;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(name = "json_payload", columnDefinition = "TEXT")
    private String jsonPayload;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getSpanId() { return spanId; }
    public void setSpanId(String spanId) { this.spanId = spanId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }
    public String getLogLevel() { return logLevel; }
    public void setLogLevel(String logLevel) { this.logLevel = logLevel; }
    public String getLogger() { return logger; }
    public void setLogger(String logger) { this.logger = logger; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getJsonPayload() { return jsonPayload; }
    public void setJsonPayload(String jsonPayload) { this.jsonPayload = jsonPayload; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}