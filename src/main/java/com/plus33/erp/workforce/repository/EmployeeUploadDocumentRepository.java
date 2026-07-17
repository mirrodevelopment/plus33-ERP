package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeUploadDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for database CRUD operations on the 'employee_upload_documents' table.
 * 
 * WHAT IT DOES:
 * Exposes Spring Data JPA database query methods.
 * 
 * WHERE DATA IS STORED:
 * Maps query operations to read/write records in the "employee_upload_documents" table in PostgreSQL database.
 */
@Repository
public interface EmployeeUploadDocumentRepository extends JpaRepository<EmployeeUploadDocument, Long> {
    // Reads all documents belonging to a specific employee ID from the database
    List<EmployeeUploadDocument> findByEmployeeId(Long employeeId);
    
    // Reads a single document by employee ID and document category type (e.g. panCard)
    Optional<EmployeeUploadDocument> findByEmployeeIdAndDocumentType(Long employeeId, String documentType);
    
    // Deletes document metadata records matching employee ID and document type category
    void deleteByEmployeeIdAndDocumentType(Long employeeId, String documentType);

    // Reads all documents matching specific approved status (e.g. false for pending verification)
    List<EmployeeUploadDocument> findByApproved(boolean approved);
}
