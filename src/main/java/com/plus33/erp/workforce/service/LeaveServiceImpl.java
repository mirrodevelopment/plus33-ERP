package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.repository.*;
import com.plus33.erp.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Enterprise Leave Service — core business logic for the PLUS33 ERP leave management module.
 * Enforces all validation rules, approval workflow, ledger accounting, and audit logging.
 */
@Service
public class LeaveServiceImpl {

    private static final int REJECTION_REASON_MIN = 10;
    private static final int REJECTION_REASON_MAX = 500;
    private static final int CANCELLATION_REASON_MIN = 10;
    private static final int SICK_DOC_THRESHOLD_DAYS = 2;

    private final EmployeeLeaveRepository leaveRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final EmployeeLeaveTransactionRepository transactionRepository;
    private final LeaveApprovalHistoryRepository approvalHistoryRepository;
    private final LeaveApprovalWorkflowRepository workflowRepository;
    private final LeaveAuditLogRepository auditLogRepository;
    private final LeaveDocumentRepository documentRepository;
    private final HolidayRepository holidayRepository;
    private final LeaveBlackoutDateRepository blackoutRepository;
    private final WorkingDayCalculatorService workingDayCalculator;
    private final LeavePolicyService leavePolicyService;
    private final LeavePolicyGroupRepository leavePolicyGroupRepository;
    private final WeeklyOffRuleRepository weeklyOffRuleRepository;
    private final EmployeeRepository employeeRepository;
    private final UserStoreRepository userStoreRepository;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeShiftRepository employeeShiftRepository;
    private final ShiftRepository shiftRepository;
    private final PayrollPeriodRepository payrollPeriodRepository;

    public LeaveServiceImpl(
            EmployeeLeaveRepository leaveRepository,
            LeaveTypeRepository leaveTypeRepository,
            EmployeeLeaveBalanceRepository balanceRepository,
            EmployeeLeaveTransactionRepository transactionRepository,
            LeaveApprovalHistoryRepository approvalHistoryRepository,
            LeaveApprovalWorkflowRepository workflowRepository,
            LeaveAuditLogRepository auditLogRepository,
            LeaveDocumentRepository documentRepository,
            HolidayRepository holidayRepository,
            LeaveBlackoutDateRepository blackoutRepository,
            WorkingDayCalculatorService workingDayCalculator,
            LeavePolicyService leavePolicyService,
            LeavePolicyGroupRepository leavePolicyGroupRepository,
            WeeklyOffRuleRepository weeklyOffRuleRepository,
            EmployeeRepository employeeRepository,
            UserStoreRepository userStoreRepository,
            AttendanceRepository attendanceRepository,
            EmployeeShiftRepository employeeShiftRepository,
            ShiftRepository shiftRepository,
            PayrollPeriodRepository payrollPeriodRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.approvalHistoryRepository = approvalHistoryRepository;
        this.workflowRepository = workflowRepository;
        this.auditLogRepository = auditLogRepository;
        this.documentRepository = documentRepository;
        this.holidayRepository = holidayRepository;
        this.blackoutRepository = blackoutRepository;
        this.workingDayCalculator = workingDayCalculator;
        this.leavePolicyService = leavePolicyService;
        this.leavePolicyGroupRepository = leavePolicyGroupRepository;
        this.weeklyOffRuleRepository   = weeklyOffRuleRepository;
        this.employeeRepository = employeeRepository;
        this.userStoreRepository = userStoreRepository;
        this.attendanceRepository = attendanceRepository;
        this.employeeShiftRepository = employeeShiftRepository;
        this.shiftRepository = shiftRepository;
        this.payrollPeriodRepository = payrollPeriodRepository;
    }

    // -------------------------------------------------------------------------
    // SUBMIT LEAVE
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave submitLeave(Employee employee, Long leaveTypeId, LocalDate startDate,
                                     LocalDate endDate, String session, String reason) {
        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new BusinessException("Leave type not found."));

