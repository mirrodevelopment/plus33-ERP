/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxCalendarRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalendarController
 * Related Service   : TaxCalendarService, TaxCalendarServiceImpl
 * Related Repository: TaxCalendarRepository
 * Related Entity    : TaxCalendar
 * Related DTO       : N/A
 * Related Mapper    : TaxCalendarMapper
 * Related DB Table  : tax_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxCalendarService, TaxCalendarServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_calendars' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalendarRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_calendars' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_calendars}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxCalendarRepository extends JpaRepository<TaxCalendar, Long> {
    List<TaxCalendar> findByCompanyId(Long companyId);
    
    Optional<TaxCalendar> findByCompanyIdAndFilingTypeAndPeriodStartAndPeriodEnd(
            Long companyId, String filingType, LocalDate periodStart, LocalDate periodEnd);
}