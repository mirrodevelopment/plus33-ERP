/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScalingActivity.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScalingActivityController
 * Related Service   : PlatformScalingActivityService, PlatformScalingActivityServiceImpl
 * Related Repository: PlatformScalingActivityRepository
 * Related Entity    : PlatformScalingActivity
 * Related DTO       : N/A
 * Related Mapper    : PlatformScalingActivityMapper
 * Related DB Table  : platform_scaling_activity
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScalingActivityRepository, PlatformScalingActivityMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scaling_activity'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScalingActivity}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scaling_activity'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scaling_activity}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scaling_activity")
public class PlatformScalingActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "activity_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String activityType;

    @Column(name = "current_replicas", nullable = false)
    @NotNull
    private Integer currentReplicas;

    @Column(name = "desired_replicas", nullable = false)
    @NotNull
    private Integer desiredReplicas;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "COMPLETED";

    @Column(name = "started_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
     * Retrieves activity type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActivityType() { return activityType; }
    /**
     * Performs the setActivityType operation in this module.
     *
     * @param activityType the activityType input value
     */
    public void setActivityType(String activityType) { this.activityType = activityType; }
    /**
     * Retrieves current replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCurrentReplicas() { return currentReplicas; }
    /**
     * Performs the setCurrentReplicas operation in this module.
     *
     * @param currentReplicas the currentReplicas input value
     */
    public void setCurrentReplicas(Integer currentReplicas) { this.currentReplicas = currentReplicas; }
    /**
     * Retrieves desired replicas data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDesiredReplicas() { return desiredReplicas; }
    /**
     * Performs the setDesiredReplicas operation in this module.
     *
     * @param desiredReplicas the desiredReplicas input value
     */
    public void setDesiredReplicas(Integer desiredReplicas) { this.desiredReplicas = desiredReplicas; }
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
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}