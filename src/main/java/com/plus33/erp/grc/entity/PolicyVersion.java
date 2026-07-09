/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : PolicyVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyVersionController
 * Related Service   : PolicyVersionService, PolicyVersionServiceImpl
 * Related Repository: PolicyVersionRepository
 * Related Entity    : PolicyVersion
 * Related DTO       : N/A
 * Related Mapper    : PolicyVersionMapper
 * Related DB Table  : grc_policy_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyVersionRepository, PolicyVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_policy_versions'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_policy_versions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"policy_id", "version_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code PolicyVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_policy_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_policy_versions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class PolicyVersion {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "policy_id", nullable = false) private Long policyId;
    @Column(name = "version_number", nullable = false) private Integer versionNumber;
    @Column(name = "content_hash", nullable = false, length = 64) private String contentHash;
    @Column(name = "approved_at") private LocalDateTime approvedAt;
    @Column(name = "published_at") private LocalDateTime publishedAt;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves policy id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; } public void setPolicyId(Long v) { this.policyId = v; }
    /**
     * Retrieves version number data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; } public void setVersionNumber(Integer v) { this.versionNumber = v; }
    /**
     * Retrieves content hash data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContentHash() { return contentHash; } public void setContentHash(String v) { this.contentHash = v; }
    /**
     * Retrieves approved at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; } public void setApprovedAt(LocalDateTime v) { this.approvedAt = v; }
    /**
     * Retrieves published at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPublishedAt() { return publishedAt; } public void setPublishedAt(LocalDateTime v) { this.publishedAt = v; }
}