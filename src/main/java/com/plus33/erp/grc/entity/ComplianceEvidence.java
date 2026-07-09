/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : ComplianceEvidence.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceEvidenceController
 * Related Service   : ComplianceEvidenceService, ComplianceEvidenceServiceImpl
 * Related Repository: ComplianceEvidenceRepository
 * Related Entity    : ComplianceEvidence
 * Related DTO       : N/A
 * Related Mapper    : ComplianceEvidenceMapper
 * Related DB Table  : grc_compliance_evidence
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceEvidenceRepository, ComplianceEvidenceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_compliance_evidence'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceEvidence}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_compliance_evidence'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_compliance_evidence}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_compliance_evidence")
public class ComplianceEvidence {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "reference_type", nullable = false, length = 50) private String referenceType;
    @Column(name = "reference_id", nullable = false) private Long referenceId;
    @Column(name = "file_name", nullable = false, length = 255) private String fileName;
    @Column(name = "content_hash", nullable = false, unique = true, length = 64) private String contentHash;
    @Column(name = "evidence_source", length = 100) private String evidenceSource;
    @Column(name = "uploaded_by_id", nullable = false) private Long uploadedById;
    @Column(name = "verified_by_id") private Long verifiedById;
    @Column(name = "verification_date") private LocalDate verificationDate;
    @Column(name = "retention_policy", nullable = false, length = 50) private String retentionPolicy = "7_YEARS";
    @Column(name = "uploaded_at", nullable = false, updatable = false) private LocalDateTime uploadedAt = LocalDateTime.now();
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves reference type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceType() { return referenceType; } public void setReferenceType(String v) { this.referenceType = v; }
    /**
     * Retrieves reference id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; } public void setReferenceId(Long v) { this.referenceId = v; }
    /**
     * Retrieves file name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFileName() { return fileName; } public void setFileName(String v) { this.fileName = v; }
    /**
     * Retrieves content hash data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContentHash() { return contentHash; } public void setContentHash(String v) { this.contentHash = v; }
    /**
     * Retrieves evidence source data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEvidenceSource() { return evidenceSource; } public void setEvidenceSource(String v) { this.evidenceSource = v; }
    /**
     * Retrieves a single uploaded by id by its identifier.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUploadedById() { return uploadedById; } public void setUploadedById(Long v) { this.uploadedById = v; }
    /**
     * Retrieves a single verified by id by its identifier.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVerifiedById() { return verifiedById; } public void setVerifiedById(Long v) { this.verifiedById = v; }
    /**
     * Retrieves verification date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getVerificationDate() { return verificationDate; } public void setVerificationDate(LocalDate v) { this.verificationDate = v; }
    /**
     * Retrieves retention policy data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRetentionPolicy() { return retentionPolicy; } public void setRetentionPolicy(String v) { this.retentionPolicy = v; }
    /**
     * Retrieves uploaded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}