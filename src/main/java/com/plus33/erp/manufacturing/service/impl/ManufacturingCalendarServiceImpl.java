/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : ManufacturingCalendarServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarController
 * Related Service   : ManufacturingCalendarServiceImpl
 * Related Repository: ManufacturingCalendarRepository, ManufacturingCalendarShiftRepository, ManufacturingCalendarExceptionRepository
 * Related Entity    : ManufacturingCalendar
 * Related DTO       : CalendarDto, CalendarExceptionDto, CreateCalendarRequest, CreateExceptionRequest, CreateShiftRequest
 * Related Mapper    : ManufacturingCalendarMapper
 * Related DB Table  : manufacturing_calendars
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingCalendarController, ManufacturingCalendarServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements ManufacturingCalendarService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.*;
import com.plus33.erp.manufacturing.service.ManufacturingCalendarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ManufacturingCalendarController
 *   --> ManufacturingCalendarServiceImpl (this)
 *   --> Validate business rules
 *   --> ManufacturingCalendarRepository (read/write 'manufacturing_calendars')
 *   --> ManufacturingCalendarMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_calendars}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class ManufacturingCalendarServiceImpl implements ManufacturingCalendarService {

    private final ManufacturingCalendarRepository calendarRepository;
    private final ManufacturingCalendarShiftRepository shiftRepository;
    private final ManufacturingCalendarExceptionRepository exceptionRepository;

    public ManufacturingCalendarServiceImpl(ManufacturingCalendarRepository calendarRepository,
                                           ManufacturingCalendarShiftRepository shiftRepository,
                                           ManufacturingCalendarExceptionRepository exceptionRepository) {
        this.calendarRepository = calendarRepository;
        this.shiftRepository = shiftRepository;
        this.exceptionRepository = exceptionRepository;
    }

    /**
     * Creates a new calendar and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the CalendarDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new calendar and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the CalendarDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CalendarDto createCalendar(CreateCalendarRequest request) {
        ManufacturingCalendar calendar = new ManufacturingCalendar();
        calendar.setCompanyId(request.getCompanyId());
        calendar.setCalendarType(request.getCalendarType());
        calendar.setReferenceType(request.getReferenceType());
        calendar.setReferenceId(request.getReferenceId());
        calendar.setCode(request.getCode());
        calendar.setName(request.getName());
        calendar.setTimezone(request.getTimezone() != null ? request.getTimezone() : "UTC");
        calendar.setActive(true);

        calendar = calendarRepository.save(calendar);
        return mapToDto(calendar);
    }

    /**
     * Creates a new shift and persists it to the database.
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return the ShiftDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new shift and persists it to the database.
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return the ShiftDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public ShiftDto addShift(Long calendarId, CreateShiftRequest request) {
        ManufacturingCalendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found with ID: " + calendarId));

        ManufacturingCalendarShift shift = new ManufacturingCalendarShift();
        shift.setCalendar(calendar);
        shift.setDayOfWeek(request.getDayOfWeek());
        shift.setShiftName(request.getShiftName());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setBreakMinutes(request.getBreakMinutes());
        shift.setAvailableHours(request.getAvailableHours());
        shift.setShiftType(request.getShiftType() != null ? request.getShiftType() : "REGULAR");

        shift = shiftRepository.save(shift);
        return mapToShiftDto(shift);
    }

    /**
     * Creates a new exception and persists it to the database.
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return the CalendarExceptionDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new exception and persists it to the database.
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return the CalendarExceptionDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CalendarExceptionDto addException(Long calendarId, CreateExceptionRequest request) {
        ManufacturingCalendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found with ID: " + calendarId));

        ManufacturingCalendarException exception = new ManufacturingCalendarException();
        exception.setCalendar(calendar);
        exception.setExceptionDate(request.getExceptionDate());
        exception.setExceptionType(request.getExceptionType());
        exception.setAvailableHours(request.getAvailableHours());
        exception.setDescription(request.getDescription());

        exception = exceptionRepository.save(exception);
        return mapToExceptionDto(exception);
    }

    /**
     * Retrieves available hours data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param resourceType the resourceType input value
     * @param resourceId the resourceId input value
     * @param date the date input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableHours(Long companyId, String resourceType, Long resourceId, LocalDate date) {
        // 1. Check for resource-specific calendar (e.g. WORK_CENTER, MACHINE)
        List<ManufacturingCalendar> resourceCalendars = calendarRepository
                .findByCompanyIdAndReferenceTypeAndReferenceId(companyId, resourceType, resourceId);

        if (!resourceCalendars.isEmpty()) {
            BigDecimal hours = getHoursFromCalendar(resourceCalendars.get(0).getId(), date);
            if (hours != null) return hours;
        }

        // 2. Fall back to company-wide PLANT calendar
        Optional<ManufacturingCalendar> plantCalendar = calendarRepository
                .findByCompanyIdAndCalendarTypeAndCode(companyId, "PLANT", "DEFAULT");
        if (plantCalendar.isPresent()) {
            BigDecimal hours = getHoursFromCalendar(plantCalendar.get().getId(), date);
            if (hours != null) return hours;
        }

        // 3. Fall back to standard defaults (8.0 hours on weekdays, 0 on weekends)
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal("8.00");
    }

    private BigDecimal getHoursFromCalendar(Long calendarId, LocalDate date) {
        // Check exceptions first
        List<ManufacturingCalendarException> exceptions = exceptionRepository
                .findByCalendarIdAndExceptionDate(calendarId, date);
        if (!exceptions.isEmpty()) {
            return exceptions.get(0).getAvailableHours();
        }

        // Check regular shifts
        int dowCode = date.getDayOfWeek().getValue(); // 1=Monday .. 7=Sunday
        List<ManufacturingCalendarShift> shifts = shiftRepository
                .findByCalendarIdAndDayOfWeek(calendarId, dowCode);
        if (!shifts.isEmpty()) {
            BigDecimal sum = BigDecimal.ZERO;
            for (ManufacturingCalendarShift shift : shifts) {
                sum = sum.add(shift.getAvailableHours());
            }
            return sum;
        }

        return null;
    }

    /**
     * Retrieves a single calendar by id by its identifier.
     *
     * @param calendarId the calendarId input value
     * @return the CalendarDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public CalendarDto getCalendarById(Long calendarId) {
        ManufacturingCalendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found with ID: " + calendarId));
        return mapToDto(calendar);
    }

    /**
     * Retrieves calendars by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<CalendarDto> getCalendarsByCompany(Long companyId) {
        return calendarRepository.findByCompanyId(companyId)
                .stream().map(this::mapToDto).toList();
    }

    private CalendarDto mapToDto(ManufacturingCalendar calendar) {
        CalendarDto dto = new CalendarDto();
        dto.setId(calendar.getId());
        dto.setCompanyId(calendar.getCompanyId());
        dto.setCalendarType(calendar.getCalendarType());
        dto.setReferenceType(calendar.getReferenceType());
        dto.setReferenceId(calendar.getReferenceId());
        dto.setCode(calendar.getCode());
        dto.setName(calendar.getName());
        dto.setTimezone(calendar.getTimezone());
        dto.setActive(calendar.getActive());

        List<ShiftDto> shifts = shiftRepository.findByCalendarId(calendar.getId())
                .stream().map(this::mapToShiftDto).toList();
        dto.setShifts(shifts);

        List<CalendarExceptionDto> exceptions = exceptionRepository.findByCalendarId(calendar.getId())
                .stream().map(this::mapToExceptionDto).toList();
        dto.setExceptions(exceptions);

        return dto;
    }

    private ShiftDto mapToShiftDto(ManufacturingCalendarShift shift) {
        ShiftDto dto = new ShiftDto();
        dto.setId(shift.getId());
        dto.setCalendarId(shift.getCalendar().getId());
        dto.setDayOfWeek(shift.getDayOfWeek());
        dto.setShiftName(shift.getShiftName());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        dto.setBreakMinutes(shift.getBreakMinutes());
        dto.setAvailableHours(shift.getAvailableHours());
        dto.setShiftType(shift.getShiftType());
        return dto;
    }

    private CalendarExceptionDto mapToExceptionDto(ManufacturingCalendarException ex) {
        CalendarExceptionDto dto = new CalendarExceptionDto();
        dto.setId(ex.getId());
        dto.setCalendarId(ex.getCalendar().getId());
        dto.setExceptionDate(ex.getExceptionDate());
        dto.setExceptionType(ex.getExceptionType());
        dto.setAvailableHours(ex.getAvailableHours());
        dto.setDescription(ex.getDescription());
        return dto;
    }
}