        if (!Boolean.TRUE.equals(leaveType.getActive())) {
            throw new BusinessException("This leave type is not currently active.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(startDate, endDate)) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        // Rule 1: End date must be >= start date
        if (endDate.isBefore(startDate)) {
            throw new BusinessException("End date cannot be before start date.");
        }

        // Rule 2: Cannot apply in the past (except Admin — not enforced here, done at controller)
        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Leave cannot be applied for a past date.");
        }

        // Resolve effective policy rules — use policy group hierarchy (INDIA/EU/UAE)
        String[] resolved = resolveGroupAndCountry(employee);
        String policyGroupCode = resolved[0];
        String countryCode     = resolved[1];
        Long   storeId         = employee.getUser() != null ?
                resolveStoreId(employee.getUser().getId()) : null;

        LeavePolicyGroup policyGroup = leavePolicyGroupRepository
                .findByCode(policyGroupCode).orElse(null);

        LeavePolicyService.ResolvedPolicy policy = leavePolicyService
                .resolvePolicy(null, storeId, 1L, leaveType, policyGroup, LocalDate.now());

        // Rule 3: Min notice period
        int minNotice = policy.minNoticeDays;
        if (minNotice > 0 && LocalDate.now().plusDays(minNotice).isAfter(startDate)) {
            throw new BusinessException("This leave type requires at least " + minNotice + " day(s) advance notice.");
        }

        // Rule 4: Calculate working days (uses DB-driven weekly-off rules, not hardcoded)
        BigDecimal totalDays = workingDayCalculator.calculate(startDate, endDate, session, policyGroupCode, countryCode);
        if (totalDays.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("The selected dates do not include any working days.");
        }

        // Rule 5: Max consecutive days
        if (policy.maxConsecutiveDays != null &&
                totalDays.compareTo(BigDecimal.valueOf(policy.maxConsecutiveDays)) > 0) {
            throw new BusinessException("This leave type allows a maximum of " +
                    policy.maxConsecutiveDays + " consecutive working days.");
        }

        // Rule 6: Overlap check
        List<EmployeeLeave> overlapping = leaveRepository.findOverlapping(employee.getId(), startDate, endDate);
        if (!overlapping.isEmpty()) {
            throw new BusinessException("The selected dates overlap with an existing leave request (ID: " +
                    overlapping.get(0).getId() + ").");
        }

        // Rule 7: Duplicate PENDING check
        long pendingCount = leaveRepository.countPendingByEmployeeAndType(employee.getId(), leaveTypeId);
        if (pendingCount > 0) {
            throw new BusinessException("You already have a pending " + leaveType.getName() + " request.");
        }

        // Rule 8: Blackout dates check
        List<LeaveBlackoutDate> blackouts = blackoutRepository
                .findActiveOverlapping(1L, startDate, endDate);
        if (!blackouts.isEmpty()) {
            LeaveBlackoutDate blackout = blackouts.get(0);
            writeAudit(null, employee.getUser() != null ? employee.getUser().getId() : null,
                    "BLACKOUT_BLOCKED", null, null,
                    "Leave blocked by blackout period: " + blackout.getName());
            throw new BusinessException("Leave cannot be taken during the company blackout period: " +
                    blackout.getName() + ". Reason: " + blackout.getReason());
        }

        // Rule 9: Balance check (for types with limits)
        if (policy.annualEntitlement != null) {
            int year = LocalDate.now().getYear();
            EmployeeLeaveBalance balance = balanceRepository
                    .findByEmployeeIdAndLeaveTypeIdAndYear(employee.getId(), leaveTypeId, year)
                    .orElse(null);

            if (balance != null) {
                BigDecimal remaining = balance.getRemaining();
                if (totalDays.compareTo(remaining) > 0) {
                    throw new BusinessException("Insufficient leave balance. Available: " +
                            remaining + " days. Requested: " + totalDays + " days.");
                }
            }
        }

        // Rule 10: Marriage leave — only one lifetime approval allowed
        if ("MARRIAGE".equals(leaveType.getCode())) {
            boolean alreadyApproved = leaveRepository
                    .existsApprovedByEmployeeAndType(employee.getId(), "MARRIAGE");
            if (alreadyApproved) {
                throw new BusinessException("Marriage leave has already been approved for this employee.");
            }
        }

        // Create the leave record
        EmployeeLeave leave = new EmployeeLeave();
        leave.setEmployee(employee);
        leave.setLeaveType(leaveType);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setTotalDays(totalDays);
        leave.setLeaveSession(session != null ? session : "FULL_DAY");
        leave.setReason(reason);
        leave.setStatus("PENDING");
        leave.setCurrentApprovalLevel(1);

        // Determine approval workflow
        LeaveApprovalWorkflow workflow = workflowRepository
                .findByCompanyIdAndLeaveTypeIdAndLevel(1L, leaveTypeId, 1)
                .orElse(null);

        if (workflow != null) {
            leave.setApproverRole(workflow.getApproverRole());
            if (workflow.getSlaHours() != null && workflow.getSlaHours() > 0) {
                leave.setApprovalDueAt(LocalDateTime.now().plusHours(workflow.getSlaHours()));
            }

            // SYSTEM approver: auto-approve immediately for protected leave
            if ("SYSTEM".equals(workflow.getApproverRole())) {
                leave.setStatus("APPROVED");
                leave.setApprovedAt(LocalDateTime.now());
                EmployeeLeave saved = leaveRepository.save(leave);

                // Debit balance
                debitBalance(employee.getId(), leaveTypeId, totalDays, saved);

                // Write approval history (SYSTEM)
                writeApprovalHistory(saved, 1, null, "SYSTEM", "APPROVED",
                        "PENDING", "APPROVED", "Auto-approved: legally protected leave type.", null, null);

                writeAudit(saved, null, "SYSTEM_AUTO_APPROVED",
                        "{\"status\":\"PENDING\"}", "{\"status\":\"APPROVED\"}",
                        "Auto-approved: " + leaveType.getName() + " is a legally protected leave type.");
                return saved;
            }
        } else {
            leave.setApproverRole("STORE_ADMIN");
        }

        // Debit pending balance
        int year = LocalDate.now().getYear();
        EmployeeLeaveBalance balance = getOrCreateBalance(employee.getId(), leaveTypeId, year);
        balance.setPending(balance.getPending().add(totalDays));
        balanceRepository.save(balance);

        // Ledger transaction: debit as PENDING
        writeTransaction(employee, leaveType, totalDays.negate(), "APPROVAL",
                null, "Pending deduction for leave request");

        EmployeeLeave saved = leaveRepository.save(leave);

        writeAudit(saved, employee.getUser() != null ? employee.getUser().getId() : null,
                "SUBMITTED", null, "{\"status\":\"PENDING\",\"days\":" + totalDays + "}",
                "Leave submitted: " + leaveType.getName());

        return saved;
    }

