/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : ServiceSurvey.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceSurveyController
 * Related Service   : ServiceSurveyService, ServiceSurveyServiceImpl
 * Related Repository: ServiceSurveyRepository
 * Related Entity    : ServiceSurvey
 * Related DTO       : N/A
 * Related Mapper    : ServiceSurveyMapper
 * Related DB Table  : esm_surveys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceSurveyRepository, ServiceSurveyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_surveys'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceSurvey}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_surveys'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_surveys}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_surveys")
public class ServiceSurvey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_order_id", nullable = false, unique = true)
    private Long workOrderId;

    @Column(name = "csat_score", nullable = false)
    private Integer csatScore;

    @Column(name = "nps_score", nullable = false)
    private Integer npsScore;

    @Column(name = "ces_score", nullable = false)
    private Integer cesScore;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
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
     * Retrieves work order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWorkOrderId() { return workOrderId; }
    /**
     * Performs the setWorkOrderId operation in this module.
     *
     * @param workOrderId the workOrderId input value
     */
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    /**
     * Retrieves csat score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCsatScore() { return csatScore; }
    /**
     * Performs the setCsatScore operation in this module.
     *
     * @param csatScore the csatScore input value
     */
    public void setCsatScore(Integer csatScore) { this.csatScore = csatScore; }
    /**
     * Retrieves nps score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getNpsScore() { return npsScore; }
    /**
     * Performs the setNpsScore operation in this module.
     *
     * @param npsScore the npsScore input value
     */
    public void setNpsScore(Integer npsScore) { this.npsScore = npsScore; }
    /**
     * Retrieves ces score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCesScore() { return cesScore; }
    /**
     * Performs the setCesScore operation in this module.
     *
     * @param cesScore the cesScore input value
     */
    public void setCesScore(Integer cesScore) { this.cesScore = cesScore; }
    /**
     * Retrieves comments data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComments() { return comments; }
    /**
     * Performs the setComments operation in this module.
     *
     * @param comments the comments input value
     */
    public void setComments(String comments) { this.comments = comments; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}