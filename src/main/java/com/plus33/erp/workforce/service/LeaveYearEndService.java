package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.repository.*;
import com.plus33.erp.workforce.service.LeavePolicyService.ResolvedPolicy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveYearEndService {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeavePolicyService leavePolicyService;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final EmployeeLeaveTransactionRepository transactionRepository;
    private final EmployeeLeaveYearSummaryRepository yearSummaryRepository;
    private final LeaveAuditLogRepository auditLogRepository;
    private final UserStoreRepository userStoreRepository;

    public LeaveYearEndService(
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeavePolicyService leavePolicyService,
            EmployeeLeaveBalanceRepository balanceRepository,
            EmployeeLeaveTransactionRepository transactionRepository,
            EmployeeLeaveYearSummaryRepository yearSummaryRepository,
            LeaveAuditLogRepository auditLogRepository,
            UserStoreRepository userStoreRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leavePolicyService = leavePolicyService;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.yearSummaryRepository = yearSummaryRepository;
        this.auditLogRepository = auditLogRepository;
        this.userStoreRepository = userStoreRepository;
    }

    /**
     * Scheduled to run on January 1st at midnight.
     * Takes snapshots of the previous year's balances, resolves carrying forward limits,
     * and seeds opening balances for the new year.
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    @Transactional
    public void processYearEnd() {
        int previousYear = LocalDate.now().getYear() - 1;
        int currentYear = LocalDate.now().getYear();

        List<Employee> employees = employeeRepository.findAll().stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive()) && "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .collect(java.util.stream.Collectors.toList());

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll().stream()
                .filter(lt -> Boolean.TRUE.equals(lt.getActive()))
                .collect(java.util.stream.Collectors.toList());

        for (Employee emp : employees) {
            String countryCode = resolveCountryCode(emp);
            for (LeaveType lt : leaveTypes) {
                // Find previous year's cached balance
                EmployeeLeaveBalance prevBalance = balanceRepository
                        .findByEmployeeIdAndLeaveTypeIdAndYear(emp.getId(), lt.getId(), previousYear)
                        .orElse(null);

                BigDecimal remaining = BigDecimal.ZERO;
                BigDecimal carryForwardAmount = BigDecimal.ZERO;
                BigDecimal expiredAmount = BigDecimal.ZERO;

                if (prevBalance != null) {
                    remaining = prevBalance.getRemaining();

                    // Freeze historical record in year summary snapshot
                    EmployeeLeaveYearSummary summary = new EmployeeLeaveYearSummary();
                    summary.setEmployee(emp);
                    summary.setYear(previousYear);
                    summary.setAnnualEntitlement(prevBalance.getOpeningBalance());
                    summary.setCarryForward(prevBalance.getCarryForward());
                    summary.setAccrued(prevBalance.getAccrued());
                    summary.setUsed(prevBalance.getUsed());
                    summary.setPending(prevBalance.getPending());
                    summary.setEncashed(prevBalance.getEncashedDays());
                    summary.setRemaining(remaining);
                    summary.setSnapshotDate(LocalDate.now());
                    yearSummaryRepository.save(summary);

                    // Resolve carry-forward limit from policy
                    ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, lt, countryCode, LocalDate.now());
                    BigDecimal maxCf = policy.carryForwardMax != null ? policy.carryForwardMax : BigDecimal.ZERO;

                    if (maxCf.compareTo(BigDecimal.ZERO) > 0 && remaining.compareTo(BigDecimal.ZERO) > 0) {
                        carryForwardAmount = remaining.min(maxCf);
                    }
                    expiredAmount = remaining.subtract(carryForwardAmount);
                }

                // Initialize balance for current year
                EmployeeLeaveBalance currentBalance = new EmployeeLeaveBalance();
                currentBalance.setEmployee(emp);
                currentBalance.setLeaveType(lt);
                currentBalance.setYear(currentYear);
                
                // Set opening balance from default policy entitlement
                ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, lt, countryCode, LocalDate.now());
                BigDecimal opening = policy.annualEntitlement != null ? policy.annualEntitlement : BigDecimal.ZERO;
                currentBalance.setOpeningBalance(opening);
                currentBalance.setCarryForward(carryForwardAmount);
                balanceRepository.save(currentBalance);

                // Write carry forward ledger transaction if applicable
                if (carryForwardAmount.compareTo(BigDecimal.ZERO) > 0) {
                    EmployeeLeaveTransaction txn = new EmployeeLeaveTransaction();
                    txn.setEmployee(emp);
                    txn.setLeaveType(lt);
                    txn.setTransactionType("CARRY_FORWARD");
                    txn.setDays(carryForwardAmount);
                    txn.setNote("Carry forward credit from year " + previousYear);
                    transactionRepository.save(txn);

                    // Write audit entry
                    LeaveAuditLog audit = new LeaveAuditLog();
                    audit.setLeave(null);
                    audit.setAction("CARRY_FORWARDED");
                    audit.setNote("Balance carry forward processed: " + carryForwardAmount + " day(s)");
                    auditLogRepository.save(audit);
                }

                // Write expiry transaction ledger entry and audit log if applicable
                if (expiredAmount.compareTo(BigDecimal.ZERO) > 0) {
                    EmployeeLeaveTransaction txn = new EmployeeLeaveTransaction();
                    txn.setEmployee(emp);
                    txn.setLeaveType(lt);
                    txn.setTransactionType("EXPIRY");
                    txn.setDays(expiredAmount.negate());
                    txn.setNote("Leave balance expired at year-end from year " + previousYear);
                    transactionRepository.save(txn);

                    // Write audit entry
                    LeaveAuditLog audit = new LeaveAuditLog();
                    audit.setLeave(null);
                    audit.setAction("EXPIRED");
                    audit.setNote("Expired " + expiredAmount + " unused day(s) of " + lt.getCode() + " at year-end.");
                    auditLogRepository.save(audit);
                }
            }
        }
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