    // -------------------------------------------------------------------------
    // APPROVE LEAVE
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave approveLeave(Long leaveId, Long approverUserId, String approverRole,
                                      String comment, String ipAddress) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if (!"PENDING".equals(leave.getStatus())) {
            throw new BusinessException("Only PENDING leave requests can be approved.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(leave.getStartDate(), leave.getEndDate())) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        LeaveType leaveType = leave.getLeaveType();
        String[] resolvedApprove = resolveGroupAndCountry(leave.getEmployee());
        String countryCode = resolvedApprove[1];
        LeavePolicyService.ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, leaveType, countryCode, leave.getStartDate());

        // Sick leave >2 days or policy requires document
        boolean reqDoc = policy.requiresDocument;
        if ("SICK".equals(leaveType.getCode()) && leave.getTotalDays().compareTo(BigDecimal.valueOf(SICK_DOC_THRESHOLD_DAYS)) > 0) {
            reqDoc = true;
        }
        if (reqDoc) {
            boolean hasDocument = documentRepository.existsByLeaveId(leaveId);
            if (!hasDocument) {
                throw new BusinessException("A valid supporting document or medical certificate is required before approval.");
            }
        }

        // Attendance synchronization & shift validation
        // Resolve weekly-off days once for efficiency (not per iteration)
        java.util.Set<String> weeklyOffs = weeklyOffRuleRepository.resolveWeeklyOffDays(resolvedApprove[0]);
        for (LocalDate d = leave.getStartDate(); !d.isAfter(leave.getEndDate()); d = d.plusDays(1)) {
            boolean isH = holidayRepository.existsByCountryCodeAndHolidayDate(countryCode, d);
            boolean isW = weeklyOffs.contains(d.getDayOfWeek().name());
            if (isH || isW) {
                continue;
            }

            Optional<EmployeeShift> empShiftOpt = employeeShiftRepository.findActiveShiftForEmployeeOnDate(leave.getEmployee().getId(), d);
            if (!empShiftOpt.isPresent()) {
                writeAudit(leave, approverUserId, "SHIFT_NOT_FOUND", null, null,
                        "Shift not assigned for employee " + leave.getEmployee().getId() + " on " + d + ". Attendance generation skipped.", ipAddress);
                continue;
            }

            Optional<Attendance> attOpt = attendanceRepository.findByEmployeeIdAndAttendanceDate(leave.getEmployee().getId(), d);
            if (attOpt.isPresent()) {
                Attendance att = attOpt.get();
                if ("PRESENT".equalsIgnoreCase(att.getStatus()) || "HALF_DAY".equalsIgnoreCase(att.getStatus()) || "ACTIVE".equalsIgnoreCase(att.getStatus()) || "ON_BREAK".equalsIgnoreCase(att.getStatus())) {
                    // Check if it's already linked to this leave or another leave to be safe, but generally it's a conflict
                    if (att.getLeave() == null || !att.getLeave().getId().equals(leave.getId())) {
                        throw new BusinessException("Attendance already exists for date: " + d);
                    }
                }
                updateAttendanceToLeave(att, leave, policy.isPaid);
            } else {
                Attendance att = new Attendance();
                att.setEmployee(leave.getEmployee());
                att.setAttendanceDate(d);
                att.setShift(empShiftOpt.get().getShift());
                updateAttendanceToLeave(att, leave, policy.isPaid);
            }
        }

        String oldStatus = leave.getStatus();
        leave.setStatus("APPROVED");
        leave.setApprovedBy(null); // resolved by approverUserId in controller
        leave.setApprovedAt(LocalDateTime.now());
        leave.setApproverRole(approverRole);

        // Move pending → used in balance
        int year = leave.getStartDate().getYear();
        final Long leaveTypeId = leaveType.getId();
        EmployeeLeaveBalance balance = getOrCreateBalance(leave.getEmployee().getId(), leaveTypeId, year);
        balance.setPending(balance.getPending().subtract(leave.getTotalDays()));
        balance.setUsed(balance.getUsed().add(leave.getTotalDays()));
        balanceRepository.save(balance);

        // Ledger: credit back pending, debit used
        writeTransaction(leave.getEmployee(), leaveType, leave.getTotalDays(), "CANCELLATION",
                leave, "Pending reversed on approval");
        writeTransaction(leave.getEmployee(), leaveType, leave.getTotalDays().negate(), "APPROVAL",
                leave, "Approved leave deduction");

        EmployeeLeave saved = leaveRepository.save(leave);

        // Approval history
        String supervisorComment = "SUPERVISOR".equals(approverRole) ? comment : null;
        String adminComment = "STORE_ADMIN".equals(approverRole) ? comment : null;
        writeApprovalHistory(saved, leave.getCurrentApprovalLevel(), approverUserId, approverRole,
                "APPROVED", oldStatus, "APPROVED", supervisorComment, adminComment, null);

        writeAudit(saved, approverUserId, "APPROVED",
                "{\"status\":\"" + oldStatus + "\"}", "{\"status\":\"APPROVED\"}",
                comment, ipAddress);

        return saved;
    }

