package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Attendance;
import com.plus33.erp.workforce.entity.AwayPermissionRequest;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.AttendanceRepository;
import com.plus33.erp.workforce.repository.AwayPermissionRepository;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p>REST controller for the Away Permission workflow.
 * Employees request to leave the store geofence; supervisors and store admins approve/deny with
 * a custom time limit. A 10-minute grace buffer applies after expiry before auto clock-out.</p>
 *
 * <p>Endpoints:
 * <ul>
 *   <li>{@code POST  /api/v1/away-permission/request}     — Employee submits request</li>
 *   <li>{@code PUT   /api/v1/away-permission/{id}/approve} — Supervisor/Admin approves + sets duration</li>
 *   <li>{@code PUT   /api/v1/away-permission/{id}/deny}    — Supervisor/Admin denies</li>
 *   <li>{@code POST  /api/v1/away-permission/{id}/extend}  — Employee requests extension during grace period</li>
 *   <li>{@code GET   /api/v1/away-permission/pending}      — Supervisor/Admin sees pending requests for their store</li>
 *   <li>{@code GET   /api/v1/away-permission/my}           — Employee sees their own requests for today</li>
 * </ul>
 * </p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@CrossOrigin
@Transactional
@RequestMapping("/api/v1/away-permission")
public class AwayPermissionController {

    private final AwayPermissionRepository awayPermissionRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;
    private final AttendanceRepository attendanceRepository;

