package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.EmployeeShift;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeShiftRepository;
import com.plus33.erp.workforce.repository.ShiftRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for Shift Supervisors to assign shifts to employees in their store.
 */
@RestController
@RequestMapping("/api/v1/shift-assignments")
public class ShiftAssignmentController {

    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final ShiftRepository shiftRepository;

    // We need to query employees by store — use the EmployeeRepository
    private final com.plus33.erp.workforce.repository.EmployeeRepository employeeRepository;

    public ShiftAssignmentController(
            UserRepository userRepository,
            UserStoreRepository userStoreRepository,
            EmployeeShiftRepository employeeShiftRepository,
            ShiftRepository shiftRepository,
            com.plus33.erp.workforce.repository.EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.userStoreRepository = userStoreRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.shiftRepository = shiftRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Resolves the supervisor's store ID from user_stores.
     */
    private Long resolveStoreId(Principal principal) {
        if (principal == null) return null;
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) return null;
        List<UserStore> userStores = userStoreRepository.findByIdUserId(userOpt.get().getId());
        if (userStores.isEmpty() || userStores.get(0).getStore() == null) return null;
        return userStores.get(0).getStore().getId();
    }

    /**
     * GET /api/v1/shift-assignments/store-employees
     * Returns employees in the supervisor's store (for the assignment form).
     */
    @GetMapping("/store-employees")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStoreEmployees(Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unable to resolve store"));
        }

        // Get all user IDs for this store
        List<UserStore> allStoreUsers = userStoreRepository.findAll().stream()
                .filter(us -> us.getStore() != null && us.getStore().getId().equals(storeId))
                .collect(Collectors.toList());

        Set<Long> storeUserIds = allStoreUsers.stream()
                .map(us -> us.getId().getUserId())
                .collect(Collectors.toSet());

        // Get employees whose user_id is in this store
        List<Employee> employees = employeeRepository.findAll().stream()
                .filter(e -> e.getUser() != null && storeUserIds.contains(e.getUser().getId()))
                .filter(e -> e.getActive() != null && e.getActive())
                .sorted(Comparator.comparing(Employee::getFirstName))
                .collect(Collectors.toList());

