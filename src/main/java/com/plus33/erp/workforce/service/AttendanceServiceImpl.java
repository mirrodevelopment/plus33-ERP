package com.plus33.erp.workforce.service;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.repository.*;
import com.plus33.erp.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceBreakRepository attendanceBreakRepository;
    private final AttendanceCorrectionRepository attendanceCorrectionRepository;
    private final AttendanceAuditTrailRepository attendanceAuditTrailRepository;
    private final CountryWorkPolicyRepository countryWorkPolicyRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeLeaveRepository employeeLeaveRepository;
    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;
    private final LeaveServiceImpl leaveService;
    private final EmployeeLeaveBalanceRepository employeeLeaveBalanceRepository;
    private final AwayPermissionRepository awayPermissionRepository;
    private final StoreRepository storeRepository;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            AttendanceBreakRepository attendanceBreakRepository,
            AttendanceCorrectionRepository attendanceCorrectionRepository,
            AttendanceAuditTrailRepository attendanceAuditTrailRepository,
            CountryWorkPolicyRepository countryWorkPolicyRepository,
            EmployeeShiftRepository employeeShiftRepository,
            ShiftRepository shiftRepository,
            EmployeeLeaveRepository employeeLeaveRepository,
            HolidayRepository holidayRepository,
            UserRepository userRepository,
            UserStoreRepository userStoreRepository,
            LeaveServiceImpl leaveService,
            EmployeeLeaveBalanceRepository employeeLeaveBalanceRepository,
            AwayPermissionRepository awayPermissionRepository,
            StoreRepository storeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceBreakRepository = attendanceBreakRepository;
        this.attendanceCorrectionRepository = attendanceCorrectionRepository;
        this.attendanceAuditTrailRepository = attendanceAuditTrailRepository;
        this.countryWorkPolicyRepository = countryWorkPolicyRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.shiftRepository = shiftRepository;
        this.employeeLeaveRepository = employeeLeaveRepository;
        this.holidayRepository = holidayRepository;
        this.userRepository = userRepository;
        this.userStoreRepository = userStoreRepository;
        this.leaveService = leaveService;
        this.employeeLeaveBalanceRepository = employeeLeaveBalanceRepository;
        this.awayPermissionRepository = awayPermissionRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public Map<String, Object> getCurrentState(Employee employee) {
        LocalDate today = LocalDate.now();
        Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);

        Map<String, Object> state = new LinkedHashMap<>();
        if (attOpt.isEmpty()) {
            state.put("clockedIn", false);
            state.put("status", "ABSENT");
            state.put("checkInTime", null);
            state.put("checkOutTime", null);
            state.put("breakActive", false);
        } else {
            Attendance att = attOpt.get();
            boolean isClockedIn = att.getCheckInTime() != null && att.getCheckOutTime() == null;
            state.put("clockedIn", isClockedIn);
            state.put("status", att.getStatus());
            state.put("checkInTime", att.getCheckInTime() != null ? att.getCheckInTime().toString() : null);
            state.put("checkOutTime", att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : null);

            boolean breakActive = "ON_BREAK".equals(att.getStatus());
            state.put("breakActive", breakActive);

            // Sum breaks so far
            List<AttendanceBreak> breaks = attendanceBreakRepository.findByAttendanceId(att.getId());
            int breakMinutes = 0;
            for (AttendanceBreak b : breaks) {
                if (b.getBreakEnd() != null) {
                    breakMinutes += b.getDurationMinutes();
                } else {
                    // currently on break, add minutes up to now
                    Duration diff = Duration.between(b.getBreakStart(), LocalDateTime.now());
                    breakMinutes += (int) diff.toMinutes();
                }
            }
            state.put("breakMinutes", breakMinutes);
            state.put("workMinutes", att.getWorkMinutes() != null ? att.getWorkMinutes() : 0);
        }

        // Add today's scheduled shift details
        Optional<EmployeeShift> empShiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), today);
        if (empShiftOpt.isPresent()) {
            Shift shift = empShiftOpt.get().getShift();
            state.put("shiftScheduled", true);
            state.put("shiftName", shift.getName());
            state.put("shiftStartTime", shift.getStartTime() != null ? shift.getStartTime().toString() : null);
            state.put("shiftEndTime", shift.getEndTime() != null ? shift.getEndTime().toString() : null);
            state.put("shiftCode", shift.getCode());
        } else {
            state.put("shiftScheduled", false);
            state.put("shiftName", "Day Off");
            state.put("shiftStartTime", null);
            state.put("shiftEndTime", null);
            state.put("shiftCode", null);
        }

        return state;
    }

    @Override
    public Map<String, Object> getDashboard(Employee employee) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        // Get policies and limits
        String countryCode = resolveCountryCode(employee);
        CountryWorkPolicy policy = countryWorkPolicyRepository.findByCountryCode(countryCode)
                .orElseGet(() -> {
                    CountryWorkPolicy cp = new CountryWorkPolicy();
                    cp.setCountryCode(countryCode);
                    return cp;
                });

        List<Attendance> monthlyRecords = attendanceRepository.findByEmployeeId(employee.getId());
        // Filter for this month
        List<Attendance> currentMonthRecords = new ArrayList<>();
        for (Attendance att : monthlyRecords) {
            if (!att.getAttendanceDate().isBefore(startOfMonth) && !att.getAttendanceDate().isAfter(endOfMonth)) {
                currentMonthRecords.add(att);
            }
        }

        long presentDays = 0;
        long lateCount = 0;
        long earlyCheckoutCount = 0;
        double workedHoursSum = 0;
        double overtimeHoursSum = 0;

        for (Attendance att : currentMonthRecords) {
            if ("PRESENT".equalsIgnoreCase(att.getStatus()) || "ACTIVE".equalsIgnoreCase(att.getStatus()) || "ON_BREAK".equalsIgnoreCase(att.getStatus())) {
                presentDays++;
            } else if ("HALF_DAY".equalsIgnoreCase(att.getStatus())) {
                presentDays++; // Counts towards shift days
            }

            if (att.getLateMinutes() != null && att.getLateMinutes() > 0) {
                lateCount++;
            }
            if (att.getEarlyOutMinutes() != null && att.getEarlyOutMinutes() > 0) {
                earlyCheckoutCount++;
            }

            workedHoursSum += (att.getWorkMinutes() != null ? att.getWorkMinutes() : 0) / 60.0;
            overtimeHoursSum += (att.getOvertimeMinutes() != null ? att.getOvertimeMinutes() : 0) / 60.0;
        }

        // Leave days this month
        List<EmployeeLeave> leaves = employeeLeaveRepository.findOverlapping(employee.getId(), startOfMonth, endOfMonth);
        long leaveDays = 0;
        for (EmployeeLeave leave : leaves) {
            LocalDate start = leave.getStartDate().isBefore(startOfMonth) ? startOfMonth : leave.getStartDate();
            LocalDate end = leave.getEndDate().isAfter(endOfMonth) ? endOfMonth : leave.getEndDate();
            leaveDays += Duration.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay()).toDays();
        }

        // Holidays this month
        long holidayCount = holidayRepository.findByCountryCodeAndHolidayDateBetween(countryCode, startOfMonth, endOfMonth).size();

        // Weekend days this month
        long weeklyOff = 0;
        for (LocalDate d = startOfMonth; !d.isAfter(endOfMonth); d = d.plusDays(1)) {
            if (d.getDayOfWeek().getValue() >= 6) {
                weeklyOff++;
            }
        }

        // Absent days: days in the past (excluding weekends, holidays, leaves) with no attendance or marked ABSENT
        long absentDays = 0;
        for (LocalDate d = startOfMonth; d.isBefore(today); d = d.plusDays(1)) {
            final LocalDate dateIter = d;
            boolean hasAtt = currentMonthRecords.stream().anyMatch(r -> r.getAttendanceDate().equals(dateIter) && !r.getStatus().equals("ABSENT"));
            boolean isWeekend = d.getDayOfWeek().getValue() >= 6;
            boolean isHoliday = holidayRepository.existsByCountryCodeAndHolidayDate(countryCode, d);
            boolean isOnLeave = leaves.stream().anyMatch(l -> !dateIter.isBefore(l.getStartDate()) && !dateIter.isAfter(l.getEndDate()));

            if (!hasAtt && !isWeekend && !isHoliday && !isOnLeave) {
                absentDays++;
            }
        }

        double requiredHours = presentDays * 7.0; // fallback standard 7 hours/day
        double remainingHours = Math.max(0, requiredHours - workedHoursSum);
        double attendanceRate = presentDays + absentDays > 0 ? ((double) presentDays / (presentDays + absentDays)) * 100.0 : 100.0;
        double averageHours = presentDays > 0 ? workedHoursSum / presentDays : 0.0;

        // Sum leave balances
        int currentYearVal = LocalDate.now().getYear();
        List<EmployeeLeaveBalance> balances = employeeLeaveBalanceRepository.findByEmployeeIdAndYear(employee.getId(), currentYearVal);
        double totalLeaveUsed = 0.0;
        double totalLeaveRemaining = 0.0;
        for (EmployeeLeaveBalance bal : balances) {
            totalLeaveUsed += bal.getUsed() != null ? bal.getUsed().doubleValue() : 0.0;
            totalLeaveRemaining += bal.getRemaining() != null ? bal.getRemaining().doubleValue() : 0.0;
        }

        Optional<EmployeeShift> realShiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), today);
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("presentDays", presentDays);
        stats.put("absentDays", absentDays);
        stats.put("leaveDays", leaveDays);
        stats.put("holidayCount", holidayCount);
        stats.put("weeklyOff", weeklyOff);
        stats.put("attendanceRate", Math.round(attendanceRate * 10) / 10.0);
        stats.put("requiredHours", Math.round(requiredHours * 10) / 10.0);
        stats.put("workedHours", Math.round(workedHoursSum * 10) / 10.0);
        stats.put("remainingHours", Math.round(remainingHours * 10) / 10.0);
        stats.put("overtime", Math.round(overtimeHoursSum * 10) / 10.0);
        stats.put("nightOvertime", Math.round(overtimeHoursSum * 0.4 * 10) / 10.0); // night shift proportion fallback
        stats.put("holidayOvertime", 0.0);
        stats.put("currentShift", realShiftOpt.isPresent() ? 
                realShiftOpt.get().getShift().getName() + " (" + realShiftOpt.get().getShift().getStartTime() + " - " + realShiftOpt.get().getShift().getEndTime() + ")" : 
                "No Shift Assigned");
        stats.put("currentStatus", presentDays > 0 ? "Active" : "Inactive");
        stats.put("averageHours", Math.round(averageHours * 10) / 10.0);
        stats.put("lateCount", lateCount);
        stats.put("earlyCheckoutCount", earlyCheckoutCount);

        // Custom stats for front-end KPIs
        stats.put("workedDays", presentDays);
        stats.put("leaveUsed", Math.round(totalLeaveUsed * 10) / 10.0);
        stats.put("leaveLeft", Math.round(totalLeaveRemaining * 10) / 10.0);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getCalendar(Employee employee, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        List<Attendance> monthlyRecords = attendanceRepository.findByEmployeeId(employee.getId());
        List<EmployeeLeave> leaves = employeeLeaveRepository.findOverlapping(employee.getId(), startOfMonth, endOfMonth);
        String countryCode = resolveCountryCode(employee);
        List<Holiday> holidays = holidayRepository.findByCountryCodeAndHolidayDateBetween(countryCode, startOfMonth, endOfMonth);

        List<Map<String, Object>> calendar = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

        for (LocalDate d = startOfMonth; !d.isAfter(endOfMonth); d = d.plusDays(1)) {
            final LocalDate dateIter = d;
            Map<String, Object> dayMap = new LinkedHashMap<>();
            dayMap.put("date", d.toString());

            // Check if there is an attendance record
            Optional<Attendance> attOpt = monthlyRecords.stream().filter(r -> r.getAttendanceDate().equals(dateIter)).findFirst();
            boolean isHoliday = holidays.stream().anyMatch(h -> h.getHolidayDate().equals(dateIter));
            boolean isWeekend = d.getDayOfWeek().getValue() >= 6;
            Optional<EmployeeLeave> leaveOpt = leaves.stream().filter(l -> !dateIter.isBefore(l.getStartDate()) && !dateIter.isAfter(l.getEndDate())).findFirst();

            if (attOpt.isPresent()) {
                Attendance att = attOpt.get();
                if (leaveOpt.isPresent() && !"FULL_DAY".equals(leaveOpt.get().getLeaveSession())) {
                    dayMap.put("status", "HALF_DAY");
                } else {
                    dayMap.put("status", att.getStatus());
                }
                dayMap.put("checkIn", att.getCheckInTime() != null ? att.getCheckInTime().format(timeFormatter) : null);
                dayMap.put("checkOut", att.getCheckOutTime() != null ? att.getCheckOutTime().format(timeFormatter) : null);
                dayMap.put("hoursWorked", Math.round(((att.getWorkMinutes() != null ? att.getWorkMinutes() : 0) / 60.0) * 10) / 10.0);
                dayMap.put("isOvertime", att.getOvertimeMinutes() != null && att.getOvertimeMinutes() > 0);
                dayMap.put("isLate", att.getLateMinutes() != null && att.getLateMinutes() > 0);
                dayMap.put("isEarlyOut", att.getEarlyOutMinutes() != null && att.getEarlyOutMinutes() > 0);
            } else if (leaveOpt.isPresent()) {
                if (!"FULL_DAY".equals(leaveOpt.get().getLeaveSession())) {
                    dayMap.put("status", "HALF_DAY");
                } else {
                    dayMap.put("status", "LEAVE");
                }
                dayMap.put("checkIn", null);
                dayMap.put("checkOut", null);
                dayMap.put("hoursWorked", 0.0);
                dayMap.put("isOvertime", false);
                dayMap.put("isLate", false);
                dayMap.put("isEarlyOut", false);
            } else if (isHoliday) {
                dayMap.put("status", "HOLIDAY");
                dayMap.put("checkIn", null);
                dayMap.put("checkOut", null);
                dayMap.put("hoursWorked", 0.0);
                dayMap.put("isOvertime", false);
                dayMap.put("isLate", false);
                dayMap.put("isEarlyOut", false);
            } else if (isWeekend) {
                dayMap.put("status", "WEEKLY_OFF");
                dayMap.put("checkIn", null);
                dayMap.put("checkOut", null);
                dayMap.put("hoursWorked", 0.0);
                dayMap.put("isOvertime", false);
                dayMap.put("isLate", false);
                dayMap.put("isEarlyOut", false);
            } else {
                // If it is in the past, it's ABSENT. If today/future, it's pending clock in.
                dayMap.put("status", d.isBefore(LocalDate.now()) ? "ABSENT" : "SCHEDULED");
                dayMap.put("checkIn", null);
                dayMap.put("checkOut", null);
                dayMap.put("hoursWorked", 0.0);
                dayMap.put("isOvertime", false);
                dayMap.put("isLate", false);
                dayMap.put("isEarlyOut", false);
            }
            calendar.add(dayMap);
        }
        return calendar;
    }

    @Override
    public List<Map<String, Object>> getHistory(Employee employee) {
        List<Attendance> records = attendanceRepository.findByEmployeeId(employee.getId());
        // Sort descending by date
        records.sort((a, b) -> b.getAttendanceDate().compareTo(a.getAttendanceDate()));

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
        List<Map<String, Object>> list = new ArrayList<>();

        for (Attendance r : records) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("date", r.getAttendanceDate().toString());
            m.put("shift", r.getShift() != null ? r.getShift().getName() : "Morning Shift");
            m.put("checkIn", r.getCheckInTime() != null ? r.getCheckInTime().format(timeFormatter) : "--:--");
            m.put("checkOut", r.getCheckOutTime() != null ? r.getCheckOutTime().format(timeFormatter) : "--:--");
            m.put("hours", r.getCheckOutTime() != null ? String.format("%.1fh", (r.getWorkMinutes() != null ? r.getWorkMinutes() : 0) / 60.0) : "--");
            m.put("status", r.getStatus());
            list.add(m);
        }
        return list;
    }

    @Override
    public Map<String, Object> getOvertime(Employee employee) {
        List<Attendance> records = attendanceRepository.findByEmployeeId(employee.getId());
        double totalOt = 0;
        double weeklyOt = 0;
        double monthlyOt = 0;

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        for (Attendance r : records) {
            double otHours = (r.getOvertimeMinutes() != null ? r.getOvertimeMinutes() : 0) / 60.0;
            totalOt += otHours;
            if (!r.getAttendanceDate().isBefore(startOfWeek)) {
                weeklyOt += otHours;
            }
            if (!r.getAttendanceDate().isBefore(startOfMonth)) {
                monthlyOt += otHours;
            }
        }

        Map<String, Object> ot = new LinkedHashMap<>();
        ot.put("currentOvertime", Math.round(totalOt * 10) / 10.0);
        ot.put("weeklyOvertime", Math.round(weeklyOt * 10) / 10.0);
        ot.put("monthlyOvertime", Math.round(monthlyOt * 10) / 10.0);
        ot.put("holidayOvertime", 0.0);
        ot.put("nightOvertime", Math.round(totalOt * 0.4 * 10) / 10.0);
        return ot;
    }

    @Override
    public Map<String, Object> getReports(Employee employee) {
        // Attendance Trend past 7 days
        LocalDate today = LocalDate.now();
        List<String> labels = new ArrayList<>();
        List<Double> workedHours = new ArrayList<>();
        List<Double> overtimeHours = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate dateIter = today.minusDays(i);
            labels.add(dateIter.toString());

            Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), dateIter);
            if (attOpt.isPresent()) {
                Attendance att = attOpt.get();
                workedHours.add(Math.round(((att.getWorkMinutes() != null ? att.getWorkMinutes() : 0) / 60.0) * 10) / 10.0);
                overtimeHours.add(Math.round(((att.getOvertimeMinutes() != null ? att.getOvertimeMinutes() : 0) / 60.0) * 10) / 10.0);
            } else {
                workedHours.add(0.0);
                overtimeHours.add(0.0);
            }
        }

        Map<String, Object> reports = new LinkedHashMap<>();
        reports.put("labels", labels);
        reports.put("workedHours", workedHours);
        reports.put("overtimeHours", overtimeHours);
        return reports;
    }

    @Override
    @Transactional
    public Attendance checkIn(Employee employee, String gps, String device, String ip, String userAgent) {
        LocalDate today = LocalDate.now();

        // ── Guard 1: Resolve or auto-assign shift for today ──────────────────
        Optional<EmployeeShift> shiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), today);
        if (shiftOpt.isEmpty()) {
            Shift defaultShift = shiftRepository.findByCode("SHIFT_MORN")
                    .orElseGet(() -> {
                        Shift morn = new Shift();
                        morn.setCode("SHIFT_MORN");
                        morn.setName("Morning Shift");
                        morn.setCompany(employee.getCompany());
                        morn.setStartTime(LocalTime.of(6, 0));
                        morn.setEndTime(LocalTime.of(14, 0));
                        morn.setBreakMinutes(30);
                        morn.setOvernight(false);
                        morn.setActive(true);
                        morn.setCreatedAt(LocalDateTime.now());
                        morn.setUpdatedAt(LocalDateTime.now());
                        return shiftRepository.save(morn);
                    });

            EmployeeShift.EmployeeShiftId shiftId = new EmployeeShift.EmployeeShiftId(employee.getId(), defaultShift.getId(), today);
            EmployeeShift es = new EmployeeShift(shiftId, employee, defaultShift, today);
            employeeShiftRepository.save(es);
            shiftOpt = Optional.of(es);
        }

        // ── Guard 2: Employee must be within store's geofence radius ───────────
        Store employeeStore = resolveEmployeeStore(employee);
        int radius = (employeeStore != null && employeeStore.getGeofenceRadiusMeters() != null)
                ? employeeStore.getGeofenceRadiusMeters()
                : 30;
        assertWithinGeofence(gps, employeeStore, radius);

        Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);
        if (attOpt.isPresent() && attOpt.get().getCheckInTime() != null) {
            Attendance att = attOpt.get();
            if (att.getCheckOutTime() == null) {
                throw new BusinessException("You are already checked in for today.");
            }

            // Re-check-in: calculate away time as break
            LocalDateTime clockedOutAt = att.getCheckOutTime();
            LocalDateTime now = LocalDateTime.now();

            AttendanceBreak awayBreak = new AttendanceBreak();
            awayBreak.setAttendance(att);
            awayBreak.setBreakStart(clockedOutAt);
            awayBreak.setBreakEnd(now);
            awayBreak.setDurationMinutes((int) Duration.between(clockedOutAt, now).toMinutes());
            attendanceBreakRepository.save(awayBreak);

            att.setCheckOutTime(null);
            att.setStatus("ACTIVE");
            att.setGpsCoordinates(gps);
            att.setDeviceInfo(device != null ? device : userAgent);

            Attendance saved = attendanceRepository.save(att);
            writeAudit(employee.getUser(), saved, "CHECK_IN", ip, userAgent, null, now.toString(), "Barista Shift Clock In (Re-entry)");
            return saved;
        }

        Shift shift = shiftOpt.get().getShift();
        String countryCode = resolveCountryCode(employee);
        CountryWorkPolicy policy = countryWorkPolicyRepository.findByCountryCode(countryCode)
                .orElseGet(() -> {
                    CountryWorkPolicy cp = new CountryWorkPolicy();
                    cp.setCountryCode(countryCode);
                    return cp;
                });

        LocalDateTime now = LocalDateTime.now();
        LocalTime shiftStart = shift.getStartTime();
        LocalTime checkInTimeOfDay = now.toLocalTime();

        int lateMinutes = 0;
        if (checkInTimeOfDay.isAfter(shiftStart.plusMinutes(policy.getGracePeriodMinutes()))) {
            Duration diff = Duration.between(shiftStart, checkInTimeOfDay);
            lateMinutes = (int) diff.toMinutes();
        }

        Attendance attendance = attOpt.orElseGet(Attendance::new);
        attendance.setEmployee(employee);
        attendance.setShift(shift);
        attendance.setAttendanceDate(today);
        attendance.setCheckInTime(now);
        attendance.setStatus("ACTIVE");
        attendance.setLateMinutes(lateMinutes);
        attendance.setGpsCoordinates(gps);
        attendance.setDeviceInfo(device != null ? device : userAgent);

        Attendance saved = attendanceRepository.save(attendance);

        // Audit log
        writeAudit(employee.getUser(), saved, "CHECK_IN", ip, userAgent, null, saved.getCheckInTime().toString(), "Barista Shift Clock In");

        return saved;
    }

    @Override
    @Transactional
    public Attendance checkOut(Employee employee, String ip, String userAgent) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .orElseThrow(() -> new BusinessException("No check-in record found for today."));

        if (attendance.getCheckInTime() == null) {
            throw new BusinessException("You have not clocked in yet today.");
        }
        if (attendance.getCheckOutTime() != null) {
            throw new BusinessException("You are already clocked out for today.");
        }

        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOutTime(now);

        // Sum breaks
        List<AttendanceBreak> breaks = attendanceBreakRepository.findByAttendanceId(attendance.getId());
        int breakMinutesTotal = 0;
        for (AttendanceBreak b : breaks) {
            if (b.getBreakEnd() != null) {
                breakMinutesTotal += b.getDurationMinutes();
            } else {
                // close uncompleted break
                b.setBreakEnd(now);
                Duration diff = Duration.between(b.getBreakStart(), now);
                b.setDurationMinutes((int) diff.toMinutes());
                attendanceBreakRepository.save(b);
                breakMinutesTotal += b.getDurationMinutes();
            }
        }

        Duration totalDuty = Duration.between(attendance.getCheckInTime(), now);
        int workMinutes = (int) totalDuty.toMinutes() - breakMinutesTotal;
        attendance.setWorkMinutes(Math.max(0, workMinutes));

        Shift shift = attendance.getShift();
        Duration shiftDuration = Duration.between(shift.getStartTime(), shift.getEndTime());
        if (shift.getOvernight()) {
            shiftDuration = Duration.between(shift.getStartTime(), shift.getEndTime().plusHours(24));
        }
        int shiftMinutes = (int) shiftDuration.toMinutes();

        int overtime = 0;
        if (workMinutes > shiftMinutes) {
            overtime = workMinutes - shiftMinutes;
        }
        attendance.setOvertimeMinutes(overtime);

        int earlyOutMinutes = 0;
        LocalTime shiftEnd = shift.getEndTime();
        if (now.toLocalTime().isBefore(shiftEnd)) {
            earlyOutMinutes = (int) Duration.between(now.toLocalTime(), shiftEnd).toMinutes();
        }
        attendance.setEarlyOutMinutes(earlyOutMinutes);

        if (workMinutes < (shiftMinutes / 2)) {
            attendance.setStatus("HALF_DAY");
        } else {
            attendance.setStatus("PRESENT");
        }

        Attendance saved = attendanceRepository.save(attendance);

        try {
            leaveService.checkAndCreditCompOff(saved);
        } catch (Exception e) {
            // Log or ignore to prevent check-out failure on secondary business logic
        }

        writeAudit(employee.getUser(), saved, "CHECK_OUT", ip, userAgent, null, saved.getCheckOutTime().toString(), "Barista Shift Clock Out");

        return saved;
    }

    @Override
    @Transactional
    public void startBreak(Employee employee, String ip, String userAgent) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .orElseThrow(() -> new BusinessException("No check-in record found for today."));

        if (!"ACTIVE".equals(attendance.getStatus())) {
            throw new BusinessException("You must be clocked in and active to start a break.");
        }

        Optional<AttendanceBreak> activeBreakOpt = attendanceBreakRepository.findActiveBreakByAttendanceId(attendance.getId());
        if (activeBreakOpt.isPresent()) {
            throw new BusinessException("You are already on a break.");
        }

        LocalDateTime now = LocalDateTime.now();
        AttendanceBreak newBreak = new AttendanceBreak();
        newBreak.setAttendance(attendance);
        newBreak.setBreakStart(now);
        attendanceBreakRepository.save(newBreak);

        attendance.setStatus("ON_BREAK");
        attendanceRepository.save(attendance);

        writeAudit(employee.getUser(), attendance, "BREAK_START", ip, userAgent, null, now.toString(), "Barista Break Start");
    }

    @Override
    @Transactional
    public void endBreak(Employee employee, String ip, String userAgent) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .orElseThrow(() -> new BusinessException("No check-in record found for today."));

        AttendanceBreak activeBreak = attendanceBreakRepository.findActiveBreakByAttendanceId(attendance.getId())
                .orElseThrow(() -> new BusinessException("No active break found to end."));

        LocalDateTime now = LocalDateTime.now();
        activeBreak.setBreakEnd(now);
        Duration diff = Duration.between(activeBreak.getBreakStart(), now);
        activeBreak.setDurationMinutes((int) diff.toMinutes());
        attendanceBreakRepository.save(activeBreak);

        attendance.setStatus("ACTIVE");
        attendanceRepository.save(attendance);

        writeAudit(employee.getUser(), attendance, "BREAK_END", ip, userAgent, null, now.toString(), "Barista Break End");
    }

    @Override
    @Transactional
    public AttendanceCorrection submitCorrection(Employee employee, LocalDate date, String reason, String checkInStr, String checkOutStr, String ip, String userAgent) {
        Attendance attendance = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), date)
                .orElseGet(() -> {
                    // Create dummy/blank attendance record
                    Attendance dummy = new Attendance();
                    dummy.setEmployee(employee);
                    dummy.setShift(getActiveShift(employee, date));
                    dummy.setAttendanceDate(date);
                    dummy.setStatus("ABSENT");
                    return attendanceRepository.save(dummy);
                });

        LocalDateTime requestedCheckIn = null;
        LocalDateTime requestedCheckOut = null;

        if (checkInStr != null && !checkInStr.trim().isEmpty()) {
            LocalTime t = parseTimeRobust(checkInStr);
            requestedCheckIn = LocalDateTime.of(date, t);
        }
        if (checkOutStr != null && !checkOutStr.trim().isEmpty()) {
            LocalTime t = parseTimeRobust(checkOutStr);
            requestedCheckOut = LocalDateTime.of(date, t);
        }

        AttendanceCorrection correction = new AttendanceCorrection();
        correction.setAttendance(attendance);
        correction.setEmployee(employee);
        correction.setRequestDate(date);
        correction.setRequestedCheckIn(requestedCheckIn);
        correction.setRequestedCheckOut(requestedCheckOut);
        correction.setReason(reason);
        correction.setStatus("PENDING");

        AttendanceCorrection saved = attendanceCorrectionRepository.save(correction);

        writeAudit(employee.getUser(), attendance, "CORRECTION_REQUEST", ip, userAgent, null, saved.getId().toString(), "Barista Correction Request: " + reason);

        return saved;
    }

    @Override
    @Transactional
    public AttendanceCorrection approveCorrection(Long id, Long approverUserId, String ip, String userAgent) {
        AttendanceCorrection correction = attendanceCorrectionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Correction request not found."));

        if (!"PENDING".equals(correction.getStatus())) {
            throw new BusinessException("This correction request has already been processed.");
        }

        User approver = userRepository.findById(approverUserId)
                .orElseThrow(() -> new BusinessException("Approver user profile not found."));

        correction.setStatus("APPROVED");
        correction.setApprovedBy(approver);
        correction.setApprovedAt(LocalDateTime.now());

        AttendanceCorrection saved = attendanceCorrectionRepository.save(correction);

        Attendance attendance = correction.getAttendance();
        String prevVal = "CheckIn=" + attendance.getCheckInTime() + ", CheckOut=" + attendance.getCheckOutTime();

        if (correction.getRequestedCheckIn() != null) {
            attendance.setCheckInTime(correction.getRequestedCheckIn());
        }
        if (correction.getRequestedCheckOut() != null) {
            attendance.setCheckOutTime(correction.getRequestedCheckOut());
        }

        // Recalculate work, overtime, status, etc.
        if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
            Duration diff = Duration.between(attendance.getCheckInTime(), attendance.getCheckOutTime());
            int totalMins = (int) diff.toMinutes();
            attendance.setWorkMinutes(totalMins);

            Shift shift = attendance.getShift();
            Duration shiftDuration = Duration.between(shift.getStartTime(), shift.getEndTime());
            if (shift.getOvernight()) {
                shiftDuration = Duration.between(shift.getStartTime(), shift.getEndTime().plusHours(24));
            }
            int shiftMinutes = (int) shiftDuration.toMinutes();

            int overtime = Math.max(0, totalMins - shiftMinutes);
            attendance.setOvertimeMinutes(overtime);

            attendance.setLateMinutes(0);
            attendance.setEarlyOutMinutes(0);
            attendance.setStatus("PRESENT");
        }

        attendanceRepository.save(attendance);

        try {
            leaveService.checkAndCreditCompOff(attendance);
        } catch (Exception e) {
            // Log or ignore
        }

        String newVal = "CheckIn=" + attendance.getCheckInTime() + ", CheckOut=" + attendance.getCheckOutTime();

        writeAudit(approver, attendance, "CORRECTION_APPROVE", ip, userAgent, prevVal, newVal, "Supervisor approved correction");

        return saved;
    }

    @Override
    public Map<String, Object> getSettings() {
        String countryCode = resolveCountryCode(null);
        CountryWorkPolicy cp = countryWorkPolicyRepository.findByCountryCode(countryCode).orElse(new CountryWorkPolicy());
        Map<String, Object> settings = new LinkedHashMap<>();
        settings.put("standardHours", cp.getWeeklyRequiredHours());
        settings.put("overtimeRate", cp.getOvertimeRate());
        settings.put("gracePeriod", cp.getGracePeriodMinutes());
        settings.put("nightRate", cp.getNightOvertimeRate());
        settings.put("holidayRate", cp.getHolidayOvertimeRate());
        return settings;
    }

    @Override
    public CountryWorkPolicy getCountryWorkPolicy(String countryCode) {
        String code = countryCode;
        if (code == null || code.trim().isEmpty()) {
            code = resolveCountryCode(null);
        }
        final String searchCode = code;
        return countryWorkPolicyRepository.findByCountryCode(searchCode)
                .or(() -> countryWorkPolicyRepository.findByCountryCode("FR"))
                .orElseThrow(() -> new BusinessException("Work policy not found for country: " + searchCode));
    }

    private String resolveCountryCode(Employee employee) {
        if (employee != null) {
            if (employee.getUser() != null) {
                List<UserStore> userStores = userStoreRepository.findByIdUserId(employee.getUser().getId());
                if (userStores != null && !userStores.isEmpty()) {
                    com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
                    if (store != null && store.getRegion() != null) {
                        String code = store.getRegion().getCode();
                        if (code != null) {
                            code = code.toUpperCase();
                            if (code.startsWith("FR")) return "FR";
                            if (code.startsWith("IN")) return "IN";
                            if (code.startsWith("AE") || code.startsWith("UAE")) return "AE";
                            if (code.startsWith("EU")) return "EU";
                        }
                    }
                }
            }
            return "FR";
        }
        
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            String email = auth.getName();
            Optional<com.plus33.erp.security.entity.User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                List<UserStore> userStores = userStoreRepository.findByIdUserId(userOpt.get().getId());
                if (userStores != null && !userStores.isEmpty()) {
                    com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
                    if (store != null && store.getRegion() != null) {
                        String code = store.getRegion().getCode();
                        if (code != null) {
                            code = code.toUpperCase();
                            if (code.startsWith("FR")) return "FR";
                            if (code.startsWith("IN")) return "IN";
                            if (code.startsWith("AE") || code.startsWith("UAE")) return "AE";
                            if (code.startsWith("EU")) return "EU";
                        }
                    }
                }
            }
        }
        return "FR";
    }

    private Shift getActiveShift(Employee employee, LocalDate date) {
        // NOTE: checkIn() now enforces shift allocation directly. This method remains
        // for other callers that need a fallback (e.g. correction, payroll).
        return employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), date)
                .map(EmployeeShift::getShift)
                .orElseGet(() -> shiftRepository.findByCode("SHIFT_MORN")
                        .orElseGet(() -> shiftRepository.findAll().stream().findFirst()
                                .orElseThrow(() -> new BusinessException("No shifts configured in the system."))));
    }

    /**
     * Resolves the primary store assigned to the employee.
     * Returns null if no store assignment exists.
     */
    private Store resolveEmployeeStore(Employee employee) {
        if (employee == null || employee.getUser() == null) return null;
        List<UserStore> userStores = userStoreRepository.findByIdUserId(employee.getUser().getId());
        if (userStores == null || userStores.isEmpty()) return null;
        return userStores.get(0).getStore();
    }

    /**
     * Validates that the employee's current GPS position is within {@code radiusMeters} of the store.
     * <ul>
     *   <li>If the store has no GPS configured → silently skip (allow clock-in with info).</li>
     *   <li>If GPS string is missing → throw GPS_REQUIRED.</li>
     *   <li>If distance exceeds radius → throw OUT_OF_RANGE with distance and store name.</li>
     * </ul>
     */
    private void assertWithinGeofence(String gps, Store store, int radiusMeters) {
        if (store == null || store.getLatitude() == null || store.getLongitude() == null) {
            // Store has no GPS configured — skip geofence check
            return;
        }
        if (gps == null || gps.isBlank()) {
            throw new BusinessException("GPS_REQUIRED: Location access is required to clock in. Please enable GPS in your browser.");
        }
        String[] parts = gps.split(",");
        if (parts.length < 2) {
            throw new BusinessException("GPS_REQUIRED: Invalid GPS format. Expected 'latitude,longitude'.");
        }
        try {
            double empLat = Double.parseDouble(parts[0].trim());
            double empLng = Double.parseDouble(parts[1].trim());
            double storeLat = store.getLatitude().doubleValue();
            double storeLng = store.getLongitude().doubleValue();
            double distance = haversineMeters(empLat, empLng, storeLat, storeLng);
            if (distance > radiusMeters) {
                int rounded = (int) Math.round(distance);
                throw new BusinessException("OUT_OF_RANGE: You are " + rounded + " m away from " + store.getName()
                        + ". You must be within " + radiusMeters + " m to clock in.");
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            throw new BusinessException("GPS_REQUIRED: Could not parse GPS coordinates. Please try again.");
        }
    }

    /**
     * Calculates the Haversine distance in metres between two GPS coordinates.
     */
    private double haversineMeters(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6_371_000; // Earth radius in metres
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    /**
     * Called by the frontend every 5 minutes while an employee is clocked in.
     * Determines whether the employee is within the geofence and handles auto clock-out logic.
     *
     * <p>Network resilience: when the client was offline, it sends {@code networkRestored=true}.
     * In that case we check the current position only — no penalty for offline time.</p>
     *
     * <p>Grace period: after the away pass {@code approvedUntil} expires, a 10-minute grace buffer
     * allows the employee to return. During grace they can request an extension.
     * After grace expires → auto clock-out.</p>
     *
     * @return a map with key {@code action}: OK | WARNING | AWAY_PASS_ACTIVE | IN_GRACE_PERIOD | AUTO_CLOCKED_OUT
     */
    @Transactional
    public Map<String, Object> pingLocation(Employee employee, String gps, boolean networkRestored, String ip, String userAgent) {
        Map<String, Object> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();

        // Must be currently clocked in
        Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);
        if (attOpt.isEmpty() || attOpt.get().getCheckInTime() == null || attOpt.get().getCheckOutTime() != null) {
            result.put("action", "NOT_CLOCKED_IN");
            return result;
        }
        Attendance att = attOpt.get();

        Store store = resolveEmployeeStore(employee);
        if (store == null || store.getLatitude() == null || store.getLongitude() == null) {
            result.put("action", "OK");
            result.put("note", "Store GPS not configured — monitoring skipped.");
            return result;
        }

        if (gps == null || gps.isBlank()) {
            result.put("action", "GPS_REQUIRED");
            return result;
        }

        String[] parts = gps.split(",");
        double empLat, empLng;
        try {
            empLat = Double.parseDouble(parts[0].trim());
            empLng = Double.parseDouble(parts[1].trim());
        } catch (Exception e) {
            result.put("action", "GPS_REQUIRED");
            return result;
        }

        double distance = haversineMeters(empLat, empLng, store.getLatitude().doubleValue(), store.getLongitude().doubleValue());
        int autoClockOutRadius = store.getGeofenceRadiusMeters() != null ? store.getGeofenceRadiusMeters() : 200;
        int warningRadius = (int) (autoClockOutRadius * 0.75); // warn at 75% (150m for 200m limit)

        result.put("distance", (int) Math.round(distance));
        result.put("storeName", store.getName());

        if (distance <= autoClockOutRadius) {
            // Within safe zone
            if (distance > warningRadius) {
                result.put("action", "WARNING");
                result.put("message", "You are getting close to the store boundary (" + (int) Math.round(distance) + " m away).");
            } else {
                result.put("action", "OK");
            }
            return result;
        }

        // Employee is beyond the auto clock-out radius — check for an active away pass
        List<AwayPermissionRequest> activePasses = awayPermissionRepository.findActivePassesForAttendance(att.getId());
        AwayPermissionRequest activePass = activePasses.isEmpty() ? null : activePasses.get(0);

        if (activePass != null && activePass.isActive()) {
            if (activePass.isInGracePeriod()) {
                // Pass has expired but still in 10-min grace — employee can extend
                result.put("action", "IN_GRACE_PERIOD");
                result.put("graceExpiresAt", activePass.getApprovedUntil().plusMinutes(activePass.getGraceBufferMins()).toString());
                result.put("message", "Your away pass has expired. You have " + activePass.getGraceBufferMins() + " minutes to return or request an extension.");
                // Mark pass as EXTENSION_REQUESTED if not already done
                if (!awayPermissionRepository.existsByAttendanceIdAndStatus(att.getId(), "EXTENSION_REQUESTED")) {
                    activePass.setStatus("EXTENSION_REQUESTED");
                    awayPermissionRepository.save(activePass);
                }
            } else {
                result.put("action", "AWAY_PASS_ACTIVE");
                result.put("approvedUntil", activePass.getApprovedUntil().toString());
                result.put("graceUntil", activePass.getApprovedUntil().plusMinutes(activePass.getGraceBufferMins()).toString());
                result.put("message", "Away pass active until " + activePass.getApprovedUntil().toString() + ".");
            }
            return result;
        }

        // No active pass and outside geofence → auto clock-out
        performAutoCheckOut(att, employee, ip, userAgent, (int) Math.round(distance), store.getName());
        result.put("action", "AUTO_CLOCKED_OUT");
        result.put("message", "You have been automatically clocked out because you moved " + (int) Math.round(distance) + " m away from " + store.getName() + ".");
        return result;
    }

    /**
     * Performs automatic clock-out when an employee exceeds the geofence without an active away pass.
     */
    private void performAutoCheckOut(Attendance att, Employee employee, String ip, String userAgent, int distanceMeters, String storeName) {
        LocalDateTime now = LocalDateTime.now();
        att.setCheckOutTime(now);

        // Sum existing breaks
        List<AttendanceBreak> breaks = attendanceBreakRepository.findByAttendanceId(att.getId());
        int breakMinutesTotal = 0;
        for (AttendanceBreak b : breaks) {
            if (b.getBreakEnd() != null) {
                breakMinutesTotal += b.getDurationMinutes();
            } else {
                b.setBreakEnd(now);
                int d = (int) Duration.between(b.getBreakStart(), now).toMinutes();
                b.setDurationMinutes(d);
                attendanceBreakRepository.save(b);
                breakMinutesTotal += d;
            }
        }

        int workMinutes = Math.max(0, (int) Duration.between(att.getCheckInTime(), now).toMinutes() - breakMinutesTotal);
        att.setWorkMinutes(workMinutes);
        att.setStatus("PRESENT");
        att.setNotes("Auto clocked-out: " + distanceMeters + " m from " + storeName);
        attendanceRepository.save(att);

        writeAudit(employee.getUser(), att, "AUTO_CHECK_OUT", ip, userAgent, null, now.toString(),
                "Auto clock-out: employee was " + distanceMeters + " m from " + storeName);
    }

    private void writeAudit(User user, Attendance attendance, String actionType, String ip, String device, String prev, String next, String reason) {
        AttendanceAuditTrail audit = new AttendanceAuditTrail();
        audit.setUser(user);
        audit.setAttendance(attendance);
        audit.setActionType(actionType);
        audit.setIpAddress(ip);
        audit.setDevice(device);
        audit.setPreviousValue(prev);
        audit.setNewValue(next);
        audit.setReason(reason);
        attendanceAuditTrailRepository.save(audit);
    }

    private LocalTime parseTimeRobust(String input) {
        if (input == null) return null;
        String clean = input.trim().toUpperCase();
        
        // Try parsing as 24-hour time first (e.g., "08:00", "16:30", "8:00")
        try {
            return LocalTime.parse(clean, DateTimeFormatter.ofPattern("[H]H:mm"));
        } catch (Exception e) {
            // Ignored, try next
        }
        
        // Try parsing as 12-hour time with AM/PM (e.g. "08:00 AM", "8:00AM", "08:00AM", "8:00 AM")
        try {
            String amPmClean = clean.replaceAll("\\s+", " ");
            return LocalTime.parse(amPmClean, DateTimeFormatter.ofPattern("[h]h:mm a", Locale.US));
        } catch (Exception e) {
            // Ignored, try next
        }
        
        throw new BusinessException("Invalid time format: '" + input + "'. Please use 'HH:mm' (e.g., 16:30) or 'hh:mm AM/PM' (e.g., 04:30 PM).");
    }
}
