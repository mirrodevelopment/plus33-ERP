/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseSagaState.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseSagaStateController
 * Related Service   : WarehouseSagaStateService, WarehouseSagaStateServiceImpl
 * Related Repository: WarehouseSagaStateRepository
 * Related Entity    : WarehouseSagaState
 * Related DTO       : N/A
 * Related Mapper    : WarehouseSagaStateMapper
 * Related DB Table  : warehouse_saga_states
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseSagaStateRepository, WarehouseSagaStateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_saga_states'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseSagaState}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_saga_states'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_saga_states}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_saga_states")
public class WarehouseSagaState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "saga_type", nullable = false, length = 50)
    private String sagaType;

    @Column(name = "correlation_id", nullable = false, unique = true, length = 100)
    private String correlationId;

    @Column(name = "current_step", nullable = false, length = 50)
    private String currentStep;

    @Column(nullable = false, length = 30)
    private String status = "IN_PROGRESS";

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves saga type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSagaType() { return sagaType; }
    /**
     * Performs the setSagaType operation in this module.
     *
     * @param sagaType the sagaType input value
     */
    public void setSagaType(String sagaType) { this.sagaType = sagaType; }
    /**
     * Retrieves correlation id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrelationId() { return correlationId; }
    /**
     * Performs the setCorrelationId operation in this module.
     *
     * @param correlationId the correlationId input value
     */
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    /**
     * Retrieves current step data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCurrentStep() { return currentStep; }
    /**
     * Performs the setCurrentStep operation in this module.
     *
     * @param currentStep the currentStep input value
     */
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
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
     * Retrieves payload json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadJson() { return payloadJson; }
    /**
     * Performs the setPayloadJson operation in this module.
     *
     * @param payloadJson the payloadJson input value
     */
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}