    public AwayPermissionController(
            AwayPermissionRepository awayPermissionRepository,
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            UserStoreRepository userStoreRepository,
            AttendanceRepository attendanceRepository) {
        this.awayPermissionRepository = awayPermissionRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.userStoreRepository = userStoreRepository;
        this.attendanceRepository = attendanceRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Employee endpoints
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Employee submits an away permission request.
     * Must be currently clocked in. Only one PENDING request allowed per attendance session.
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestAwayPass(
            @RequestBody(required = false) Map<String, Object> body, Principal principal) {
        Employee employee = resolveEmployee(principal);
        LocalDate today = LocalDate.now();

        Attendance att = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today)
                .orElseThrow(() -> new BusinessException("You must be clocked in to request an away pass."));
        if (att.getCheckInTime() == null || att.getCheckOutTime() != null) {
            throw new BusinessException("You must be clocked in to request an away pass.");
        }

        // Prevent duplicate PENDING requests
        if (awayPermissionRepository.existsByAttendanceIdAndStatus(att.getId(), "PENDING")) {
            throw new BusinessException("You already have a pending away pass request. Please wait for a response.");
        }

        Store store = resolveEmployeeStore(employee);
        if (store == null) {
            throw new BusinessException("You are not assigned to a store.");
        }

        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : null;

        AwayPermissionRequest req = new AwayPermissionRequest();
        req.setEmployee(employee);
        req.setStore(store);
        req.setAttendance(att);
        req.setStatus("PENDING");
        req.setReason(reason);
        req.setRequestedAt(LocalDateTime.now());
        awayPermissionRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success(buildRequestMap(req)));
    }

    /**
     * Employee requests an extension of an expired (grace-period) away pass.
     */
    @PostMapping("/{id}/extend")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestExtension(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> body,
            Principal principal) {
        Employee employee = resolveEmployee(principal);
        AwayPermissionRequest req = awayPermissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Away pass request not found."));

        if (!req.getEmployee().getId().equals(employee.getId())) {
            throw new BusinessException("You can only extend your own away pass.");
        }
        if (!"EXTENSION_REQUESTED".equals(req.getStatus()) && !"APPROVED".equals(req.getStatus())) {
            throw new BusinessException("This away pass cannot be extended in its current state: " + req.getStatus());
        }
        if (!req.isInGracePeriod() && !req.isActive()) {
            throw new BusinessException("The grace period for this away pass has already expired.");
        }

        String reason = body != null && body.get("reason") != null ? body.get("reason").toString() : null;
        req.setStatus("EXTENSION_REQUESTED");
        req.setExtensionRequestedAt(LocalDateTime.now());
        req.setExtensionReason(reason);
        awayPermissionRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success(buildRequestMap(req)));
    }

    /**
     * Employee ends the away pass manually when they return to the store early.
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse<Map<String, Object>>> returnFromAway(
            @PathVariable Long id,
            Principal principal) {
        Employee employee = resolveEmployee(principal);
        AwayPermissionRequest req = awayPermissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Away pass request not found."));

        if (!req.getEmployee().getId().equals(employee.getId())) {
            throw new BusinessException("You can only return from your own away pass.");
        }
        if (!"APPROVED".equals(req.getStatus()) && !"EXTENSION_REQUESTED".equals(req.getStatus())) {
            throw new BusinessException("You can only complete an approved away pass.");
        }

        req.setStatus("RETURNED");
        req.setResolvedAt(LocalDateTime.now());
        awayPermissionRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success(buildRequestMap(req)));
    }

    /**
     * Employee views their own away permission requests for today's attendance.
     */
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> myRequests(Principal principal) {
        Employee employee = resolveEmployee(principal);
        LocalDate today = LocalDate.now();

        Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(employee.getId(), today);
        if (attOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
        }

        List<AwayPermissionRequest> reqs = awayPermissionRepository.findByAttendanceIdOrderByRequestedAtAsc(attOpt.get().getId());
        List<Map<String, Object>> result = new ArrayList<>();
        for (AwayPermissionRequest r : reqs) result.add(buildRequestMap(r));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Supervisor / Store Admin endpoints
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns all PENDING and EXTENSION_REQUESTED away pass requests for the supervisor's store.
     * Visible on the Shift Planner page and on the supervisor dashboard.
     */
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPending(Principal principal) {
        User user = resolveUser(principal);
        Store store = resolveSupervisorStore(user);

        List<AwayPermissionRequest> reqs = awayPermissionRepository.findPendingForStore(store.getId());
        List<Map<String, Object>> result = new ArrayList<>();
        for (AwayPermissionRequest r : reqs) result.add(buildRequestMap(r));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Returns all active APPROVED away pass requests for the supervisor's store.
     */
    @GetMapping("/ongoing")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getOngoing(Principal principal) {
        User user = resolveUser(principal);
        Store store = resolveSupervisorStore(user);

        List<AwayPermissionRequest> reqs = awayPermissionRepository.findOngoingForStore(store.getId());
        List<Map<String, Object>> result = new ArrayList<>();
        for (AwayPermissionRequest r : reqs) result.add(buildRequestMap(r));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Supervisor/Admin approves an away pass and sets the duration in minutes.
     * The {@code approved_until} timestamp is computed as: now + durationMins.
     * After {@code approved_until}, a 10-minute grace buffer applies before auto clock-out.
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Map<String, Object>>> approve(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            Principal principal) {
        User user = resolveUser(principal);
        AwayPermissionRequest req = awayPermissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Away pass request not found."));

        if (!isAuthorisedForStore(user, req.getStore())) {
            throw new BusinessException("You are not authorised to manage away passes for this store.");
        }

        if (!"PENDING".equals(req.getStatus()) && !"EXTENSION_REQUESTED".equals(req.getStatus())) {
            throw new BusinessException("This request is already resolved: " + req.getStatus());
        }

        int durationMins;
        try {
            durationMins = Integer.parseInt(body.getOrDefault("durationMins", "30").toString());
            if (durationMins <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new BusinessException("Please provide a valid duration in minutes (e.g. 30, 60).");
        }

        LocalDateTime now = LocalDateTime.now();
        req.setStatus("APPROVED");
        req.setResolvedAt(now);
        req.setResolvedBy(user);
        req.setApprovedDurationMins(durationMins);
        req.setApprovedUntil(now.plusMinutes(durationMins));
        req.setGraceBufferMins(10); // fixed 10-minute grace buffer
        awayPermissionRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success(buildRequestMap(req)));
    }

    /**
     * Supervisor/Admin denies an away pass request.
     */
    @PutMapping("/{id}/deny")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deny(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, Object> body,
            Principal principal) {
        User user = resolveUser(principal);
        AwayPermissionRequest req = awayPermissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Away pass request not found."));

        if (!isAuthorisedForStore(user, req.getStore())) {
            throw new BusinessException("You are not authorised to manage away passes for this store.");
        }
        if (!"PENDING".equals(req.getStatus()) && !"EXTENSION_REQUESTED".equals(req.getStatus())) {
            throw new BusinessException("This request is already resolved: " + req.getStatus());
        }

        req.setStatus("DENIED");
        req.setResolvedAt(LocalDateTime.now());
        req.setResolvedBy(user);
        awayPermissionRepository.save(req);

        return ResponseEntity.ok(ApiResponse.success(buildRequestMap(req)));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private Map<String, Object> buildRequestMap(AwayPermissionRequest r) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", r.getId());
        m.put("status", r.getStatus());
        m.put("reason", r.getReason());
        m.put("requestedAt", r.getRequestedAt() != null ? r.getRequestedAt().toString() : null);
        m.put("resolvedAt", r.getResolvedAt() != null ? r.getResolvedAt().toString() : null);
        m.put("approvedDurationMins", r.getApprovedDurationMins());
        m.put("approvedUntil", r.getApprovedUntil() != null ? r.getApprovedUntil().toString() : null);
        m.put("graceUntil", r.getApprovedUntil() != null
                ? r.getApprovedUntil().plusMinutes(r.getGraceBufferMins() != null ? r.getGraceBufferMins() : 10).toString()
                : null);
        m.put("graceBufferMins", r.getGraceBufferMins());
        m.put("isActive", r.isActive());
        m.put("isInGracePeriod", r.isInGracePeriod());
        m.put("extensionRequestedAt", r.getExtensionRequestedAt() != null ? r.getExtensionRequestedAt().toString() : null);
        m.put("extensionReason", r.getExtensionReason());
        m.put("storeName", r.getStore() != null ? r.getStore().getName() : null);
        if (r.getEmployee() != null && r.getEmployee().getUser() != null) {
            m.put("employeeName", r.getEmployee().getUser().getFirstName() + " " + r.getEmployee().getUser().getLastName());
        }
        return m;
    }

    private Employee resolveEmployee(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User not found."));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("Employee record not found."));
    }

    private User resolveUser(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User not found."));
    }

    private Store resolveEmployeeStore(Employee employee) {
        if (employee == null || employee.getUser() == null) return null;
        List<UserStore> userStores = userStoreRepository.findByIdUserId(employee.getUser().getId());
        if (userStores == null || userStores.isEmpty()) return null;
        return userStores.get(0).getStore();
    }

    private Store resolveSupervisorStore(User user) {
        List<UserStore> userStores = userStoreRepository.findByIdUserId(user.getId());
        if (userStores == null || userStores.isEmpty()) {
            throw new BusinessException("You are not assigned to any store.");
        }
        return userStores.get(0).getStore();
    }

    private boolean isAuthorisedForStore(User user, Store store) {
        if (store == null) return false;
        List<UserStore> userStores = userStoreRepository.findByIdUserId(user.getId());
        return userStores.stream().anyMatch(us -> us.getStore().getId().equals(store.getId()));
    }
}