        List<Map<String, Object>> result = new ArrayList<>();
        for (Employee emp : employees) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", emp.getId());
            m.put("firstName", emp.getFirstName());
            m.put("lastName", emp.getLastName() != null ? emp.getLastName() : "");
            m.put("designation", emp.getDesignation());
            m.put("employeeCode", emp.getEmployeeCode());
            result.add(m);
        }

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * GET /api/v1/shift-assignments
     * Returns all shift assignments for the supervisor's store for the next 3 days.
     */
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAssignments(Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unable to resolve store"));
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(2);

        // Get store employee IDs
        List<UserStore> allStoreUsers = userStoreRepository.findAll().stream()
                .filter(us -> us.getStore() != null && us.getStore().getId().equals(storeId))
                .collect(Collectors.toList());
        Set<Long> storeUserIds = allStoreUsers.stream()
                .map(us -> us.getId().getUserId())
                .collect(Collectors.toSet());

        List<Employee> storeEmployees = employeeRepository.findAll().stream()
                .filter(e -> e.getUser() != null && storeUserIds.contains(e.getUser().getId()))
                .filter(e -> e.getActive() != null && e.getActive())
                .collect(Collectors.toList());

        if (storeEmployees.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(new ArrayList<>()));
        }

        List<Long> empIds = storeEmployees.stream().map(Employee::getId).collect(Collectors.toList());
        Map<Long, Employee> empMap = storeEmployees.stream().collect(Collectors.toMap(Employee::getId, e -> e));

        List<EmployeeShift> assignments = employeeShiftRepository.findByEmployeeIdsInDateRange(empIds, today, endDate);

        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (EmployeeShift es : assignments) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("employeeId", es.getId().getEmployeeId());
            Employee emp = empMap.get(es.getId().getEmployeeId());
            m.put("employeeName", emp != null ? emp.getFirstName() + " " + (emp.getLastName() != null ? emp.getLastName() : "") : "Unknown");
            m.put("shiftId", es.getId().getShiftId());
            m.put("shiftName", es.getShift() != null ? es.getShift().getName() : "");
            m.put("shiftCode", es.getShift() != null ? es.getShift().getCode() : "");
            m.put("startTime", es.getShift() != null && es.getShift().getStartTime() != null ? es.getShift().getStartTime().toString() : "");
            m.put("endTime", es.getShift() != null && es.getShift().getEndTime() != null ? es.getShift().getEndTime().toString() : "");
            m.put("effectiveFrom", es.getId().getEffectiveFrom().format(fmt));
            m.put("effectiveTo", es.getEffectiveTo() != null ? es.getEffectiveTo().format(fmt) : null);
            result.add(m);
        }

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * POST /api/v1/shift-assignments
     * Assigns a shift to an employee for a 3-day range starting from a given date.
     * Validates: no overlapping shift on the same days.
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<Map<String, Object>>> assignShift(
            @RequestBody Map<String, Object> payload, Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unable to resolve store"));
        }

        Long employeeId = ((Number) payload.get("employeeId")).longValue();
        Long shiftId = ((Number) payload.get("shiftId")).longValue();
        String startDateStr = (String) payload.get("startDate");

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = startDate; // 1 day duration

        // Check if startDate is in the past
        if (startDate.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Cannot assign shifts for past dates."));
        }

        // Validate shift exists
        Optional<Shift> shiftOpt = shiftRepository.findById(shiftId);
        if (shiftOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Shift not found"));
        }

        // Validate employee exists
        Optional<Employee> empOpt = employeeRepository.findById(employeeId);
        if (empOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Employee not found"));
        }

        // Check for overlap — employee cannot have any shift on this day
        List<EmployeeShift> overlapping = employeeShiftRepository.findOverlapping(employeeId, startDate, endDate);
        if (!overlapping.isEmpty()) {
            // Build a descriptive message showing which days are already taken
            StringBuilder msg = new StringBuilder("Shift overlap: Employee already has a shift assigned on ");
            Set<String> conflictDays = new TreeSet<>();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
            for (EmployeeShift es : overlapping) {
                LocalDate from = es.getId().getEffectiveFrom();
                LocalDate to = es.getEffectiveTo() != null ? es.getEffectiveTo() : from;
                for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
                    if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                        conflictDays.add(d.format(fmt));
                    }
                }
            }
            msg.append(String.join(", ", conflictDays));
            msg.append(". Cannot overlap shifts.");
            return ResponseEntity.badRequest().body(ApiResponse.error(msg.toString()));
        }

        // Create the assignment
        EmployeeShift es = new EmployeeShift();
        EmployeeShift.EmployeeShiftId esId = new EmployeeShift.EmployeeShiftId();
        esId.setEmployeeId(employeeId);
        esId.setShiftId(shiftId);
        esId.setEffectiveFrom(startDate);
        es.setId(esId);
        es.setEmployee(empOpt.get());
        es.setShift(shiftOpt.get());
        es.setEffectiveTo(endDate);

        employeeShiftRepository.save(es);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("employeeId", employeeId);
        result.put("shiftId", shiftId);
        result.put("effectiveFrom", startDate.toString());
        result.put("effectiveTo", endDate.toString());
        result.put("message", "Shift assigned successfully");

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * GET /api/v1/shift-assignments/my-schedule
     * Returns the current week's shift assignments for the logged-in employee.
     */
    @GetMapping("/my-schedule")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMySchedule(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(ApiResponse.error("User not found"));
        }
        Optional<Employee> empOpt = employeeRepository.findByUserId(userOpt.get().getId());
        if (empOpt.isEmpty()) {
            return ResponseEntity.status(404).body(ApiResponse.error("Employee profile not found"));
        }
        Employee employee = empOpt.get();

        LocalDate start;
        if (startDateStr != null) {
            start = LocalDate.parse(startDateStr);
        } else {
            // Default to Monday of the current week
            LocalDate today = LocalDate.now();
            start = today.with(java.time.DayOfWeek.MONDAY);
        }
        LocalDate end = start.plusDays(6); // 7 days (Mon to Sun)

        List<EmployeeShift> assignments = employeeShiftRepository.findByEmployeeIdsInDateRange(
                Collections.singletonList(employee.getId()), start, end);

        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (EmployeeShift es : assignments) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", es.getShift().getId());
            m.put("date", es.getId().getEffectiveFrom().format(fmt));
            m.put("effectiveTo", es.getEffectiveTo() != null ? es.getEffectiveTo().format(fmt) : null);
            m.put("type", es.getShift().getName());
            m.put("code", es.getShift().getCode());
            m.put("time", es.getShift().getStartTime().toString() + " - " + es.getShift().getEndTime().toString());
            m.put("role", employee.getDesignation() != null ? employee.getDesignation() : "Operations");
            // No dummy coworkers/notes
            m.put("coworkers", new ArrayList<>());
            m.put("notes", "");
            result.add(m);
        }

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * DELETE /api/v1/shift-assignments/{employeeId}/{shiftId}/{effectiveFrom}
     * Removes a specific shift assignment.
     */
    @DeleteMapping("/{employeeId}/{shiftId}/{effectiveFrom}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(
            @PathVariable Long employeeId,
            @PathVariable Long shiftId,
            @PathVariable String effectiveFrom) {
        LocalDate from = LocalDate.parse(effectiveFrom);

        // Check if shift is in the past
        if (from.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Cannot modify or delete shifts in the past."));
        }

        EmployeeShift.EmployeeShiftId id = new EmployeeShift.EmployeeShiftId(employeeId, shiftId, from);
        if (!employeeShiftRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Assignment not found"));
        }
        employeeShiftRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("Shift assignment removed", null));
    }
}
