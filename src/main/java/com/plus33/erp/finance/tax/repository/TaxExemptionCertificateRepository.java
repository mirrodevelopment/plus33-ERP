/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxExemptionCertificateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxExemptionCertificateController
 * Related Service   : TaxExemptionCertificateService, TaxExemptionCertificateServiceImpl
 * Related Repository: TaxExemptionCertificateRepository
 * Related Entity    : TaxExemptionCertificate
 * Related DTO       : N/A
 * Related Mapper    : TaxExemptionCertificateMapper
 * Related DB Table  : tax_exemption_certificates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxExemptionCertificateService, TaxExemptionCertificateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_exemption_certificates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxExemptionCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxExemptionCertificateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_exemption_certificates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_exemption_certificates}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxExemptionCertificateRepository extends JpaRepository<TaxExemptionCertificate, Long> {
    Optional<TaxExemptionCertificate> findByCompanyIdAndCustomerIdAndCertificateNumberAndActiveTrue(Long companyId, Long customerId, String certificateNumber);

    List<TaxExemptionCertificate> findByCompanyIdAndCustomerIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            Long companyId, Long customerId, LocalDate date1, LocalDate date2);

    default List<TaxExemptionCertificate> findActiveCertificates(Long companyId, Long customerId, LocalDate date) {
        return findByCompanyIdAndCustomerIdAndActiveTrueAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
                companyId, customerId, date, date);
    }
}