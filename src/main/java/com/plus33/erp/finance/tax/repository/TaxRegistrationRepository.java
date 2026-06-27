package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxRegistrationRepository extends JpaRepository<TaxRegistration, Long> {
    List<TaxRegistration> findByEntityTypeAndEntityIdAndActiveTrue(String entityType, Long entityId);
    
    Optional<TaxRegistration> findByEntityTypeAndEntityIdAndTaxSchemeAndActiveTrue(String entityType, Long entityId, String taxScheme);
}
