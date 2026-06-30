package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.DocumentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentReferenceRepository extends JpaRepository<DocumentReference, Long> {
    List<DocumentReference> findByCompanyIdAndDocumentTypeAndDocumentNumber(Long companyId, String documentType, String documentNumber);
}
