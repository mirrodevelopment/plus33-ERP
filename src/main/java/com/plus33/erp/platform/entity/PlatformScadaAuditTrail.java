/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaAuditTrail.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaAuditTrailController
 * Related Service   : PlatformScadaAuditTrailService, PlatformScadaAuditTrailServiceImpl
 * Related Repository: PlatformScadaAuditTrailRepository
 * Related Entity    : PlatformScadaAuditTrail
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaAuditTrailMapper
 * Related DB Table  : platform_scada_audit_trail
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaAuditTrailRepository, PlatformScadaAuditTrailMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_audit_trail'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaAuditTrail}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_audit_trail'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_audit_trail}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_audit_trail")
public class PlatformScadaAuditTrail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "command_id", nullable = false)
    @NotNull
    private Long commandId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "audit_hash", nullable = false)
    @NotNull
    @Size(max = 150)
    private String auditHash;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

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
     * Retrieves command id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCommandId() { return commandId; }
    /**
     * Performs the setCommandId operation in this module.
     *
     * @param commandId the commandId input value
     */
    public void setCommandId(Long commandId) { this.commandId = commandId; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves audit hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAuditHash() { return auditHash; }
    /**
     * Performs the setAuditHash operation in this module.
     *
     * @param auditHash the auditHash input value
     */
    public void setAuditHash(String auditHash) { this.auditHash = auditHash; }
    /**
     * Retrieves audited at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAuditedAt() { return auditedAt; }
    /**
     * Performs the setAuditedAt operation in this module.
     *
     * @param auditedAt the auditedAt input value
     */
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}