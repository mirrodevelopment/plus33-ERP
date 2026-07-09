/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : EmployeeDocument.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeDocumentController
 * Related Service   : EmployeeDocumentService, EmployeeDocumentServiceImpl
 * Related Repository: EmployeeDocumentRepository
 * Related Entity    : EmployeeDocument
 * Related DTO       : N/A
 * Related Mapper    : EmployeeDocumentMapper
 * Related DB Table  : hcm_employee_documents
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeDocumentRepository, EmployeeDocumentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_employee_documents'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeDocument}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_employee_documents'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_employee_documents}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_employee_documents")
public class EmployeeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Boolean notified = false;

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
     * Retrieves employee id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEmployeeId() { return employeeId; }
    /**
     * Performs the setEmployeeId operation in this module.
     *
     * @param employeeId the employeeId input value
     */
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
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
     * Retrieves expiry date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExpiryDate() { return expiryDate; }
    /**
     * Performs the setExpiryDate operation in this module.
     *
     * @param expiryDate the expiryDate input value
     */
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    /**
     * Retrieves notified data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getNotified() { return notified; }
    /**
     * Performs the setNotified operation in this module.
     *
     * @param notified the notified input value
     */
    public void setNotified(Boolean notified) { this.notified = notified; }
}