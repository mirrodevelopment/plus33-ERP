/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiAnalyticsRole.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAnalyticsRoleController
 * Related Service   : BiAnalyticsRoleService, BiAnalyticsRoleServiceImpl
 * Related Repository: BiAnalyticsRoleRepository
 * Related Entity    : BiAnalyticsRole
 * Related DTO       : N/A
 * Related Mapper    : BiAnalyticsRoleMapper
 * Related DB Table  : bi_analytics_role
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAnalyticsRoleRepository, BiAnalyticsRoleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_analytics_role'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiAnalyticsRole}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_analytics_role'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_analytics_role}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_analytics_role")
public class BiAnalyticsRole {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "role_code", nullable = false, unique = true, length = 100) private String roleCode;
    @Column(name = "role_name", nullable = false, length = 200) private String roleName;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(name = "is_active", nullable = false) private Boolean isActive = true;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves role code data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoleCode() { return roleCode; } public void setRoleCode(String v) { this.roleCode = v; }
    /**
     * Retrieves role name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoleName() { return roleName; } public void setRoleName(String v) { this.roleName = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
    /**
     * Retrieves is active data from the database.
     *
     * @param v the v input value
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsActive() { return isActive; } public void setIsActive(Boolean v) { this.isActive = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}