/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : EmployeeDocumentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeDocumentController
 * Related Service   : EmployeeDocumentService, EmployeeDocumentServiceImpl
 * Related Repository: EmployeeDocumentRepository
 * Related Entity    : EmployeeDocument
 * Related DTO       : N/A
 * Related Mapper    : EmployeeDocumentMapper
 * Related DB Table  : employee_documents
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeDocumentService, EmployeeDocumentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'employee_documents' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.EmployeeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeDocumentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employee_documents' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_documents}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {
    List<EmployeeDocument> findByEmployeeId(Long employeeId);
}
