package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_compliance_framework")
public class PlatformComplianceFramework {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "framework_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String frameworkCode;

    @Column(name = "framework_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String frameworkName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getFrameworkCode() { return frameworkCode; }
    public void setFrameworkCode(String frameworkCode) { this.frameworkCode = frameworkCode; }
    public String getFrameworkName() { return frameworkName; }
    public void setFrameworkName(String frameworkName) { this.frameworkName = frameworkName; }
}