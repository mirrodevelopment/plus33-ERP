package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.repository.*;
import com.plus33.erp.workforce.service.LeaveServiceImpl;
import com.plus33.erp.workforce.service.LeavePolicyService;
import com.plus33.erp.workforce.service.WorkingDayCalculatorService;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.entity.Role;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

/**
 * PLUS33 ERP — Enterprise Leave Management REST Controller
 * Base path: /api/v1/leaves
 */
@RestController
@RequestMapping("/api/v1/leaves")
@Transactional
public class LeaveController {

    private final LeaveServiceImpl leaveService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveRepository leaveRepository;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final HolidayCalendarRepository holidayCalendarRepository;
    private final HolidayRepository holidayRepository;
    private final LeavePolicyRuleRepository leavePolicyRuleRepository;
    private final LeavePayRuleRepository leavePayRuleRepository;
    private final WeeklyOffRuleRepository weeklyOffRuleRepository;
    private final LeavePolicyGroupRepository leavePolicyGroupRepository;
    private final LeavePolicyChangeLogRepository leavePolicyChangeLogRepository;
    private final LeaveBlackoutDateRepository blackoutRepository;
    private final LeaveAuditLogRepository auditLogRepository;
    private final LeaveDocumentRepository documentRepository;
    private final WorkingDayCalculatorService workingDayCalculator;
    private final UserRepository userRepository;
    private final com.plus33.erp.workforce.repository.EmployeeRepository employeeRepository;
    private final LeavePolicyService leavePolicyService;
    private final UserStoreRepository userStoreRepository;

    public LeaveController(
            LeaveServiceImpl leaveService,
            LeaveTypeRepository leaveTypeRepository,
            EmployeeLeaveRepository leaveRepository,
            EmployeeLeaveBalanceRepository balanceRepository,
            HolidayCalendarRepository holidayCalendarRepository,
            HolidayRepository holidayRepository,
            LeavePolicyRuleRepository leavePolicyRuleRepository,
            LeavePayRuleRepository leavePayRuleRepository,
            WeeklyOffRuleRepository weeklyOffRuleRepository,
            LeavePolicyGroupRepository leavePolicyGroupRepository,
            LeavePolicyChangeLogRepository leavePolicyChangeLogRepository,
            LeaveBlackoutDateRepository blackoutRepository,
            LeaveAuditLogRepository auditLogRepository,
            LeaveDocumentRepository documentRepository,
            WorkingDayCalculatorService workingDayCalculator,
            UserRepository userRepository,
            com.plus33.erp.workforce.repository.EmployeeRepository employeeRepository,
            LeavePolicyService leavePolicyService,
            UserStoreRepository userStoreRepository) {
        this.leaveService = leaveService;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveRepository = leaveRepository;
        this.balanceRepository = balanceRepository;
        this.holidayCalendarRepository = holidayCalendarRepository;
        this.holidayRepository = holidayRepository;
        this.leavePolicyRuleRepository = leavePolicyRuleRepository;
        this.leavePayRuleRepository = leavePayRuleRepository;
        this.weeklyOffRuleRepository = weeklyOffRuleRepository;
        this.leavePolicyGroupRepository = leavePolicyGroupRepository;
        this.leavePolicyChangeLogRepository = leavePolicyChangeLogRepository;
        this.blackoutRepository = blackoutRepository;
        this.auditLogRepository = auditLogRepository;
        this.documentRepository = documentRepository;
        this.workingDayCalculator = workingDayCalculator;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.leavePolicyService = leavePolicyService;
        this.userStoreRepository = userStoreRepository;
    }

    // =========================================================================
    // GET /api/v1/leaves/types — All active leave types
    // =========================================================================
    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getLeaveTypes(Principal principal) {
        String countryCode = "FR";
        Employee employee = null;
        if (principal != null) {
            String email = principal.getName();
            Optional<com.plus33.erp.security.entity.User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                Optional<Employee> empOpt = employeeRepository.findByUserId(userOpt.get().getId());
                if (empOpt.isPresent()) {
                    employee = empOpt.get();
                    countryCode = resolveCountryCode(employee);
                }
            }
        }

        final String finalCountryCode = countryCode;
        List<LeaveType> types = leaveTypeRepository.findAll().stream()
                .filter(lt -> Boolean.TRUE.equals(lt.getActive()))
                .sorted(Comparator.comparing(LeaveType::getName))
                .collect(java.util.stream.Collectors.toList());

        List<Map<String, Object>> result = types.stream().map(lt -> {
            LeavePolicyService.ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, lt, finalCountryCode, LocalDate.now());
            
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", lt.getId());
            m.put("code", lt.getCode());
            m.put("name", lt.getName());
            m.put("paid", policy.isPaid);
            m.put("annualLimit", policy.annualEntitlement != null ? policy.annualEntitlement.doubleValue() : null);
            m.put("approvalLevel", policy.approvalLevel);
            m.put("protected", policy.isProtected);
            m.put("requiresDocument", policy.requiresDocument);
            m.put("monthlyAccrual", policy.monthlyAccrual != null ? policy.monthlyAccrual.doubleValue() : null);
            m.put("maxConsecutiveDays", policy.maxConsecutiveDays);
            m.put("minNoticeDays", policy.minNoticeDays);
            return m;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // GET /api/v1/leaves/my — Own leave history
    // =========================================================================
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyLeaves(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = resolveUser(principal);
        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
        }
        List<EmployeeLeave> leaves = leaveService.getMyLeaves(empOpt.get().getId());
        return ResponseEntity.ok(ApiResponse.success(mapLeaves(leaves)));
    }

