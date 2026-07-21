package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.EmployeeShift;
import com.plus33.erp.workforce.entity.OvertimeRequest;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.EmployeeShiftRepository;
import com.plus33.erp.workforce.repository.OvertimeRequestRepository;
import com.plus33.erp.workforce.repository.ShiftRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/v1/overtime-requests")
public class OvertimeRequestController {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final OvertimeRequestRepository overtimeRequestRepository;
    private final ShiftRepository shiftRepository;
    private final UserStoreRepository userStoreRepository;

    public OvertimeRequestController(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            EmployeeShiftRepository employeeShiftRepository,
            OvertimeRequestRepository overtimeRequestRepository,
            ShiftRepository shiftRepository,
            UserStoreRepository userStoreRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.overtimeRequestRepository = overtimeRequestRepository;
        this.shiftRepository = shiftRepository;
        this.userStoreRepository = userStoreRepository;
    }

    private Employee resolveEmployee(Principal principal) {
        if (principal == null) return null;
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) return null;
        return employeeRepository.findByUserId(userOpt.get().getId()).orElse(null);
    }

    private Long resolveStoreId(Principal principal) {
        if (principal == null) return null;
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) return null;
        List<UserStore> userStores = userStoreRepository.findByIdUserId(userOpt.get().getId());
        if (userStores.isEmpty() || userStores.get(0).getStore() == null) return null;
        return userStores.get(0).getStore().getId();
    }

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("EEE, dd MMM");

    /**
     * POST /api/v1/overtime-requests
     * Submit an overtime shift request.
     */
    @PostMapping
    @Transactional
    public ResponseEntity<ApiResponse<Void>> createRequest(
            @RequestBody Map<String, Object> payload,
            Principal principal) {
        Employee employee = resolveEmployee(principal);
        if (employee == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Unable to resolve store"));
        }

        LocalDate requestedDate = LocalDate.parse((String) payload.get("requestedDate"));
        Long shiftId = ((Number) payload.get("shiftId")).longValue();
        String reason = (String) payload.get("reason");

        LocalDate today = LocalDate.now();
        if (!requestedDate.isAfter(today)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Overtime requests must be submitted for future dates."));
        }

        // Validate shift exists
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new IllegalArgumentException("Shift not found"));

        // Check if employee already has a shift assigned on that date
        List<EmployeeShift> overlapping = employeeShiftRepository.findOverlapping(employee.getId(), requestedDate, requestedDate);
        if (!overlapping.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("You are already assigned to a shift on " + requestedDate.format(DISPLAY_FMT) + "."));
        }

        // Save new overtime request
        OvertimeRequest request = OvertimeRequest.builder()
                .employee(employee)
                .storeId(storeId)
                .requestedDate(requestedDate)
                .shift(shift)
                .reason(reason)
                .status("PENDING")
                .build();

        overtimeRequestRepository.save(request);
        return ResponseEntity.ok(ApiResponse.success("Overtime request submitted for supervisor approval.", null));
    }

    /**
     * GET /api/v1/overtime-requests/my
     * Returns overtime requests submitted by the logged-in employee.
     */
    @GetMapping("/my")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyRequests(Principal principal) {
        Employee employee = resolveEmployee(principal);
        if (employee == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<OvertimeRequest> requests = overtimeRequestRepository.findByEmployeeIdOrderByCreatedAtDesc(employee.getId());
        List<Map<String, Object>> result = new ArrayList<>();

        for (OvertimeRequest r : requests) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("requestedDate", r.getRequestedDate().format(DATE_FMT));
            m.put("requestedDateDisplay", r.getRequestedDate().format(DISPLAY_FMT));
            m.put("shiftName", r.getShift().getName());
            m.put("shiftId", r.getShift().getId());
            m.put("reason", r.getReason());
            m.put("status", r.getStatus());
            result.add(m);
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * GET /api/v1/overtime-requests/pending
     * Returns pending overtime requests for the supervisor's store.
     */
    @GetMapping("/pending")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPendingRequests(Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<OvertimeRequest> requests = overtimeRequestRepository.findByStoreIdAndStatusOrderByCreatedAtDesc(storeId, "PENDING");
        List<Map<String, Object>> result = new ArrayList<>();

        for (OvertimeRequest r : requests) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("employeeName", r.getEmployee().getFirstName() + " " + (r.getEmployee().getLastName() != null ? r.getEmployee().getLastName() : ""));
            m.put("requestedDate", r.getRequestedDate().format(DATE_FMT));
            m.put("requestedDateDisplay", r.getRequestedDate().format(DISPLAY_FMT));
            m.put("shiftName", r.getShift().getName());
            m.put("shiftId", r.getShift().getId());
            m.put("reason", r.getReason());
            m.put("status", r.getStatus());
            result.add(m);
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * PUT /api/v1/overtime-requests/{id}/approve
     * Approves the request and automatically creates the EmployeeShift record.
     */
    @PutMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> approveRequest(
            @PathVariable Long id,
            Principal principal) {
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Optional<OvertimeRequest> requestOpt = overtimeRequestRepository.findById(id);
        if (requestOpt.isEmpty() || !"PENDING".equals(requestOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Pending overtime request not found."));
        }

        OvertimeRequest req = requestOpt.get();
        req.setStatus("APPROVED");
        req.setResolvedAt(LocalDateTime.now());
        req.setResolvedBy(userOpt.get());
        overtimeRequestRepository.save(req);

        // Automatically assign the shift
        EmployeeShift.EmployeeShiftId shiftId = new EmployeeShift.EmployeeShiftId(
                req.getEmployee().getId(),
                req.getShift().getId(),
                req.getRequestedDate());
        EmployeeShift es = new EmployeeShift(shiftId, req.getEmployee(), req.getShift(), req.getRequestedDate());
        employeeShiftRepository.save(es);

        return ResponseEntity.ok(ApiResponse.success("Overtime request approved. Roster updated.", null));
    }

    /**
     * PUT /api/v1/overtime-requests/{id}/deny
     * Denies the overtime request.
     */
    @PutMapping("/{id}/deny")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> denyRequest(
            @PathVariable Long id,
            Principal principal) {
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Optional<OvertimeRequest> requestOpt = overtimeRequestRepository.findById(id);
        if (requestOpt.isEmpty() || !"PENDING".equals(requestOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Pending overtime request not found."));
        }

        OvertimeRequest req = requestOpt.get();
        req.setStatus("DENIED");
        req.setResolvedAt(LocalDateTime.now());
        req.setResolvedBy(userOpt.get());
        overtimeRequestRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success("Overtime request denied.", null));
    }
}
