package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.EmployeeDocument;
import com.plus33.erp.hcm.repository.EmployeeDocumentRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentExpiryEngine {

    private final EmployeeDocumentRepository documentRepository;
    private final HcmEventBus eventBus;

    public DocumentExpiryEngine(EmployeeDocumentRepository documentRepository, HcmEventBus eventBus) {
        this.documentRepository = documentRepository;
        this.eventBus = eventBus;
    }

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
