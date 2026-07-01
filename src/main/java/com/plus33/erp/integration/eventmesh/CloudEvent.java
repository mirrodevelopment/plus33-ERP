package com.plus33.erp.integration.eventmesh;

import java.time.LocalDateTime;

public class CloudEvent {
    private String id;
    private String source;
    private String type;
    private String specversion = "1.0";
    private String subject;
    private LocalDateTime time = LocalDateTime.now();
    private String datacontenttype = "application/json";
    private String dataschema;
    private String data;
    private String traceparent;
    private String correlationId;
    private String tenantId;
    private Long sequenceNumber;

    // Default constructor
    public CloudEvent() {}

    public CloudEvent(String id, String source, String type, String data) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.data = data;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSpecversion() { return specversion; }
    public void setSpecversion(String specversion) { this.specversion = specversion; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public String getDatacontenttype() { return datacontenttype; }
    public void setDatacontenttype(String datacontenttype) { this.datacontenttype = datacontenttype; }
    public String getDataschema() { return dataschema; }
    public void setDataschema(String dataschema) { this.dataschema = dataschema; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getTraceparent() { return traceparent; }
    public void setTraceparent(String traceparent) { this.traceparent = traceparent; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Long getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(Long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
}
