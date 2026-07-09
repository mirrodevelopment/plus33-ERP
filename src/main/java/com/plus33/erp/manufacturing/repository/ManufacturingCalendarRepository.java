/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ManufacturingCalendarRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarController
 * Related Service   : ManufacturingCalendarService, ManufacturingCalendarServiceImpl
 * Related Repository: ManufacturingCalendarRepository
 * Related Entity    : ManufacturingCalendar
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingCalendarMapper
 * Related DB Table  : manufacturing_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarService, ManufacturingCalendarServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'manufacturing_calendars' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'manufacturing_calendars' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendars}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingCalendarRepository extends JpaRepository<ManufacturingCalendar, Long> {
    List<ManufacturingCalendar> findByCompanyId(Long companyId);
    Optional<ManufacturingCalendar> findByCompanyIdAndCalendarTypeAndCode(Long companyId, String calendarType, String code);
    List<ManufacturingCalendar> findByCompanyIdAndReferenceTypeAndReferenceId(Long companyId, String referenceType, Long referenceId);
}
