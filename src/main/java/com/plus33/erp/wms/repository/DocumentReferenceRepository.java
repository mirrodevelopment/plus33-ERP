/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : DocumentReferenceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
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
 * Used By           : DocumentReferenceService, DocumentReferenceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'document_references' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.DocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code DocumentReferenceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'document_references' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code document_references}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, Long> {
    List<DocumentReference> findByCompanyIdAndDocumentTypeAndDocumentNumber(Long companyId, String documentType, String documentNumber);
}
