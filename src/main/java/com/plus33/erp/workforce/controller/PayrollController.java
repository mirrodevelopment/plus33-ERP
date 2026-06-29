package com.plus33.erp.workforce.controller;

import com.plus33.erp.workforce.dto.PayrollDashboardSummaryResponse;
import com.plus33.erp.workforce.dto.PayrollRunRequest;
import com.plus33.erp.workforce.dto.PayrollRunResponse;
import com.plus33.erp.workforce.exporter.CsvPayrollExporter;
import com.plus33.erp.workforce.service.PayrollProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/payroll")
public class PayrollController {

    private final PayrollProcessingService payrollProcessingService;
    private final CsvPayrollExporter csvPayrollExporter;

    public PayrollController(PayrollProcessingService payrollProcessingService,
                             CsvPayrollExporter csvPayrollExporter) {
        this.payrollProcessingService = payrollProcessingService;
        this.csvPayrollExporter = csvPayrollExporter;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAYROLL_PROCESS')")
    public ResponseEntity<PayrollRunResponse> createPayrollRun(@RequestBody PayrollRunRequest request) {
        return ResponseEntity.ok(payrollProcessingService.createPayrollRun(request));
    }

    @PostMapping("/{id}/calculate")
    @PreAuthorize("hasAuthority('PAYROLL_PROCESS')")
    public ResponseEntity<PayrollRunResponse> calculatePayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.calculatePayrollRun(id));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PAYROLL_APPROVE')")
    public ResponseEntity<PayrollRunResponse> approvePayrollRun(@PathVariable Long id, @RequestParam(required = false) String approver) {
        return ResponseEntity.ok(payrollProcessingService.approvePayrollRun(id, approver));
    }

    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('PAYROLL_POST_GL')")
    public ResponseEntity<PayrollRunResponse> postPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.postPayrollRun(id));
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('PAYROLL_PAY')")
    public ResponseEntity<PayrollRunResponse> payPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.payPayrollRun(id));
    }

    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAuthority('PAYROLL_REVERSAL')")
    public ResponseEntity<PayrollRunResponse> reversePayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.reversePayrollRun(id));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYROLL_VIEW')")
    public ResponseEntity<PayrollRunResponse> getPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.getPayrollRun(id));
    }

    @GetMapping("/dashboard/{companyId}")
    @PreAuthorize("hasAuthority('PAYROLL_REPORT_VIEW')")
    public ResponseEntity<PayrollDashboardSummaryResponse> getDashboardSummary(@PathVariable Long companyId) {
        return ResponseEntity.ok(payrollProcessingService.getDashboardSummary(companyId));
    }

    @GetMapping("/{id}/export/csv")
    @PreAuthorize("hasAuthority('PAYROLL_EXPORT')")
    public ResponseEntity<String> exportCsv(@PathVariable Long id) {
        PayrollRunResponse run = payrollProcessingService.getPayrollRun(id);
        return ResponseEntity.ok(csvPayrollExporter.exportToCsv(run));
    }
}
