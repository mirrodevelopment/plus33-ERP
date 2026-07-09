/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : ReconciliationService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReconciliationController
 * Related Service   : ReconciliationService, ReconciliationServiceImpl
 * Related Repository: ReconciliationRepository
 * Related Entity    : Reconciliation
 * Related DTO       : BankFeeRuleRequest, BankFeeRuleResponse, BankStatementRequest, BankStatementResponse, ReconciliationRuleRequest
 * Related Mapper    : ReconciliationMapper
 * Related DB Table  : reconciliations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.StatementAndRecDtos.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ReconciliationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
