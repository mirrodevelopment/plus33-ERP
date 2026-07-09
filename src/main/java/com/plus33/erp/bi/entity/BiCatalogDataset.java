/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiCatalogDataset.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCatalogDatasetController
 * Related Service   : BiCatalogDatasetService, BiCatalogDatasetServiceImpl
 * Related Repository: BiCatalogDatasetRepository
 * Related Entity    : BiCatalogDataset
 * Related DTO       : N/A
 * Related Mapper    : BiCatalogDatasetMapper
 * Related DB Table  : bi_catalog_dataset
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCatalogDatasetRepository, BiCatalogDatasetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_catalog_dataset'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiCatalogDataset}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_catalog_dataset'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_catalog_dataset}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_catalog_dataset")
public class BiCatalogDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dataset_name", nullable = false, unique = true)
    private String datasetName;
    private String description;
    @Column(name = "owner_role", nullable = false)
    private String ownerRole;
    @Column(name = "steward_user")
    private String stewardUser;
    @Column(name = "certification_status", nullable = false)
    private String certificationStatus = "BRONZE";
    @Column(name = "last_certified_at")
    private LocalDateTime lastCertifiedAt;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves dataset name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDatasetName() { return datasetName; }
    /**
     * Performs the setDatasetName operation in this module.
     *
     * @param datasetName the datasetName input value
     */
    public void setDatasetName(String datasetName) { this.datasetName = datasetName; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves owner role data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwnerRole() { return ownerRole; }
    /**
     * Performs the setOwnerRole operation in this module.
     *
     * @param ownerRole the ownerRole input value
     */
    public void setOwnerRole(String ownerRole) { this.ownerRole = ownerRole; }
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
     * Retrieves certification status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCertificationStatus() { return certificationStatus; }
    /**
     * Performs the setCertificationStatus operation in this module.
     *
     * @param certificationStatus the certificationStatus input value
     */
    public void setCertificationStatus(String certificationStatus) { this.certificationStatus = certificationStatus; }
    /**
     * Retrieves last certified at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastCertifiedAt() { return lastCertifiedAt; }
    /**
     * Performs the setLastCertifiedAt operation in this module.
     *
     * @param lastCertifiedAt the lastCertifiedAt input value
     */
    public void setLastCertifiedAt(LocalDateTime lastCertifiedAt) { this.lastCertifiedAt = lastCertifiedAt; }
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
}