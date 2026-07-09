/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : ManufacturingCalendarService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarController
 * Related Service   : ManufacturingCalendarService, ManufacturingCalendarServiceImpl
 * Related Repository: ManufacturingCalendarRepository
 * Related Entity    : ManufacturingCalendar
 * Related DTO       : CalendarDto, CalendarExceptionDto, CreateCalendarRequest, CreateExceptionRequest, CreateShiftRequest
 * Related Mapper    : ManufacturingCalendarMapper
 * Related DB Table  : manufacturing_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingCalendarService {
    CalendarDto createCalendar(CreateCalendarRequest request);
    ShiftDto addShift(Long calendarId, CreateShiftRequest request);
    CalendarExceptionDto addException(Long calendarId, CreateExceptionRequest request);
    BigDecimal getAvailableHours(Long companyId, String resourceType, Long resourceId, LocalDate date);
    CalendarDto getCalendarById(Long calendarId);
    List<CalendarDto> getCalendarsByCompany(Long companyId);
}