    // -------------------------------------------------------------------------
    // REJECT LEAVE
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave rejectLeave(Long leaveId, Long approverUserId, String approverRole,
                                     String rejectionReason, String ipAddress) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if (!"PENDING".equals(leave.getStatus())) {
            throw new BusinessException("Only PENDING leave requests can be rejected.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(leave.getStartDate(), leave.getEndDate())) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        // Rule 1: Protected leave rejections blocked
        String[] resolvedReject = resolveGroupAndCountry(leave.getEmployee());
        LeavePolicyService.ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, leave.getLeaveType(), resolvedReject[1], leave.getStartDate());
        if (policy.isProtected) {
            throw new BusinessException("Protected leave types cannot be rejected.");
        }

        // Rejection reason required (10–500 chars)
        if (rejectionReason == null || rejectionReason.trim().length() < REJECTION_REASON_MIN) {
            throw new BusinessException("Rejection reason must be at least " + REJECTION_REASON_MIN + " characters.");
        }
        if (rejectionReason.trim().length() > REJECTION_REASON_MAX) {
            throw new BusinessException("Rejection reason cannot exceed " + REJECTION_REASON_MAX + " characters.");
        }

        String oldStatus = leave.getStatus();
        leave.setStatus("REJECTED");
        leave.setRejectionReason(rejectionReason.trim());

        // Refund pending balance
        int year = leave.getStartDate().getYear();
        final Long leaveTypeId = leave.getLeaveType().getId();
        EmployeeLeaveBalance balance = getOrCreateBalance(leave.getEmployee().getId(), leaveTypeId, year);
        balance.setPending(balance.getPending().subtract(leave.getTotalDays()));
        balanceRepository.save(balance);

        // Ledger: credit back
        writeTransaction(leave.getEmployee(), leave.getLeaveType(), leave.getTotalDays(), "CANCELLATION",
                leave, "Balance refunded on rejection");

        EmployeeLeave saved = leaveRepository.save(leave);

        writeApprovalHistory(saved, leave.getCurrentApprovalLevel(), approverUserId, approverRole,
                "REJECTED", oldStatus, "REJECTED",
                "SUPERVISOR".equals(approverRole) ? rejectionReason : null,
                "STORE_ADMIN".equals(approverRole) ? rejectionReason : null, null);

        writeAudit(saved, approverUserId, "REJECTED",
                "{\"status\":\"" + oldStatus + "\"}", "{\"status\":\"REJECTED\"}",
                "Rejected. Reason: " + rejectionReason, ipAddress);

        return saved;
    }

    // -------------------------------------------------------------------------
    // CANCEL LEAVE (PENDING)
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave cancelLeave(Long leaveId, Long employeeUserId, String cancellationReason) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if ("REJECTED".equals(leave.getStatus()) || "APPROVED".equals(leave.getStatus())) {
            if ("APPROVED".equals(leave.getStatus())) {
                throw new BusinessException("Use 'Request Cancellation' to cancel an approved leave.");
            }
            throw new BusinessException("Rejected or completed leave requests cannot be cancelled.");
        }

