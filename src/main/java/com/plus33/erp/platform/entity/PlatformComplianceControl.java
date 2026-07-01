package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_compliance_control")
public class PlatformComplianceControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "framework_id", nullable = false)
    @NotNull
    private Long frameworkId;

    @Column(name = "control_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String controlCode;

    @Column(name = "control_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String controlName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "COMPLIANT";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getFrameworkId() { return frameworkId; }
    public void setFrameworkId(Long frameworkId) { this.frameworkId = frameworkId; }
    public String getControlCode() { return controlCode; }
    public void setControlCode(String controlCode) { this.controlCode = controlCode; }
    public String getControlName() { return controlName; }
    public void setControlName(String controlName) { this.controlName = controlName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}