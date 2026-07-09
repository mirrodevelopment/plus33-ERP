package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.repository.HolidayCalendarRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reusable working day calculator service for PLUS33 ERP.
 * Excludes weekends (Saturday, Sunday) and public holidays from the holiday_calendar table.
 * Supports FULL_DAY, HALF_DAY_AM, and HALF_DAY_PM sessions.
 */
@Service
public class WorkingDayCalculatorService {

    private final HolidayCalendarRepository holidayCalendarRepository;

    public WorkingDayCalculatorService(HolidayCalendarRepository holidayCalendarRepository) {
        this.holidayCalendarRepository = holidayCalendarRepository;
    }

    /**
     * Calculates working days between startDate and endDate (inclusive),
     * excluding weekends and public holidays.
     *
     * @param startDate   the first day of the leave
     * @param endDate     the last day of the leave
     * @param session     FULL_DAY / HALF_DAY_AM / HALF_DAY_PM
     * @param countryCode e.g. "FR"
     * @return working days as BigDecimal (0.5 for half-day)
     */
    public BigDecimal calculate(LocalDate startDate, LocalDate endDate, String session, String countryCode) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return BigDecimal.ZERO;
        }

        // If it's a single day and half-day session — quick path
        if (startDate.equals(endDate) && (isHalfDay(session))) {
            if (!isWeekend(startDate, countryCode) && !isHoliday(startDate, countryCode)) {
                return new BigDecimal("0.5");
            }
            return BigDecimal.ZERO;
        }

        // Load all holidays in range for efficiency
        final Set<LocalDate> holidays = holidayCalendarRepository
                .findByCountryCodeAndHolidayDateBetween(countryCode, startDate, endDate)
                .stream()
                .filter(h -> !Boolean.TRUE.equals(h.getIsWorkingDay()))
                .map(h -> h.getHolidayDate())
                .collect(Collectors.toSet());

        long workingDays = startDate.datesUntil(endDate.plusDays(1))
                .filter(d -> !isWeekend(d, countryCode) && !holidays.contains(d))
                .count();

        if (isHalfDay(session) && workingDays > 0) {
            // Half-day: count = (workingDays - 1) + 0.5
            return BigDecimal.valueOf(workingDays - 1).add(new BigDecimal("0.5"));
        }

        return BigDecimal.valueOf(workingDays);
    }

    private boolean isWeekend(LocalDate date, String countryCode) {
        if ("IN".equalsIgnoreCase(countryCode) || "AE".equalsIgnoreCase(countryCode)) {
            return date.getDayOfWeek() == DayOfWeek.SUNDAY;
        }
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean isHalfDay(String session) {
        return "HALF_DAY_AM".equalsIgnoreCase(session) || "HALF_DAY_PM".equalsIgnoreCase(session);
    }

    private boolean isHoliday(LocalDate date, String countryCode) {
        return holidayCalendarRepository.existsByCountryCodeAndHolidayDate(countryCode, date);
    }
}
