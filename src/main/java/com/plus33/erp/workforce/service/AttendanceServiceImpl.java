package com.plus33.erp.workforce.service;

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
    private final HolidayCalendarRepository holidayCalendarRepository;
    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;
    private final LeaveServiceImpl leaveService;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            AttendanceBreakRepository attendanceBreakRepository,
            AttendanceCorrectionRepository attendanceCorrectionRepository,
            AttendanceAuditTrailRepository attendanceAuditTrailRepository,
            CountryWorkPolicyRepository countryWorkPolicyRepository,
            EmployeeShiftRepository employeeShiftRepository,
            ShiftRepository shiftRepository,
            EmployeeLeaveRepository employeeLeaveRepository,
            HolidayCalendarRepository holidayCalendarRepository,
            UserRepository userRepository,
            UserStoreRepository userStoreRepository,
            LeaveServiceImpl leaveService) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceBreakRepository = attendanceBreakRepository;
        this.attendanceCorrectionRepository = attendanceCorrectionRepository;
        this.attendanceAuditTrailRepository = attendanceAuditTrailRepository;
        this.countryWorkPolicyRepository = countryWorkPolicyRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.shiftRepository = shiftRepository;
        this.employeeLeaveRepository = employeeLeaveRepository;
        this.holidayCalendarRepository = holidayCalendarRepository;
        this.userRepository = userRepository;
        this.userStoreRepository = userStoreRepository;
        this.leaveService = leaveService;
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
        long holidayCount = holidayCalendarRepository.findByCountryCodeAndHolidayDateBetween(countryCode, startOfMonth, endOfMonth).size();

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
            boolean isHoliday = holidayCalendarRepository.existsByCountryCodeAndHolidayDate(countryCode, d);
            boolean isOnLeave = leaves.stream().anyMatch(l -> !dateIter.isBefore(l.getStartDate()) && !dateIter.isAfter(l.getEndDate()));

            if (!hasAtt && !isWeekend && !isHoliday && !isOnLeave) {
                absentDays++;
            }
        }

        double requiredHours = presentDays * 7.0; // fallback standard 7 hours/day
        double remainingHours = Math.max(0, requiredHours - workedHoursSum);
        double attendanceRate = presentDays + absentDays > 0 ? ((double) presentDays / (presentDays + absentDays)) * 100.0 : 100.0;
        double averageHours = presentDays > 0 ? workedHoursSum / presentDays : 0.0;

        Shift activeShift = getActiveShift(employee, today);

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
        stats.put("currentShift", activeShift != null ? activeShift.getName() + " (" + activeShift.getStartTime() + " - " + activeShift.getEndTime() + ")" : "Morning Shift");
        stats.put("currentStatus", presentDays > 0 ? "Active" : "Inactive");
        stats.put("averageHours", Math.round(averageHours * 10) / 10.0);
        stats.put("lateCount", lateCount);
        stats.put("earlyCheckoutCount", earlyCheckoutCount);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getCalendar(Employee employee, int year, int month) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        List<Attendance> monthlyRecords = attendanceRepository.findByEmployeeId(employee.getId());
        List<EmployeeLeave> leaves = employeeLeaveRepository.findOverlapping(employee.getId(), startOfMonth, endOfMonth);
        String countryCode = resolveCountryCode(employee);
        List<HolidayCalendar> holidays = holidayCalendarRepository.findByCountryCodeAndHolidayDateBetween(countryCode, startOfMonth, endOfMonth);

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
                dayMap.put("status", att.getStatus());
                dayMap.put("checkIn", att.getCheckInTime() != null ? att.getCheckInTime().format(timeFormatter) : null);
                dayMap.put("checkOut", att.getCheckOutTime() != null ? att.getCheckOutTime().format(timeFormatter) : null);
                dayMap.put("hoursWorked", Math.round(((att.getWorkMinutes() != null ? att.getWorkMinutes() : 0) / 60.0) * 10) / 10.0);
                dayMap.put("isOvertime", att.getOvertimeMinutes() != null && att.getOvertimeMinutes() > 0);
                dayMap.put("isLate", att.getLateMinutes() != null && att.getLateMinutes() > 0);
                dayMap.put("isEarlyOut", att.getEarlyOutMinutes() != null && att.getEarlyOutMinutes() > 0);
            } else if (leaveOpt.isPresent()) {
                dayMap.put("status", "LEAVE");
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
        Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);
        if (attOpt.isPresent() && attOpt.get().getCheckInTime() != null) {
            throw new BusinessException("You are already checked in for today.");
        }

        Shift shift = getActiveShift(employee, today);
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

        DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
        LocalDateTime requestedCheckIn = null;
        LocalDateTime requestedCheckOut = null;

        if (checkInStr != null && !checkInStr.trim().isEmpty()) {
            LocalTime t = LocalTime.parse(checkInStr.trim(), timeParser);
            requestedCheckIn = LocalDateTime.of(date, t);
        }
        if (checkOutStr != null && !checkOutStr.trim().isEmpty()) {
            LocalTime t = LocalTime.parse(checkOutStr.trim(), timeParser);
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
        return countryWorkPolicyRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new BusinessException("Work policy not found for country: " + countryCode));
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
        return employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), date)
                .map(EmployeeShift::getShift)
                .orElseGet(() -> shiftRepository.findByCode("SHIFT_MORN")
                        .orElseGet(() -> shiftRepository.findAll().stream().findFirst()
                                .orElseThrow(() -> new BusinessException("No shifts configured in the system."))));
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
}
