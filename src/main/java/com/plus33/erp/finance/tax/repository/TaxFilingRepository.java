package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxFiling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxFilingRepository extends JpaRepository<TaxFiling, Long> {
    Optional<TaxFiling> findByCalendarId(Long calendarId);
}
