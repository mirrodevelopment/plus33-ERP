package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.EmployeeShift;
import com.plus33.erp.workforce.entity.EmployeeShiftSwap;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.EmployeeShiftRepository;
import com.plus33.erp.workforce.repository.EmployeeShiftSwapRepository;
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

@RestController
@RequestMapping("/api/v1/shift-swaps")
public class ShiftSwapController {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final EmployeeShiftSwapRepository employeeShiftSwapRepository;
    private final ShiftRepository shiftRepository;
    private final UserStoreRepository userStoreRepository;

    public ShiftSwapController(
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            EmployeeShiftRepository employeeShiftRepository,
            EmployeeShiftSwapRepository employeeShiftSwapRepository,
            ShiftRepository shiftRepository,
            UserStoreRepository userStoreRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.employeeShiftSwapRepository = employeeShiftSwapRepository;
        this.shiftRepository = shiftRepository;
        this.userStoreRepository = userStoreRepository;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

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

    /** Builds the list of employees for a given store (by storeId). */
    private List<Employee> resolveStoreEmployees(Long storeId) {
        List<UserStore> storeUsers = userStoreRepository.findAll().stream()
                .filter(us -> us.getStore() != null && us.getStore().getId().equals(storeId))
                .collect(Collectors.toList());
        Set<Long> userIds = storeUsers.stream().map(us -> us.getId().getUserId()).collect(Collectors.toSet());
        return employeeRepository.findAll().stream()
                .filter(e -> e.getUser() != null && userIds.contains(e.getUser().getId()))
                .collect(Collectors.toList());
    }

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_FMT = DateTimeFormatter.ofPattern("EEE, dd MMM");

    // -------------------------------------------------------------------------
    // Employee endpoints
    // -------------------------------------------------------------------------

    /**
     * GET /api/v1/shift-swaps/my-requests
     * Returns all swap requests submitted by the logged-in employee.
     */
    @GetMapping("/my-requests")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyRequests(Principal principal) {
        Employee employee = resolveEmployee(principal);
        if (employee == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<EmployeeShiftSwap> swaps = employeeShiftSwapRepository.findByEmployeeIdOrderByCreatedAtDesc(employee.getId());
        List<Map<String, Object>> result = new ArrayList<>();

        for (EmployeeShiftSwap s : swaps) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", s.getId());
            m.put("shiftDate", s.getShiftDate().format(FMT));
            m.put("myShiftDate", s.getShiftDate().format(DISPLAY_FMT));
            m.put("myShiftType", s.getCurrentShift().getName());
            m.put("currentShiftName", s.getCurrentShift().getName());
            m.put("preferredShiftName", s.getPreferredShift().getName());
            m.put("preferredDate", s.getPreferredDate() != null ? s.getPreferredDate().format(FMT) : null);
            m.put("preferredDateDisplay", s.getPreferredDate() != null ? s.getPreferredDate().format(DISPLAY_FMT) : null);
            m.put("approvedDate", s.getApprovedDate() != null ? s.getApprovedDate().format(FMT) : null);
            m.put("approvedDateDisplay", s.getApprovedDate() != null ? s.getApprovedDate().format(DISPLAY_FMT) : null);
            m.put("approvedShiftName", s.getApprovedShift() != null ? s.getApprovedShift().getName() : null);
            m.put("status", s.getStatus());
            m.put("rejectionReason", s.getRejectionReason());
            m.put("adminRejectionReason", s.getAdminRejectionReason());
            // legacy bridge
            m.put("peerShiftDate", s.getPreferredShift().getName());
            result.add(m);
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * POST /api/v1/shift-swaps
     * Employee submits a new shift swap request.
     *
     * Body: { shiftDate, currentShiftId, preferredShiftId, preferredDate }
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

        LocalDate shiftDate = LocalDate.parse((String) payload.get("shiftDate"));
        Long currentShiftId = ((Number) payload.get("currentShiftId")).longValue();
        Long preferredShiftId = ((Number) payload.get("preferredShiftId")).longValue();

        // Parse preferred date (optional — defaults to shiftDate if not provided)
        LocalDate preferredDate = null;
        if (payload.get("preferredDate") != null && !((String) payload.get("preferredDate")).isBlank()) {
            preferredDate = LocalDate.parse((String) payload.get("preferredDate"));
        }

        LocalDate today = LocalDate.now();

        // 1. Shift date must be strictly in the future (cannot swap today's shift)
        if (!shiftDate.isAfter(today)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Swap requests are only allowed before the day of the shift starts."));
        }

        // 2. Shift date must be within next 7 days
        LocalDate maxFutureDate = today.plusDays(7);
        if (shiftDate.isAfter(maxFutureDate)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Swap requests can only be made for shifts in the next 7 days."));
        }

        // 3. Validate preferred date window (must be tomorrow → today+7)
        if (preferredDate != null) {
            if (!preferredDate.isAfter(today)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Preferred date must be a future date."));
            }
            if (preferredDate.isAfter(maxFutureDate)) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Preferred date must be within the next 7 days."));
            }
        }

        // 4. Employee must have a shift matching currentShiftId on shiftDate
        Optional<EmployeeShift> empShiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), shiftDate);
        if (empShiftOpt.isEmpty() || !empShiftOpt.get().getShift().getId().equals(currentShiftId)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("You do not have this shift assigned on the selected date."));
        }

        // 5. No duplicate active/approved swap for this date
        boolean alreadyHasActiveRequest =
                employeeShiftSwapRepository.existsByEmployeeIdAndShiftDateAndStatus(employee.getId(), shiftDate, "PENDING")
                || employeeShiftSwapRepository.existsByEmployeeIdAndShiftDateAndStatus(employee.getId(), shiftDate, "ESCALATED")
                || employeeShiftSwapRepository.existsByEmployeeIdAndShiftDateAndStatus(employee.getId(), shiftDate, "APPROVED");
        if (alreadyHasActiveRequest) {
            return ResponseEntity.badRequest().body(ApiResponse.error("A swap request for this shift already exists or has been approved."));
        }

        Shift currentShift = shiftRepository.findById(currentShiftId)
                .orElseThrow(() -> new IllegalArgumentException("Current shift not found"));
        Shift preferredShift = shiftRepository.findById(preferredShiftId)
                .orElseThrow(() -> new IllegalArgumentException("Preferred shift not found"));

        EmployeeShiftSwap swap = EmployeeShiftSwap.builder()
                .employee(employee)
                .shiftDate(shiftDate)
                .currentShift(currentShift)
                .preferredShift(preferredShift)
                .preferredDate(preferredDate)
                .status("PENDING")
                .build();

        employeeShiftSwapRepository.save(swap);
        return ResponseEntity.ok(ApiResponse.success("Roster swap request submitted for supervisor approval.", null));
    }

    /**
     * POST /api/v1/shift-swaps/{id}/escalate
     * Employee escalates a REJECTED swap to Store Administrator.
     */
    @PostMapping("/{id}/escalate")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> escalateRequest(
            @PathVariable Long id,
            Principal principal) {
        Employee employee = resolveEmployee(principal);
        if (employee == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Optional<EmployeeShiftSwap> swapOpt = employeeShiftSwapRepository.findById(id);
        if (swapOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Request not found"));
        }

        EmployeeShiftSwap swap = swapOpt.get();
        if (!swap.getEmployee().getId().equals(employee.getId())) {
            return ResponseEntity.status(403).body(ApiResponse.error("Forbidden"));
        }

        if (!"REJECTED".equals(swap.getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Only rejected requests can be escalated."));
        }

        swap.setStatus("ESCALATED");
        employeeShiftSwapRepository.save(swap);
        return ResponseEntity.ok(ApiResponse.success("Request escalated to Store Administrator.", null));
    }

    // -------------------------------------------------------------------------
    // Supervisor endpoints
    // -------------------------------------------------------------------------

    /**
     * GET /api/v1/shift-swaps/store-requests
     * Returns all PENDING swap requests for employees in the supervisor's store.
     * Also returns list of store employees for the replacement picker.
     */
    @GetMapping("/store-requests")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStoreRequests(Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unable to resolve store"));
        }

        List<Employee> storeEmps = resolveStoreEmployees(storeId);
        List<Long> empIds = storeEmps.stream().map(Employee::getId).collect(Collectors.toList());

        List<EmployeeShiftSwap> swaps = employeeShiftSwapRepository.findByEmployeeIdInAndStatusOrderByCreatedAtDesc(empIds, "PENDING");
        List<Map<String, Object>> swapList = buildSwapSummaryList(swaps);

        // Build replacement employee options (all store employees)
        List<Map<String, Object>> employeeOptions = storeEmps.stream().map(e -> {
            Map<String, Object> em = new LinkedHashMap<>();
            em.put("id", e.getId());
            em.put("name", e.getFirstName() + " " + (e.getLastName() != null ? e.getLastName() : ""));
            em.put("employeeCode", e.getEmployeeCode());
            return em;
        }).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("swaps", swapList);
        response.put("storeEmployees", employeeOptions);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /api/v1/shift-swaps/{id}/approve
     * Supervisor approves a PENDING swap request.
     *
     * Body (all optional):
     * {
     *   "approvedDate": "2026-07-22",         // override employee's preferredDate
     *   "approvedShiftId": 2,                 // override employee's preferredShift
     *   "replacementEmployeeId": 15           // fill the vacated shift slot
     * }
     */
    @PostMapping("/{id}/approve")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> approveRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> payload,
            Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Optional<EmployeeShiftSwap> swapOpt = employeeShiftSwapRepository.findById(id);
        if (swapOpt.isEmpty() || !"PENDING".equals(swapOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Pending swap request not found"));
        }

        EmployeeShiftSwap swap = swapOpt.get();
        return processApproval(swap, payload, "Swap request approved and shift assigned.");
    }

    /**
     * POST /api/v1/shift-swaps/{id}/reject
     * Supervisor rejects a PENDING swap request with a mandatory reason.
     */
    @PostMapping("/{id}/reject")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> rejectRequest(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload,
            Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String reason = (String) payload.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Rejection reason is mandatory."));
        }

        Optional<EmployeeShiftSwap> swapOpt = employeeShiftSwapRepository.findById(id);
        if (swapOpt.isEmpty() || !"PENDING".equals(swapOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Pending swap request not found"));
        }

        EmployeeShiftSwap swap = swapOpt.get();
        swap.setStatus("REJECTED");
        swap.setRejectionReason(reason.trim());
        employeeShiftSwapRepository.save(swap);

        return ResponseEntity.ok(ApiResponse.success("Swap request rejected.", null));
    }

    // -------------------------------------------------------------------------
    // Store Admin (escalated) endpoints
    // -------------------------------------------------------------------------

    /**
     * GET /api/v1/shift-swaps/escalated-requests
     * Returns all ESCALATED swap requests for the admin's store.
     */
    @GetMapping("/escalated-requests")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEscalatedRequests(Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        List<Employee> storeEmps = resolveStoreEmployees(storeId);
        List<Long> empIds = storeEmps.stream().map(Employee::getId).collect(Collectors.toList());

        List<EmployeeShiftSwap> swaps = employeeShiftSwapRepository.findByEmployeeIdInAndStatusOrderByCreatedAtDesc(empIds, "ESCALATED");
        List<Map<String, Object>> swapList = buildSwapSummaryList(swaps);

        List<Map<String, Object>> employeeOptions = storeEmps.stream().map(e -> {
            Map<String, Object> em = new LinkedHashMap<>();
            em.put("id", e.getId());
            em.put("name", e.getFirstName() + " " + (e.getLastName() != null ? e.getLastName() : ""));
            em.put("employeeCode", e.getEmployeeCode());
            return em;
        }).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("swaps", swapList);
        response.put("storeEmployees", employeeOptions);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * POST /api/v1/shift-swaps/{id}/admin-approve
     * Store admin approves an ESCALATED swap request with optional override and replacement.
     */
    @PostMapping("/{id}/admin-approve")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> adminApproveRequest(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> payload,
            Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        Optional<EmployeeShiftSwap> swapOpt = employeeShiftSwapRepository.findById(id);
        if (swapOpt.isEmpty() || !"ESCALATED".equals(swapOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Escalated swap request not found"));
        }

        EmployeeShiftSwap swap = swapOpt.get();
        return processApproval(swap, payload, "Escalated swap request approved by admin.");
    }

    /**
     * POST /api/v1/shift-swaps/{id}/admin-reject
     * Store admin rejects an ESCALATED swap with a mandatory reason.
     */
    @PostMapping("/{id}/admin-reject")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> adminRejectRequest(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload,
            Principal principal) {
        Long storeId = resolveStoreId(principal);
        if (storeId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));
        }

        String reason = (String) payload.get("reason");
        if (reason == null || reason.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Rejection reason is mandatory."));
        }

        Optional<EmployeeShiftSwap> swapOpt = employeeShiftSwapRepository.findById(id);
        if (swapOpt.isEmpty() || !"ESCALATED".equals(swapOpt.get().getStatus())) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Escalated swap request not found"));
        }

        EmployeeShiftSwap swap = swapOpt.get();
        swap.setStatus("REJECTED_BY_ADMIN");
        swap.setAdminRejectionReason(reason.trim());
        employeeShiftSwapRepository.save(swap);

        return ResponseEntity.ok(ApiResponse.success("Escalated swap request rejected by admin.", null));
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Core approval logic shared by both supervisor approve and admin-approve.
     *
     * Payload fields (all optional):
     *   approvedDate           — override the employee's preferredDate
     *   approvedShiftId        — override the employee's preferredShift
     *   replacementEmployeeId  — assign a worker to the vacated slot
     */
    private ResponseEntity<ApiResponse<Void>> processApproval(
            EmployeeShiftSwap swap,
            Map<String, Object> payload,
            String successMessage) {

        // 1. Resolve final approved date (supervisor override → employee preference → original shift date)
        LocalDate approvedDate = swap.getShiftDate();
        if (swap.getPreferredDate() != null) {
            approvedDate = swap.getPreferredDate();
        }
        if (payload != null && payload.get("approvedDate") != null) {
            String dateStr = (String) payload.get("approvedDate");
            if (!dateStr.isBlank()) {
                approvedDate = LocalDate.parse(dateStr);
            }
        }

        // 2. Resolve final approved shift (supervisor override → employee preferred shift)
        Shift approvedShift = swap.getPreferredShift();
        if (payload != null && payload.get("approvedShiftId") != null) {
            Long shiftId = ((Number) payload.get("approvedShiftId")).longValue();
            approvedShift = shiftRepository.findById(shiftId)
                    .orElseThrow(() -> new IllegalArgumentException("Approved shift not found: " + shiftId));
        }

        // 3. Resolve optional replacement employee
        Employee replacementEmployee = null;
        if (payload != null && payload.get("replacementEmployeeId") != null) {
            Long replId = ((Number) payload.get("replacementEmployeeId")).longValue();
            replacementEmployee = employeeRepository.findById(replId)
                    .orElseThrow(() -> new IllegalArgumentException("Replacement employee not found: " + replId));
        }

        // 4. Update swap record
        swap.setStatus("APPROVED");
        swap.setApprovedDate(approvedDate);
        swap.setApprovedShift(approvedShift);
        swap.setReplacementEmployee(replacementEmployee);
        employeeShiftSwapRepository.save(swap);

        // 5. Remove swapping employee from original shift slot (shiftDate + currentShift)
        employeeShiftRepository.deleteByIdEmployeeIdAndIdShiftIdAndIdEffectiveFrom(
                swap.getEmployee().getId(),
                swap.getCurrentShift().getId(),
                swap.getShiftDate());

        // 6. Assign swapping employee to approved date + approved shift
        EmployeeShift.EmployeeShiftId newId = new EmployeeShift.EmployeeShiftId(
                swap.getEmployee().getId(), approvedShift.getId(), approvedDate);
        EmployeeShift newShift = new EmployeeShift(newId, swap.getEmployee(), approvedShift, approvedDate);
        employeeShiftRepository.save(newShift);

        // 7. If a replacement was specified → assign them to the vacated slot (shiftDate + currentShift)
        if (replacementEmployee != null) {
            EmployeeShift.EmployeeShiftId replShiftId = new EmployeeShift.EmployeeShiftId(
                    replacementEmployee.getId(),
                    swap.getCurrentShift().getId(),
                    swap.getShiftDate());
            EmployeeShift replacementShift = new EmployeeShift(
                    replShiftId, replacementEmployee, swap.getCurrentShift(), swap.getShiftDate());
            employeeShiftRepository.save(replacementShift);
        }

        return ResponseEntity.ok(ApiResponse.success(successMessage, null));
    }

    /** Builds a uniform swap summary map used in both store-requests and escalated-requests. */
    private List<Map<String, Object>> buildSwapSummaryList(List<EmployeeShiftSwap> swaps) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (EmployeeShiftSwap s : swaps) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", s.getId());
            m.put("employeeId", s.getEmployee().getId());
            m.put("employeeName", s.getEmployee().getFirstName() + " "
                    + (s.getEmployee().getLastName() != null ? s.getEmployee().getLastName() : ""));
            m.put("shiftDate", s.getShiftDate().format(FMT));
            m.put("shiftDateDisplay", s.getShiftDate().format(DISPLAY_FMT));
            m.put("currentShiftName", s.getCurrentShift().getName());
            m.put("currentShiftId", s.getCurrentShift().getId());
            m.put("preferredShiftName", s.getPreferredShift().getName());
            m.put("preferredShiftId", s.getPreferredShift().getId());
            m.put("preferredDate", s.getPreferredDate() != null ? s.getPreferredDate().format(FMT) : null);
            m.put("preferredDateDisplay", s.getPreferredDate() != null ? s.getPreferredDate().format(DISPLAY_FMT) : null);
            m.put("rejectionReason", s.getRejectionReason());
            m.put("status", s.getStatus());
            result.add(m);
        }
        return result;
    }
}
