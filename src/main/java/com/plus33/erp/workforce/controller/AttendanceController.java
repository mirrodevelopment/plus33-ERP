package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Attendance;
import com.plus33.erp.workforce.entity.AttendanceCorrection;
import com.plus33.erp.workforce.entity.CountryWorkPolicy;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.service.AttendanceService;
import com.plus33.erp.workforce.service.AttendanceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceServiceImpl attendanceServiceImpl;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public AttendanceController(
            AttendanceService attendanceService,
            AttendanceServiceImpl attendanceServiceImpl,
            EmployeeRepository employeeRepository,
            UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.attendanceServiceImpl = attendanceServiceImpl;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/v1/attendance/today")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTodayState(Principal principal) {
        Employee employee = resolveEmployee(principal);
        Map<String, Object> state = attendanceService.getCurrentState(employee);
        return ResponseEntity.ok(ApiResponse.success(state));
    }

    @GetMapping("/api/v1/attendance/dashboard")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboard(Principal principal) {
        Employee employee = resolveEmployee(principal);
        Map<String, Object> stats = attendanceService.getDashboard(employee);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/api/v1/attendance/calendar")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCalendar(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            Principal principal) {
        Employee employee = resolveEmployee(principal);
        int targetYear = year != null ? year : LocalDate.now().getYear();
        int targetMonth = month != null ? month : LocalDate.now().getMonthValue();
        List<Map<String, Object>> calendar = attendanceService.getCalendar(employee, targetYear, targetMonth);
        return ResponseEntity.ok(ApiResponse.success(calendar));
    }

    @GetMapping("/api/v1/attendance/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHistory(Principal principal) {
        Employee employee = resolveEmployee(principal);
        List<Map<String, Object>> list = attendanceService.getHistory(employee);
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/api/v1/attendance/overtime")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getOvertime(Principal principal) {
        Employee employee = resolveEmployee(principal);
        Map<String, Object> ot = attendanceService.getOvertime(employee);
        return ResponseEntity.ok(ApiResponse.success(ot));
    }

    @GetMapping("/api/v1/attendance/reports")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReports(Principal principal) {
        Employee employee = resolveEmployee(principal);
        Map<String, Object> trend = attendanceService.getReports(employee);
        return ResponseEntity.ok(ApiResponse.success(trend));
    }

    @PostMapping("/api/v1/attendance/check-in")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkIn(
            @RequestBody(required = false) Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        String gps = body != null && body.get("gps") != null ? body.get("gps").toString() : null;
        String device = body != null && body.get("device") != null ? body.get("device").toString() : null;
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Attendance att = attendanceService.checkIn(employee, gps, device, ip, userAgent);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", att.getId());
        m.put("status", att.getStatus());
        m.put("checkInTime", att.getCheckInTime().toString());
        return ResponseEntity.ok(ApiResponse.success(m));
    }

    @PostMapping("/api/v1/attendance/check-out")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkOut(
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Attendance att = attendanceService.checkOut(employee, ip, userAgent);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", att.getId());
        m.put("status", att.getStatus());
        m.put("checkOutTime", att.getCheckOutTime().toString());
        m.put("workMinutes", att.getWorkMinutes());
        m.put("overtimeMinutes", att.getOvertimeMinutes());
        return ResponseEntity.ok(ApiResponse.success(m));
    }

    @PostMapping("/api/v1/attendance/break/start")
    public ResponseEntity<ApiResponse<String>> startBreak(
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        attendanceService.startBreak(employee, ip, userAgent);
        return ResponseEntity.ok(ApiResponse.success("Break started successfully."));
    }

    @PostMapping("/api/v1/attendance/break/end")
    public ResponseEntity<ApiResponse<String>> endBreak(
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        attendanceService.endBreak(employee, ip, userAgent);
        return ResponseEntity.ok(ApiResponse.success("Break ended successfully."));
    }

    @PutMapping("/api/v1/attendance/correction")
    public ResponseEntity<ApiResponse<Map<String, Object>>> correction(
            @RequestBody Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        LocalDate date = body != null && body.get("date") != null ? LocalDate.parse(body.get("date").toString()) : null;
        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : null;
        if (date == null || reason == null) {
            throw new BusinessException("Date and reason are required.");
        }
        String checkIn = body.get("checkIn") != null ? body.get("checkIn").toString() : null;
        String checkOut = body.get("checkOut") != null ? body.get("checkOut").toString() : null;
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        AttendanceCorrection corr = attendanceService.submitCorrection(employee, date, reason, checkIn, checkOut, ip, userAgent);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", corr.getId());
        m.put("status", corr.getStatus());
        return ResponseEntity.ok(ApiResponse.success(m));
    }

    @PutMapping("/api/v1/attendance/approve")
    public ResponseEntity<ApiResponse<Map<String, Object>>> approveCorrection(
            @RequestBody Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        User user = resolveUser(principal);
        Long correctionId = body != null && body.get("id") != null ? Long.valueOf(body.get("id").toString()) : null;
        if (correctionId == null) {
            throw new BusinessException("Correction ID is required.");
        }
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        AttendanceCorrection corr = attendanceService.approveCorrection(correctionId, user.getId(), ip, userAgent);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", corr.getId());
        m.put("status", corr.getStatus());
        return ResponseEntity.ok(ApiResponse.success(m));
    }

    @GetMapping("/api/v1/attendance/settings")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSettings() {
        Map<String, Object> s = attendanceService.getSettings();
        return ResponseEntity.ok(ApiResponse.success(s));
    }

    @GetMapping("/api/v1/country-work-policy")
    public ResponseEntity<ApiResponse<CountryWorkPolicy>> getCountryWorkPolicy(
            @RequestParam(value = "countryCode", required = false) String countryCode) {
        CountryWorkPolicy p = attendanceService.getCountryWorkPolicy(countryCode);
        return ResponseEntity.ok(ApiResponse.success(p));
    }

    /**
     * Called by the frontend every 5 minutes while the employee is clocked in.
     * Handles geofence monitoring, away pass checking, auto clock-out, and grace period detection.
     *
     * <p>Body: {@code { "gps": "lat,lng", "networkRestored": true }}</p>
     * <ul>
     *   <li>{@code networkRestored=true} — sent after reconnection; the backend checks current location only,
     *       no penalty applied for offline gap.</li>
     * </ul>
     *
     * <p>Response {@code action} values: OK | WARNING | AWAY_PASS_ACTIVE | IN_GRACE_PERIOD | AUTO_CLOCKED_OUT | NOT_CLOCKED_IN</p>
     */
    @PostMapping("/api/v1/attendance/ping-location")
    public ResponseEntity<ApiResponse<Map<String, Object>>> pingLocation(
            @RequestBody(required = false) Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        Employee employee = resolveEmployee(principal);
        String gps = body != null && body.get("gps") != null ? body.get("gps").toString() : null;
        boolean networkRestored = body != null && Boolean.TRUE.equals(body.get("networkRestored"));
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> result = attendanceServiceImpl.pingLocation(employee, gps, networkRestored, ip, userAgent);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    private Employee resolveEmployee(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User profile not found."));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("Employee record not found for this user."));
    }

    private User resolveUser(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User profile not found."));
    }
}
