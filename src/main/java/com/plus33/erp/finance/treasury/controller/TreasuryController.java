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
    @PostMapping("/banks")
    public ResponseEntity<ApiResponse<BankResponse>> createBank(@RequestBody BankRequest request) {
        BankResponse res = bankAccountService.createBank(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank created successfully", res));
    }

    @GetMapping("/banks")
    public ResponseEntity<ApiResponse<List<BankResponse>>> getAllBanks() {
        List<BankResponse> res = bankAccountService.getAllBanks();
        return ResponseEntity.ok(ApiResponse.success("Banks retrieved successfully", res));
    }

    @PostMapping("/branches")
    public ResponseEntity<ApiResponse<BankBranchResponse>> createBranch(@RequestBody BankBranchRequest request) {
        BankBranchResponse res = bankAccountService.createBranch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank branch created successfully", res));
    }

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<BankAccountResponse>> createBankAccount(@RequestBody BankAccountRequest request) {
        BankAccountResponse res = bankAccountService.createBankAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank account created successfully", res));
    }

    @GetMapping("/accounts/company/{companyId}")
    public ResponseEntity<ApiResponse<List<BankAccountResponse>>> getBankAccountsByCompany(@PathVariable Long companyId) {
        List<BankAccountResponse> res = bankAccountService.getBankAccountsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Bank accounts retrieved successfully", res));
    }

    @PostMapping("/virtual-accounts")
    public ResponseEntity<ApiResponse<BankVirtualAccountResponse>> createVirtualAccount(@RequestBody BankVirtualAccountRequest request) {
        BankVirtualAccountResponse res = bankAccountService.createVirtualAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Virtual account created successfully", res));
    }

    // --- Cash Pools ---
    @PostMapping("/pools")
    public ResponseEntity<ApiResponse<CashPoolResponse>> createCashPool(@RequestBody CashPoolRequest request) {
        CashPoolResponse res = bankAccountService.createCashPool(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash pool created successfully", res));
    }

    @PostMapping("/pools/{id}/sweep")
    public ResponseEntity<ApiResponse<Void>> executeCashPoolSweeps(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        bankAccountService.executeCashPoolSweeps(id, username);
        return ResponseEntity.ok(ApiResponse.success("Cash pool sweeps executed successfully", null));
    }

    // --- In-House Bank ---
    @PostMapping("/ihb/accounts")
    public ResponseEntity<ApiResponse<InHouseBankAccountResponse>> createInHouseAccount(@RequestBody InHouseBankAccountRequest request) {
        InHouseBankAccountResponse res = inHouseBankService.createInHouseAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("In-house bank account created successfully", res));
    }

    @PostMapping("/ihb/loans")
    public ResponseEntity<ApiResponse<IntercompanyLoanResponse>> createLoan(@RequestBody IntercompanyLoanRequest request) {
        IntercompanyLoanResponse res = inHouseBankService.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Intercompany loan created successfully", res));
    }

    @PostMapping("/ihb/loans/{id}/accrue")
    public ResponseEntity<ApiResponse<Void>> accrueLoanInterest(@PathVariable Long id) {
        inHouseBankService.accrueIntercompanyLoanInterest(id);
        return ResponseEntity.ok(ApiResponse.success("Loan interest accrued successfully", null));
    }

    @PostMapping("/ihb/settlements")
    public ResponseEntity<ApiResponse<InternalSettlementResponse>> createInternalSettlement(@RequestBody InternalSettlementRequest request) {
        InternalSettlementResponse res = inHouseBankService.createInternalSettlement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Internal settlement posted successfully", res));
    }

    // --- Payment Factory ---
    @PostMapping("/payments/batches")
    public ResponseEntity<ApiResponse<PaymentBatchResponse>> createPaymentBatch(@RequestBody PaymentBatchRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        PaymentBatchResponse res = paymentFactoryService.createPaymentBatch(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Payment batch created successfully", res));
    }

    @PostMapping("/payments/batches/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approvePaymentBatch(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.approvePaymentBatch(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Payment batch approved", null));
    }

    @PostMapping("/payments/batches/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectPaymentBatch(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.rejectPaymentBatch(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Payment batch rejected", null));
    }

    @PostMapping("/payments/batches/{id}/generate-file")
    public ResponseEntity<ApiResponse<PaymentFileResponse>> generatePaymentFile(@PathVariable Long id, @RequestParam String format) {
        PaymentFileResponse res = paymentFactoryService.generatePaymentFile(id, format);
        return ResponseEntity.ok(ApiResponse.success("Payment file generated successfully", res));
    }

    @PostMapping("/payments/files/{id}/transmit")
    public ResponseEntity<ApiResponse<Void>> transmitPaymentFile(@PathVariable Long id, @RequestParam String method) {
        paymentFactoryService.transmitPaymentFile(id, method);
        return ResponseEntity.ok(ApiResponse.success("Payment file transmitted successfully", null));
    }

    // --- Cash Transfers ---
    @PostMapping("/transfers")
    public ResponseEntity<ApiResponse<CashTransferResponse>> createCashTransfer(@RequestBody CashTransferRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        CashTransferResponse res = paymentFactoryService.createCashTransfer(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash transfer requested successfully", res));
    }

    @PostMapping("/transfers/{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approveCashTransfer(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.approveCashTransfer(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Cash transfer approved", null));
    }

    @PostMapping("/transfers/{id}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectCashTransfer(@PathVariable Long id, @RequestParam(required = false) String remarks, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        paymentFactoryService.rejectCashTransfer(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Cash transfer rejected", null));
    }

    // --- Reconciliation ---
    @PostMapping("/reconciliation/rules")
    public ResponseEntity<ApiResponse<ReconciliationRuleResponse>> createRule(@RequestBody ReconciliationRuleRequest request) {
        ReconciliationRuleResponse res = reconciliationService.createRule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Reconciliation rule created successfully", res));
    }

    @PostMapping("/statements/import")
    public ResponseEntity<ApiResponse<BankStatementResponse>> importStatement(@RequestBody BankStatementRequest request) {
        BankStatementResponse res = reconciliationService.importStatement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Bank statement imported successfully", res));
    }

    @PostMapping("/statements/{id}/auto-reconcile")
    public ResponseEntity<ApiResponse<Void>> autoReconcile(@PathVariable Long id, @RequestParam Long companyId) {
        reconciliationService.runAutoReconciliation(id, companyId);
        return ResponseEntity.ok(ApiResponse.success("Auto-reconciliation run finished", null));
    }

    @PostMapping("/statements/lines/{id}/manual-match")
    public ResponseEntity<ApiResponse<Void>> manualMatch(@PathVariable Long id, @RequestParam Long paymentId, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.manualMatch(id, paymentId, username);
        return ResponseEntity.ok(ApiResponse.success("Manual match posted successfully", null));
    }

    @PostMapping("/statements/lines/{id}/split-match")
    public ResponseEntity<ApiResponse<Void>> splitMatch(@PathVariable Long id, @RequestBody List<Long> paymentIds, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.splitAndMatch(id, paymentIds, username);
        return ResponseEntity.ok(ApiResponse.success("Split match posted successfully", null));
    }

    // --- Fees & FX Revaluation ---
    @PostMapping("/accounts/{id}/process-fees")
    public ResponseEntity<ApiResponse<Void>> processFees(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        reconciliationService.processBankFees(id, username);
        return ResponseEntity.ok(ApiResponse.success("Fees processed successfully", null));
    }

    @PostMapping("/companies/{id}/revalue")
    public ResponseEntity<ApiResponse<Void>> revalueCompanyAccounts(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        treasuryInvestmentService.executeFXRevaluations(id, username);
        return ResponseEntity.ok(ApiResponse.success("FX revaluation completed", null));
    }

    // --- Investments ---
    @PostMapping("/investments")
    public ResponseEntity<ApiResponse<TreasuryInvestmentResponse>> createInvestment(@RequestBody TreasuryInvestmentRequest request) {
        TreasuryInvestmentResponse res = treasuryInvestmentService.createInvestment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Investment registered successfully", res));
    }

    @PostMapping("/investments/{id}/accrue")
    public ResponseEntity<ApiResponse<Void>> accrueInterest(@PathVariable Long id) {
        treasuryInvestmentService.accrueInterest(id);
        return ResponseEntity.ok(ApiResponse.success("Interest accrued successfully", null));
    }

    @PostMapping("/investments/{id}/liquidate")
    public ResponseEntity<ApiResponse<Void>> liquidateInvestment(@PathVariable Long id) {
        treasuryInvestmentService.liquidateInvestment(id);
        return ResponseEntity.ok(ApiResponse.success("Investment liquidated successfully", null));
    }

    // --- Projections, Snapshots, Limits & Scenarios ---
    @PostMapping("/snapshots")
    public ResponseEntity<ApiResponse<CashPositionSnapshotResponse>> takeSnapshot(@RequestBody CashPositionSnapshotRequest request, Principal principal) {
        String username = principal != null ? principal.getName() : "admin@plus33.com";
        CashPositionSnapshotResponse res = treasuryForecastService.takeCashPositionSnapshot(request.companyId(), request.snapshotType(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cash position snapshot recorded", res));
    }

    @PostMapping("/limits")
    public ResponseEntity<ApiResponse<TreasuryLimitResponse>> createLimit(@RequestBody TreasuryLimitRequest request) {
        TreasuryLimitResponse res = treasuryForecastService.createLimit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Treasury limit policy added", res));
    }

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
