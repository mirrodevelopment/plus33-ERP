package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxOverrideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxOverrideRequestRepository extends JpaRepository<TaxOverrideRequest, Long> {
    List<TaxOverrideRequest> findByCompanyId(Long companyId);
    Optional<TaxOverrideRequest> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}
