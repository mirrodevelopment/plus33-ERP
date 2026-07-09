/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : EngineeringChangeLine.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeLineController
 * Related Service   : EngineeringChangeLineService, EngineeringChangeLineServiceImpl
 * Related Repository: EngineeringChangeLineRepository
 * Related Entity    : EngineeringChangeLine
 * Related DTO       : N/A
 * Related Mapper    : EngineeringChangeLineMapper
 * Related DB Table  : engineering_change_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EngineeringChangeLineRepository, EngineeringChangeLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'engineering_change_lines'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code EngineeringChangeLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'engineering_change_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code engineering_change_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "engineering_change_lines")
public class EngineeringChangeLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eco_id", nullable = false)
    private EngineeringChangeOrder engineeringChangeOrder;

    @Column(name = "change_type", nullable = false, length = 30)
    private String changeType; // BOM_ADD, BOM_REMOVE, BOM_MODIFY, ROUTING_ADD, ROUTING_REMOVE, ROUTING_MODIFY

    @Column(name = "reference_type", nullable = false, length = 30)
    private String referenceType; // BOM_HEADER, BOM_LINE, ROUTING_HEADER, ROUTING_OPERATION

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "before_snapshot", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String beforeSnapshot;

    @Column(name = "after_snapshot", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String afterSnapshot;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "sort_sequence", nullable = false)
    private Integer sortSequence = 10;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public EngineeringChangeLine() {}

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
     * Retrieves engineering change order data from the database.
     *
     * @return the EngineeringChangeOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public EngineeringChangeOrder getEngineeringChangeOrder() { return engineeringChangeOrder; }
    /**
     * Performs the setEngineeringChangeOrder operation in this module.
     *
     * @param eco the eco input value
     */
    public void setEngineeringChangeOrder(EngineeringChangeOrder eco) { this.engineeringChangeOrder = eco; }
    /**
     * Retrieves change type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChangeType() { return changeType; }
    /**
     * Performs the setChangeType operation in this module.
     *
     * @param changeType the changeType input value
     */
    public void setChangeType(String changeType) { this.changeType = changeType; }
    /**
     * Retrieves reference type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceType() { return referenceType; }
    /**
     * Performs the setReferenceType operation in this module.
     *
     * @param referenceType the referenceType input value
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /**
     * Retrieves reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * Performs the setReferenceId operation in this module.
     *
     * @param referenceId the referenceId input value
     */
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    /**
     * Retrieves before snapshot data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBeforeSnapshot() { return beforeSnapshot; }
    /**
     * Performs the setBeforeSnapshot operation in this module.
     *
     * @param beforeSnapshot the beforeSnapshot input value
     */
    public void setBeforeSnapshot(String beforeSnapshot) { this.beforeSnapshot = beforeSnapshot; }
    /**
     * Retrieves after snapshot data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAfterSnapshot() { return afterSnapshot; }
    /**
     * Performs the setAfterSnapshot operation in this module.
     *
     * @param afterSnapshot the afterSnapshot input value
     */
    public void setAfterSnapshot(String afterSnapshot) { this.afterSnapshot = afterSnapshot; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves sort sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSortSequence() { return sortSequence; }
    /**
     * Performs the setSortSequence operation in this module.
     *
     * @param sortSequence the sortSequence input value
     */
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}