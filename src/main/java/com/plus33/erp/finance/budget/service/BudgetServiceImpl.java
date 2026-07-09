/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : BudgetServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetController
 * Related Service   : BudgetServiceImpl
 * Related Repository: BudgetRepository, BudgetVersionRepository, BudgetLineRepository, BudgetControlCacheRepository, BudgetPolicyRepository, BudgetWorkflowTemplateRepository, BudgetDriverRepository, BudgetRevisionRepository, BudgetReservationRepository, BudgetConsumptionRepository, BudgetSnapshotRepository, BudgetSnapshotLineRepository, BudgetApprovalRepository, BudgetAuditLogRepository, CompanyRepository, FiscalYearRepository, AccountRepository, BudgetDimensionSetRepository
 * Related Entity    : Budget
 * Related DTO       : BudgetComparisonResponse, BudgetConsumptionResponse, BudgetDimensionSetRequest, BudgetDimensionSetResponse, BudgetDrilldownResponse
 * Related Mapper    : BudgetMapper
 * Related DB Table  : budgets
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : BudgetController, BudgetServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements BudgetService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.*;
import com.plus33.erp.finance.budget.entity.*;
import com.plus33.erp.finance.budget.exception.BudgetExceededException;
import com.plus33.erp.finance.budget.repository.*;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.reporting.entity.FiscalYear;
import com.plus33.erp.finance.reporting.repository.FiscalYearRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.finance.assets.entity.AssetCategory;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BudgetController
 *   --> BudgetServiceImpl (this)
 *   --> Validate business rules
 *   --> BudgetRepository (read/write 'budgets')
 *   --> BudgetMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code budgets}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetVersionRepository budgetVersionRepository;
    private final BudgetLineRepository budgetLineRepository;
    private final BudgetControlCacheRepository budgetControlCacheRepository;
    private final BudgetPolicyRepository budgetPolicyRepository;
    private final BudgetWorkflowTemplateRepository workflowTemplateRepository;
    private final BudgetDriverRepository budgetDriverRepository;
    private final BudgetRevisionRepository budgetRevisionRepository;
    private final BudgetReservationRepository budgetReservationRepository;
    private final BudgetConsumptionRepository budgetConsumptionRepository;
    private final BudgetSnapshotRepository budgetSnapshotRepository;
    private final BudgetSnapshotLineRepository budgetSnapshotLineRepository;
    private final BudgetApprovalRepository budgetApprovalRepository;
    private final BudgetAuditLogRepository budgetAuditLogRepository;
    
    private final CompanyRepository companyRepository;
    private final FiscalYearRepository fiscalYearRepository;
    private final AccountRepository accountRepository;
    
    private final BudgetDimensionSetService dimensionSetService;
    private final BudgetDimensionSetRepository dimensionSetRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Creates a new budget and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return the BudgetResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetResponse createBudget(Long companyId, BudgetRequest request, String username) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));
        FiscalYear fiscalYear = fiscalYearRepository.findById(request.fiscalYearId())
            .orElseThrow(() -> new BusinessException("Fiscal year not found"));
        BudgetPolicy policy = budgetPolicyRepository.findById(request.budgetPolicyId())
            .orElseThrow(() -> new BusinessException("Budget policy not found"));
            
        BudgetWorkflowTemplate wfTemplate = null;
        if (request.workflowTemplateId() != null) {
            wfTemplate = workflowTemplateRepository.findById(request.workflowTemplateId())
                .orElseThrow(() -> new BusinessException("Workflow template not found"));
        }

        if (budgetRepository.findByCompanyIdAndFiscalYearIdAndCodeAndIsForecast(
                companyId, fiscalYear.getId(), request.code(), request.isForecast() != null ? request.isForecast() : false).isPresent()) {
            throw new BusinessException("Budget code already exists: " + request.code());
        }

        Budget budget = Budget.builder()
            .company(company)
            .fiscalYear(fiscalYear)
            .budgetPolicy(policy)
            .workflowTemplate(wfTemplate)
            .code(request.code())
            .name(request.name())
            .budgetType(BudgetType.valueOf(request.budgetType().toUpperCase()))
            .periodType(BudgetPeriodType.valueOf(request.periodType().toUpperCase()))
            .scenario(request.scenario() != null ? BudgetScenario.valueOf(request.scenario().toUpperCase()) : BudgetScenario.EXPECTED)
            .status(BudgetStatus.DRAFT)
            .versionNumber(1)
            .isForecast(request.isForecast() != null ? request.isForecast() : false)
            .forecastType(request.forecastType())
            .forecastCycleCode(request.forecastCycleCode())
            .isFrozen(false)
            .isActive(false)
            .rateLockType(request.rateLockType() != null ? request.rateLockType().toUpperCase() : "SPOT")
            .budgetExchangeRate(request.budgetExchangeRate() != null ? request.budgetExchangeRate() : BigDecimal.ONE)
            .createdBy(username)
            .build();

        Budget savedBudget = budgetRepository.save(budget);

        BudgetVersion version = BudgetVersion.builder()
            .budget(savedBudget)
            .versionCode("V1")
            .description("Initial DRAFT version")
            .status("DRAFT")
            .createdBy(username)
            .build();
        BudgetVersion savedVersion = budgetVersionRepository.save(version);

        List<BudgetLine> linesToSave = new ArrayList<>();
        if (request.lines() != null) {
            for (BudgetLineRequest lineReq : request.lines()) {
                Account account = accountRepository.findById(lineReq.accountId())
                    .orElseThrow(() -> new BusinessException("Account not found: " + lineReq.accountId()));
                BudgetDimensionSet dimSet = dimensionSetService.getOrCreateDimensionSet(companyId, lineReq.dimensionSet());
                linesToSave.addAll(distributeLine(savedBudget, savedVersion, account, dimSet, lineReq));
            }
        }

        List<BudgetLine> savedLines = budgetLineRepository.saveAll(linesToSave);
        budgetLineRepository.flush();
        savedLines.forEach(this::updateCache);

        saveAuditLog(savedBudget, "CREATE", "Budget plan created in DRAFT status by " + username, username);

        return mapToResponse(savedBudget, savedLines);
    }

    /**
     * Updates an existing budget record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return the BudgetResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetResponse updateBudget(Long id, BudgetRequest request, String username) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found: " + id));

        if (budget.getIsFrozen()) {
            throw new BusinessException("Budget plan is frozen and cannot be modified.");
        }
        if (budget.getStatus() == BudgetStatus.APPROVED || budget.getStatus() == BudgetStatus.LOCKED) {
            throw new BusinessException("Approved or Locked budgets cannot be updated directly.");
        }

        budget.setName(request.name());
        budget.setScenario(request.scenario() != null ? BudgetScenario.valueOf(request.scenario().toUpperCase()) : BudgetScenario.EXPECTED);
        budget.setRateLockType(request.rateLockType() != null ? request.rateLockType().toUpperCase() : "SPOT");
        budget.setBudgetExchangeRate(request.budgetExchangeRate() != null ? request.budgetExchangeRate() : BigDecimal.ONE);
        
        Budget savedBudget = budgetRepository.save(budget);

        // Fetch active version
        BudgetVersion version = budgetVersionRepository.findAllByBudgetId(id).stream()
            .filter(v -> "DRAFT".equals(v.getStatus()))
            .findFirst()
            .orElseThrow(() -> new BusinessException("No active DRAFT version found"));

        // Delete existing lines and cache
        List<BudgetLine> existingLines = budgetLineRepository.findAllByBudgetVersionId(version.getId());
        existingLines.forEach(l -> budgetControlCacheRepository.deleteById(l.getId()));
        budgetLineRepository.deleteAll(existingLines);

        List<BudgetLine> linesToSave = new ArrayList<>();
        if (request.lines() != null) {
            for (BudgetLineRequest lineReq : request.lines()) {
                Account account = accountRepository.findById(lineReq.accountId())
                    .orElseThrow(() -> new BusinessException("Account not found: " + lineReq.accountId()));
                BudgetDimensionSet dimSet = dimensionSetService.getOrCreateDimensionSet(budget.getCompany().getId(), lineReq.dimensionSet());
                linesToSave.addAll(distributeLine(savedBudget, version, account, dimSet, lineReq));
            }
        }

        List<BudgetLine> savedLines = budgetLineRepository.saveAll(linesToSave);
        budgetLineRepository.flush();
        savedLines.forEach(this::updateCache);

        saveAuditLog(savedBudget, "UPDATE", "Budget plan updated by " + username, username);

        return mapToResponse(savedBudget, savedLines);
    }

    /**
     * Retrieves budget data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the BudgetResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetResponse getBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found: " + id));
        List<BudgetLine> lines = budgetLineRepository.findAllByBudgetId(id);
        return mapToResponse(budget, lines);
    }

    /**
     * Retrieves budgets by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgetsByCompany(Long companyId) {
        return budgetRepository.findAll().stream()
            .filter(b -> b.getCompany().getId().equals(companyId))
            .map(b -> mapToResponse(b, budgetLineRepository.findAllByBudgetId(b.getId())))
            .toList();
    }

    /**
     * Submits the budget for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param username the username input value
     * @return the BudgetResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetResponse submitBudget(Long id, String username) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found: " + id));

        if (budget.getStatus() != BudgetStatus.DRAFT) {
            throw new BusinessException("Only DRAFT budgets can be submitted.");
        }

        budget.setStatus(BudgetStatus.SUBMITTED);
        Budget savedBudget = budgetRepository.save(budget);

        budgetApprovalRepository.findAllByBudgetIdOrderByApprovalStepAsc(id)
            .forEach(budgetApprovalRepository::delete);

        if (budget.getWorkflowTemplate() != null) {
            List<BudgetWorkflowStep> steps = workflowTemplateRepository.findById(budget.getWorkflowTemplate().getId())
                .map(BudgetWorkflowTemplate::getSteps)
                .orElse(Collections.emptyList());

            for (int i = 0; i < steps.size(); i++) {
                BudgetWorkflowStep step = steps.get(i);
                BudgetApproval approval = BudgetApproval.builder()
                    .budget(savedBudget)
                    .approvalStep(step.getStepSequence())
                    .roleCode(step.getRoleCode())
                    .status(i == 0 ? "PENDING" : "WAIT")
                    .build();
                budgetApprovalRepository.save(approval);
            }
        } else {
            // Auto approve if no workflow template set
            savedBudget.setStatus(BudgetStatus.APPROVED);
            savedBudget.setApprovedBy(username);
            savedBudget.setApprovedAt(LocalDateTime.now());
            savedBudget.setIsActive(true);
            budgetRepository.save(savedBudget);
            
            // Deactivate previous active budget of the same type/scenario/year
            deactivatePreviousBudgets(savedBudget);
        }

        createSnapshot(savedBudget, savedBudget.getVersionNumber(), "SUBMIT", "Submission Snapshot", username);
        saveAuditLog(savedBudget, "SUBMIT", "Budget plan submitted for approval by " + username, username);

        return mapToResponse(savedBudget, budgetLineRepository.findAllByBudgetId(id));
    }

    /**
     * Approves the budget step, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param username the username input value
     * @return the BudgetResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetResponse approveBudgetStep(Long id, String remarks, String username) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found"));

        List<BudgetApproval> approvals = budgetApprovalRepository.findAllByBudgetIdOrderByApprovalStepAsc(id);
        BudgetApproval pendingStep = approvals.stream()
            .filter(a -> "PENDING".equals(a.getStatus()))
            .findFirst()
            .orElseThrow(() -> new BusinessException("No pending approval steps found"));

        pendingStep.setStatus("APPROVED");
        pendingStep.setApproverUsername(username);
        pendingStep.setApprovedAt(LocalDateTime.now());
        pendingStep.setRemarks(remarks);
        budgetApprovalRepository.save(pendingStep);

        // Move next step to PENDING
        Optional<BudgetApproval> nextStep = approvals.stream()
            .filter(a -> "WAIT".equals(a.getStatus()) && a.getApprovalStep() > pendingStep.getApprovalStep())
            .findFirst();

        if (nextStep.isPresent()) {
            nextStep.get().setStatus("PENDING");
            budgetApprovalRepository.save(nextStep.get());
        } else {
            // No more steps, fully approved!
            budget.setStatus(BudgetStatus.APPROVED);
            budget.setApprovedBy(username);
            budget.setApprovedAt(LocalDateTime.now());
            budget.setIsActive(true);
            budgetRepository.save(budget);

            deactivatePreviousBudgets(budget);
            createSnapshot(budget, budget.getVersionNumber(), "APPROVED", "Approved Final Snapshot", username);
        }

        saveAuditLog(budget, "APPROVE_STEP", "Approval step " + pendingStep.getApprovalStep() + " approved by " + username, username);

        return mapToResponse(budget, budgetLineRepository.findAllByBudgetId(id));
    }

    /**
     * Performs the rejectBudget operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param remarks the remarks input value
     * @param username the username input value
     * @return the BudgetResponse result
     */
    @Override
    @Transactional
    public BudgetResponse rejectBudget(Long id, String remarks, String username) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found"));

        budget.setStatus(BudgetStatus.DRAFT);
        Budget savedBudget = budgetRepository.save(budget);

        budgetApprovalRepository.findAllByBudgetIdOrderByApprovalStepAsc(id)
            .forEach(budgetApprovalRepository::delete);

        saveAuditLog(savedBudget, "REJECT", "Budget plan rejected by " + username + ". Reason: " + remarks, username);

        return mapToResponse(savedBudget, budgetLineRepository.findAllByBudgetId(id));
    }

    /**
     * Performs the freezeBudget operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param isFrozen the isFrozen input value
     * @param username the username input value
     * @return the BudgetResponse result
     */
    @Override
    @Transactional
    public BudgetResponse freezeBudget(Long id, Boolean isFrozen, String username) {
        Budget budget = budgetRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Budget not found"));

        budget.setIsFrozen(isFrozen);
        Budget savedBudget = budgetRepository.save(budget);

        saveAuditLog(savedBudget, isFrozen ? "FREEZE" : "UNFREEZE", "Budget plan " + (isFrozen ? "frozen" : "unfrozen") + " by " + username, username);

        return mapToResponse(savedBudget, budgetLineRepository.findAllByBudgetId(id));
    }

    /**
     * Performs the lockBudgetLine operation in this module.
     *
     * @param lineId the lineId input value
     * @param isLocked the isLocked input value
     * @param username the username input value
     * @return the BudgetLineResponse result
     */
    @Override
    @Transactional
    public BudgetLineResponse lockBudgetLine(Long lineId, Boolean isLocked, String username) {
        BudgetLine line = budgetLineRepository.findById(lineId)
            .orElseThrow(() -> new BusinessException("Budget line not found"));

        line.setIsLocked(isLocked);
        BudgetLine savedLine = budgetLineRepository.save(line);

        saveAuditLog(line.getBudget(), "LOCK_LINE", "Budget line ID " + lineId + (isLocked ? " locked" : " unlocked") + " by " + username, username);

        return mapLineToResponse(savedLine);
    }

    /**
     * Creates a new revision and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return the BudgetRevisionResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetRevisionResponse createRevision(Long companyId, BudgetRevisionRequest request, String username) {
        BudgetLine line = budgetLineRepository.findById(request.budgetLineId())
            .orElseThrow(() -> new BusinessException("Budget line not found"));
        Budget budget = line.getBudget();

        if (budget.getIsFrozen()) {
            throw new BusinessException("Budget plan is frozen.");
        }
        if (!budget.getBudgetPolicy().getAllowRevisions()) {
            throw new BusinessException("Budget revisions are not allowed by policy.");
        }

        BigDecimal previous = line.getAllocatedAmount();
        BigDecimal targetValue = request.newAmount() != null ? request.newAmount() : BigDecimal.ZERO;
        BigDecimal change = targetValue.subtract(previous);

        line.setAllocatedAmount(targetValue);
        budgetLineRepository.save(line);
        updateCache(line);

        BudgetRevision revision = BudgetRevision.builder()
            .budget(budget)
            .budgetLine(line)
            .revisionDate(request.revisionDate() != null ? request.revisionDate() : LocalDate.now())
            .previousAmount(previous)
            .newAmount(targetValue)
            .changeAmount(change)
            .reason(request.reason())
            .performedBy(username)
            .status("APPROVED")
            .build();
        BudgetRevision savedRevision = budgetRevisionRepository.save(revision);

        saveAuditLog(budget, "REVISE", "Budget line ID " + line.getId() + " revised from " + previous + " to " + targetValue + " by " + username, username);

        return new BudgetRevisionResponse(
            savedRevision.getId(),
            budget.getId(),
            line.getId(),
            savedRevision.getRevisionDate(),
            previous,
            targetValue,
            change,
            request.reason(),
            username,
            "APPROVED"
        );
    }

    /**
     * Performs the transferFunds operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param sourceLineId the sourceLineId input value
     * @param targetLineId the targetLineId input value
     * @param amount the amount input value
     * @param reason the reason input value
     * @param username the username input value
     * @return the BudgetRevisionResponse result
     */
    @Override
    @Transactional
    public BudgetRevisionResponse transferFunds(Long companyId, Long sourceLineId, Long targetLineId, BigDecimal amount, String reason, String username) {
        BudgetLine source = budgetLineRepository.findById(sourceLineId)
            .orElseThrow(() -> new BusinessException("Source budget line not found"));
        BudgetLine target = budgetLineRepository.findById(targetLineId)
            .orElseThrow(() -> new BusinessException("Target budget line not found"));

        if (!source.getBudget().getId().equals(target.getBudget().getId())) {
            throw new BusinessException("Transfer must be within the same budget plan.");
        }

        Budget budget = source.getBudget();
        if (budget.getIsFrozen()) {
            throw new BusinessException("Budget plan is frozen.");
        }
        if (!budget.getBudgetPolicy().getAllowTransfers()) {
            throw new BusinessException("Budget transfers are not allowed by policy.");
        }

        BigDecimal srcAvailable = source.getAllocatedAmount().subtract(source.getReservedAmount()).subtract(source.getConsumedAmount());
        if (srcAvailable.compareTo(amount) < 0 && !budget.getBudgetPolicy().getAllowNegative()) {
            throw new BudgetExceededException("Insufficient funds on source budget line.");
        }

        BigDecimal prevSrc = source.getAllocatedAmount();
        BigDecimal prevTgt = target.getAllocatedAmount();

        source.setAllocatedAmount(prevSrc.subtract(amount));
        target.setAllocatedAmount(prevTgt.add(amount));

        budgetLineRepository.save(source);
        budgetLineRepository.save(target);

        updateCache(source);
        updateCache(target);

        BudgetRevision srcRevision = BudgetRevision.builder()
            .budget(budget)
            .budgetLine(source)
            .revisionDate(LocalDate.now())
            .previousAmount(prevSrc)
            .newAmount(source.getAllocatedAmount())
            .changeAmount(amount.negate())
            .reason("Transfer to line " + targetLineId + ": " + reason)
            .performedBy(username)
            .status("APPROVED")
            .build();
        budgetRevisionRepository.save(srcRevision);

        BudgetRevision tgtRevision = BudgetRevision.builder()
            .budget(budget)
            .budgetLine(target)
            .revisionDate(LocalDate.now())
            .previousAmount(prevTgt)
            .newAmount(target.getAllocatedAmount())
            .changeAmount(amount)
            .reason("Transfer from line " + sourceLineId + ": " + reason)
            .performedBy(username)
            .status("APPROVED")
            .build();
        budgetRevisionRepository.save(tgtRevision);

        saveAuditLog(budget, "TRANSFER", "Transferred " + amount + " from line ID " + sourceLineId + " to line ID " + targetLineId + " by " + username, username);

        return new BudgetRevisionResponse(
            tgtRevision.getId(),
            budget.getId(),
            target.getId(),
            LocalDate.now(),
            prevTgt,
            target.getAllocatedAmount(),
            amount,
            reason,
            username,
            "APPROVED"
        );
    }

    /**
     * Creates a new reservation and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the BudgetReservationResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetReservationResponse createReservation(Long companyId, BudgetReservationRequest request) {
        BudgetDimensionSet dimSet = dimensionSetService.getOrCreateDimensionSet(companyId, request.dimensionSet());
        BudgetLine line = resolveBudgetLine(companyId, request.accountId(), dimSet, request.transactionDate());

        if (line == null) {
            BudgetPolicy policy = getActivePolicyForCompany(companyId);
            if (policy != null && "HARD".equals(policy.getControlType())) {
                throw new BudgetExceededException("No budget line defined for account " + request.accountId());
            }
            log.warn("Bypassing budget check: No active budget line found for Account: {} and Dimensions: {}", request.accountId(), request.dimensionSet());
            return new BudgetReservationResponse(0L, 0L, request.sourceModule(), request.sourceReferenceId(), request.referenceNumber(), request.amount(), "ACTIVE", request.expiryDate(), LocalDateTime.now());
        }

        if (line.getIsLocked()) {
            throw new BusinessException("Budget line is locked.");
        }

        BudgetControlCache cache = budgetControlCacheRepository.findById(line.getId())
            .orElseThrow(() -> new BusinessException("Budget cache not initialized for line: " + line.getId()));

        BigDecimal available = cache.getAvailableAmount();
        BigDecimal reqAmount = request.amount();

        BudgetPolicy policy = line.getBudget().getBudgetPolicy();

        if (available.compareTo(reqAmount) < 0 && !policy.getAllowNegative()) {
            if ("HARD".equals(policy.getControlType())) {
                throw new BudgetExceededException("Budget exceeded. Available: " + available + ", Requested: " + reqAmount + " for Account " + line.getAccount().getAccountCode());
            } else if ("SOFT".equals(policy.getControlType())) {
                log.warn("SOFT BUDGET BYPASS: Budget exceeded. Available: {}, Requested: {} for Account {}", available, reqAmount, line.getAccount().getAccountCode());
            }
        }

        line.setReservedAmount(line.getReservedAmount().add(reqAmount));
        budgetLineRepository.save(line);
        updateCache(line);

        BudgetReservation reservation = BudgetReservation.builder()
            .budgetLine(line)
            .sourceModule(request.sourceModule())
            .sourceReferenceId(request.sourceReferenceId())
            .referenceNumber(request.referenceNumber())
            .reservedAmount(reqAmount)
            .status(ReservationStatus.ACTIVE)
            .expiryDate(request.expiryDate())
            .build();

        BudgetReservation savedRes = budgetReservationRepository.save(reservation);
        return new BudgetReservationResponse(
            savedRes.getId(),
            line.getId(),
            savedRes.getSourceModule(),
            savedRes.getSourceReferenceId(),
            savedRes.getReferenceNumber(),
            savedRes.getReservedAmount(),
            savedRes.getStatus().name(),
            savedRes.getExpiryDate(),
            savedRes.getCreatedAt()
        );
    }

    /**
     * Performs the consumeReservation operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param sourceModule the sourceModule input value
     * @param sourceReferenceId the sourceReferenceId input value
     * @param consumeAmount the consumeAmount input value
     * @param referenceNumber the referenceNumber input value
     * @return the BudgetConsumptionResponse result
     */
    @Override
    @Transactional
    public BudgetConsumptionResponse consumeReservation(Long companyId, String sourceModule, Long sourceReferenceId, BigDecimal consumeAmount, String referenceNumber) {
        BudgetReservation reservation = budgetReservationRepository.findBySourceModuleAndSourceReferenceId(sourceModule, sourceReferenceId)
            .orElseThrow(() -> new BusinessException("Reservation not found for source " + sourceModule + " ID " + sourceReferenceId));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new BusinessException("Reservation is not in ACTIVE status.");
        }

        BudgetLine line = reservation.getBudgetLine();
        BigDecimal resAmt = reservation.getReservedAmount();

        // Release the reservation portion, consume the actual amount
        line.setReservedAmount(line.getReservedAmount().subtract(resAmt));
        line.setConsumedAmount(line.getConsumedAmount().add(consumeAmount));
        budgetLineRepository.save(line);
        updateCache(line);

        reservation.setStatus(ReservationStatus.CONSUMED);
        budgetReservationRepository.save(reservation);

        BudgetConsumption consumption = BudgetConsumption.builder()
            .budgetLine(line)
            .sourceModule(sourceModule)
            .sourceReferenceId(sourceReferenceId)
            .referenceNumber(referenceNumber)
            .consumedAmount(consumeAmount)
            .build();
        BudgetConsumption savedCons = budgetConsumptionRepository.save(consumption);

        return new BudgetConsumptionResponse(
            savedCons.getId(),
            line.getId(),
            savedCons.getSourceModule(),
            savedCons.getSourceReferenceId(),
            savedCons.getReferenceNumber(),
            savedCons.getConsumedAmount(),
            savedCons.getCreatedAt()
        );
    }

    /**
     * Creates a new direct consumption and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param accountId the accountId input value
     * @param dimensionSet the dimensionSet input value
     * @param amount the amount input value
     * @param sourceModule the sourceModule input value
     * @param sourceReferenceId the sourceReferenceId input value
     * @param referenceNumber the referenceNumber input value
     * @param transactionDate the transactionDate input value
     * @return the BudgetConsumptionResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BudgetConsumptionResponse createDirectConsumption(Long companyId, Long accountId, BudgetDimensionSetRequest dimensionSet, BigDecimal amount, String sourceModule, Long sourceReferenceId, String referenceNumber, LocalDate transactionDate) {
        BudgetDimensionSet dimSet = dimensionSetService.getOrCreateDimensionSet(companyId, dimensionSet);
        BudgetLine line = resolveBudgetLine(companyId, accountId, dimSet, transactionDate);

        if (line == null) {
            BudgetPolicy policy = getActivePolicyForCompany(companyId);
            if (policy != null && "HARD".equals(policy.getControlType())) {
                throw new BudgetExceededException("No budget line defined for account " + accountId);
            }
            return new BudgetConsumptionResponse(0L, 0L, sourceModule, sourceReferenceId, referenceNumber, amount, LocalDateTime.now());
        }

        if (line.getIsLocked()) {
            throw new BusinessException("Budget line is locked.");
        }

        BigDecimal available = line.getAllocatedAmount().subtract(line.getReservedAmount()).subtract(line.getConsumedAmount());
        BudgetPolicy policy = line.getBudget().getBudgetPolicy();

        if (available.compareTo(amount) < 0 && !policy.getAllowNegative()) {
            if ("HARD".equals(policy.getControlType())) {
                throw new BudgetExceededException("Budget exceeded for Direct GL posting. Available: " + available + ", Requested: " + amount);
            } else if ("SOFT".equals(policy.getControlType())) {
                log.warn("SOFT BUDGET BYPASS: Direct posting budget exceeded. Available: {}, Requested: {}", available, amount);
            }
        }

        line.setConsumedAmount(line.getConsumedAmount().add(amount));
        budgetLineRepository.save(line);
        updateCache(line);

        BudgetConsumption consumption = BudgetConsumption.builder()
            .budgetLine(line)
            .sourceModule(sourceModule)
            .sourceReferenceId(sourceReferenceId)
            .referenceNumber(referenceNumber)
            .consumedAmount(amount)
            .build();
        BudgetConsumption savedCons = budgetConsumptionRepository.save(consumption);

        return new BudgetConsumptionResponse(
            savedCons.getId(),
            line.getId(),
            savedCons.getSourceModule(),
            savedCons.getSourceReferenceId(),
            savedCons.getReferenceNumber(),
            savedCons.getConsumedAmount(),
            savedCons.getCreatedAt()
        );
    }

    /**
     * Releases previously reserved reservation resources back to the available pool.
     *
     * @param sourceModule the sourceModule input value
     * @param sourceReferenceId the sourceReferenceId input value
     */
    @Override
    @Transactional
    public void releaseReservation(String sourceModule, Long sourceReferenceId) {
        Optional<BudgetReservation> reservationOpt = budgetReservationRepository.findBySourceModuleAndSourceReferenceId(sourceModule, sourceReferenceId);
        if (reservationOpt.isPresent()) {
            BudgetReservation reservation = reservationOpt.get();
            if (reservation.getStatus() == ReservationStatus.ACTIVE) {
                BudgetLine line = reservation.getBudgetLine();
                line.setReservedAmount(line.getReservedAmount().subtract(reservation.getReservedAmount()));
                budgetLineRepository.save(line);
                updateCache(line);

                reservation.setStatus(ReservationStatus.RELEASED);
                budgetReservationRepository.save(reservation);
            }
        }
    }

    /**
     * Releases previously reserved consumption resources back to the available pool.
     *
     * @param sourceModule the sourceModule input value
     * @param sourceReferenceId the sourceReferenceId input value
     */
    @Override
    @Transactional
    public void releaseConsumption(String sourceModule, Long sourceReferenceId) {
        Optional<BudgetConsumption> consumptionOpt = budgetConsumptionRepository.findBySourceModuleAndSourceReferenceId(sourceModule, sourceReferenceId);
        if (consumptionOpt.isPresent()) {
            BudgetConsumption consumption = consumptionOpt.get();
            BudgetLine line = consumption.getBudgetLine();
            line.setConsumedAmount(line.getConsumedAmount().subtract(consumption.getConsumedAmount()));
            budgetLineRepository.save(line);
            updateCache(line);

            budgetConsumptionRepository.delete(consumption);
        }
    }

    /**
     * Performs the massUpdateBudgetLines operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @param username the username input value
     * @return List of matching records
     */
    @Override
    @Transactional
    public List<BudgetLineResponse> massUpdateBudgetLines(Long companyId, BudgetMassUpdateRequest request, String username) {
        List<BudgetLine> lines = budgetLineRepository.findAllById(request.budgetLineIds());
        List<BudgetLine> updated = new ArrayList<>();

        for (BudgetLine line : lines) {
            BigDecimal previous = line.getAllocatedAmount();
            BigDecimal nextAmount = previous;

            if ("PERCENTAGE".equalsIgnoreCase(request.adjustmentType())) {
                BigDecimal factor = BigDecimal.ONE.add(request.adjustmentValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
                nextAmount = previous.multiply(factor).setScale(2, RoundingMode.HALF_UP);
            } else if ("FIXED_AMOUNT".equalsIgnoreCase(request.adjustmentType())) {
                nextAmount = previous.add(request.adjustmentValue());
            }

            line.setAllocatedAmount(nextAmount);
            updated.add(budgetLineRepository.save(line));
            updateCache(line);

            BudgetRevision revision = BudgetRevision.builder()
                .budget(line.getBudget())
                .budgetLine(line)
                .revisionDate(LocalDate.now())
                .previousAmount(previous)
                .newAmount(nextAmount)
                .changeAmount(nextAmount.subtract(previous))
                .reason("Mass Update: " + request.reason())
                .performedBy(username)
                .status("APPROVED")
                .build();
            budgetRevisionRepository.save(revision);
        }

        return updated.stream().map(this::mapLineToResponse).toList();
    }

    /**
     * Performs the compareBudgets operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param budgetId1 the budgetId1 input value
     * @param budgetId2 the budgetId2 input value
     * @return the BudgetComparisonResponse result
     */
    @Override
    @Transactional(readOnly = true)
    public BudgetComparisonResponse compareBudgets(Long companyId, Long budgetId1, Long budgetId2) {
        Budget b1 = budgetRepository.findById(budgetId1).orElseThrow(() -> new BusinessException("Budget 1 not found"));
        Budget b2 = budgetRepository.findById(budgetId2).orElseThrow(() -> new BusinessException("Budget 2 not found"));

        List<BudgetLine> lines1 = budgetLineRepository.findAllByBudgetId(budgetId1);
        List<BudgetLine> lines2 = budgetLineRepository.findAllByBudgetId(budgetId2);

        Map<String, BudgetLine> map1 = lines1.stream()
            .collect(Collectors.toMap(l -> l.getAccount().getId() + "_" + l.getDimensionSet().getId() + "_" + l.getPeriodStartDate(), l -> l));
        Map<String, BudgetLine> map2 = lines2.stream()
            .collect(Collectors.toMap(l -> l.getAccount().getId() + "_" + l.getDimensionSet().getId() + "_" + l.getPeriodStartDate(), l -> l));

        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(map1.keySet());
        allKeys.addAll(map2.keySet());

        List<BudgetComparisonItem> items = new ArrayList<>();
        for (String key : allKeys) {
            BudgetLine l1 = map1.get(key);
            BudgetLine l2 = map2.get(key);

            Long accountId = l1 != null ? l1.getAccount().getId() : l2.getAccount().getId();
            String code = l1 != null ? l1.getAccount().getAccountCode() : l2.getAccount().getAccountCode();
            String name = l1 != null ? l1.getAccount().getAccountName() : l2.getAccount().getAccountName();
            BudgetDimensionSet dim = l1 != null ? l1.getDimensionSet() : l2.getDimensionSet();

            BigDecimal alloc1 = l1 != null ? l1.getAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal res1 = l1 != null ? l1.getReservedAmount() : BigDecimal.ZERO;
            BigDecimal cons1 = l1 != null ? l1.getConsumedAmount() : BigDecimal.ZERO;
            BigDecimal avail1 = alloc1.subtract(res1).subtract(cons1);

            BigDecimal alloc2 = l2 != null ? l2.getAllocatedAmount() : BigDecimal.ZERO;
            BigDecimal res2 = l2 != null ? l2.getReservedAmount() : BigDecimal.ZERO;
            BigDecimal cons2 = l2 != null ? l2.getConsumedAmount() : BigDecimal.ZERO;
            BigDecimal avail2 = alloc2.subtract(res2).subtract(cons2);

            BigDecimal variance = alloc2.subtract(alloc1);
            BigDecimal varPercent = BigDecimal.ZERO;
            if (alloc1.compareTo(BigDecimal.ZERO) > 0) {
                varPercent = variance.multiply(BigDecimal.valueOf(100)).divide(alloc1, 2, RoundingMode.HALF_UP);
            }

            items.add(new BudgetComparisonItem(
                accountId, code, name, mapDimSetToResponse(dim),
                alloc1, res1, cons1, avail1,
                alloc2, res2, cons2, avail2,
                variance, varPercent
            ));
        }

        return new BudgetComparisonResponse(budgetId1, b1.getCode(), budgetId2, b2.getCode(), items);
    }

    /**
     * Performs the drilldownBudget operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param budgetId the budgetId input value
     * @param dimensionName the dimensionName input value
     * @param parentValue the parentValue input value
     * @return List of matching records
     */
    @Override
    @Transactional(readOnly = true)
    public List<BudgetDrilldownResponse> drilldownBudget(Long companyId, Long budgetId, String dimensionName, String parentValue) {
        List<BudgetLine> lines = budgetLineRepository.findAllByBudgetId(budgetId);
        List<BudgetDrilldownResponse> results = new ArrayList<>();

        if ("DEPARTMENT".equalsIgnoreCase(dimensionName)) {
            Map<String, List<BudgetLine>> grouped = lines.stream()
                .filter(l -> l.getDimensionSet().getDepartment() != null)
                .collect(Collectors.groupingBy(l -> l.getDimensionSet().getDepartment().getCode() + " - " + l.getDimensionSet().getDepartment().getName()));

            grouped.forEach((dept, valLines) -> {
                results.add(buildDrilldownNode("Department", dept, valLines));
            });
        } else if ("COST_CENTER".equalsIgnoreCase(dimensionName)) {
            Map<String, List<BudgetLine>> grouped = lines.stream()
                .filter(l -> l.getDimensionSet().getCostCenter() != null)
                .collect(Collectors.groupingBy(l -> l.getDimensionSet().getCostCenter().getCode() + " - " + l.getDimensionSet().getCostCenter().getName()));

            grouped.forEach((cc, valLines) -> {
                results.add(buildDrilldownNode("Cost Center", cc, valLines));
            });
        } else if ("PROJECT".equalsIgnoreCase(dimensionName)) {
            Map<String, List<BudgetLine>> grouped = lines.stream()
                .filter(l -> l.getDimensionSet().getProject() != null)
                .collect(Collectors.groupingBy(l -> l.getDimensionSet().getProject().getCode() + " - " + l.getDimensionSet().getProject().getName()));

            grouped.forEach((proj, valLines) -> {
                results.add(buildDrilldownNode("Project", proj, valLines));
            });
        } else {
            // Roll up by Account
            Map<String, List<BudgetLine>> grouped = lines.stream()
                .collect(Collectors.groupingBy(l -> l.getAccount().getAccountCode() + " - " + l.getAccount().getAccountName()));

            grouped.forEach((acc, valLines) -> {
                results.add(buildDrilldownNode("Account", acc, valLines));
            });
        }

        return results;
    }

    /**
     * Performs the copyBudget operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param sourceBudgetId the sourceBudgetId input value
     * @param targetFiscalYearId the targetFiscalYearId input value
     * @param targetCode the targetCode input value
     * @param targetName the targetName input value
     * @param percentageMultiplier the percentageMultiplier input value
     * @param username the username input value
     * @return the BudgetResponse result
     */
    @Override
    @Transactional
    public BudgetResponse copyBudget(Long companyId, Long sourceBudgetId, Long targetFiscalYearId, String targetCode, String targetName, BigDecimal percentageMultiplier, String username) {
        Budget source = budgetRepository.findById(sourceBudgetId)
            .orElseThrow(() -> new BusinessException("Source budget not found"));
        FiscalYear targetFy = fiscalYearRepository.findById(targetFiscalYearId)
            .orElseThrow(() -> new BusinessException("Target fiscal year not found"));

        if (budgetRepository.findByCompanyIdAndFiscalYearIdAndCodeAndIsForecast(companyId, targetFiscalYearId, targetCode, false).isPresent()) {
            throw new BusinessException("Target budget code already exists: " + targetCode);
        }

        Budget target = Budget.builder()
            .company(source.getCompany())
            .fiscalYear(targetFy)
            .budgetPolicy(source.getBudgetPolicy())
            .workflowTemplate(source.getWorkflowTemplate())
            .code(targetCode)
            .name(targetName)
            .budgetType(source.getBudgetType())
            .periodType(source.getPeriodType())
            .scenario(source.getScenario())
            .status(BudgetStatus.DRAFT)
            .versionNumber(1)
            .isForecast(source.getIsForecast())
            .forecastType(source.getForecastType())
            .forecastCycleCode(source.getForecastCycleCode())
            .isFrozen(false)
            .isActive(false)
            .rateLockType(source.getRateLockType())
            .budgetExchangeRate(source.getBudgetExchangeRate())
            .createdBy(username)
            .build();

        Budget savedTarget = budgetRepository.save(target);

        BudgetVersion version = BudgetVersion.builder()
            .budget(savedTarget)
            .versionCode("V1")
            .description("Copied from " + source.getCode())
            .status("DRAFT")
            .createdBy(username)
            .build();
        BudgetVersion savedVersion = budgetVersionRepository.save(version);

        List<BudgetLine> sourceLines = budgetLineRepository.findAllByBudgetId(sourceBudgetId);
        List<BudgetLine> linesToSave = new ArrayList<>();

        BigDecimal factor = percentageMultiplier != null ? percentageMultiplier : BigDecimal.ONE;

        for (BudgetLine srcLine : sourceLines) {
            BigDecimal nextAmount = srcLine.getAllocatedAmount().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            linesToSave.add(BudgetLine.builder()
                .budget(savedTarget)
                .budgetVersion(savedVersion)
                .account(srcLine.getAccount())
                .dimensionSet(srcLine.getDimensionSet())
                .periodStartDate(srcLine.getPeriodStartDate().plusYears(1)) // advance by 1 year conceptually
                .periodEndDate(srcLine.getPeriodEndDate().plusYears(1))
                .allocatedAmount(nextAmount)
                .distributionMethod(srcLine.getDistributionMethod())
                .formulaExpression(srcLine.getFormulaExpression())
                .notes("Copied from line ID " + srcLine.getId())
                .build());
        }

        List<BudgetLine> savedLines = budgetLineRepository.saveAll(linesToSave);
        budgetLineRepository.flush();
        savedLines.forEach(this::updateCache);

        saveAuditLog(savedTarget, "COPY", "Budget plan copied from " + source.getCode() + " with multiplier " + factor + " by " + username, username);

        return mapToResponse(savedTarget, savedLines);
    }

    // --- Helpers ---

    private List<BudgetLine> distributeLine(Budget budget, BudgetVersion version, Account account, BudgetDimensionSet dimSet, BudgetLineRequest lineReq) {
        List<BudgetLine> lines = new ArrayList<>();
        LocalDate start = lineReq.periodStartDate();
        LocalDate end = lineReq.periodEndDate();
        BigDecimal totalAmount = lineReq.allocatedAmount() != null ? lineReq.allocatedAmount() : BigDecimal.ZERO;
        String method = lineReq.distributionMethod() != null ? lineReq.distributionMethod() : "MANUAL";
        
        if ("MANUAL".equalsIgnoreCase(method) || start.plusDays(35).isAfter(end)) {
            lines.add(BudgetLine.builder()
                .budget(budget)
                .budgetVersion(version)
                .account(account)
                .dimensionSet(dimSet)
                .periodStartDate(start)
                .periodEndDate(end)
                .allocatedAmount(totalAmount)
                .distributionMethod(method)
                .notes(lineReq.notes())
                .build());
        } else {
            List<LocalDate[]> months = getMonthlyRanges(start, end);
            int count = months.size();
            double[] weights = new double[count];
            if ("SEASONAL".equalsIgnoreCase(method)) {
                double[] stdWeights = {0.06, 0.06, 0.08, 0.08, 0.09, 0.09, 0.07, 0.07, 0.09, 0.09, 0.11, 0.11};
                double sum = 0;
                for (int i = 0; i < count; i++) {
                    int m = months.get(i)[0].getMonthValue();
                    weights[i] = stdWeights[m - 1];
                    sum += weights[i];
                }
                for (int i = 0; i < count; i++) {
                    weights[i] = weights[i] / sum;
                }
            } else {
                for (int i = 0; i < count; i++) {
                    weights[i] = 1.0 / count;
                }
            }
            
            for (int i = 0; i < count; i++) {
                BigDecimal allocated = totalAmount.multiply(BigDecimal.valueOf(weights[i]))
                    .setScale(2, RoundingMode.HALF_UP);
                lines.add(BudgetLine.builder()
                    .budget(budget)
                    .budgetVersion(version)
                    .account(account)
                    .dimensionSet(dimSet)
                    .periodStartDate(months.get(i)[0])
                    .periodEndDate(months.get(i)[1])
                    .allocatedAmount(allocated)
                    .distributionMethod(method)
                    .notes(lineReq.notes() + " (" + method + " month " + (i+1) + ")")
                    .build());
            }
        }
        return lines;
    }

    private List<LocalDate[]> getMonthlyRanges(LocalDate start, LocalDate end) {
        List<LocalDate[]> ranges = new ArrayList<>();
        LocalDate curr = start;
        while (!curr.isAfter(end)) {
            LocalDate monthStart = curr.equals(start) ? curr : curr.withDayOfMonth(1);
            LocalDate monthEnd = curr.plusMonths(1).withDayOfMonth(1).minusDays(1);
            if (monthEnd.isAfter(end)) {
                monthEnd = end;
            }
            ranges.add(new LocalDate[]{monthStart, monthEnd});
            curr = monthEnd.plusDays(1);
        }
        return ranges;
    }

    private void updateCache(BudgetLine line) {
        BigDecimal available = line.getAllocatedAmount().subtract(line.getReservedAmount()).subtract(line.getConsumedAmount());
        BudgetControlCache cache = budgetControlCacheRepository.findById(line.getId())
            .orElseGet(() -> BudgetControlCache.builder().budgetLine(line).build());
        cache.setAllocatedAmount(line.getAllocatedAmount());
        cache.setReservedAmount(line.getReservedAmount());
        cache.setConsumedAmount(line.getConsumedAmount());
        cache.setAvailableAmount(available);
        budgetControlCacheRepository.save(cache);

        if (line.getBudget() != null && line.getBudget().getCompany() != null) {
            eventPublisher.publishEvent(new com.plus33.erp.finance.budget.event.BudgetChangedEvent(this, line.getBudget().getCompany().getId()));
        }
    }

    private BudgetLine resolveBudgetLine(Long companyId, Long accountId, BudgetDimensionSet dimSet, LocalDate txDate) {
        FiscalYear fy = getFiscalYear(companyId, txDate);
        if (fy == null) return null;

        // 1. Exact match on dimension set
        List<BudgetLine> lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, dimSet.getId(), txDate);
        if (!lines.isEmpty()) {
            return lines.get(0);
        }

        // 2. Resolve coarser sets
        // Priority 2: Dept + CostCenter + Project
        BudgetDimensionSet ds2 = findDimensionSet(companyId, dimSet.getDepartment(), dimSet.getCostCenter(), dimSet.getProject(), null, null, null, null);
        if (ds2 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds2.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        // Priority 3: Dept + CostCenter
        BudgetDimensionSet ds3 = findDimensionSet(companyId, dimSet.getDepartment(), dimSet.getCostCenter(), null, null, null, null, null);
        if (ds3 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds3.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        // Priority 4: Dept
        BudgetDimensionSet ds4 = findDimensionSet(companyId, dimSet.getDepartment(), null, null, null, null, null, null);
        if (ds4 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds4.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        // Priority 5: Warehouse
        BudgetDimensionSet ds5 = findDimensionSet(companyId, null, null, null, dimSet.getWarehouse(), null, null, null);
        if (ds5 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds5.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        // Priority 6: AssetCategory
        BudgetDimensionSet ds6 = findDimensionSet(companyId, null, null, null, null, dimSet.getAssetCategory(), null, null);
        if (ds6 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds6.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        // Priority 7: Global Company (dimensions null)
        BudgetDimensionSet ds7 = findDimensionSet(companyId, null, null, null, null, null, null, null);
        if (ds7 != null) {
            lines = budgetLineRepository.findActiveBudgetLines(companyId, fy.getId(), accountId, ds7.getId(), txDate);
            if (!lines.isEmpty()) return lines.get(0);
        }

        return null;
    }

    private BudgetDimensionSet findDimensionSet(
            Long companyId, Department d, CostCenter c, Project p, Warehouse w, AssetCategory a, Region r, Store s) {
        return dimensionSetRepository.findByDimensions(
            companyId,
            d != null ? d.getId() : null,
            c != null ? c.getId() : null,
            p != null ? p.getId() : null,
            w != null ? w.getId() : null,
            a != null ? a.getId() : null,
            r != null ? r.getId() : null,
            s != null ? s.getId() : null
        ).orElse(null);
    }

    private FiscalYear getFiscalYear(Long companyId, LocalDate date) {
        return fiscalYearRepository.findAll().stream()
            .filter(fy -> fy.getCompany().getId().equals(companyId)
                && !fy.getStartDate().isAfter(date)
                && !fy.getEndDate().isBefore(date))
            .findFirst()
            .orElse(null);
    }

    private BudgetPolicy getActivePolicyForCompany(Long companyId) {
        return budgetPolicyRepository.findAll().stream()
            .filter(p -> p.getCompany().getId().equals(companyId) && p.getActive())
            .findFirst()
            .orElse(null);
    }

    private void deactivatePreviousBudgets(Budget current) {
        budgetRepository.findAll().stream()
            .filter(b -> b.getCompany().getId().equals(current.getCompany().getId())
                && b.getFiscalYear().getId().equals(current.getFiscalYear().getId())
                && b.getBudgetType() == current.getBudgetType()
                && b.getScenario() == current.getScenario()
                && !b.getId().equals(current.getId()))
            .forEach(b -> {
                b.setIsActive(false);
                budgetRepository.save(b);
            });
    }

    private void createSnapshot(Budget budget, Integer versionNumber, String triggerEvent, String notes, String username) {
        BudgetSnapshot snapshot = BudgetSnapshot.builder()
            .budget(budget)
            .versionNumber(versionNumber)
            .createdBy(username)
            .triggerEvent(triggerEvent)
            .notes(notes)
            .build();
        BudgetSnapshot savedSnapshot = budgetSnapshotRepository.save(snapshot);

        List<BudgetLine> lines = budgetLineRepository.findAllByBudgetId(budget.getId());
        List<BudgetSnapshotLine> snapLines = lines.stream()
            .map(l -> BudgetSnapshotLine.builder()
                .snapshot(savedSnapshot)
                .account(l.getAccount())
                .dimensionSet(l.getDimensionSet())
                .periodStartDate(l.getPeriodStartDate())
                .periodEndDate(l.getPeriodEndDate())
                .allocatedAmount(l.getAllocatedAmount())
                .reservedAmount(l.getReservedAmount())
                .consumedAmount(l.getConsumedAmount())
                .build())
            .toList();
        budgetSnapshotLineRepository.saveAll(snapLines);
    }

    private void saveAuditLog(Budget budget, String eventType, String desc, String username) {
        BudgetAuditLog log = BudgetAuditLog.builder()
            .budget(budget)
            .eventType(eventType)
            .description(desc)
            .performedBy(username)
            .build();
        budgetAuditLogRepository.save(log);
    }

    private BudgetDrilldownResponse buildDrilldownNode(String dimName, String dimVal, List<BudgetLine> valLines) {
        BigDecimal alloc = valLines.stream().map(BudgetLine::getAllocatedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal res = valLines.stream().map(BudgetLine::getReservedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cons = valLines.stream().map(BudgetLine::getConsumedAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avail = alloc.subtract(res).subtract(cons);

        BigDecimal util = BigDecimal.ZERO;
        if (alloc.compareTo(BigDecimal.ZERO) > 0) {
            util = cons.multiply(BigDecimal.valueOf(100)).divide(alloc, 2, RoundingMode.HALF_UP);
        }

        return new BudgetDrilldownResponse(dimName, dimVal, alloc, res, cons, avail, util, Collections.emptyList());
    }

    private BudgetResponse mapToResponse(Budget b, List<BudgetLine> lines) {
        List<BudgetLineResponse> lineResponses = lines.stream()
            .map(this::mapLineToResponse)
            .toList();
        return new BudgetResponse(
            b.getId(),
            b.getCompany().getId(),
            b.getFiscalYear().getId(),
            b.getBudgetPolicy().getId(),
            b.getWorkflowTemplate() != null ? b.getWorkflowTemplate().getId() : null,
            b.getCode(),
            b.getName(),
            b.getBudgetType().name(),
            b.getPeriodType().name(),
            b.getScenario().name(),
            b.getStatus().name(),
            b.getVersionNumber(),
            b.getIsForecast(),
            b.getForecastType(),
            b.getForecastCycleCode(),
            b.getIsFrozen(),
            b.getIsActive(),
            b.getRateLockType(),
            b.getBudgetExchangeRate(),
            b.getCreatedBy(),
            b.getApprovedBy(),
            b.getApprovedAt(),
            b.getCreatedAt(),
            b.getUpdatedAt(),
            lineResponses
        );
    }

    private BudgetLineResponse mapLineToResponse(BudgetLine line) {
        BigDecimal available = line.getAllocatedAmount().subtract(line.getReservedAmount()).subtract(line.getConsumedAmount());
        return new BudgetLineResponse(
            line.getId(),
            line.getBudget().getId(),
            line.getBudgetVersion().getId(),
            line.getAccount().getId(),
            line.getAccount().getAccountCode(),
            line.getAccount().getAccountName(),
            mapDimSetToResponse(line.getDimensionSet()),
            line.getPeriodStartDate(),
            line.getPeriodEndDate(),
            line.getAllocatedAmount(),
            line.getReservedAmount(),
            line.getConsumedAmount(),
            available,
            line.getIsLocked(),
            line.getDistributionMethod(),
            line.getFormulaExpression(),
            line.getForecastConfidence(),
            line.getPredictedSpend(),
            line.getPredictedRevenue(),
            line.getAiRecommendation(),
            line.getAiGeneratedAt(),
            line.getNotes()
        );
    }

    private BudgetDimensionSetResponse mapDimSetToResponse(BudgetDimensionSet ds) {
        if (ds == null) return null;
        return new BudgetDimensionSetResponse(
            ds.getId(),
            ds.getCompany().getId(),
            ds.getDepartment() != null ? ds.getDepartment().getId() : null,
            ds.getDepartment() != null ? ds.getDepartment().getCode() : null,
            ds.getDepartment() != null ? ds.getDepartment().getName() : null,
            ds.getCostCenter() != null ? ds.getCostCenter().getId() : null,
            ds.getCostCenter() != null ? ds.getCostCenter().getCode() : null,
            ds.getCostCenter() != null ? ds.getCostCenter().getName() : null,
            ds.getProject() != null ? ds.getProject().getId() : null,
            ds.getProject() != null ? ds.getProject().getCode() : null,
            ds.getProject() != null ? ds.getProject().getName() : null,
            ds.getWarehouse() != null ? ds.getWarehouse().getId() : null,
            ds.getWarehouse() != null ? ds.getWarehouse().getCode() : null,
            ds.getWarehouse() != null ? ds.getWarehouse().getName() : null,
            ds.getAssetCategory() != null ? ds.getAssetCategory().getId() : null,
            ds.getAssetCategory() != null ? ds.getAssetCategory().getCode() : null,
            ds.getAssetCategory() != null ? ds.getAssetCategory().getName() : null,
            ds.getRegion() != null ? ds.getRegion().getId() : null,
            ds.getRegion() != null ? ds.getRegion().getCode() : null,
            ds.getRegion() != null ? ds.getRegion().getName() : null,
            ds.getStore() != null ? ds.getStore().getId() : null,
            ds.getStore() != null ? ds.getStore().getCode() : null,
            ds.getStore() != null ? ds.getStore().getName() : null
        );
    }
}