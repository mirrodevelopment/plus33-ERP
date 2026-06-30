package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

public interface ManufacturingCalendarService {
    CalendarDto createCalendar(CreateCalendarRequest request);
    ShiftDto addShift(Long calendarId, CreateShiftRequest request);
    CalendarExceptionDto addException(Long calendarId, CreateExceptionRequest request);
    BigDecimal getAvailableHours(Long companyId, String resourceType, Long resourceId, LocalDate date);
    CalendarDto getCalendarById(Long calendarId);
    List<CalendarDto> getCalendarsByCompany(Long companyId);
}
