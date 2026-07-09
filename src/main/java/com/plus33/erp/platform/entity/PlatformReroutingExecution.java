/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformReroutingExecution.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformReroutingExecutionController
 * Related Service   : PlatformReroutingExecutionService, PlatformReroutingExecutionServiceImpl
 * Related Repository: PlatformReroutingExecutionRepository
 * Related Entity    : PlatformReroutingExecution
 * Related DTO       : N/A
 * Related Mapper    : PlatformReroutingExecutionMapper
 * Related DB Table  : platform_rerouting_execution
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformReroutingExecutionRepository, PlatformReroutingExecutionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_rerouting_execution'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformReroutingExecution}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_rerouting_execution'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_rerouting_execution}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_rerouting_execution")
public class PlatformReroutingExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rerouting_id", nullable = false)
    @NotNull
    private Long reroutingId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "EXECUTED";

    @Column(name = "executed_at", nullable = false)
    @NotNull
    private LocalDateTime executedAt = LocalDateTime.now();

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
     * Retrieves rerouting id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReroutingId() { return reroutingId; }
    /**
     * Performs the setReroutingId operation in this module.
     *
     * @param reroutingId the reroutingId input value
     */
    public void setReroutingId(Long reroutingId) { this.reroutingId = reroutingId; }
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
     * Retrieves executed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExecutedAt() { return executedAt; }
    /**
     * Performs the setExecutedAt operation in this module.
     *
     * @param executedAt the executedAt input value
     */
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}