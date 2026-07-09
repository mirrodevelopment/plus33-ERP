/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiSelfServiceWorkspace.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiSelfServiceWorkspaceController
 * Related Service   : BiSelfServiceWorkspaceService, BiSelfServiceWorkspaceServiceImpl
 * Related Repository: BiSelfServiceWorkspaceRepository
 * Related Entity    : BiSelfServiceWorkspace
 * Related DTO       : N/A
 * Related Mapper    : BiSelfServiceWorkspaceMapper
 * Related DB Table  : bi_self_service_workspace
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiSelfServiceWorkspaceRepository, BiSelfServiceWorkspaceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_self_service_workspace'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiSelfServiceWorkspace}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_self_service_workspace'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_self_service_workspace}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_self_service_workspace")
public class BiSelfServiceWorkspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "workspace_code", nullable = false, unique = true)
    private String workspaceCode;
    @Column(name = "owner_user", nullable = false)
    private String ownerUser;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "workspace_name", nullable = false)
    private String workspaceName;
    @Column(name = "config_json", nullable = false)
    private String configJson;
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
     * Retrieves workspace code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkspaceCode() { return workspaceCode; }
    /**
     * Performs the setWorkspaceCode operation in this module.
     *
     * @param workspaceCode the workspaceCode input value
     */
    public void setWorkspaceCode(String workspaceCode) { this.workspaceCode = workspaceCode; }
    /**
     * Retrieves owner user data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwnerUser() { return ownerUser; }
    /**
     * Performs the setOwnerUser operation in this module.
     *
     * @param ownerUser the ownerUser input value
     */
    public void setOwnerUser(String ownerUser) { this.ownerUser = ownerUser; }
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
     * Retrieves workspace name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkspaceName() { return workspaceName; }
    /**
     * Performs the setWorkspaceName operation in this module.
     *
     * @param workspaceName the workspaceName input value
     */
    public void setWorkspaceName(String workspaceName) { this.workspaceName = workspaceName; }
    /**
     * Retrieves config json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfigJson() { return configJson; }
    /**
     * Performs the setConfigJson operation in this module.
     *
     * @param configJson the configJson input value
     */
    public void setConfigJson(String configJson) { this.configJson = configJson; }
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