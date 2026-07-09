/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaAlarmPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaAlarmPolicyController
 * Related Service   : PlatformScadaAlarmPolicyService, PlatformScadaAlarmPolicyServiceImpl
 * Related Repository: PlatformScadaAlarmPolicyRepository
 * Related Entity    : PlatformScadaAlarmPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaAlarmPolicyMapper
 * Related DB Table  : platform_scada_alarm_policy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaAlarmPolicyRepository, PlatformScadaAlarmPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_alarm_policy'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaAlarmPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_alarm_policy'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_alarm_policy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_alarm_policy")
public class PlatformScadaAlarmPolicy {
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

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // Critical, High, Medium, Low

    @Column(name = "threshold_value", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal thresholdValue;

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
     * Retrieves threshold value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdValue() { return thresholdValue; }
    /**
     * Performs the setThresholdValue operation in this module.
     *
     * @param thresholdValue the thresholdValue input value
     */
    public void setThresholdValue(BigDecimal thresholdValue) { this.thresholdValue = thresholdValue; }
}