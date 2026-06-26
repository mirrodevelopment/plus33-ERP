package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.ExchangeRate;
import com.plus33.erp.finance.reporting.entity.ExchangeRateType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findFirstByCompanyIdAndFromCurrencyAndToCurrencyAndRateTypeAndEffectiveDateLessThanEqualOrderByEffectiveDateDescIdDesc(
        Long companyId, 
        String fromCurrency, 
        String toCurrency, 
        ExchangeRateType rateType, 
        LocalDate effectiveDate
    );
}
