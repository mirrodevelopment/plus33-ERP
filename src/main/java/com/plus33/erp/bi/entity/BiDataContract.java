/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiDataContract.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDataContractController
 * Related Service   : BiDataContractService, BiDataContractServiceImpl
 * Related Repository: BiDataContractRepository
 * Related Entity    : BiDataContract
 * Related DTO       : N/A
 * Related Mapper    : BiDataContractMapper
 * Related DB Table  : bi_data_contract
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDataContractRepository, BiDataContractMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_data_contract'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDataContract}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_data_contract'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_data_contract}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_data_contract")
public class BiDataContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "contract_name", nullable = false, unique = true)
    private String contractName;
    @Column(name = "version_offset", nullable = false)
    private Integer versionOffset = 1;
    @Column(name = "schema_definition", nullable = false)
    private String schemaDefinition;
    @Column(name = "compatibility_level", nullable = false)
    private String compatibilityLevel = "FULL";
    @Column(nullable = false)
    private String status = "DRAFT";
    @Column(name = "created_by", nullable = false)
    private String createdBy;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at", nullable = false)
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
     * Retrieves contract name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContractName() { return contractName; }
    /**
     * Performs the setContractName operation in this module.
     *
     * @param contractName the contractName input value
     */
    public void setContractName(String contractName) { this.contractName = contractName; }
    /**
     * Retrieves version offset data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionOffset() { return versionOffset; }
    /**
     * Performs the setVersionOffset operation in this module.
     *
     * @param versionOffset the versionOffset input value
     */
    public void setVersionOffset(Integer versionOffset) { this.versionOffset = versionOffset; }
    /**
     * Retrieves schema definition data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSchemaDefinition() { return schemaDefinition; }
    /**
     * Performs the setSchemaDefinition operation in this module.
     *
     * @param schemaDefinition the schemaDefinition input value
     */
    public void setSchemaDefinition(String schemaDefinition) { this.schemaDefinition = schemaDefinition; }
    /**
     * Retrieves compatibility level data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompatibilityLevel() { return compatibilityLevel; }
    /**
     * Performs the setCompatibilityLevel operation in this module.
     *
     * @param compatibilityLevel the compatibilityLevel input value
     */
    public void setCompatibilityLevel(String compatibilityLevel) { this.compatibilityLevel = compatibilityLevel; }
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
     * Retrieves created by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
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