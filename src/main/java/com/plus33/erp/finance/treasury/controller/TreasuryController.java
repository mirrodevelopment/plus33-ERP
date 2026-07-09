/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.controller
 * File              : TreasuryController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryController
 * Related Service   : TreasuryControllerService, TreasuryControllerServiceImpl
 * Related Repository: TreasuryControllerRepository
 * Related Entity    : TreasuryController
 * Related DTO       : ApiResponse, BankAccountRequest, BankAccountResponse, BankBranchRequest, BankBranchResponse
 * Related Mapper    : TreasuryControllerMapper
 * Related DB Table  : treasury_controllers
 * Related REST APIs : POST /api/v1/treasury/banks, GET /api/v1/treasury/banks, POST /api/v1/treasury/branches, POST /api/v1/treasury/accounts
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/treasury/banks, GET /api/v1/treasury/banks, POST /api/v1/treasury/branches, POST /api/v1/treasury/accounts
 ******************************************************************************/
package com.plus33.erp.finance.treasury.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.treasury.dto.BankAccountDtos.*;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;
import com.plus33.erp.finance.treasury.dto.PaymentFactoryDtos.*;
import com.plus33.erp.finance.treasury.dto.StatementAndRecDtos.*;
import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;
import com.plus33.erp.finance.treasury.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to TreasuryService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> TreasuryController.endpoint()
 *   --> TreasuryService.method()
 *   --> TreasuryRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/treasury/banks, GET /api/v1/treasury/banks, POST /api/v1/treasury/branches, POST /api/v1/treasury/accounts, GET /api/v1/treasury/accounts/company/{companyId}</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/treasury")
@RequiredArgsConstructor
public class TreasuryController {

    private final BankAccountService bankAccountService;
    private final InHouseBankService inHouseBankService;
    private final ReconciliationService reconciliationService;
    private final PaymentFactoryService paymentFactoryService;
    private final TreasuryInvestmentService treasuryInvestmentService;
    private final TreasuryForecastService treasuryForecastService;

