/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : ControlTestResult.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlTestResultController
 * Related Service   : ControlTestResultService, ControlTestResultServiceImpl
 * Related Repository: ControlTestResultRepository
 * Related Entity    : ControlTestResult
 * Related DTO       : N/A
 * Related Mapper    : ControlTestResultMapper
 * Related DB Table  : grc_control_test_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlTestResultRepository, ControlTestResultMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_control_test_results'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ControlTestResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_control_test_results'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_control_test_results}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_control_test_results")
public class ControlTestResult {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "test_plan_id", nullable = false) private Long testPlanId;
    @Column(nullable = false, length = 20) private String result = "PENDING";
    @Column(name = "tested_by_id") private Long testedById;
    @Column(name = "tested_at", nullable = false) private LocalDateTime testedAt = LocalDateTime.now();
    @Column(columnDefinition = "TEXT") private String notes;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves test plan id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTestPlanId() { return testPlanId; } public void setTestPlanId(Long v) { this.testPlanId = v; }
    /**
     * Retrieves result data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getResult() { return result; } public void setResult(String v) { this.result = v; }
    /**
     * Retrieves a single tested by id by its identifier.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTestedById() { return testedById; } public void setTestedById(Long v) { this.testedById = v; }
    /**
     * Retrieves tested at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTestedAt() { return testedAt; } public void setTestedAt(LocalDateTime v) { this.testedAt = v; }
    /**
     * Retrieves notes data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; } public void setNotes(String v) { this.notes = v; }
}