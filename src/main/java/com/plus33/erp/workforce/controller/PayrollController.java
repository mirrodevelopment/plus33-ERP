/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.controller
 * File              : PayrollController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollController
 * Related Service   : PayrollControllerService, PayrollControllerServiceImpl
 * Related Repository: PayrollControllerRepository
 * Related Entity    : PayrollController
 * Related DTO       : PayrollDashboardSummaryResponse, PayrollRunRequest, PayrollRunResponse
 * Related Mapper    : PayrollControllerMapper
 * Related DB Table  : payroll_controllers
 * Related REST APIs : POST /api/v2/payroll, POST /api/v2/payroll/{id}/calculate, POST /api/v2/payroll/{id}/approve, POST /api/v2/payroll/{id}/post
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Workforce Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v2/payroll, POST /api/v2/payroll/{id}/calculate, POST /api/v2/payroll/{id}/approve, POST /api/v2/payroll/{id}/post
 ******************************************************************************/
package com.plus33.erp.workforce.controller;

import com.plus33.erp.workforce.dto.PayrollDashboardSummaryResponse;
import com.plus33.erp.workforce.dto.PayrollRunRequest;
import com.plus33.erp.workforce.dto.PayrollRunResponse;
import com.plus33.erp.workforce.exporter.CsvPayrollExporter;
import com.plus33.erp.workforce.service.PayrollProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PayrollService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PayrollController.endpoint()
 *   --> PayrollService.method()
 *   --> PayrollRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v2/payroll, POST /api/v2/payroll/{id}/calculate, POST /api/v2/payroll/{id}/approve, POST /api/v2/payroll/{id}/post, POST /api/v2/payroll/{id}/pay</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new payroll run and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PAYROLL_PROCESS')")
    public ResponseEntity<PayrollRunResponse> createPayrollRun(@RequestBody PayrollRunRequest request) {
        return ResponseEntity.ok(payrollProcessingService.createPayrollRun(request));
    }

    /**
     * Calculates payroll run totals including subtotal, tax, discounts, and net amount.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/calculate")
    @PreAuthorize("hasAuthority('PAYROLL_PROCESS')")
    public ResponseEntity<PayrollRunResponse> calculatePayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.calculatePayrollRun(id));
    }

    /**
     * Approves the payroll run, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param approver the approver input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PAYROLL_APPROVE')")
    public ResponseEntity<PayrollRunResponse> approvePayrollRun(@PathVariable Long id, @RequestParam(required = false) String approver) {
        return ResponseEntity.ok(payrollProcessingService.approvePayrollRun(id, approver));
    }

    /**
     * Posts payroll run entries to the General Ledger and updates financial balances.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('PAYROLL_POST_GL')")
    public ResponseEntity<PayrollRunResponse> postPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.postPayrollRun(id));
    }

    /**
     * Processes payment for payroll run and updates the outstanding balance.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('PAYROLL_PAY')")
    public ResponseEntity<PayrollRunResponse> payPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.payPayrollRun(id));
    }

    /**
     * Performs the reversePayrollRun operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/reverse")
    @PreAuthorize("hasAuthority('PAYROLL_REVERSAL')")
    public ResponseEntity<PayrollRunResponse> reversePayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.reversePayrollRun(id));
    }

    /**
     * Retrieves payroll run data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYROLL_VIEW')")
    public ResponseEntity<PayrollRunResponse> getPayrollRun(@PathVariable Long id) {
        return ResponseEntity.ok(payrollProcessingService.getPayrollRun(id));
    }

    /**
     * Retrieves dashboard summary data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dashboard/{companyId}")
    @PreAuthorize("hasAuthority('PAYROLL_REPORT_VIEW')")
    public ResponseEntity<PayrollDashboardSummaryResponse> getDashboardSummary(@PathVariable Long companyId) {
        return ResponseEntity.ok(payrollProcessingService.getDashboardSummary(companyId));
    }

    /**
     * Exports csv data as a report or downloadable file.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     */
    @GetMapping("/{id}/export/csv")
    @PreAuthorize("hasAuthority('PAYROLL_EXPORT')")
    public ResponseEntity<String> exportCsv(@PathVariable Long id) {
        PayrollRunResponse run = payrollProcessingService.getPayrollRun(id);
        return ResponseEntity.ok(csvPayrollExporter.exportToCsv(run));
    }
}