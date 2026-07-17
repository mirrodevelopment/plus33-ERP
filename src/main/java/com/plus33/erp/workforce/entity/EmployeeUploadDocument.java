package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity representing the uploaded onboarding/identity verification documents of employees.
 * 
 * WHAT IT DOES: 
 * Maps persistence fields to track document metadata uploaded by the active employee (e.g. PAN card, Aadhaar, contract).
 * 
 * STORAGE: 
 * Stores data in the "employee_upload_documents" table in the PostgreSQL database.
 * 
 * DATA FLOW:
 * - Collected in frontend profile page (profile.js).
 * - Sent as POST request to WebServer (/api/upload-document) for physical folder saving.
 * - URL path is sent to REST endpoint (/api/v2/employee-self-service/documents) which saves records here.
 */
@Entity
@Table(name = "employee_upload_documents")
public class EmployeeUploadDocument {

    // Primary key identifier mapped to database column "id" (BIGSERIAL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key identifier referencing the employee record (employees.id) in PostgreSQL
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    // Type identifier (e.g., 'panCard', 'aadhaarCard', 'workPermit', 'contract')
    @Column(name = "document_type", nullable = false, length = 100)
    private String documentType;

    // Original uploaded filename (e.g., 'pan_card_doc.pdf')
    @Column(name = "document_name", nullable = false, length = 255)
    private String documentName;

    // Relative web URL file path referencing the physical file location (e.g., 'user_uploads/documents/...')
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    // Timestamp when the record was successfully committed to database
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    // Approval status flag indicating whether the document has been approved by the Store Admin
    @Column(name = "approved", nullable = false)
    private boolean approved = false;

    // Lifecycle hook: auto-assigns timestamp before saving the document metadata
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
