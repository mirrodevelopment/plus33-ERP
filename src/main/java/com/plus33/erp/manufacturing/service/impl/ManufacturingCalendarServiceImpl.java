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

    @Override
    @Transactional(readOnly = true)
    public CalendarDto getCalendarById(Long calendarId) {
        ManufacturingCalendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar not found with ID: " + calendarId));
        return mapToDto(calendar);
    }

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
