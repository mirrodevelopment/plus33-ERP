/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseRetentionPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseRetentionPolicyController
 * Related Service   : WarehouseRetentionPolicyService, WarehouseRetentionPolicyServiceImpl
 * Related Repository: WarehouseRetentionPolicyRepository
 * Related Entity    : WarehouseRetentionPolicy
 * Related DTO       : N/A
 * Related Mapper    : WarehouseRetentionPolicyMapper
 * Related DB Table  : warehouse_retention_policies
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseRetentionPolicyRepository, WarehouseRetentionPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_retention_policies'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRetentionPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_retention_policies'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_retention_policies}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_retention_policies")
public class WarehouseRetentionPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "entity_name", nullable = false, unique = true, length = 50)
    private String entityName;

    @Column(name = "archive_after_days", nullable = false)
    private int archiveAfterDays = 365;

    @Column(name = "purge_after_days", nullable = false)
    private int purgeAfterDays = 730;

    @Column(name = "compression_policy", length = 30)
    private String compressionPolicy = "STANDARD";

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
     * Retrieves entity name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEntityName() { return entityName; }
    /**
     * Performs the setEntityName operation in this module.
     *
     * @param entityName the entityName input value
     */
    public void setEntityName(String entityName) { this.entityName = entityName; }
    /**
     * Retrieves archive after days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getArchiveAfterDays() { return archiveAfterDays; }
    /**
     * Performs the setArchiveAfterDays operation in this module.
     *
     * @param archiveAfterDays the archiveAfterDays input value
     */
    public void setArchiveAfterDays(int archiveAfterDays) { this.archiveAfterDays = archiveAfterDays; }
    /**
     * Retrieves purge after days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPurgeAfterDays() { return purgeAfterDays; }
    /**
     * Performs the setPurgeAfterDays operation in this module.
     *
     * @param purgeAfterDays the purgeAfterDays input value
     */
    public void setPurgeAfterDays(int purgeAfterDays) { this.purgeAfterDays = purgeAfterDays; }
    /**
     * Retrieves compression policy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCompressionPolicy() { return compressionPolicy; }
    /**
     * Performs the setCompressionPolicy operation in this module.
     *
     * @param compressionPolicy the compressionPolicy input value
     */
    public void setCompressionPolicy(String compressionPolicy) { this.compressionPolicy = compressionPolicy; }
}