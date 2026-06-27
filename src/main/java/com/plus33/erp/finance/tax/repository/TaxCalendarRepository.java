package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaxCalendarRepository extends JpaRepository<TaxCalendar, Long> {
    List<TaxCalendar> findByCompanyId(Long companyId);
    
    Optional<TaxCalendar> findByCompanyIdAndFilingTypeAndPeriodStartAndPeriodEnd(
            Long companyId, String filingType, LocalDate periodStart, LocalDate periodEnd);
}
