/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDispatchConstraintCheck.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDispatchConstraintCheckController
 * Related Service   : PlatformDispatchConstraintCheckService, PlatformDispatchConstraintCheckServiceImpl
 * Related Repository: PlatformDispatchConstraintCheckRepository
 * Related Entity    : PlatformDispatchConstraintCheck
 * Related DTO       : N/A
 * Related Mapper    : PlatformDispatchConstraintCheckMapper
 * Related DB Table  : platform_dispatch_constraint_check
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDispatchConstraintCheckRepository, PlatformDispatchConstraintCheckMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_dispatch_constraint_check'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDispatchConstraintCheck}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_dispatch_constraint_check'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_dispatch_constraint_check}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_dispatch_constraint_check")
public class PlatformDispatchConstraintCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dispatch_id", nullable = false)
    @NotNull
    private Long dispatchId;

    @Column(name = "constraint_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String constraintType; // Capacity, ShiftLimits, DeliveryWindows

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // PASSED, VIOLATED

    @Size(max = 500)
    private String reason;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // INFO, WARNING, CRITICAL

    @Column(name = "checked_at", nullable = false)
    @NotNull
    private LocalDateTime checkedAt = LocalDateTime.now();

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
     * Retrieves dispatch id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDispatchId() { return dispatchId; }
    /**
     * Performs the setDispatchId operation in this module.
     *
     * @param dispatchId the dispatchId input value
     */
    public void setDispatchId(Long dispatchId) { this.dispatchId = dispatchId; }
    /**
     * Retrieves constraint type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConstraintType() { return constraintType; }
    /**
     * Performs the setConstraintType operation in this module.
     *
     * @param constraintType the constraintType input value
     */
    public void setConstraintType(String constraintType) { this.constraintType = constraintType; }
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
     * Retrieves reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReason() { return reason; }
    /**
     * Performs the setReason operation in this module.
     *
     * @param reason the reason input value
     */
    public void setReason(String reason) { this.reason = reason; }
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
     * Retrieves checked at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCheckedAt() { return checkedAt; }
    /**
     * Performs the setCheckedAt operation in this module.
     *
     * @param checkedAt the checkedAt input value
     */
    public void setCheckedAt(LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
}