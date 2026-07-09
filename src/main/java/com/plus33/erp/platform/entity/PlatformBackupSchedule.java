/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformBackupSchedule.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformBackupScheduleController
 * Related Service   : PlatformBackupScheduleService, PlatformBackupScheduleServiceImpl
 * Related Repository: PlatformBackupScheduleRepository
 * Related Entity    : PlatformBackupSchedule
 * Related DTO       : N/A
 * Related Mapper    : PlatformBackupScheduleMapper
 * Related DB Table  : platform_backup_schedule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformBackupScheduleRepository, PlatformBackupScheduleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_backup_schedule'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformBackupSchedule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_backup_schedule'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_backup_schedule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_backup_schedule")
public class PlatformBackupSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_cron", nullable = false)
    @NotNull
    @Size(max = 100)
    private String scheduleCron;

    @Column(name = "target_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String targetType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String destination;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
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
     * Retrieves schedule cron data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScheduleCron() { return scheduleCron; }
    /**
     * Performs the setScheduleCron operation in this module.
     *
     * @param scheduleCron the scheduleCron input value
     */
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    /**
     * Retrieves target type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTargetType() { return targetType; }
    /**
     * Performs the setTargetType operation in this module.
     *
     * @param targetType the targetType input value
     */
    public void setTargetType(String targetType) { this.targetType = targetType; }
    /**
     * Retrieves destination data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDestination() { return destination; }
    /**
     * Performs the setDestination operation in this module.
     *
     * @param destination the destination input value
     */
    public void setDestination(String destination) { this.destination = destination; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
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