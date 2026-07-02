package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_device_compliance_policy")
public class PlatformDeviceCompliancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "policy_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String policyName;

    @Column(name = "policy_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String policyType;

    @Column(name = "required_os")
    @Size(max = 100)
    private String requiredOs;

    @Column(name = "minimum_agent_version")
    @Size(max = 50)
    private String minimumAgentVersion;

    @Column(name = "required_packages", columnDefinition = "TEXT")
    private String requiredPackages;

    @Column(name = "required_services", columnDefinition = "TEXT")
    private String requiredServices;

    @Column(name = "required_ports")
    @Size(max = 200)
    private String requiredPorts;

    @Column(name = "required_kernel_version")
    @Size(max = 100)
    private String requiredKernelVersion;

    @Column(name = "required_certificate")
    @Size(max = 200)
    private String requiredCertificate;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // Low, Medium, High, Critical

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "created_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public String getPolicyType() { return policyType; }
    public void setPolicyType(String policyType) { this.policyType = policyType; }
    public String getRequiredOs() { return requiredOs; }
    public void setRequiredOs(String requiredOs) { this.requiredOs = requiredOs; }
    public String getMinimumAgentVersion() { return minimumAgentVersion; }
    public void setMinimumAgentVersion(String minimumAgentVersion) { this.minimumAgentVersion = minimumAgentVersion; }
    public String getRequiredPackages() { return requiredPackages; }
    public void setRequiredPackages(String requiredPackages) { this.requiredPackages = requiredPackages; }
    public String getRequiredServices() { return requiredServices; }
    public void setRequiredServices(String requiredServices) { this.requiredServices = requiredServices; }
    public String getRequiredPorts() { return requiredPorts; }
    public void setRequiredPorts(String requiredPorts) { this.requiredPorts = requiredPorts; }
    public String getRequiredKernelVersion() { return requiredKernelVersion; }
    public void setRequiredKernelVersion(String requiredKernelVersion) { this.requiredKernelVersion = requiredKernelVersion; }
    public String getRequiredCertificate() { return requiredCertificate; }
    public void setRequiredCertificate(String requiredCertificate) { this.requiredCertificate = requiredCertificate; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}