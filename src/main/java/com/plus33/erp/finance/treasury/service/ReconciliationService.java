package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.StatementAndRecDtos.*;

import java.util.List;

public interface ReconciliationService {
    BankStatementResponse importStatement(BankStatementRequest request);
    BankStatementResponse getStatementById(Long id);
    List<BankStatementResponse> getStatementsByAccount(Long bankAccountId);

    ReconciliationRuleResponse createRule(ReconciliationRuleRequest request);
    List<ReconciliationRuleResponse> getRulesByCompany(Long companyId);

    void runAutoReconciliation(Long statementId, Long companyId);
    void manualMatch(Long statementLineId, Long paymentId, String username);
    void splitAndMatch(Long statementLineId, List<Long> paymentIds, String username);

    BankFeeRuleResponse createFeeRule(BankFeeRuleRequest request);
    List<BankFeeRuleResponse> getFeeRulesByAccount(Long bankAccountId);
    void processBankFees(Long bankAccountId, String username);
}
