package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.repository.HolidayRepository;
import com.plus33.erp.workforce.repository.WeeklyOffRuleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code WorkingDayCalculatorService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Stateless Business Service — leave duration calculator.</p>
 *
 * <p>Calculates the number of working days between two dates, excluding:
 * <ul>
 *   <li>Weekly off days resolved from the {@code weekly_off_rules} table per policy group.</li>
 *   <li>Public holidays from the {@code holiday_calendar} table per country code.</li>
 * </ul>
 *
 * <p><b>Key improvement over legacy implementation:</b> Weekly off days are no
 * longer hardcoded ({@code if India → SUNDAY}). They are loaded from the
 * {@code weekly_off_rules} table via the policy group code (INDIA, EU, UAE)
 * and cached per group within the request lifetime.</p>
 *
 * <p>Supports FULL_DAY, HALF_DAY_AM, and HALF_DAY_PM sessions.</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class WorkingDayCalculatorService {

    private final HolidayRepository holidayRepository;
    private final WeeklyOffRuleRepository   weeklyOffRuleRepository;

    /** In-memory cache: policyGroupCode → set of weekly-off DayOfWeek names. */
    private final Map<String, Set<String>> weeklyOffCache = new ConcurrentHashMap<>();

    public WorkingDayCalculatorService(
            HolidayRepository holidayRepository,
            WeeklyOffRuleRepository weeklyOffRuleRepository) {
        this.holidayRepository = holidayRepository;
        this.weeklyOffRuleRepository   = weeklyOffRuleRepository;
    }

    /** Clears cached weekly off rules. */
    public void clearCache() {
        weeklyOffCache.clear();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIMARY API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Calculates working days between startDate and endDate (inclusive), using
     * the policy group to determine weekly off days and the country code to
     * exclude public holidays.
     *
     * @param startDate       first day of leave (inclusive)
     * @param endDate         last day of leave (inclusive)
     * @param session         FULL_DAY | HALF_DAY_AM | HALF_DAY_PM
     * @param policyGroupCode INDIA | EU | UAE — used to resolve weekly offs from DB
     * @param countryCode     e.g. "FR", "IN", "AE" — used for holiday lookup
     * @return working days as BigDecimal (0.5 for single half-day)
     */
    public BigDecimal calculate(LocalDate startDate, LocalDate endDate,
                                String session, String policyGroupCode, String countryCode) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return BigDecimal.ZERO;
        }

        Set<String> weeklyOffs = resolveWeeklyOffDays(policyGroupCode);

        // Single half-day: quick path
        if (startDate.equals(endDate) && isHalfDay(session)) {
            if (!isWeeklyOff(startDate, weeklyOffs) && !isHoliday(startDate, countryCode)) {
                return new BigDecimal("0.5");
            }
            return BigDecimal.ZERO;
        }

        // Load all holidays in range once (efficient batch query)
        final Set<LocalDate> holidays = holidayRepository
                .findByCountryCodeAndHolidayDateBetween(countryCode, startDate, endDate)
                .stream()
                .filter(h -> !Boolean.TRUE.equals(h.getIsWorkingDay()))
                .map(h -> h.getHolidayDate())
                .collect(Collectors.toSet());

        long workingDays = startDate.datesUntil(endDate.plusDays(1))
                .filter(d -> !isWeeklyOff(d, weeklyOffs) && !holidays.contains(d))
                .count();

        if (isHalfDay(session) && workingDays > 0) {
            return BigDecimal.valueOf(workingDays - 1).add(new BigDecimal("0.5"));
        }

        return BigDecimal.valueOf(workingDays);
    }

    /**
     * Backward-compatible overload that accepts a raw country code string and
     * maps it to the appropriate policy group automatically.
     *
     * @deprecated Prefer {@link #calculate(LocalDate, LocalDate, String, String, String)}
     *             with an explicit policyGroupCode.
     */
    @Deprecated
    public BigDecimal calculate(LocalDate startDate, LocalDate endDate,
                                String session, String countryCode) {
        String groupCode = mapCountryToGroupCode(countryCode);
        return calculate(startDate, endDate, session, groupCode, countryCode);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // WEEKLY OFF RESOLUTION
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Resolves and caches the set of weekly-off day name strings for a policy group.
     * Falls back to {SATURDAY, SUNDAY} if the group code is unrecognized.
     */
    private Set<String> resolveWeeklyOffDays(String policyGroupCode) {
        if (policyGroupCode == null) policyGroupCode = "EU";
        final String code = policyGroupCode;
        return weeklyOffCache.computeIfAbsent(code, c ->
                weeklyOffRuleRepository.resolveWeeklyOffDays(c));
    }

    private boolean isWeeklyOff(LocalDate date, Set<String> weeklyOffs) {
        return weeklyOffs.contains(date.getDayOfWeek().name());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private boolean isHalfDay(String session) {
        return "HALF_DAY_AM".equalsIgnoreCase(session) || "HALF_DAY_PM".equalsIgnoreCase(session);
    }

    private boolean isHoliday(LocalDate date, String countryCode) {
        return holidayRepository.existsByCountryCodeAndHolidayDate(countryCode, date);
    }

    /** Maps a legacy country code to its policy group code. */
    private String mapCountryToGroupCode(String countryCode) {
        if (countryCode == null) return "EU";
        return switch (countryCode.toUpperCase()) {
            case "IN", "IND"  -> "INDIA";
            case "AE", "UAE"  -> "UAE";
            default            -> "EU";
        };
    }

    /** Clears the weekly-off cache (useful after admin updates to weekly_off_rules). */
    public void clearWeeklyOffCache() {
        weeklyOffCache.clear();
    }
}
