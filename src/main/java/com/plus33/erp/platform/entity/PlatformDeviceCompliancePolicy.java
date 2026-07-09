/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDeviceCompliancePolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeviceCompliancePolicyController
 * Related Service   : PlatformDeviceCompliancePolicyService, PlatformDeviceCompliancePolicyServiceImpl
 * Related Repository: PlatformDeviceCompliancePolicyRepository
 * Related Entity    : PlatformDeviceCompliancePolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeviceCompliancePolicyMapper
 * Related DB Table  : platform_device_compliance_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeviceCompliancePolicyRepository, PlatformDeviceCompliancePolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_device_compliance_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeviceCompliancePolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_device_compliance_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_device_compliance_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves policy code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyCode() { return policyCode; }
    /**
     * Performs the setPolicyCode operation in this module.
     *
     * @param policyCode the policyCode input value
     */
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    /**
     * Retrieves policy name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyName() { return policyName; }
    /**
     * Performs the setPolicyName operation in this module.
     *
     * @param policyName the policyName input value
     */
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    /**
     * Retrieves policy type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyType() { return policyType; }
    /**
     * Performs the setPolicyType operation in this module.
     *
     * @param policyType the policyType input value
     */
    public void setPolicyType(String policyType) { this.policyType = policyType; }
    /**
     * Retrieves required os data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredOs() { return requiredOs; }
    /**
     * Performs the setRequiredOs operation in this module.
     *
     * @param requiredOs the requiredOs input value
     */
    public void setRequiredOs(String requiredOs) { this.requiredOs = requiredOs; }
    /**
     * Retrieves minimum agent version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMinimumAgentVersion() { return minimumAgentVersion; }
    /**
     * Performs the setMinimumAgentVersion operation in this module.
     *
     * @param minimumAgentVersion the minimumAgentVersion input value
     */
    public void setMinimumAgentVersion(String minimumAgentVersion) { this.minimumAgentVersion = minimumAgentVersion; }
    /**
     * Retrieves required packages data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredPackages() { return requiredPackages; }
    /**
     * Performs the setRequiredPackages operation in this module.
     *
     * @param requiredPackages the requiredPackages input value
     */
    public void setRequiredPackages(String requiredPackages) { this.requiredPackages = requiredPackages; }
    /**
     * Retrieves required services data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredServices() { return requiredServices; }
    /**
     * Performs the setRequiredServices operation in this module.
     *
     * @param requiredServices the requiredServices input value
     */
    public void setRequiredServices(String requiredServices) { this.requiredServices = requiredServices; }
    /**
     * Retrieves required ports data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredPorts() { return requiredPorts; }
    /**
     * Performs the setRequiredPorts operation in this module.
     *
     * @param requiredPorts the requiredPorts input value
     */
    public void setRequiredPorts(String requiredPorts) { this.requiredPorts = requiredPorts; }
    /**
     * Retrieves required kernel version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredKernelVersion() { return requiredKernelVersion; }
    /**
     * Performs the setRequiredKernelVersion operation in this module.
     *
     * @param requiredKernelVersion the requiredKernelVersion input value
     */
    public void setRequiredKernelVersion(String requiredKernelVersion) { this.requiredKernelVersion = requiredKernelVersion; }
    /**
     * Retrieves required certificate data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequiredCertificate() { return requiredCertificate; }
    /**
     * Performs the setRequiredCertificate operation in this module.
     *
     * @param requiredCertificate the requiredCertificate input value
     */
    public void setRequiredCertificate(String requiredCertificate) { this.requiredCertificate = requiredCertificate; }
    /**
     * Retrieves severity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; }
    /**
     * Performs the setSeverity operation in this module.
     *
     * @param severity the severity input value
     */
    public void setSeverity(String severity) { this.severity = severity; }
    /**
     * Retrieves enabled data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getEnabled() { return enabled; }
    /**
     * Performs the setEnabled operation in this module.
     *
     * @param enabled the enabled input value
     */
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    /**
     * Retrieves created by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}