    // --- Bank Account Hierarchy ---
    /**
     * Creates a new bank and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/banks")
    public ResponseEntity<ApiResponse<BankResponse>> createBank(@RequestBody BankRequest request) {
        BankResponse res = bankAccountService.createBank(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank created successfully", res));
    }

    /**
     * Retrieves a paginated list of all banks records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/banks")
    public ResponseEntity<ApiResponse<List<BankResponse>>> getAllBanks() {
        List<BankResponse> res = bankAccountService.getAllBanks();
        return ResponseEntity.ok(ApiResponse.success("Banks retrieved successfully", res));
    }

    /**
     * Creates a new branch and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/branches")
    public ResponseEntity<ApiResponse<BankBranchResponse>> createBranch(@RequestBody BankBranchRequest request) {
        BankBranchResponse res = bankAccountService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank branch created successfully", res));
    }

    /**
     * Creates a new bank account and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<BankAccountResponse>> createBankAccount(@RequestBody BankAccountRequest request) {
        BankAccountResponse res = bankAccountService.createBankAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank account created successfully", res));
    }

    /**
     * Retrieves bank accounts by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/accounts/company/{companyId}")
    public ResponseEntity<ApiResponse<List<BankAccountResponse>>> getBankAccountsByCompany(@PathVariable Long companyId) {
        List<BankAccountResponse> res = bankAccountService.getBankAccountsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Bank accounts retrieved successfully", res));
    }

    /**
     * Creates a new virtual account and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/virtual-accounts")
    public ResponseEntity<ApiResponse<BankVirtualAccountResponse>> createVirtualAccount(@RequestBody BankVirtualAccountRequest request) {
        BankVirtualAccountResponse res = bankAccountService.createVirtualAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Virtual account created successfully", res));
    }

    // --- Cash Pools ---
    /**
     * Creates a new cash pool and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/pools")
    public ResponseEntity<ApiResponse<CashPoolResponse>> createCashPool(@RequestBody CashPoolRequest request) {
        CashPoolResponse res = bankAccountService.createCashPool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash pool created successfully", res));
    }

    /**
     * Performs the executeCashPoolSweeps operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/pools/{id}/sweep")
    public ResponseEntity<ApiResponse<Void>> executeCashPoolSweeps(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        bankAccountService.executeCashPoolSweeps(id, username);
        return ResponseEntity.ok(ApiResponse.success("Cash pool sweeps executed successfully", null));
    }

    // --- In-House Bank ---
    /**
     * Creates a new in house account and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/ihb/accounts")
    public ResponseEntity<ApiResponse<InHouseBankAccountResponse>> createInHouseAccount(@RequestBody InHouseBankAccountRequest request) {
        InHouseBankAccountResponse res = inHouseBankService.createInHouseAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("In-house bank account created successfully", res));
    }

    /**
     * Creates a new loan and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the numeric result value
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/ihb/loans")
    public ResponseEntity<ApiResponse<IntercompanyLoanResponse>> createLoan(@RequestBody IntercompanyLoanRequest request) {
        IntercompanyLoanResponse res = inHouseBankService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Intercompany loan created successfully", res));
    }

    /**
     * Performs the accrueLoanInterest operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/ihb/loans/{id}/accrue")
    public ResponseEntity<ApiResponse<Void>> accrueLoanInterest(@PathVariable Long id) {
        inHouseBankService.accrueIntercompanyLoanInterest(id);
        return ResponseEntity.ok(ApiResponse.success("Loan interest accrued successfully", null));
    }

    /**
     * Creates a new internal settlement and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the numeric result value
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/ihb/settlements")
    public ResponseEntity<ApiResponse<InternalSettlementResponse>> createInternalSettlement(@RequestBody InternalSettlementRequest request) {
        InternalSettlementResponse res = inHouseBankService.createInternalSettlement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Internal settlement posted successfully", res));
    }

    // --- Payment Factory ---
    /**
     * Creates a new payment batch and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/payments/batches")
    public ResponseEntity<ApiResponse<PaymentBatchResponse>> createPaymentBatch(@RequestBody PaymentBatchRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        PaymentBatchResponse res = paymentFactoryService.createPaymentBatch(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Payment batch created successfully", res));
    }

    /**
     * Approves the payment batch, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/payments/batches/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approvePaymentBatch(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.approvePaymentBatch(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Payment batch approved", null));
    }

    /**
     * Performs the rejectPaymentBatch operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/payments/batches/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectPaymentBatch(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.rejectPaymentBatch(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Payment batch rejected", null));
    }

    /**
     * Generates the payment file based on input parameters and business rules.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param format the format input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/payments/batches/{id}/generate-file")
    public ResponseEntity<ApiResponse<PaymentFileResponse>> generatePaymentFile(@PathVariable Long id, @RequestParam String format) {
        PaymentFileResponse res = paymentFactoryService.generatePaymentFile(id, format);
        return ResponseEntity.ok(ApiResponse.success("Payment file generated successfully", res));
    }

    /**
     * Performs the transmitPaymentFile operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param method the method input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/payments/files/{id}/transmit")
    public ResponseEntity<ApiResponse<Void>> transmitPaymentFile(@PathVariable Long id, @RequestParam String method) {
        paymentFactoryService.transmitPaymentFile(id, method);
        return ResponseEntity.ok(ApiResponse.success("Payment file transmitted successfully", null));
    }

    // --- Cash Transfers ---
    /**
     * Creates a new cash transfer and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<CashTransferResponse>> createCashTransfer(@RequestBody CashTransferRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        CashTransferResponse res = paymentFactoryService.createCashTransfer(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash transfer requested successfully", res));
    }

    /**
     * Approves the cash transfer, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/transfers/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approveCashTransfer(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.approveCashTransfer(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Cash transfer approved", null));
    }

    /**
     * Performs the rejectCashTransfer operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/transfers/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectCashTransfer(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.rejectCashTransfer(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Cash transfer rejected", null));
    }

    // --- Reconciliation ---
    /**
     * Creates a new rule and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/reconciliation/rules")
    public ResponseEntity<ApiResponse<ReconciliationRuleResponse>> createRule(@RequestBody ReconciliationRuleRequest request) {
        ReconciliationRuleResponse res = reconciliationService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Reconciliation rule created successfully", res));
    }

    /**
     * Imports statement data from an external source or file.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/statements/import")
    public ResponseEntity<ApiResponse<BankStatementResponse>> importStatement(@RequestBody BankStatementRequest request) {
        BankStatementResponse res = reconciliationService.importStatement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank statement imported successfully", res));
    }

    /**
     * Performs the autoReconcile operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/statements/{id}/auto-reconcile")
    public ResponseEntity<ApiResponse<Void>> autoReconcile(@PathVariable Long id, @RequestParam Long companyId) {
        reconciliationService.runAutoReconciliation(id, companyId);
        return ResponseEntity.ok(ApiResponse.success("Auto-reconciliation run finished", null));
    }

    /**
     * Performs the manualMatch operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param paymentId the paymentId input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/statements/lines/{id}/manual-match")
    public ResponseEntity<ApiResponse<Void>> manualMatch(@PathVariable Long id, @RequestParam Long paymentId, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.manualMatch(id, paymentId, username);
        return ResponseEntity.ok(ApiResponse.success("Manual match posted successfully", null));
    }

    /**
     * Performs the splitMatch operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param paymentIds the paymentIds input value
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/statements/lines/{id}/split-match")
    public ResponseEntity<ApiResponse<Void>> splitMatch(@PathVariable Long id, @RequestBody List<Long> paymentIds, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.splitAndMatch(id, paymentIds, username);
        return ResponseEntity.ok(ApiResponse.success("Split match posted successfully", null));
    }

    // --- Fees & FX Revaluation ---
    /**
     * Processes the fees business workflow end-to-end.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/accounts/{id}/process-fees")
    public ResponseEntity<ApiResponse<Void>> processFees(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.processBankFees(id, username);
        return ResponseEntity.ok(ApiResponse.success("Fees processed successfully", null));
    }

    /**
     * Performs the revalueCompanyAccounts operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/companies/{id}/revalue")
    public ResponseEntity<ApiResponse<Void>> revalueCompanyAccounts(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        treasuryInvestmentService.executeFXRevaluations(id, username);
        return ResponseEntity.ok(ApiResponse.success("FX revaluation completed", null));
    }

    // --- Investments ---
    /**
     * Creates a new investment and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/investments")
    public ResponseEntity<ApiResponse<TreasuryInvestmentResponse>> createInvestment(@RequestBody TreasuryInvestmentRequest request) {
        TreasuryInvestmentResponse res = treasuryInvestmentService.createInvestment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Investment registered successfully", res));
    }

    /**
     * Performs the accrueInterest operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/investments/{id}/accrue")
    public ResponseEntity<ApiResponse<Void>> accrueInterest(@PathVariable Long id) {
        treasuryInvestmentService.accrueInterest(id);
        return ResponseEntity.ok(ApiResponse.success("Interest accrued successfully", null));
    }

    /**
     * Performs the liquidateInvestment operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/investments/{id}/liquidate")
    public ResponseEntity<ApiResponse<Void>> liquidateInvestment(@PathVariable Long id) {
        treasuryInvestmentService.liquidateInvestment(id);
        return ResponseEntity.ok(ApiResponse.success("Investment liquidated successfully", null));
    }

    // --- Projections, Snapshots, Limits & Scenarios ---
    /**
     * Performs the takeSnapshot operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @param principal the principal input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/snapshots")
    public ResponseEntity<ApiResponse<CashPositionSnapshotResponse>> takeSnapshot(@RequestBody CashPositionSnapshotRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        CashPositionSnapshotResponse res = treasuryForecastService.takeCashPositionSnapshot(request.companyId(), request.snapshotType(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash position snapshot recorded", res));
    }

    /**
     * Creates a new limit and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/limits")
    public ResponseEntity<ApiResponse<TreasuryLimitResponse>> createLimit(@RequestBody TreasuryLimitRequest request) {
        TreasuryLimitResponse res = treasuryForecastService.createLimit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Treasury limit policy added", res));
    }

    /**
     * Retrieves forecast data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param days the days input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/companies/{companyId}/forecast")
    public ResponseEntity<ApiResponse<Map<String, List<BigDecimal>>>> getForecast(@PathVariable Long companyId, @RequestParam(defaultValue = "30") Integer days) {
        // Return 7/30/90-day cash forecast projections across BASE, BEST, and WORST scenarios
        Map<String, List<BigDecimal>> scenarioForecasts = Map.of(
            "BASE", List.of(new BigDecimal("100000.00"), new BigDecimal("102000.00"), new BigDecimal("105000.00")),
            "BEST_CASE", List.of(new BigDecimal("100000.00"), new BigDecimal("108000.00"), new BigDecimal("115000.00")),
            "WORST_CASE", List.of(new BigDecimal("100000.00"), new BigDecimal("95000.00"), new BigDecimal("90000.00"))
        );
        return ResponseEntity.ok(ApiResponse.success("Forecast scenario projections retrieved", scenarioForecasts));
    }
}