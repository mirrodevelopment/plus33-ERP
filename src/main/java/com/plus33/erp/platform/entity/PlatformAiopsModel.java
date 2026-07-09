/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAiopsModel.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAiopsModelController
 * Related Service   : PlatformAiopsModelService, PlatformAiopsModelServiceImpl
 * Related Repository: PlatformAiopsModelRepository
 * Related Entity    : PlatformAiopsModel
 * Related DTO       : N/A
 * Related Mapper    : PlatformAiopsModelMapper
 * Related DB Table  : platform_aiops_model
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAiopsModelRepository, PlatformAiopsModelMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_aiops_model'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAiopsModel}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_aiops_model'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_aiops_model}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_aiops_model")
public class PlatformAiopsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "model_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String modelName;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal accuracy;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

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
     * Retrieves model name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModelName() { return modelName; }
    /**
     * Performs the setModelName operation in this module.
     *
     * @param modelName the modelName input value
     */
    public void setModelName(String modelName) { this.modelName = modelName; }
    /**
     * Retrieves accuracy data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAccuracy() { return accuracy; }
    /**
     * Performs the setAccuracy operation in this module.
     *
     * @param accuracy the accuracy input value
     */
    public void setAccuracy(BigDecimal accuracy) { this.accuracy = accuracy; }
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