        if (!"PENDING".equals(leave.getStatus())) {
            throw new BusinessException("Only PENDING leave requests can be directly cancelled.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(leave.getStartDate(), leave.getEndDate())) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        if (cancellationReason == null || cancellationReason.trim().length() < CANCELLATION_REASON_MIN) {
            throw new BusinessException("Cancellation reason must be at least " + CANCELLATION_REASON_MIN + " characters.");
        }

        String oldStatus = leave.getStatus();
        leave.setStatus("CANCELLED");
        leave.setCancelledAt(LocalDateTime.now());
        leave.setCancellationReason(cancellationReason.trim());

        // Refund pending balance
        int year = leave.getStartDate().getYear();
        final Long leaveTypeId = leave.getLeaveType().getId();
        EmployeeLeaveBalance balance = getOrCreateBalance(leave.getEmployee().getId(), leaveTypeId, year);
        balance.setPending(balance.getPending().subtract(leave.getTotalDays()));
        balanceRepository.save(balance);

        writeTransaction(leave.getEmployee(), leave.getLeaveType(), leave.getTotalDays(), "CANCELLATION",
                leave, "Pending refunded on employee cancellation");

        EmployeeLeave saved = leaveRepository.save(leave);

        writeAudit(saved, employeeUserId, "CANCELLED",
                "{\"status\":\"" + oldStatus + "\"}", "{\"status\":\"CANCELLED\"}",
                "Cancelled by employee. Reason: " + cancellationReason.trim(), null);

        return saved;
    }

    // -------------------------------------------------------------------------
    // REQUEST CANCELLATION (APPROVED leave)
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave requestCancellation(Long leaveId, Long employeeUserId, String cancellationReason) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if (!"APPROVED".equals(leave.getStatus())) {
            throw new BusinessException("Only APPROVED leave requests can have cancellation requested.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(leave.getStartDate(), leave.getEndDate())) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        if (cancellationReason == null || cancellationReason.trim().length() < CANCELLATION_REASON_MIN) {
            throw new BusinessException("Cancellation reason must be at least " + CANCELLATION_REASON_MIN + " characters.");
        }

        leave.setCancellationRequested(true);
        leave.setCancellationReason(cancellationReason.trim());
        EmployeeLeave saved = leaveRepository.save(leave);

        writeAudit(saved, employeeUserId, "CANCELLATION_REQUESTED", null, null,
                "Employee requested cancellation. Reason: " + cancellationReason.trim(), null);

        return saved;
    }

    // -------------------------------------------------------------------------
    // APPROVE CANCELLATION
    // -------------------------------------------------------------------------

    @Transactional
    public EmployeeLeave approveCancellation(Long leaveId, Long approverUserId, String ipAddress) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if (!"APPROVED".equals(leave.getStatus()) || !Boolean.TRUE.equals(leave.getCancellationRequested())) {
            throw new BusinessException("No pending cancellation request found for this leave.");
        }

        // Rule 0: Payroll Period Lock Check
        if (payrollPeriodRepository.existsLockedOrClosedPeriodOverlapping(leave.getStartDate(), leave.getEndDate())) {
            throw new BusinessException("Payroll has already been processed for this period.");
        }

        // Attendance synchronization cleanup
        List<Attendance> synced = attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(leave.getEmployee().getId(), leave.getStartDate(), leave.getEndDate());
        for (Attendance att : synced) {
            if (att.getLeave() != null && att.getLeave().getId().equals(leave.getId())) {
                revertAttendanceLeave(att);
            }
        }

        String oldStatus = leave.getStatus();
        leave.setStatus("CANCELLED");
        leave.setCancelledAt(LocalDateTime.now());
        leave.setCancellationRequested(false);

        // Refund used balance back
        int year = leave.getStartDate().getYear();
        final Long leaveTypeId = leave.getLeaveType().getId();
        EmployeeLeaveBalance balance = getOrCreateBalance(leave.getEmployee().getId(), leaveTypeId, year);
        balance.setUsed(balance.getUsed().subtract(leave.getTotalDays()));
        balanceRepository.save(balance);

        writeTransaction(leave.getEmployee(), leave.getLeaveType(), leave.getTotalDays(), "CANCELLATION",
                leave, "Used balance refunded on cancellation approval");

        EmployeeLeave saved = leaveRepository.save(leave);

        writeAudit(saved, approverUserId, "CANCELLATION_APPROVED",
                "{\"status\":\"" + oldStatus + "\"}", "{\"status\":\"CANCELLED\"}",
                "Cancellation approved by manager.", ipAddress);

