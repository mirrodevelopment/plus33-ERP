package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.FiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long> {
    Optional<FiscalYear> findByCompanyIdAndFiscalYear(Long companyId, Integer fiscalYear);
    boolean existsByCompanyIdAndFiscalYearAndStatus(Long companyId, Integer fiscalYear, com.plus33.erp.finance.reporting.entity.FiscalYearStatus status);
}
