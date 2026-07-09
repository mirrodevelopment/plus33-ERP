/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : DocumentExpiryEngine.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DocumentExpiryEngineController
 * Related Service   : DocumentExpiryEngine
 * Related Repository: EmployeeDocumentRepository
 * Related Entity    : DocumentExpiryEngine
 * Related DTO       : N/A
 * Related Mapper    : DocumentExpiryEngineMapper
 * Related DB Table  : document_expiry_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DocumentExpiryEngineController, DocumentExpiryEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements DocumentExpiryEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.EmployeeDocument;
import com.plus33.erp.hcm.repository.EmployeeDocumentRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code DocumentExpiryEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DocumentExpiryEngineController
 *   --> DocumentExpiryEngine (this)
 *   --> Validate business rules
 *   --> DocumentExpiryEngineRepository (read/write 'document_expiry_engines')
 *   --> DocumentExpiryEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code document_expiry_engines}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DocumentExpiryEngine {

    private final EmployeeDocumentRepository documentRepository;
    private final HcmEventBus eventBus;

    public DocumentExpiryEngine(EmployeeDocumentRepository documentRepository, HcmEventBus eventBus) {
        this.documentRepository = documentRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the scanAndFlagExpirations operation in this module.
     *
     * @param employeeId the employeeId input value
     */
    @Transactional
    public void scanAndFlagExpirations(Long employeeId) {
        List<EmployeeDocument> docs = documentRepository.findByEmployeeId(employeeId);
        LocalDate warningThreshold = LocalDate.now().plusDays(30);

        for (EmployeeDocument doc : docs) {
            if (doc.getExpiryDate().isBefore(LocalDate.now())) {
                doc.setNotified(true);
                documentRepository.save(doc);
                eventBus.publish("CertificationExpired", 1L, employeeId, doc.getDocumentType() + " is expired!");
            } else if (doc.getExpiryDate().isBefore(warningThreshold)) {
                doc.setNotified(true);
                documentRepository.save(doc);
                eventBus.publish("DocumentExpiring", 1L, employeeId, doc.getDocumentType() + " is expiring soon!");
            }
        }
    }
}