/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : DocumentReference.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DocumentReferenceController
 * Related Service   : DocumentReferenceService, DocumentReferenceServiceImpl
 * Related Repository: DocumentReferenceRepository
 * Related Entity    : DocumentReference
 * Related DTO       : N/A
 * Related Mapper    : DocumentReferenceMapper
 * Related DB Table  : document_references
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DocumentReferenceRepository, DocumentReferenceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'document_references'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code DocumentReference}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'document_references'.</p>
 *
 * <p><b>Database Table   :</b> {@code document_references}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "document_references")
public class DocumentReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "document_type", nullable = false, length = 30)
    private String documentType;

    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(nullable = false, length = 30)
    private String module;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves document type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDocumentType() { return documentType; }
    /**
     * Performs the setDocumentType operation in this module.
     *
     * @param documentType the documentType input value
     */
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    /**
     * Retrieves document number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDocumentNumber() { return documentNumber; }
    /**
     * Performs the setDocumentNumber operation in this module.
     *
     * @param documentNumber the documentNumber input value
     */
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    /**
     * Retrieves document id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDocumentId() { return documentId; }
    /**
     * Performs the setDocumentId operation in this module.
     *
     * @param documentId the documentId input value
     */
    public void setDocumentId(Long documentId) { this.documentId = documentId; }
    /**
     * Retrieves module data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getModule() { return module; }
    /**
     * Performs the setModule operation in this module.
     *
     * @param module the module input value
     */
    public void setModule(String module) { this.module = module; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}