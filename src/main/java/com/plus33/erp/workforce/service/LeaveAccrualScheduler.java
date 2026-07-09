package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.EmployeeLeaveBalance;
import com.plus33.erp.workforce.entity.EmployeeLeaveTransaction;
import com.plus33.erp.workforce.entity.LeaveType;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeLeaveBalanceRepository;
import com.plus33.erp.workforce.repository.EmployeeLeaveTransactionRepository;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.LeaveTypeRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import com.plus33.erp.workforce.service.LeavePolicyService.ResolvedPolicy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class LeaveAccrualScheduler {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeavePolicyService leavePolicyService;
    private final EmployeeLeaveBalanceRepository balanceRepository;
    private final EmployeeLeaveTransactionRepository transactionRepository;
    private final UserStoreRepository userStoreRepository;

    public LeaveAccrualScheduler(
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeavePolicyService leavePolicyService,
            EmployeeLeaveBalanceRepository balanceRepository,
            EmployeeLeaveTransactionRepository transactionRepository,
            UserStoreRepository userStoreRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leavePolicyService = leavePolicyService;
        this.balanceRepository = balanceRepository;
        this.transactionRepository = transactionRepository;
        this.userStoreRepository = userStoreRepository;
    }

    /**
     * Runs on the 1st of every month at midnight.
     * Credits monthly accrual days to all active employees based on resolved policy.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void runMonthlyAccrual() {
        List<Employee> activeEmployees = employeeRepository.findAll().stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive()) && "ACTIVE".equalsIgnoreCase(e.getStatus()))
                .collect(java.util.stream.Collectors.toList());

        List<LeaveType> activeLeaveTypes = leaveTypeRepository.findAll().stream()
                .filter(lt -> Boolean.TRUE.equals(lt.getActive()))
                .collect(java.util.stream.Collectors.toList());

        int currentYear = LocalDate.now().getYear();
        String currentMonthName = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        for (Employee emp : activeEmployees) {
            String countryCode = resolveCountryCode(emp);
            for (LeaveType lt : activeLeaveTypes) {
                ResolvedPolicy policy = leavePolicyService.resolvePolicy(1L, lt, countryCode, LocalDate.now());
                if (policy.monthlyAccrual != null && policy.monthlyAccrual.compareTo(BigDecimal.ZERO) > 0) {
                    // Credit monthly accrual
                    BigDecimal amount = policy.monthlyAccrual;
                    
                    // Write transaction ledger entry
                    EmployeeLeaveTransaction txn = new EmployeeLeaveTransaction();
                    txn.setEmployee(emp);
                    txn.setLeaveType(lt);
                    txn.setTransactionType("ACCRUAL");
                    txn.setDays(amount);
                    txn.setNote("Monthly accrual credit for " + currentMonthName + " " + currentYear);
                    transactionRepository.save(txn);

                    // Update balance cache
                    EmployeeLeaveBalance balance = balanceRepository
                            .findByEmployeeIdAndLeaveTypeIdAndYear(emp.getId(), lt.getId(), currentYear)
                            .orElseGet(() -> {
                                EmployeeLeaveBalance b = new EmployeeLeaveBalance();
                                b.setEmployee(emp);
                                b.setLeaveType(lt);
                                b.setYear(currentYear);
                                return b;
                            });
                    balance.setAccrued(balance.getAccrued().add(amount));
                    balanceRepository.save(balance);
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
