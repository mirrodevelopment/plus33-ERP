/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmMergeRequest.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmMergeController
 * Related Service   : MdmMergeService, MdmMergeServiceImpl
 * Related Repository: MdmMergeRepository
 * Related Entity    : MdmMergeRequest
 * Related DTO       : MdmMergeRequest
 * Related Mapper    : MdmMergeMapper
 * Related DB Table  : bi_mdm_merge_request
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmMergeRepository, MdmMergeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_merge_request'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmMergeRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_merge_request'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_merge_request}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_merge_request")
public class MdmMergeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @Column(name = "request_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String requestCode;

    @Column(name = "record_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String recordType;

    @Column(name = "source_system_a", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemA;

    @Column(name = "source_dim_id_a", nullable = false)
    @NotNull
    private Long sourceDimIdA;

    @Column(name = "source_system_b", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemB;

    @Column(name = "source_dim_id_b", nullable = false)
    @NotNull
    private Long sourceDimIdB;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "REQUESTED";

    @Column(name = "requested_by", nullable = false)
    @NotNull
    @Size(max = 100)
    private String requestedBy;

    private String comments;

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
     * Retrieves request code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequestCode() { return requestCode; }
    /**
     * Performs the setRequestCode operation in this module.
     *
     * @param requestCode the requestCode input value
     */
    public void setRequestCode(String requestCode) { this.requestCode = requestCode; }
    /**
     * Retrieves record type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecordType() { return recordType; }
    /**
     * Performs the setRecordType operation in this module.
     *
     * @param recordType the recordType input value
     */
    public void setRecordType(String recordType) { this.recordType = recordType; }
    /**
     * Retrieves source system a data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystemA() { return sourceSystemA; }
    /**
     * Performs the setSourceSystemA operation in this module.
     *
     * @param sourceSystemA the sourceSystemA input value
     */
    public void setSourceSystemA(String sourceSystemA) { this.sourceSystemA = sourceSystemA; }
    /**
     * Retrieves source dim id a data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceDimIdA() { return sourceDimIdA; }
    /**
     * Performs the setSourceDimIdA operation in this module.
     *
     * @param sourceDimIdA the sourceDimIdA input value
     */
    public void setSourceDimIdA(Long sourceDimIdA) { this.sourceDimIdA = sourceDimIdA; }
    /**
     * Retrieves source system b data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystemB() { return sourceSystemB; }
    /**
     * Performs the setSourceSystemB operation in this module.
     *
     * @param sourceSystemB the sourceSystemB input value
     */
    public void setSourceSystemB(String sourceSystemB) { this.sourceSystemB = sourceSystemB; }
    /**
     * Retrieves source dim id b data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceDimIdB() { return sourceDimIdB; }
    /**
     * Performs the setSourceDimIdB operation in this module.
     *
     * @param sourceDimIdB the sourceDimIdB input value
     */
    public void setSourceDimIdB(Long sourceDimIdB) { this.sourceDimIdB = sourceDimIdB; }
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
     * Retrieves requested by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequestedBy() { return requestedBy; }
    /**
     * Performs the setRequestedBy operation in this module.
     *
     * @param requestedBy the requestedBy input value
     */
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    /**
     * Retrieves comments data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComments() { return comments; }
    /**
     * Performs the setComments operation in this module.
     *
     * @param comments the comments input value
     */
    public void setComments(String comments) { this.comments = comments; }
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