    // =========================================================================
    // GET /api/v1/leaves/my/balance — Own leave balances
    // =========================================================================
    @GetMapping("/my/balance")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getMyBalance(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = resolveUser(principal);
        Optional<Employee> empOpt = employeeRepository.findByUserId(user.getId());
        if (empOpt.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success(Collections.emptyList()));
        }
        List<EmployeeLeaveBalance> balances = leaveService.getMyBalance(empOpt.get().getId());

        List<Map<String, Object>> result = balances.stream().map(b -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("leaveTypeId", b.getLeaveType().getId());
            m.put("leaveTypeCode", b.getLeaveType().getCode());
            m.put("leaveTypeName", b.getLeaveType().getName());
            m.put("year", b.getYear());
            m.put("openingBalance", b.getOpeningBalance());
            m.put("accrued", b.getAccrued());
            m.put("carryForward", b.getCarryForward());
            m.put("used", b.getUsed());
            m.put("pending", b.getPending());
            m.put("remaining", b.getRemaining());
            return m;
        }).collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // POST /api/v1/leaves — Submit leave request
    // =========================================================================
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitLeave(
            @RequestBody Map<String, Object> body, Principal principal) {
        Employee employee = resolveEmployee(principal);
        Long leaveTypeId = Long.parseLong(body.get("leaveTypeId").toString());
        LocalDate startDate = LocalDate.parse(body.get("startDate").toString());
        LocalDate endDate = LocalDate.parse(body.get("endDate").toString());
        String session = body.getOrDefault("session", "FULL_DAY").toString();
        String reason = body.getOrDefault("reason", "").toString();

        EmployeeLeave leave = leaveService.submitLeave(employee, leaveTypeId, startDate, endDate, session, reason);
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/cancel — Cancel PENDING leave
    // =========================================================================
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cancelLeave(
            @PathVariable Long id, @RequestBody Map<String, Object> body, Principal principal) {
        User user = resolveUser(principal);
        String reason = body.getOrDefault("reason", "").toString();
        EmployeeLeave leave = leaveService.cancelLeave(id, user.getId(), reason);
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/request-cancellation
    // =========================================================================
    @PutMapping("/{id}/request-cancellation")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestCancellation(
            @PathVariable Long id, @RequestBody Map<String, Object> body, Principal principal) {
        User user = resolveUser(principal);
        String reason = body.getOrDefault("reason", "").toString();
        EmployeeLeave leave = leaveService.requestCancellation(id, user.getId(), reason);
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // POST /api/v1/leaves/{id}/documents — Upload document
    // =========================================================================
    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadDocument(
            @PathVariable Long id, @RequestBody Map<String, Object> body, Principal principal) {
        User user = resolveUser(principal);
        String fileName = body.getOrDefault("fileName", "document").toString();
        String filePath = body.getOrDefault("filePath", "").toString();
        String mimeType = body.getOrDefault("mimeType", "application/pdf").toString();
        LeaveDocument doc = leaveService.addDocument(id, fileName, filePath, mimeType, user.getId());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", doc.getId());
        result.put("fileName", doc.getFileName());
        result.put("uploadedAt", doc.getUploadedAt());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // POST /api/v1/leaves/{id}/upload-document — Upload binary document file
    // =========================================================================
    @PostMapping(value = "/{id}/upload-document", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadDocumentFile(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            Principal principal) {
        User user = resolveUser(principal);
        
        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty.");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
            throw new BusinessException("File size exceeds the 10MB limit.");
        }

        try {
            // Ensure directory exists
            java.nio.file.Path uploadDir = java.nio.file.Paths.get("storage/documents");
            if (!java.nio.file.Files.exists(uploadDir)) {
                java.nio.file.Files.createDirectories(uploadDir);
            }

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String uniqueName = UUID.randomUUID().toString() + extension;
            java.nio.file.Path targetPath = uploadDir.resolve(uniqueName);
            java.nio.file.Files.copy(file.getInputStream(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "storage/documents/" + uniqueName;
            String mimeType = file.getContentType();
            if (mimeType == null) mimeType = "application/octet-stream";

            LeaveDocument doc = leaveService.addDocument(id, originalName, relativePath, mimeType, user.getId());

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", doc.getId());
            result.put("fileName", doc.getFileName());
            result.put("filePath", doc.getFilePath());
            result.put("uploadedAt", doc.getUploadedAt());
            
            return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", result));
        } catch (java.io.IOException e) {
            throw new BusinessException("Failed to save uploaded file: " + e.getMessage());
        }
    }


    // =========================================================================
    // GET /api/v1/leaves/pending — Pending approvals (scoped by role and store-level isolation)
    // =========================================================================
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPendingLeaves(Principal principal) {
        User user = resolveUser(principal);
        String role = user.getRoles().stream().map(Role::getCode).findFirst().orElse("").toUpperCase();

        String approvalLevel = role.contains("SUPERVISOR") ? "SUPERVISOR" :
                               role.contains("ADMIN") ? "STORE_ADMIN" : "SUPERVISOR";

        List<EmployeeLeave> pending = leaveService.getPendingForApprover(approvalLevel);
        List<EmployeeLeave> cancellations = leaveService.getCancellationRequests(approvalLevel);

        List<UserStore> userStores = userStoreRepository.findByIdUserId(user.getId());
        if (!userStores.isEmpty() && !role.contains("ULTIMATE_ADMIN") && !role.contains("NATIONAL_ADMIN") && !role.contains("REGIONAL_ADMIN")) {
            Long storeId = userStores.get(0).getStore().getId();
            pending = pending.stream()
                .filter(l -> {
                    if (l.getEmployee() == null || l.getEmployee().getUser() == null) return true;
                    List<UserStore> empStores = userStoreRepository.findByIdUserId(l.getEmployee().getUser().getId());
                    if (empStores.isEmpty()) return true;
                    return empStores.stream().anyMatch(us -> us.getStore().getId().equals(storeId));
                })
                .toList();

            cancellations = cancellations.stream()
                .filter(l -> {
                    if (l.getEmployee() == null || l.getEmployee().getUser() == null) return true;
                    List<UserStore> empStores = userStoreRepository.findByIdUserId(l.getEmployee().getUser().getId());
                    if (empStores.isEmpty()) return true;
                    return empStores.stream().anyMatch(us -> us.getStore().getId().equals(storeId));
                })
                .toList();
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("pending", mapLeaves(pending));
        result.put("cancellationRequests", mapLeaves(cancellations));
        result.put("totalPending", pending.size());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/approve
    // =========================================================================
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<Map<String, Object>>> approveLeave(
            @PathVariable Long id, @RequestBody Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        User user = resolveUser(principal);
        String role = user.getRoles().stream().map(Role::getCode).findFirst().orElse("SUPERVISOR").toUpperCase();
        String approverRole = role.contains("ADMIN") ? "STORE_ADMIN" : "SUPERVISOR";
        String comment = body.getOrDefault("comment", "").toString();
        String ip = request.getRemoteAddr();

        EmployeeLeave leave = leaveService.approveLeave(id, user.getId(), approverRole, comment, ip);
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/reject
    // =========================================================================
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<Map<String, Object>>> rejectLeave(
            @PathVariable Long id, @RequestBody Map<String, Object> body,
            Principal principal, HttpServletRequest request) {
        User user = resolveUser(principal);
        String role = user.getRoles().stream().map(Role::getCode).findFirst().orElse("SUPERVISOR").toUpperCase();
        String approverRole = role.contains("ADMIN") ? "STORE_ADMIN" : "SUPERVISOR";
        String rejectionReason = body.getOrDefault("rejectionReason", "").toString();
        String ip = request.getRemoteAddr();

        EmployeeLeave leave = leaveService.rejectLeave(id, user.getId(), approverRole, rejectionReason, ip);
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/approve-cancellation
    // =========================================================================
    @PutMapping("/{id}/approve-cancellation")
    public ResponseEntity<ApiResponse<Map<String, Object>>> approveCancellation(
            @PathVariable Long id, Principal principal, HttpServletRequest request) {
        User user = resolveUser(principal);
        EmployeeLeave leave = leaveService.approveCancellation(id, user.getId(), request.getRemoteAddr());
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // PUT /api/v1/leaves/{id}/escalate
    // =========================================================================
    @PutMapping("/{id}/escalate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> escalateLeave(
            @PathVariable Long id, Principal principal) {
        User user = resolveUser(principal);
        EmployeeLeave leave = leaveService.escalateLeave(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(mapLeave(leave)));
    }

    // =========================================================================
    // GET /api/v1/leaves/holidays — Public holidays
    // =========================================================================
    @GetMapping("/holidays")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHolidays(
            @RequestParam(defaultValue = "FR") String countryCode,
            @RequestParam(defaultValue = "2026") Integer year) {
        List<Holiday> holidays = holidayRepository
                .findByCountryCodeAndHolidayYear(countryCode, year);
        List<Map<String, Object>> result = holidays.stream().map(h -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", h.getId());
            m.put("name", h.getHolidayName());
            m.put("date", h.getHolidayDate().toString());
            m.put("isRecurring", false);
            return m;
        }).sorted(Comparator.comparing(m -> m.get("date").toString()))
          .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // POST /api/v1/leaves/holidays — Create or replace a public holiday
    // =========================================================================
    @PostMapping("/holidays")
    public ResponseEntity<ApiResponse<Holiday>> createHoliday(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String dateStr = (String) payload.get("date");
        if (name == null || name.trim().isEmpty() || dateStr == null || dateStr.trim().isEmpty()) {
            throw new BusinessException("Holiday name and date are required");
        }
        LocalDate date = LocalDate.parse(dateStr);
        String countryCode = (String) payload.getOrDefault("countryCode", "FR");
        
        // Remove existing unique conflict if any exists
        holidayRepository.findByCountryCodeAndHolidayYear(countryCode, date.getYear()).stream()
            .filter(item -> item.getHolidayDate().equals(date))
            .forEach(item -> holidayRepository.delete(item));
        
        String policyGroupCode = switch(countryCode.toUpperCase()) {
            case "IN", "IND" -> "INDIA";
            case "AE", "UAE" -> "UAE";
            default -> "EU";
        };
        LeavePolicyGroup policyGroup = leavePolicyGroupRepository.findByCode(policyGroupCode)
            .orElseThrow(() -> new BusinessException("Policy group not found for code: " + policyGroupCode));
            
        HolidayCalendar calendar = holidayCalendarRepository.findByPolicyGroupCodeAndYear(policyGroupCode, date.getYear())
            .orElseGet(() -> {
                HolidayCalendar c = new HolidayCalendar();
                c.setPolicyGroup(policyGroup);
                c.setYear(date.getYear());
                c.setName(policyGroup.getName() + " Calendar " + date.getYear());
                c.setActive(true);
                return holidayCalendarRepository.save(c);
            });

        Holiday h = new Holiday();
        h.setHolidayCalendar(calendar);
        h.setHolidayName(name);
        h.setHolidayDate(date);
        h.setIsWorkingDay(false);
        holidayRepository.save(h);
        
        return ResponseEntity.ok(ApiResponse.success(h));
    }

    // =========================================================================
    // DELETE /api/v1/leaves/holidays/{id} — Delete holiday
    // =========================================================================
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Long id) {
        if (!holidayRepository.existsById(id)) {
            throw new BusinessException("Holiday not found");
        }
        holidayRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    // =========================================================================
    // GET /api/v1/leaves/blackout — Active blackout periods
    // =========================================================================
    @GetMapping("/blackout")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBlackouts() {
        List<LeaveBlackoutDate> blackouts = blackoutRepository.findByCompanyIdAndActiveTrue(1L);
        List<Map<String, Object>> result = blackouts.stream().map(b -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", b.getId());
            m.put("name", b.getName());
            m.put("startDate", b.getStartDate().toString());
            m.put("endDate", b.getEndDate().toString());
            m.put("reason", b.getReason());
            return m;
        }).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // GET /api/v1/leaves/audit/{leaveId} — Audit trail
    // =========================================================================
    @GetMapping("/audit/{leaveId}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAuditLog(@PathVariable Long leaveId) {
        List<LeaveAuditLog> logs = leaveService.getAuditLog(leaveId);
        List<Map<String, Object>> result = logs.stream().map(l -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", l.getId());
            m.put("action", l.getAction());
            m.put("actorUserId", l.getActorUserId());
            m.put("oldValue", l.getOldValue());
            m.put("newValue", l.getNewValue());
            m.put("note", l.getNote());
            m.put("ipAddress", l.getIpAddress());
            m.put("timestamp", l.getCreatedAt());
            return m;
        }).collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // GET /api/v1/leaves/reports/summary
    // =========================================================================
    @GetMapping("/reports/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReportSummary() {
        int year = LocalDate.now().getYear();
        List<EmployeeLeave> all = leaveRepository.findAll();
        long totalPending = all.stream().filter(l -> "PENDING".equals(l.getStatus())).count();
        long totalApproved = all.stream().filter(l -> "APPROVED".equals(l.getStatus())).count();
        long totalRejected = all.stream().filter(l -> "REJECTED".equals(l.getStatus())).count();
        long totalCancelled = all.stream().filter(l -> "CANCELLED".equals(l.getStatus())).count();

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("year", year);
        summary.put("totalPending", totalPending);
        summary.put("totalApproved", totalApproved);
        summary.put("totalRejected", totalRejected);
        summary.put("totalCancelled", totalCancelled);
        summary.put("totalRequests", all.size());
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    private Employee resolveEmployee(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User not found."));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("Employee record not found for this user."));
    }

    private User resolveUser(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User not found."));
    }

    private Map<String, Object> mapLeave(EmployeeLeave l) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", l.getId());
        m.put("leaveType", l.getLeaveType().getName());
        m.put("leaveTypeCode", l.getLeaveType().getCode());

        String countryCode = resolveCountryCode(l.getEmployee());
        LeavePolicyService.ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, l.getLeaveType(), countryCode, l.getStartDate());
        boolean reqDoc = policy.requiresDocument;
        if ("SICK".equals(l.getLeaveType().getCode()) && l.getTotalDays().compareTo(BigDecimal.valueOf(2)) > 0) {
            reqDoc = true;
        }

        m.put("isProtected", policy.isProtected);
        m.put("requiresDocument", reqDoc);
        m.put("startDate", l.getStartDate());
        m.put("endDate", l.getEndDate());
        m.put("totalDays", l.getTotalDays());
        m.put("session", l.getLeaveSession());
        m.put("reason", l.getReason());
        m.put("status", l.getStatus());
        m.put("approverRole", l.getApproverRole());
        m.put("rejectionReason", l.getRejectionReason());
        m.put("cancellationRequested", l.getCancellationRequested());
        m.put("cancellationReason", l.getCancellationReason());
        m.put("approvalDueAt", l.getApprovalDueAt());
        m.put("escalatedAt", l.getEscalatedAt());
        boolean rejectedBySupervisor = false;
        if ("REJECTED".equals(l.getStatus())) {
            rejectedBySupervisor = leaveService.isRejectedBySupervisor(l.getId());
        }
        m.put("rejectedBySupervisor", rejectedBySupervisor);
        m.put("createdAt", l.getCreatedAt());
        if (l.getEmployee() != null) {
            m.put("employeeId", l.getEmployee().getId());
            m.put("employeeName", l.getEmployee().getFirstName() + " " + l.getEmployee().getLastName());
        }
        // Check if document is uploaded
        m.put("hasDocument", documentRepository.existsByLeaveId(l.getId()));
        return m;
    }

    private List<Map<String, Object>> mapLeaves(List<EmployeeLeave> leaves) {
        return leaves.stream().map(this::mapLeave).collect(java.util.stream.Collectors.toList());
    }

    private String resolveCountryCode(Employee employee) {
        if (employee != null) {
            String[] res = leaveService.resolveGroupAndCountry(employee);
            if (res != null && res.length > 1) {
                return res[1];
            }
        }
        return "FR";
    }

    // =========================================================================
    // LEAVE POLICY MANAGEMENT - ADMINISTRATIVE ENDPOINTS
    // =========================================================================

    private void checkAdminAccess(Principal principal, boolean requireWrite) {
        User user = resolveUser(principal);
        boolean isUltimate = user.getRoles().stream().anyMatch(r -> 
            "ultimateAdmin".equalsIgnoreCase(r.getCode()) || "ULTIMATE_ADMIN".equalsIgnoreCase(r.getCode()));
        boolean isNational = user.getRoles().stream().anyMatch(r -> 
            "nationalAdmin".equalsIgnoreCase(r.getCode()) || "NATIONAL_ADMIN".equalsIgnoreCase(r.getCode()));
        
        if (requireWrite) {
            if (!isNational) {
                throw new BusinessException("Access Denied: National Admin role required.");
            }
        } else {
            if (!isUltimate && !isNational) {
                throw new BusinessException("Access Denied: Admin role required.");
            }
        }
    }

    @GetMapping("/admin/policy-groups")
    public ResponseEntity<ApiResponse<List<LeavePolicyGroup>>> getPolicyGroups(Principal principal) {
        checkAdminAccess(principal, false);
        return ResponseEntity.ok(ApiResponse.success(leavePolicyGroupRepository.findAll()));
    }

    @GetMapping("/admin/policies")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getPolicies(
            @RequestParam Long policyGroupId, Principal principal) {
        checkAdminAccess(principal, false);
        List<LeavePolicyRule> rules = leavePolicyRuleRepository.findByPolicyGroupId(policyGroupId);
        List<Map<String, Object>> result = rules.stream().map(r -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", r.getId());
            m.put("policyGroupId", r.getPolicyGroup().getId());
            m.put("leaveTypeId", r.getLeaveType().getId());
            m.put("leaveTypeName", r.getLeaveType().getName());
            m.put("defaultEntitlement", r.getDefaultEntitlement());
            m.put("monthlyAccrual", r.getMonthlyAccrual());
            m.put("entitlementUnit", r.getEntitlementUnit());
            m.put("maxConsecutiveDays", r.getMaxConsecutiveDays());
            m.put("maxPerYear", r.getMaxPerYear());
            m.put("minNoticeDays", r.getMinNoticeDays());
            m.put("documentRequiredAfterDays", r.getDocumentRequiredAfterDays());
            m.put("allowHalfDay", r.getAllowHalfDay());
            m.put("minimumLeaveUnit", r.getMinimumLeaveUnit());
            m.put("carryForwardAllowed", r.getCarryForwardAllowed());
            m.put("carryForwardLimit", r.getCarryForwardLimit());
            m.put("carryForwardExpiryMonths", r.getCarryForwardExpiryMonths());
            m.put("encashmentAllowed", r.getEncashmentAllowed());
            m.put("maximumEncashmentDays", r.getMaximumEncashmentDays());
            m.put("minimumBalanceForEncashment", r.getMinimumBalanceForEncashment());
            m.put("approvalLevel", r.getApprovalLevel());
            m.put("approvalMode", r.getApprovalMode());
            m.put("isPaid", r.getIsPaid());
            m.put("isProtected", r.getIsProtected());
            m.put("lifetimeLimit", r.getLifetimeLimit());
            m.put("version", r.getVersion());
            m.put("effectiveFrom", r.getEffectiveFrom().toString());
            m.put("effectiveTo", r.getEffectiveTo() != null ? r.getEffectiveTo().toString() : null);
            return m;
        }).toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PutMapping("/admin/policies/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<LeavePolicyRule>> updatePolicyRule(
            @PathVariable Long id, @RequestBody Map<String, Object> payload, Principal principal, HttpServletRequest request) {
        checkAdminAccess(principal, true);
        User actor = resolveUser(principal);
        
        LeavePolicyRule rule = leavePolicyRuleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Policy rule not found."));

        // Validation Rules
        BigDecimal defaultEntitlement = payload.get("defaultEntitlement") != null ? new BigDecimal(payload.get("defaultEntitlement").toString()) : null;
        BigDecimal monthlyAccrual = payload.get("monthlyAccrual") != null ? new BigDecimal(payload.get("monthlyAccrual").toString()) : null;
        BigDecimal carryForwardLimit = payload.get("carryForwardLimit") != null ? new BigDecimal(payload.get("carryForwardLimit").toString()) : null;
        BigDecimal maxEncashment = payload.get("maximumEncashmentDays") != null ? new BigDecimal(payload.get("maximumEncashmentDays").toString()) : null;
        boolean carryForwardAllowed = Boolean.TRUE.equals(payload.get("carryForwardAllowed"));
        boolean encashmentAllowed = Boolean.TRUE.equals(payload.get("encashmentAllowed"));

        if (carryForwardAllowed && carryForwardLimit != null && defaultEntitlement != null) {
            if (carryForwardLimit.compareTo(defaultEntitlement) > 0) {
                throw new BusinessException("Carry forward limit cannot exceed default entitlement.");
            }
        }
        if (encashmentAllowed && maxEncashment != null && defaultEntitlement != null) {
            if (maxEncashment.compareTo(defaultEntitlement) > 0) {
                throw new BusinessException("Maximum encashment days cannot exceed default entitlement.");
            }
        }
        if (monthlyAccrual != null && defaultEntitlement != null) {
            BigDecimal yearlyAccrual = monthlyAccrual.multiply(BigDecimal.valueOf(12));
            if (yearlyAccrual.compareTo(defaultEntitlement) > 0) {
                throw new BusinessException("Monthly accrual over 12 months cannot exceed default entitlement.");
            }
        }

        // Check if effective dates overlap
        LocalDate effFrom = LocalDate.parse(payload.get("effectiveFrom").toString());
        LocalDate effTo = payload.get("effectiveTo") != null ? LocalDate.parse(payload.get("effectiveTo").toString()) : null;
        
        List<LeavePolicyRule> others = leavePolicyRuleRepository
            .findByPolicyGroupIdAndLeaveTypeId(rule.getPolicyGroup().getId(), rule.getLeaveType().getId());
            
        for (LeavePolicyRule o : others) {
            if (!o.getId().equals(id)) {
                boolean overlaps = !(effFrom.isAfter(o.getEffectiveTo() != null ? o.getEffectiveTo() : LocalDate.MAX) || 
                                     (effTo != null && effTo.isBefore(o.getEffectiveFrom())));
                if (overlaps) {
                    throw new BusinessException("Effective dates overlap with another version of this policy rule.");
                }
            }
        }

        // Log audit change trail
        logAuditTrail(rule, payload, actor, request.getRemoteAddr());

        // Update fields
        rule.setDefaultEntitlement(defaultEntitlement);
        rule.setMonthlyAccrual(monthlyAccrual);
        rule.setCarryForwardAllowed(carryForwardAllowed);
        rule.setCarryForwardLimit(carryForwardLimit);
        rule.setCarryForwardExpiryMonths(payload.get("carryForwardExpiryMonths") != null ? Integer.parseInt(payload.get("carryForwardExpiryMonths").toString()) : null);
        rule.setEncashmentAllowed(encashmentAllowed);
        rule.setMaximumEncashmentDays(maxEncashment);
        rule.setMinimumBalanceForEncashment(payload.get("minimumBalanceForEncashment") != null ? new BigDecimal(payload.get("minimumBalanceForEncashment").toString()) : null);
        rule.setMaxConsecutiveDays(payload.get("maxConsecutiveDays") != null ? Integer.parseInt(payload.get("maxConsecutiveDays").toString()) : null);
        rule.setMaxPerYear(payload.get("maxPerYear") != null ? Integer.parseInt(payload.get("maxPerYear").toString()) : null);
        rule.setMinNoticeDays(payload.get("minNoticeDays") != null ? Integer.parseInt(payload.get("minNoticeDays").toString()) : 0);
        rule.setDocumentRequiredAfterDays(payload.get("documentRequiredAfterDays") != null ? Integer.parseInt(payload.get("documentRequiredAfterDays").toString()) : 0);
        rule.setAllowHalfDay(Boolean.TRUE.equals(payload.get("allowHalfDay")));
        rule.setMinimumLeaveUnit(payload.get("minimumLeaveUnit") != null ? new BigDecimal(payload.get("minimumLeaveUnit").toString()) : BigDecimal.ONE);
        rule.setApprovalLevel(payload.getOrDefault("approvalLevel", "SHIFT_SUPERVISOR").toString());
        rule.setApprovalMode(payload.getOrDefault("approvalMode", "MANAGER_APPROVAL").toString());
        rule.setIsPaid(Boolean.TRUE.equals(payload.get("isPaid")));
        rule.setIsProtected(Boolean.TRUE.equals(payload.get("isProtected")));
        rule.setLifetimeLimit(payload.get("lifetimeLimit") != null ? Integer.parseInt(payload.get("lifetimeLimit").toString()) : null);
        rule.setEffectiveFrom(effFrom);
        rule.setEffectiveTo(effTo);
        rule.setVersion(payload.get("version") != null ? Integer.parseInt(payload.get("version").toString()) : rule.getVersion());

        leavePolicyRuleRepository.save(rule);
        
        // Cache refresh invalidation trigger
        workingDayCalculator.clearCache();

        return ResponseEntity.ok(ApiResponse.success(rule));
    }

    private void logAuditTrail(LeavePolicyRule rule, Map<String, Object> payload, User actor, String ip) {
        String reason = (String) payload.get("auditReason");
        payload.forEach((key, val) -> {
            if ("auditReason".equals(key)) return;
            String oldVal = "";
            String newVal = val != null ? val.toString() : "";
            
            // Map specific fields for audit comparison
            switch (key) {
                case "defaultEntitlement":
                    oldVal = rule.getDefaultEntitlement() != null ? rule.getDefaultEntitlement().toString() : "";
                    break;
                case "monthlyAccrual":
                    oldVal = rule.getMonthlyAccrual() != null ? rule.getMonthlyAccrual().toString() : "";
                    break;
                case "carryForwardAllowed":
                    oldVal = rule.getCarryForwardAllowed() != null ? rule.getCarryForwardAllowed().toString() : "false";
                    break;
                case "carryForwardLimit":
                    oldVal = rule.getCarryForwardLimit() != null ? rule.getCarryForwardLimit().toString() : "";
                    break;
                case "approvalLevel":
                    oldVal = rule.getApprovalLevel() != null ? rule.getApprovalLevel() : "";
                    break;
                default:
                    return; // Audit only core operational parameters to prevent log bloat
            }
            
            if (!oldVal.equals(newVal)) {
                LeavePolicyChangeLog log = new LeavePolicyChangeLog();
                log.setPolicyGroup(rule.getPolicyGroup());
                log.setLeaveType(rule.getLeaveType());
                log.setFieldChanged(key);
                log.setOldValue(oldVal);
                log.setNewValue(newVal);
                log.setChangedBy(actor);
                log.setReason(reason);
                log.setIpAddress(ip);
                leavePolicyChangeLogRepository.save(log);
            }
        });
    }

    @GetMapping("/admin/policies/{ruleId}/pay-rules")
    public ResponseEntity<ApiResponse<List<LeavePayRule>>> getPayRules(@PathVariable Long ruleId, Principal principal) {
        checkAdminAccess(principal, false);
        return ResponseEntity.ok(ApiResponse.success(leavePayRuleRepository.findByPolicyRuleId(ruleId)));
    }

    @PostMapping("/admin/policies/{ruleId}/pay-rules")
    public ResponseEntity<ApiResponse<LeavePayRule>> addPayRule(
            @PathVariable Long ruleId, @RequestBody Map<String, Object> payload, Principal principal) {
        checkAdminAccess(principal, true);
        
        LeavePolicyRule rule = leavePolicyRuleRepository.findById(ruleId)
            .orElseThrow(() -> new BusinessException("Policy rule not found."));
            
        int dayFrom = Integer.parseInt(payload.get("dayFrom").toString());
        Integer dayTo = payload.get("dayTo") != null ? Integer.parseInt(payload.get("dayTo").toString()) : null;
        BigDecimal payPct = new BigDecimal(payload.get("payPercentage").toString());

        if (dayTo != null && dayFrom > dayTo) {
            throw new BusinessException("Day From cannot exceed Day To.");
        }

        // Validate ranges do not overlap
        List<LeavePayRule> existing = leavePayRuleRepository.findByPolicyRuleId(ruleId);
        for (LeavePayRule e : existing) {
            boolean overlap = !(dayFrom > (e.getDayTo() != null ? e.getDayTo() : Integer.MAX_VALUE) ||
                                (dayTo != null && dayTo < e.getDayFrom()));
            if (overlap) {
                throw new BusinessException("Day range overlaps with an existing pay rule tier.");
            }
        }

        LeavePayRule pr = new LeavePayRule();
        pr.setPolicyRule(rule);
        pr.setTierLabel((String) payload.get("tierLabel"));
        pr.setDayFrom(dayFrom);
        pr.setDayTo(dayTo);
        pr.setPayPercentage(payPct);
        
        leavePayRuleRepository.save(pr);
        return ResponseEntity.ok(ApiResponse.success(pr));
    }

    @DeleteMapping("/admin/pay-rules/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePayRule(@PathVariable Long id, Principal principal) {
        checkAdminAccess(principal, true); // Admin permission check
        if (!leavePayRuleRepository.existsById(id)) {
            throw new BusinessException("Pay rule not found.");
        }
        leavePayRuleRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/admin/holiday-calendars")
    public ResponseEntity<ApiResponse<List<HolidayCalendar>>> getHolidayCalendars(
            @RequestParam Long policyGroupId, Principal principal) {
        checkAdminAccess(principal, false);
        return ResponseEntity.ok(ApiResponse.success(
            holidayCalendarRepository.findAll().stream()
                .filter(c -> c.getPolicyGroup().getId().equals(policyGroupId))
                .toList()
        ));
    }

    @PostMapping("/admin/holiday-calendars")
    public ResponseEntity<ApiResponse<HolidayCalendar>> addHolidayCalendar(
            @RequestBody Map<String, Object> payload, Principal principal) {
        checkAdminAccess(principal, true);
        Long policyGroupId = Long.parseLong(payload.get("policyGroupId").toString());
        int year = Integer.parseInt(payload.get("year").toString());
        String name = (String) payload.get("name");

        LeavePolicyGroup group = leavePolicyGroupRepository.findById(policyGroupId)
            .orElseThrow(() -> new BusinessException("Policy group not found."));

        if (holidayCalendarRepository.findByPolicyGroupCodeAndYear(group.getCode(), year).isPresent()) {
            throw new BusinessException("Calendar already exists for this group and year.");
        }

        HolidayCalendar c = new HolidayCalendar();
        c.setPolicyGroup(group);
        c.setYear(year);
        c.setName(name);
        c.setActive(true);
        
        holidayCalendarRepository.save(c);
        return ResponseEntity.ok(ApiResponse.success(c));
    }

    @PostMapping("/api/v1/leaves/admin/holidays")
    public ResponseEntity<ApiResponse<Holiday>> addHolidayToCalendar(
            @RequestBody Map<String, Object> payload, Principal principal) {
        checkAdminAccess(principal, true);
        Long calendarId = Long.parseLong(payload.get("calendarId").toString());
        String name = (String) payload.get("name");
        LocalDate date = LocalDate.parse(payload.get("date").toString());
        boolean isWorking = Boolean.TRUE.equals(payload.get("isWorkingDay"));

        HolidayCalendar cal = holidayCalendarRepository.findById(calendarId)
            .orElseThrow(() -> new BusinessException("Calendar not found."));

        Holiday h = new Holiday();
        h.setHolidayCalendar(cal);
        h.setHolidayName(name);
        h.setHolidayDate(date);
        h.setIsWorkingDay(isWorking);
        
        holidayRepository.save(h);
        return ResponseEntity.ok(ApiResponse.success(h));
    }

    @DeleteMapping("/admin/holidays/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHolidayAdmin(@PathVariable Long id, Principal principal) {
        checkAdminAccess(principal, true);
        if (!holidayRepository.existsById(id)) {
            throw new BusinessException("Holiday not found.");
        }
        holidayRepository.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/admin/weekly-offs")
    public ResponseEntity<ApiResponse<List<WeeklyOffRule>>> getWeeklyOffRules(
            @RequestParam Long policyGroupId, Principal principal) {
        checkAdminAccess(principal, false);
        return ResponseEntity.ok(ApiResponse.success(weeklyOffRuleRepository.findByPolicyGroupId(policyGroupId)));
    }

    @PutMapping("/admin/weekly-offs")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> updateWeeklyOffRules(
            @RequestBody Map<String, Object> payload, Principal principal) {
        checkAdminAccess(principal, true);
        Long policyGroupId = Long.parseLong(payload.get("policyGroupId").toString());
        List<String> days = (List<String>) payload.get("days");

        if (days == null || days.isEmpty()) {
            throw new BusinessException("Weekly off must contain at least one day.");
        }

        LeavePolicyGroup group = leavePolicyGroupRepository.findById(policyGroupId)
            .orElseThrow(() -> new BusinessException("Policy group not found."));

        weeklyOffRuleRepository.deleteByPolicyGroupId(policyGroupId);
        weeklyOffRuleRepository.flush();

        for (String day : days) {
            WeeklyOffRule rule = new WeeklyOffRule();
            rule.setPolicyGroup(group);
            rule.setDayOfWeek(day);
            weeklyOffRuleRepository.save(rule);
        }

        // Evict cached weekly offs
        workingDayCalculator.clearCache();

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/admin/audit")
    public ResponseEntity<ApiResponse<List<LeavePolicyChangeLog>>> getAuditLogs(
            @RequestParam String policyGroupCode, Principal principal) {
        checkAdminAccess(principal, false);
        return ResponseEntity.ok(ApiResponse.success(leavePolicyChangeLogRepository.findByPolicyGroupCodeOrderByChangedAtDesc(policyGroupCode)));
    }
}
