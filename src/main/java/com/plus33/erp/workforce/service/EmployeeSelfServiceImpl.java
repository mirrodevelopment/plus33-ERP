package com.plus33.erp.workforce.service;

import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.workforce.dto.PayslipResponse;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.PayrollRun;
import com.plus33.erp.workforce.entity.PayrollRunItem;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.PayrollRunItemRepository;
import com.plus33.erp.workforce.repository.PayrollRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeSelfServiceImpl implements EmployeeSelfService {

    private final PayrollRunRepository payrollRunRepository;
    private final PayrollRunItemRepository payrollRunItemRepository;
    private final EmployeeRepository employeeRepository;

    public EmployeeSelfServiceImpl(PayrollRunRepository payrollRunRepository,
                                   PayrollRunItemRepository payrollRunItemRepository,
                                   EmployeeRepository employeeRepository) {
        this.payrollRunRepository = payrollRunRepository;
        this.payrollRunItemRepository = payrollRunItemRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PayslipResponse getPayslipForEmployee(Long runId, Long employeeId) {
        PayrollRun run = payrollRunRepository.findById(runId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll run not found: " + runId));
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        PayrollRunItem item = payrollRunItemRepository.findByPayrollRunId(runId).stream()
                .filter(i -> i.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payslip item not found for employee: " + employeeId));

        String fullName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " " + (emp.getLastName() != null ? emp.getLastName() : "");
        return new PayslipResponse(emp.getId(), fullName.trim(), run.getRunNumber(), item.getGrossPay(), item.getNetPay(), item.getTotalDeductions(), item.getTaxWithheld(), "USD");
    }
}
