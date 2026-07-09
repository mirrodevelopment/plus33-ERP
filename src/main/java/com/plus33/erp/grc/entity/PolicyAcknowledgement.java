/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : PolicyAcknowledgement.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyAcknowledgementController
 * Related Service   : PolicyAcknowledgementService, PolicyAcknowledgementServiceImpl
 * Related Repository: PolicyAcknowledgementRepository
 * Related Entity    : PolicyAcknowledgement
 * Related DTO       : N/A
 * Related Mapper    : PolicyAcknowledgementMapper
 * Related DB Table  : grc_policy_acknowledgements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyAcknowledgementRepository, PolicyAcknowledgementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_policy_acknowledgements'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_policy_acknowledgements",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_version_id", "employee_id"}))
/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code PolicyAcknowledgement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_policy_acknowledgements'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_policy_acknowledgements}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class PolicyAcknowledgement {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "policy_version_id", nullable = false) private Long policyVersionId;
    @Column(name = "employee_id", nullable = false) private Long employeeId;
    @Column(name = "acknowledged_at", nullable = false) private LocalDateTime acknowledgedAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves policy version id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyVersionId() { return policyVersionId; } public void setPolicyVersionId(Long v) { this.policyVersionId = v; }
    /**
     * Retrieves employee id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEmployeeId() { return employeeId; } public void setEmployeeId(Long v) { this.employeeId = v; }
    /**
     * Retrieves acknowledged at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; } public void setAcknowledgedAt(LocalDateTime v) { this.acknowledgedAt = v; }
}