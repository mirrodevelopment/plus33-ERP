/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmStewardDecision.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmStewardDecisionController
 * Related Service   : MdmStewardDecisionService, MdmStewardDecisionServiceImpl
 * Related Repository: MdmStewardDecisionRepository
 * Related Entity    : MdmStewardDecision
 * Related DTO       : N/A
 * Related Mapper    : MdmStewardDecisionMapper
 * Related DB Table  : bi_mdm_steward_decision
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmStewardDecisionRepository, MdmStewardDecisionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_steward_decision'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmStewardDecision}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_steward_decision'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_steward_decision}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_steward_decision")
public class MdmStewardDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "steward_assignment_id", nullable = false)
    @NotNull
    private MdmStewardAssignment stewardAssignment;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String decision;

    private String notes;

    @Column(name = "decided_at", nullable = false)
    @NotNull
    private LocalDateTime decidedAt = LocalDateTime.now();

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
     * Retrieves steward assignment data from the database.
     *
     * @return the MdmStewardAssignment result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public MdmStewardAssignment getStewardAssignment() { return stewardAssignment; }
    /**
     * Performs the setStewardAssignment operation in this module.
     *
     * @param stewardAssignment the stewardAssignment input value
     */
    public void setStewardAssignment(MdmStewardAssignment stewardAssignment) { this.stewardAssignment = stewardAssignment; }
    /**
     * Retrieves decision data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDecision() { return decision; }
    /**
     * Performs the setDecision operation in this module.
     *
     * @param decision the decision input value
     */
    public void setDecision(String decision) { this.decision = decision; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves decided at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDecidedAt() { return decidedAt; }
    /**
     * Performs the setDecidedAt operation in this module.
     *
     * @param decidedAt the decidedAt input value
     */
    public void setDecidedAt(LocalDateTime decidedAt) { this.decidedAt = decidedAt; }
}