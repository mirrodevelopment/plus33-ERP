/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationSaga.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSagaController
 * Related Service   : IntegrationSagaService, IntegrationSagaServiceImpl
 * Related Repository: IntegrationSagaRepository
 * Related Entity    : IntegrationSaga
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSagaMapper
 * Related DB Table  : integration_saga
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSagaRepository, IntegrationSagaMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_saga'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSaga}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_saga'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_saga}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_saga")
public class IntegrationSaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "saga_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String sagaCode;

    @Column(name = "saga_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sagaType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CREATED";

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves saga code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSagaCode() { return sagaCode; }
    /**
     * Performs the setSagaCode operation in this module.
     *
     * @param sagaCode the sagaCode input value
     */
    public void setSagaCode(String sagaCode) { this.sagaCode = sagaCode; }
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
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}