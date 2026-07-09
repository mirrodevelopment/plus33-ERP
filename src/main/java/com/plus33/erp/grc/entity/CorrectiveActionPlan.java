/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : CorrectiveActionPlan.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CorrectiveActionPlanController
 * Related Service   : CorrectiveActionPlanService, CorrectiveActionPlanServiceImpl
 * Related Repository: CorrectiveActionPlanRepository
 * Related Entity    : CorrectiveActionPlan
 * Related DTO       : N/A
 * Related Mapper    : CorrectiveActionPlanMapper
 * Related DB Table  : grc_corrective_action_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CorrectiveActionPlanRepository, CorrectiveActionPlanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_corrective_action_plans'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code CorrectiveActionPlan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_corrective_action_plans'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_corrective_action_plans}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_corrective_action_plans")
public class CorrectiveActionPlan {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "issue_id", nullable = false) private Long issueId;
    @Column(nullable = false, columnDefinition = "TEXT") private String description;
    @Column(name = "owner_id") private Long ownerId;
    @Column(name = "due_date", nullable = false) private LocalDate dueDate;
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(name = "closed_at") private LocalDateTime closedAt;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves issue id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getIssueId() { return issueId; } public void setIssueId(Long v) { this.issueId = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    /**
     * Retrieves owner id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
    /**
     * Retrieves due date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { this.dueDate = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves closed at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getClosedAt() { return closedAt; } public void setClosedAt(LocalDateTime v) { this.closedAt = v; }
}