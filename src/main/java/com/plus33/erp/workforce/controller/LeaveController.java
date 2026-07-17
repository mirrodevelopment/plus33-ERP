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
public class LeaveController {

    private final LeaveServiceImpl leaveService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveRepository leaveRepository;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final HolidayCalendarRepository holidayCalendarRepository;
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
                .filter(l -> userStoreRepository.findByIdUserId(l.getEmployee().getUser().getId()).stream()
                    .anyMatch(us -> us.getStore().getId().equals(storeId)))
                .toList();

            cancellations = cancellations.stream()
                .filter(l -> userStoreRepository.findByIdUserId(l.getEmployee().getUser().getId()).stream()
                    .anyMatch(us -> us.getStore().getId().equals(storeId)))
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
    // GET /api/v1/leaves/holidays — Public holidays
    // =========================================================================
    @GetMapping("/holidays")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHolidays(
            @RequestParam(defaultValue = "FR") String countryCode,
            @RequestParam(defaultValue = "2026") Integer year) {
        List<HolidayCalendar> holidays = holidayCalendarRepository
                .findByCountryCodeAndHolidayYear(countryCode, year);
        List<Map<String, Object>> result = holidays.stream().map(h -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", h.getId());
            m.put("name", h.getHolidayName());
            m.put("date", h.getHolidayDate().toString());
            m.put("isRecurring", h.getIsRecurring());
            return m;
        }).sorted(Comparator.comparing(m -> m.get("date").toString()))
          .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // =========================================================================
    // POST /api/v1/leaves/holidays — Create or replace a public holiday
    // =========================================================================
    @PostMapping("/holidays")
    public ResponseEntity<ApiResponse<HolidayCalendar>> createHoliday(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String dateStr = (String) payload.get("date");
        if (name == null || name.trim().isEmpty() || dateStr == null || dateStr.trim().isEmpty()) {
            throw new BusinessException("Holiday name and date are required");
        }
        LocalDate date = LocalDate.parse(dateStr);
        String countryCode = (String) payload.getOrDefault("countryCode", "FR");
        boolean isRecurring = Boolean.TRUE.equals(payload.get("isRecurring"));
        
        // Remove existing unique conflict if any exists
        holidayCalendarRepository.findByCountryCodeAndHolidayYear(countryCode, date.getYear()).stream()
            .filter(item -> item.getHolidayDate().equals(date))
            .forEach(item -> holidayCalendarRepository.delete(item));
        
        HolidayCalendar h = new HolidayCalendar();
        h.setHolidayName(name);
        h.setHolidayDate(date);
        h.setHolidayYear(date.getYear());
        h.setCountryCode(countryCode);
        h.setIsRecurring(isRecurring);
        h.setIsWorkingDay(false);
        h.setRegion((String) payload.get("region"));
        
        holidayCalendarRepository.save(h);
        return ResponseEntity.ok(ApiResponse.success(h));
    }

    // =========================================================================
    // DELETE /api/v1/leaves/holidays/{id} — Delete holiday
    // =========================================================================
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Long id) {
        if (!holidayCalendarRepository.existsById(id)) {
            throw new BusinessException("Holiday not found");
        }
        holidayCalendarRepository.deleteById(id);
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
        if (employee != null && employee.getUser() != null) {
            List<UserStore> userStores = userStoreRepository.findByIdUserId(employee.getUser().getId());
            if (userStores != null && !userStores.isEmpty()) {
                com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
                if (store != null && store.getRegion() != null && store.getRegion().getCode() != null) {
                    String code = store.getRegion().getCode().toUpperCase();
                    if (code.startsWith("FR")) return "FR";
                    if (code.startsWith("IN")) return "IN";
                    if (code.startsWith("AE") || code.startsWith("UAE")) return "AE";
                    if (code.startsWith("EU")) return "EU";
                    return code;
                }
            }
        }
        return "FR";
    }
}
