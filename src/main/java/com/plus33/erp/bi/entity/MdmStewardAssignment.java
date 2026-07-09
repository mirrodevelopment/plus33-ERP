/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmStewardAssignment.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmStewardAssignmentController
 * Related Service   : MdmStewardAssignmentService, MdmStewardAssignmentServiceImpl
 * Related Repository: MdmStewardAssignmentRepository
 * Related Entity    : MdmStewardAssignment
 * Related DTO       : getMergeRequest, MdmMergeRequest, mergeRequest, setMergeRequest
 * Related Mapper    : MdmStewardAssignmentMapper
 * Related DB Table  : bi_mdm_steward_assignment
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmStewardAssignmentRepository, MdmStewardAssignmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_steward_assignment'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmStewardAssignment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_steward_assignment'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_steward_assignment}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_steward_assignment")
public class MdmStewardAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merge_request_id", nullable = false)
    @NotNull
    private MdmMergeRequest mergeRequest;

    @Column(name = "steward_user", nullable = false)
    @NotNull
    @Size(max = 100)
    private String stewardUser;

    @Column(name = "assigned_at", nullable = false)
    @NotNull
    private LocalDateTime assignedAt = LocalDateTime.now();

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ASSIGNED";

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
     * Retrieves merge request data from the database.
     *
     * @return the MdmMergeRequest result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public MdmMergeRequest getMergeRequest() { return mergeRequest; }
    /**
     * Performs the setMergeRequest operation in this module.
     *
     * @param mergeRequest the mergeRequest input value
     */
    public void setMergeRequest(MdmMergeRequest mergeRequest) { this.mergeRequest = mergeRequest; }
    /**
     * Retrieves steward user data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStewardUser() { return stewardUser; }
    /**
     * Performs the setStewardUser operation in this module.
     *
     * @param stewardUser the stewardUser input value
     */
    public void setStewardUser(String stewardUser) { this.stewardUser = stewardUser; }
    /**
     * Retrieves assigned at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAssignedAt() { return assignedAt; }
    /**
     * Performs the setAssignedAt operation in this module.
     *
     * @param assignedAt the assignedAt input value
     */
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    /**
     * Retrieves due at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDueAt() { return dueAt; }
    /**
     * Performs the setDueAt operation in this module.
     *
     * @param dueAt the dueAt input value
     */
    public void setDueAt(LocalDateTime dueAt) { this.dueAt = dueAt; }
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
}