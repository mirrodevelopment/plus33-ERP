package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_conformance_rule")
public class PlatformConformanceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "process_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String processName;

    @Column(name = "expected_activity", nullable = false)
    @NotNull
    @Size(max = 150)
    private String expectedActivity;

    @Column(name = "sequence_order", nullable = false)
    @NotNull
    private Integer sequenceOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getProcessName() { return processName; }
    public void setProcessName(String processName) { this.processName = processName; }
    public String getExpectedActivity() { return expectedActivity; }
    public void setExpectedActivity(String expectedActivity) { this.expectedActivity = expectedActivity; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
}