/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : EmployeeSelfServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSelfController
 * Related Service   : EmployeeSelfServiceImpl
 * Related Repository: PayrollRunRepository, PayrollRunItemRepository, EmployeeRepository
 * Related Entity    : EmployeeSelf
 * Related DTO       : PayslipResponse
 * Related Mapper    : EmployeeSelfMapper
 * Related DB Table  : employee_selfs
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : EmployeeSelfController, EmployeeSelfServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements EmployeeSelfService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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
import java.time.format.DateTimeFormatter;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeSelfServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EmployeeSelfController
 *   --> EmployeeSelfServiceImpl (this)
 *   --> Validate business rules
 *   --> EmployeeSelfRepository (read/write 'employee_selfs')
 *   --> EmployeeSelfMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code employee_selfs}</p>
 * <p><b>Module Deps      :</b> Common, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves payslip for employee data from the database.
     *
     * @param runId the runId input value
     * @param employeeId the employeeId input value
     * @return the PayslipResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
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
        String paidAtStr = run.getPaidAt() != null ? run.getPaidAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
        String statusStr = run.getStatus() != null ? run.getStatus().name() : "DRAFT";
        return new PayslipResponse(
                run.getId(),
                emp.getId(),
                fullName.trim(),
                emp.getEmployeeCode(),
                run.getRunNumber(),
                paidAtStr,
                statusStr,
                item.getGrossPay(),
                item.getNetPay(),
                item.getTotalDeductions(),
                item.getTaxWithheld(),
                "EUR",
                item.getAttendanceSnapshot(),
                item.getLeaveSnapshot(),
                item.getSalarySnapshot(),
                item.getWorkingHourSnapshot(),
                item.getOvertimeSnapshot(),
                item.getBenefitSnapshot(),
                item.getTaxSnapshot(),
                item.getEmployerContributionSnapshot(),
                item.getPayrollAudit(),
                emp.getBankName(),
                emp.getBankAccountNumber(),
                emp.getIfscNumber()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<PayslipResponse> getPayslipsForEmployee(Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));
        String fullName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " " + (emp.getLastName() != null ? emp.getLastName() : "");
        String empCode = emp.getEmployeeCode() != null ? emp.getEmployeeCode() : "N/A";

        java.util.List<PayrollRunItem> items = payrollRunItemRepository.findByEmployeeId(employeeId);
        java.util.List<PayslipResponse> list = new java.util.ArrayList<>();
        for (PayrollRunItem item : items) {
            PayrollRun run = payrollRunRepository.findById(item.getPayrollRunId()).orElse(null);
            String runNum  = run != null ? run.getRunNumber() : "RUN-UNKNOWN";
            String paidAt  = (run != null && run.getPaidAt() != null)
                    ? run.getPaidAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
            String status  = (run != null && run.getStatus() != null)
                    ? run.getStatus().name() : "DRAFT";
            Long runId     = run != null ? run.getId() : null;
            list.add(new PayslipResponse(
                    runId,
                    emp.getId(),
                    fullName.trim(),
                    empCode,
                    runNum,
                    paidAt,
                    status,
                    item.getGrossPay(),
                    item.getNetPay(),
                    item.getTotalDeductions(),
                    item.getTaxWithheld(),
                    "EUR",
                    item.getAttendanceSnapshot(),
                    item.getLeaveSnapshot(),
                    item.getSalarySnapshot(),
                    item.getWorkingHourSnapshot(),
                    item.getOvertimeSnapshot(),
                    item.getBenefitSnapshot(),
                    item.getTaxSnapshot(),
                    item.getEmployerContributionSnapshot(),
                    item.getPayrollAudit(),
                    emp.getBankName(),
                    emp.getBankAccountNumber(),
                    emp.getIfscNumber()
            ));
        }
        // Sort most recent first (paidAt desc, fall back to runNumber desc)
        list.sort((a, b) -> {
            if (a.paidAt() == null && b.paidAt() == null) return 0;
            if (a.paidAt() == null) return 1;
            if (b.paidAt() == null) return -1;
            return b.paidAt().compareTo(a.paidAt());
        });
        return list;
    }
}