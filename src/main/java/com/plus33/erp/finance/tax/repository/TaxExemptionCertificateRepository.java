package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxExemptionCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