        return saved;
    }

    // -------------------------------------------------------------------------
    // DOCUMENT UPLOAD
    // -------------------------------------------------------------------------

    @Transactional
    public LeaveDocument addDocument(Long leaveId, String fileName, String filePath, String mimeType, Long uploadedByUserId) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        LeaveDocument doc = new LeaveDocument();
        doc.setLeave(leave);
        doc.setFileName(fileName);
        doc.setFilePath(filePath);
        doc.setMimeType(mimeType);
        doc.setUploadedAt(LocalDateTime.now());

        LeaveDocument saved = documentRepository.save(doc);

        writeAudit(leave, uploadedByUserId, "DOCUMENT_UPLOADED", null,
                "{\"fileName\":\"" + fileName + "\"}", "Document uploaded: " + fileName, null);

        return saved;
    }

    // -------------------------------------------------------------------------
    // GETTERS FOR FRONTEND
    // -------------------------------------------------------------------------

    public List<EmployeeLeave> getMyLeaves(Long employeeId) {
        return leaveRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    @Transactional
    public List<EmployeeLeaveBalance> getMyBalance(Long employeeId) {
        int currentYear = LocalDate.now().getYear();
        List<EmployeeLeaveBalance> existing = balanceRepository.findByEmployeeIdAndYear(employeeId, currentYear);
        List<LeaveType> activeTypes = leaveTypeRepository.findAll().stream()
                .filter(lt -> Boolean.TRUE.equals(lt.getActive()))
                .collect(java.util.stream.Collectors.toList());

        if (existing.size() < activeTypes.size()) {
            Employee emp = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new BusinessException("Employee not found"));

            String[] resolvedInit = resolveGroupAndCountry(emp);
            for (LeaveType lt : activeTypes) {
                boolean hasBalance = existing.stream().anyMatch(b -> b.getLeaveType().getId().equals(lt.getId()));
                if (!hasBalance) {
                    LeavePolicyService.ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, lt, resolvedInit[1], LocalDate.now());
                    BigDecimal opening = policy.annualEntitlement != null ? policy.annualEntitlement : BigDecimal.ZERO;

                    EmployeeLeaveBalance b = new EmployeeLeaveBalance();
                    b.setEmployee(emp);
                    b.setLeaveType(lt);
                    b.setYear(currentYear);
                    b.setOpeningBalance(opening);
                    b.setAccrued(BigDecimal.ZERO);
                    b.setCarryForward(BigDecimal.ZERO);
                    b.setUsed(BigDecimal.ZERO);
                    b.setPending(BigDecimal.ZERO);

                    balanceRepository.save(b);
                }
            }
            existing = balanceRepository.findByEmployeeIdAndYear(employeeId, currentYear);
        }
        return existing;
    }

    public List<EmployeeLeave> getPendingForApprover(String approverRole) {
        return leaveRepository.findPendingByApprovalLevel(approverRole);
    }

    public List<EmployeeLeave> getCancellationRequests(String approverRole) {
        return leaveRepository.findCancellationRequests(approverRole);
    }

    public List<LeaveAuditLog> getAuditLog(Long leaveId) {
        return auditLogRepository.findByLeaveIdOrderByCreatedAtDesc(leaveId);
    }

    // -------------------------------------------------------------------------
    // PRIVATE HELPERS
    // -------------------------------------------------------------------------

    private EmployeeLeaveBalance getOrCreateBalance(Long employeeId, Long leaveTypeId, int year) {
        return balanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, year)
                .orElseGet(() -> {
                    EmployeeLeaveBalance b = new EmployeeLeaveBalance();
                    Employee emp = new Employee(); emp.setId(employeeId);
                    LeaveType lt = new LeaveType(); lt.setId(leaveTypeId);
                    b.setEmployee(emp);
                    b.setLeaveType(lt);
                    b.setYear(year);
                    return balanceRepository.save(b);
                });
    }

    private void debitBalance(Long employeeId, Long leaveTypeId, BigDecimal days, EmployeeLeave leave) {
        int year = LocalDate.now().getYear();
        EmployeeLeaveBalance balance = getOrCreateBalance(employeeId, leaveTypeId, year);
        balance.setUsed(balance.getUsed().add(days));
        balanceRepository.save(balance);
        writeTransaction(leave.getEmployee(), leave.getLeaveType(), days.negate(), "APPROVAL",
                leave, "Balance deducted for auto-approved leave");
    }

    private void writeTransaction(Employee employee, LeaveType leaveType, BigDecimal days,
                                   String type, EmployeeLeave refLeave, String note) {
        EmployeeLeaveTransaction txn = new EmployeeLeaveTransaction();
        txn.setEmployee(employee);
        txn.setLeaveType(leaveType);
        txn.setDays(days);
        txn.setTransactionType(type);
        txn.setReferenceLeave(refLeave);
        txn.setNote(note);
        transactionRepository.save(txn);
    }

    private void writeApprovalHistory(EmployeeLeave leave, int level, Long approverUserId,
                                       String approverRole, String decision,
                                       String oldStatus, String newStatus,
                                       String supervisorComment, String adminComment, String hrComment) {
        LeaveApprovalHistory history = new LeaveApprovalHistory();
        history.setLeave(leave);
        history.setLevel(level);
        history.setApproverRole(approverRole);
        history.setDecision(decision);
        history.setOldStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setSupervisorComment(supervisorComment);
        history.setStoreAdminComment(adminComment);
        history.setHrComment(hrComment);
        history.setDecidedAt(LocalDateTime.now());
        approvalHistoryRepository.save(history);
    }

    private void writeAudit(EmployeeLeave leave, Long actorId, String action,
                             String oldValue, String newValue, String note) {
        writeAudit(leave, actorId, action, oldValue, newValue, note, null);
    }

    private void writeAudit(EmployeeLeave leave, Long actorId, String action,
                             String oldValue, String newValue, String note, String ipAddress) {
        LeaveAuditLog audit = new LeaveAuditLog();
        audit.setLeave(leave);
        audit.setActorUserId(actorId);
        audit.setAction(action);
        audit.setOldValue(oldValue);
        audit.setNewValue(newValue);
        audit.setNote(note);
        audit.setIpAddress(ipAddress);
        auditLogRepository.save(audit);
    }

    /**
     * Resolves the policy group code and legacy country code for an employee
     * based on their assigned store region. Returns a two-element array:
     * [0] = policy group code (INDIA | EU | UAE)
     * [1] = legacy country code (IN | FR | AE) for holiday calendar lookups
     */
    public String[] resolveGroupAndCountry(Employee employee) {
        if (employee != null && employee.getUser() != null) {
            List<UserStore> userStores = userStoreRepository.findByIdUserId(employee.getUser().getId());
            if (userStores != null && !userStores.isEmpty()) {
                com.plus33.erp.organization.entity.Store store = userStores.get(0).getStore();
                if (store != null && store.getRegion() != null && store.getRegion().getCode() != null) {
                    String code = store.getRegion().getCode().toUpperCase();
                    if (code.startsWith("IN"))  return new String[]{"INDIA", "IN"};
                    if (code.startsWith("AE") || code.startsWith("UAE")) return new String[]{"UAE", "AE"};
                    if (code.startsWith("FR") || code.startsWith("EU")) return new String[]{"EU", "FR"};
                    // Unknown region: default to EU
                    return new String[]{"EU", code};
                }
            }
        }
        return new String[]{"EU", "FR"}; // Global default
    }

    private Long resolveStoreId(Long userId) {
        List<UserStore> userStores = userStoreRepository.findByIdUserId(userId);
        if (userStores != null && !userStores.isEmpty() && userStores.get(0).getStore() != null) {
            return userStores.get(0).getStore().getId();
        }
        return null;
    }

    private boolean isHalfDay(String session) {
        return "HALF_DAY_AM".equalsIgnoreCase(session) || "HALF_DAY_PM".equalsIgnoreCase(session);
    }

    private void updateAttendanceToLeave(Attendance att, EmployeeLeave leave, boolean isPaid) {
        att.setLeave(leave);
        att.setLeaveType(leave.getLeaveType());
        att.setPaidLeave(isPaid);
        att.setDeduction(!isPaid);
        att.setPayrollStatus("Pending");
        att.setOvertimeMinutes(0);
        att.setLateMinutes(0);
        att.setEarlyOutMinutes(0);

        if (isHalfDay(leave.getLeaveSession())) {
            att.setStatus("HALF_DAY");
            att.setLeaveMinutes(240);
            if (att.getWorkMinutes() == null || att.getWorkMinutes() == 0) {
                att.setWorkMinutes(240);
            }
        } else {
            att.setStatus("LEAVE");
            att.setLeaveMinutes(480);
            att.setWorkMinutes(0);
        }
        attendanceRepository.save(att);
    }

    private void revertAttendanceLeave(Attendance att) {
        if (att.getCheckInTime() != null || att.getCheckOutTime() != null) {
            att.setStatus(att.getWorkMinutes() != null && att.getWorkMinutes() >= 240 ? "PRESENT" : "HALF_DAY");
            att.setLeave(null);
            att.setLeaveType(null);
            att.setPaidLeave(false);
            att.setDeduction(false);
            att.setLeaveMinutes(0);
            att.setPayrollStatus("Pending");
            attendanceRepository.save(att);
        } else {
            attendanceRepository.delete(att);
        }
    }

    private Shift getActiveShift(Employee employee, LocalDate date) {
        return employeeShiftRepository.findActiveShiftForEmployeeOnDate(employee.getId(), date)
                .map(EmployeeShift::getShift)
                .orElseGet(() -> shiftRepository.findByCode("SHIFT_MORN")
                        .orElseGet(() -> shiftRepository.findAll().stream().findFirst()
                                .orElseThrow(() -> new BusinessException("No shifts configured in the system."))));
    }

    @Transactional
    public void checkAndCreditCompOff(Attendance att) {
        Employee employee = att.getEmployee();
        String[] resolved  = resolveGroupAndCountry(employee);
        String countryCode = resolved[1]; // Legacy country code for holiday calendar
        boolean isHoliday  = holidayRepository.existsByCountryCodeAndHolidayDate(countryCode, att.getAttendanceDate());
        if (isHoliday && ("PRESENT".equalsIgnoreCase(att.getStatus()) || "HALF_DAY".equalsIgnoreCase(att.getStatus()))) {
            LeaveType compOffType = leaveTypeRepository.findByCode("COMP_OFF").orElse(null);
            if (compOffType != null) {
                int currentYear = att.getAttendanceDate().getYear();
                BigDecimal credit = "HALF_DAY".equalsIgnoreCase(att.getStatus()) ? new BigDecimal("0.5") : BigDecimal.ONE;
                String note = "Comp Off Earned for working on holiday: " + att.getAttendanceDate().toString();
                
                boolean exists = transactionRepository.existsByEmployeeIdAndTransactionTypeAndNote(
                        employee.getId(), "COMP_OFF_EARNED", note);
                if (!exists) {
                    EmployeeLeaveBalance balance = getOrCreateBalance(employee.getId(), compOffType.getId(), currentYear);
                    balance.setAccrued(balance.getAccrued().add(credit));
                    balanceRepository.save(balance);

                    EmployeeLeaveTransaction txn = new EmployeeLeaveTransaction();
                    txn.setEmployee(employee);
                    txn.setLeaveType(compOffType);
                    txn.setTransactionType("COMP_OFF_EARNED");
                    txn.setDays(credit);
                    txn.setNote(note);
                    transactionRepository.save(txn);

                    writeAudit(null, employee.getUser() != null ? employee.getUser().getId() : null,
                            "COMP_OFF_EARNED", null, null,
                            "Credited " + credit + " days Comp Off for working on holiday: " + att.getAttendanceDate(), null);
                }
            }
        }
    }

    public boolean isRejectedBySupervisor(Long leaveId) {
        return approvalHistoryRepository.findByLeaveIdOrderByLevelAsc(leaveId).stream()
                .anyMatch(h -> "SUPERVISOR".equals(h.getApproverRole()) && "REJECTED".equals(h.getDecision()));
    }

    @Transactional
    public EmployeeLeave escalateLeave(Long leaveId, Long employeeUserId) {
        EmployeeLeave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new BusinessException("Leave request not found."));

        if (!"REJECTED".equals(leave.getStatus())) {
            throw new BusinessException("Only REJECTED leave requests can be escalated.");
        }

        if (!isRejectedBySupervisor(leaveId)) {
            throw new BusinessException("Only requests rejected by a Shift Supervisor can be escalated.");
        }

        String oldStatus = leave.getStatus();
        leave.setStatus("PENDING");
        leave.setApproverRole("STORE_ADMIN");
        leave.setCurrentApprovalLevel(2);
        leave.setEscalatedAt(LocalDateTime.now());
        leave.setEscalatedTo("STORE_ADMIN");

        // Debit pending balance again (since rejection refunded it)
        int year = leave.getStartDate().getYear();
        EmployeeLeaveBalance balance = getOrCreateBalance(leave.getEmployee().getId(), leave.getLeaveType().getId(), year);
        balance.setPending(balance.getPending().add(leave.getTotalDays()));
        balanceRepository.save(balance);

        // Ledger transaction: debit as PENDING
        writeTransaction(leave.getEmployee(), leave.getLeaveType(), leave.getTotalDays().negate(), "APPROVAL",
                leave, "Escalated pending deduction for leave request");

        EmployeeLeave saved = leaveRepository.save(leave);

        writeApprovalHistory(saved, 2, null, "SYSTEM", "ESCALATED",
                oldStatus, "PENDING", "Escalated to Store Admin after supervisor rejection", null, null);

        writeAudit(saved, employeeUserId, "ESCALATED",
                "{\"status\":\"REJECTED\"}", "{\"status\":\"PENDING\",\"approverRole\":\"STORE_ADMIN\"}",
                "Escalated leave request to Store Admin after supervisor rejection", null);

        return saved;
    }
}
