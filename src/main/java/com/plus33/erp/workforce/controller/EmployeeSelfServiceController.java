package com.plus33.erp.workforce.controller;

import com.plus33.erp.workforce.dto.PayslipResponse;
import com.plus33.erp.workforce.service.EmployeeSelfService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/employee-self-service")
public class EmployeeSelfServiceController {

    private final EmployeeSelfService employeeSelfService;

    public EmployeeSelfServiceController(EmployeeSelfService employeeSelfService) {
        this.employeeSelfService = employeeSelfService;
    }

    @GetMapping("/payslip/{runId}/{employeeId}")
    @PreAuthorize("hasAuthority('PAYROLL_VIEW')")
    public ResponseEntity<PayslipResponse> getPayslip(@PathVariable Long runId, @PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeSelfService.getPayslipForEmployee(runId, employeeId));
    }
}
