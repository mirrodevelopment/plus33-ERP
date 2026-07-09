/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetController
 * Related Service   : BudgetService, BudgetServiceImpl
 * Related Repository: BudgetRepository
 * Related Entity    : Budget
 * Related DTO       : BudgetComparisonResponse, BudgetConsumptionResponse, BudgetDimensionSetRequest, BudgetDrilldownResponse, BudgetLineResponse
 * Related Mapper    : BudgetMapper
 * Related DB Table  : budgets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BudgetService {
    BudgetResponse createBudget(Long companyId, BudgetRequest request, String username);
    BudgetResponse updateBudget(Long id, BudgetRequest request, String username);
    BudgetResponse getBudget(Long id);
    List<BudgetResponse> getBudgetsByCompany(Long companyId);

    BudgetResponse submitBudget(Long id, String username);
    BudgetResponse approveBudgetStep(Long id, String remarks, String username);
    BudgetResponse rejectBudget(Long id, String remarks, String username);
    BudgetResponse freezeBudget(Long id, Boolean isFrozen, String username);
    BudgetLineResponse lockBudgetLine(Long lineId, Boolean isLocked, String username);

    BudgetRevisionResponse createRevision(Long companyId, BudgetRevisionRequest request, String username);
    BudgetRevisionResponse transferFunds(Long companyId, Long sourceLineId, Long targetLineId, BigDecimal amount, String reason, String username);

    BudgetReservationResponse createReservation(Long companyId, BudgetReservationRequest request);
    BudgetConsumptionResponse consumeReservation(Long companyId, String sourceModule, Long sourceReferenceId, BigDecimal consumeAmount, String referenceNumber);
    BudgetConsumptionResponse createDirectConsumption(Long companyId, Long accountId, BudgetDimensionSetRequest dimensionSet, BigDecimal amount, String sourceModule, Long sourceReferenceId, String referenceNumber, LocalDate transactionDate);
    void releaseReservation(String sourceModule, Long sourceReferenceId);
    void releaseConsumption(String sourceModule, Long sourceReferenceId);

    List<BudgetLineResponse> massUpdateBudgetLines(Long companyId, BudgetMassUpdateRequest request, String username);
    BudgetComparisonResponse compareBudgets(Long companyId, Long budgetId1, Long budgetId2);
    List<BudgetDrilldownResponse> drilldownBudget(Long companyId, Long budgetId, String dimensionName, String parentValue);
    BudgetResponse copyBudget(Long companyId, Long sourceBudgetId, Long targetFiscalYearId, String targetCode, String targetName, BigDecimal percentageMultiplier, String username);
}
