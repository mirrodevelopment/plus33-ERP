/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.repository
 * File              : ExchangeRateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ExchangeRateController
 * Related Service   : ExchangeRateService, ExchangeRateServiceImpl
 * Related Repository: ExchangeRateRepository
 * Related Entity    : ExchangeRate
 * Related DTO       : N/A
 * Related Mapper    : ExchangeRateMapper
 * Related DB Table  : exchange_rates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ExchangeRateService, ExchangeRateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'exchange_rates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.repository;

import com.plus33.erp.finance.reporting.entity.ExchangeRate;
import com.plus33.erp.finance.reporting.entity.ExchangeRateType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ExchangeRateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'exchange_rates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code exchange_rates}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findFirstByCompanyIdAndFromCurrencyAndToCurrencyAndRateTypeAndEffectiveDateLessThanEqualOrderByEffectiveDateDescIdDesc(
        Long companyId, 
        String fromCurrency, 
        String toCurrency, 
        ExchangeRateType rateType, 
        LocalDate effectiveDate
    );
}
