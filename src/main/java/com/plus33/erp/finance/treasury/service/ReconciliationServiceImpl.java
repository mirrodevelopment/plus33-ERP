/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : ReconciliationServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReconciliationController
 * Related Service   : ReconciliationServiceImpl
 * Related Repository: BankStatementRepository, BankStatementLineRepository, ReconciliationRuleRepository, BankFeeRuleRepository, BankAccountRepository, PaymentRepository, CompanyRepository, AccountRepository
 * Related Entity    : Reconciliation
 * Related DTO       : BankFeeRuleRequest, BankFeeRuleResponse, BankStatementLineRequest, BankStatementLineResponse, BankStatementRequest
 * Related Mapper    : ReconciliationMapper
 * Related DB Table  : reconciliations
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : ReconciliationController, ReconciliationServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements ReconciliationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.treasury.dto.StatementAndRecDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.organization.entity.Company;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ReconciliationServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ReconciliationController
 *   --> ReconciliationServiceImpl (this)
 *   --> Validate business rules
 *   --> ReconciliationRepository (read/write 'reconciliations')
 *   --> ReconciliationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code reconciliations}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReconciliationServiceImpl implements ReconciliationService {

    private final BankStatementRepository bankStatementRepository;
    private final BankStatementLineRepository bankStatementLineRepository;
    private final ReconciliationRuleRepository reconciliationRuleRepository;
    private final BankFeeRuleRepository bankFeeRuleRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentRepository paymentRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Imports statement data from an external source or file.
     *
     * @param request the validated request DTO containing input data
     * @return the BankStatementResponse result
     */
    @Override
    @Transactional
    public BankStatementResponse importStatement(BankStatementRequest request) {
        BankAccount bankAccount = bankAccountRepository.findById(request.bankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with ID: " + request.bankAccountId()));

        // Duplicate statement detection
        if (bankStatementRepository.findByBankAccountIdAndStatementNumber(request.bankAccountId(), request.statementNumber()).isPresent()) {
            throw new BusinessException("Bank Statement already imported: " + request.statementNumber());
        }

        BankStatement statement = BankStatement.builder()
            .bankAccount(bankAccount)
            .statementNumber(request.statementNumber())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .openingBalance(request.openingBalance())
            .closingBalance(request.closingBalance())
            .status("DRAFT")
            .build();
        
        BankStatement savedStatement = bankStatementRepository.save(statement);

        List<BankStatementLineResponse> lineResponses = new ArrayList<>();
        if (request.lines() != null) {
            for (BankStatementLineRequest lineReq : request.lines()) {
                // Filter out duplicate statement lines (within the same import batch)
                boolean isDuplicate = savedStatement.getLines().stream()
                    .anyMatch(l -> l.getTransactionDate().equals(lineReq.transactionDate())
                        && l.getDescription().equals(lineReq.description())
                        && l.getAmount().compareTo(lineReq.amount()) == 0
                        && l.getReference() != null && l.getReference().equals(lineReq.reference()));
                
                if (isDuplicate) {
                    log.warn("Filtering duplicate statement line: Ref: {}, Amount: {}", lineReq.reference(), lineReq.amount());
                    continue;
                }

                BankStatementLine line = BankStatementLine.builder()
                    .statement(savedStatement)
                    .transactionDate(lineReq.transactionDate())
                    .valueDate(lineReq.valueDate())
                    .description(lineReq.description())
                    .reference(lineReq.reference())
                    .amount(lineReq.amount())
                    .reconciled(false)
                    .build();
                
                BankStatementLine savedLine = bankStatementLineRepository.save(line);
                savedStatement.getLines().add(savedLine);
                lineResponses.add(mapToLineResponse(savedLine));
            }
        }

        // Publish Event
        eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.StatementImportedEvent(this, savedStatement.getId()));

        return mapToStatementResponse(savedStatement, lineResponses);
    }

    /**
     * Retrieves a single statement by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the BankStatementResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single statement by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the BankStatementResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public BankStatementResponse getStatementById(Long id) {
        BankStatement statement = bankStatementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Statement not found with ID: " + id));
        List<BankStatementLineResponse> lines = statement.getLines().stream().map(this::mapToLineResponse).toList();
        return mapToStatementResponse(statement, lines);
    }

    /**
     * Retrieves statements by account data from the database.
     *
     * @param bankAccountId the bankAccountId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves statements by account data from the database.
     *
     * @param bankAccountId the bankAccountId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<BankStatementResponse> getStatementsByAccount(Long bankAccountId) {
        return bankStatementRepository.findByBankAccountId(bankAccountId).stream().map(s -> {
            List<BankStatementLineResponse> lines = s.getLines().stream().map(this::mapToLineResponse).toList();
            return mapToStatementResponse(s, lines);
        }).toList();
    }

    /**
     * Creates a new rule and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the ReconciliationRuleResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ReconciliationRuleResponse createRule(ReconciliationRuleRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        ReconciliationRule rule = ReconciliationRule.builder()
            .company(company)
            .ruleName(request.ruleName())
            .dateToleranceDays(request.dateToleranceDays() != null ? request.dateToleranceDays() : 3)
            .referenceMatchType(request.referenceMatchType() != null ? request.referenceMatchType() : "EXACT")
            .allowManyToOne(request.allowManyToOne() != null ? request.allowManyToOne() : false)
            .allowOneToMany(request.allowOneToMany() != null ? request.allowOneToMany() : false)
            .allowSplits(request.allowSplits() != null ? request.allowSplits() : false)
            .active(true)
            .build();
        ReconciliationRule saved = reconciliationRuleRepository.save(rule);
        return mapToRuleResponse(saved);
    }

    /**
     * Retrieves rules by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves rules by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<ReconciliationRuleResponse> getRulesByCompany(Long companyId) {
        return reconciliationRuleRepository.findByCompanyId(companyId).stream().map(this::mapToRuleResponse).toList();
    }

    /**
     * Performs the runAutoReconciliation operation in this module.
     *
     * @param statementId the statementId input value
     * @param companyId owning company ID for multi-tenant data isolation
     */
    @Override
    @Transactional
    public void runAutoReconciliation(Long statementId, Long companyId) {
        BankStatement statement = bankStatementRepository.findById(statementId)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Statement not found with ID: " + statementId));

        List<ReconciliationRule> rules = reconciliationRuleRepository.findByCompanyIdAndActiveTrue(companyId);
        if (rules.isEmpty()) {
            log.warn("No active reconciliation rules found for company: {}", companyId);
            return;
        }

        ReconciliationRule rule = rules.get(0); // Use the first active rule

        List<Payment> unmatchedPayments = paymentRepository.findAll().stream()
            .filter(p -> p.getCompany().getId().equals(companyId) && p.getStatus() == com.plus33.erp.finance.entity.PaymentStatus.COMPLETED)
            .toList();

        for (BankStatementLine line : statement.getLines()) {
            if (line.getReconciled()) continue;

            // Attempt matching
            for (Payment payment : unmatchedPayments) {
                // Check if payment was already matched in this transaction execution
                boolean alreadyMatched = bankStatementLineRepository.findAll().stream()
                    .anyMatch(l -> l.getReconciled() && l.getPayment() != null && l.getPayment().getId().equals(payment.getId()));
                if (alreadyMatched) continue;

                boolean amountMatch = payment.getAmount().compareTo(line.getAmount().abs()) == 0;
                boolean dateMatch = Math.abs(ChronoUnitDays(payment.getPaymentDate(), line.getTransactionDate())) <= rule.getDateToleranceDays();
                
                boolean refMatch = false;
                if ("EXACT".equals(rule.getReferenceMatchType())) {
                    refMatch = line.getReference() != null && line.getReference().equals(payment.getReferenceNumber());
                } else if ("PARTIAL".equals(rule.getReferenceMatchType())) {
                    refMatch = line.getReference() != null && payment.getReferenceNumber() != null
                        && (line.getReference().contains(payment.getReferenceNumber()) || payment.getReferenceNumber().contains(line.getReference()));
                } else if ("FUZZY".equals(rule.getReferenceMatchType())) {
                    // Fuzzy match textual similarity on description containment
                    refMatch = line.getDescription() != null && payment.getReferenceNumber() != null
                        && line.getDescription().toLowerCase().contains(payment.getReferenceNumber().toLowerCase());
                }

                if (amountMatch && dateMatch && (refMatch || "NONE".equals(rule.getReferenceMatchType()))) {
                    // Match found!
                    line.setReconciled(true);
                    line.setPayment(payment);
                    line.setReconciledAt(LocalDateTime.now());
                    bankStatementLineRepository.save(line);

                    // Update reconciled balance of account
                    BankAccount account = statement.getBankAccount();
                    account.setReconciledBalance(account.getReconciledBalance().add(line.getAmount()));
                    bankAccountRepository.save(account);

                    log.info("AUTO-MATCH: Matched statement line ID {} to payment ID {}", line.getId(), payment.getId());
                    break;
                }
            }
        }

        // Check if all lines reconciled, if so update statement status
        long unreconciledCount = statement.getLines().stream().filter(l -> !l.getReconciled()).count();
        if (unreconciledCount == 0) {
            statement.setStatus("RECONCILED");
        } else {
            statement.setStatus("RECONCILING");
        }
        bankStatementRepository.save(statement);

        // Publish Event
        eventPublisher.publishEvent(new com.plus33.erp.finance.treasury.entity.ReconciliationCompletedEvent(this, statementId));
    }

    /**
     * Performs the manualMatch operation in this module.
     *
     * @param statementLineId the statementLineId input value
     * @param paymentId the paymentId input value
     * @param username the username input value
     */
    @Override
    @Transactional
    public void manualMatch(Long statementLineId, Long paymentId, String username) {
        BankStatementLine line = bankStatementLineRepository.findById(statementLineId)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Statement Line not found: " + statementLineId));
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        if (line.getReconciled()) {
            throw new BusinessException("Statement line is already reconciled.");
        }

        if (payment.getAmount().compareTo(line.getAmount().abs()) != 0) {
            throw new BusinessException("Amount mismatch: Statement Line is " + line.getAmount() + ", Payment is " + payment.getAmount());
        }

        line.setReconciled(true);
        line.setPayment(payment);
        line.setReconciledAt(LocalDateTime.now());
        bankStatementLineRepository.save(line);

        BankAccount account = line.getStatement().getBankAccount();
        account.setReconciledBalance(account.getReconciledBalance().add(line.getAmount()));
        bankAccountRepository.save(account);

        log.info("MANUAL-MATCH: Statement line ID {} matched to payment ID {} by {}", statementLineId, paymentId, username);
    }

    /**
     * Performs the splitAndMatch operation in this module.
     *
     * @param statementLineId the statementLineId input value
     * @param paymentIds the paymentIds input value
     * @param username the username input value
     */
    @Override
    @Transactional
    public void splitAndMatch(Long statementLineId, List<Long> paymentIds, String username) {
        BankStatementLine line = bankStatementLineRepository.findById(statementLineId)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Statement Line not found: " + statementLineId));

        if (line.getReconciled()) {
            throw new BusinessException("Statement line is already reconciled.");
        }

        BigDecimal paymentsSum = BigDecimal.ZERO;
        List<Payment> payments = new ArrayList<>();
        for (Long pId : paymentIds) {
            Payment p = paymentRepository.findById(pId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + pId));
            paymentsSum = paymentsSum.add(p.getAmount());
            payments.add(p);
        }

        if (paymentsSum.compareTo(line.getAmount().abs()) != 0) {
            throw new BusinessException("Amount sum mismatch: Statement Line is " + line.getAmount() + ", sum of payments is " + paymentsSum);
        }

        // Mark reconciled and link to primary payment
        line.setReconciled(true);
        line.setPayment(payments.get(0));
        line.setReconciledAt(LocalDateTime.now());
        bankStatementLineRepository.save(line);

        BankAccount account = line.getStatement().getBankAccount();
        account.setReconciledBalance(account.getReconciledBalance().add(line.getAmount()));
        bankAccountRepository.save(account);

        log.info("SPLIT-MATCH: Statement line ID {} split-matched to {} payments by {}", statementLineId, paymentIds.size(), username);
    }

    /**
     * Creates a new fee rule and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the BankFeeRuleResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public BankFeeRuleResponse createFeeRule(BankFeeRuleRequest request) {
        BankAccount account = bankAccountRepository.findById(request.bankAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with ID: " + request.bankAccountId()));
        Account glExpense = accountRepository.findById(request.glExpenseAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("GL Account not found with ID: " + request.glExpenseAccountId()));

        BankFeeRule rule = BankFeeRule.builder()
            .bankAccount(account)
            .chargeType(request.chargeType())
            .ratePercent(request.ratePercent() != null ? request.ratePercent() : BigDecimal.ZERO)
            .fixedAmount(request.fixedAmount() != null ? request.fixedAmount() : BigDecimal.ZERO)
            .glExpenseAccount(glExpense)
            .active(true)
            .build();
        BankFeeRule saved = bankFeeRuleRepository.save(rule);
        return mapToFeeRuleResponse(saved);
    }

    /**
     * Retrieves fee rules by account data from the database.
     *
     * @param bankAccountId the bankAccountId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves fee rules by account data from the database.
     *
     * @param bankAccountId the bankAccountId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<BankFeeRuleResponse> getFeeRulesByAccount(Long bankAccountId) {
        return bankFeeRuleRepository.findByBankAccountId(bankAccountId).stream().map(this::mapToFeeRuleResponse).toList();
    }

    /**
     * Processes the bank fees business workflow end-to-end.
     *
     * @param bankAccountId the bankAccountId input value
     * @param username the username input value
     */
    @Override
    @Transactional
    public void processBankFees(Long bankAccountId, String username) {
        BankAccount account = bankAccountRepository.findById(bankAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found: " + bankAccountId));

        List<BankFeeRule> rules = bankFeeRuleRepository.findByBankAccountIdAndActiveTrue(bankAccountId);
        if (rules.isEmpty()) return;

        for (BankFeeRule rule : rules) {
            BigDecimal feeAmount = BigDecimal.ZERO;
            if ("MONTHLY_MAINTENANCE".equals(rule.getChargeType())) {
                feeAmount = rule.getFixedAmount();
            }
            
            if (feeAmount.compareTo(BigDecimal.ZERO) > 0) {
                // Post GL or deduct from account balance directly
                account.setCurrentBalance(account.getCurrentBalance().subtract(feeAmount));
                bankAccountRepository.save(account);
                log.info("FEE-ENGINE: Charge rule ID {} applied fee {} to account {}", rule.getId(), feeAmount, account.getAccountNumber());
            }
        }
    }

    private long ChronoUnitDays(LocalDate d1, LocalDate d2) {
        return ChronoUnit.DAYS.between(d1, d2);
    }

    // Mapping Helpers
    private BankStatementResponse mapToStatementResponse(BankStatement stmt, List<BankStatementLineResponse> lines) {
        return new BankStatementResponse(
            stmt.getId(),
            stmt.getBankAccount().getId(),
            stmt.getBankAccount().getAccountNumber(),
            stmt.getStatementNumber(),
            stmt.getStartDate(),
            stmt.getEndDate(),
            stmt.getOpeningBalance(),
            stmt.getClosingBalance(),
            stmt.getStatus(),
            stmt.getImportedAt(),
            lines
        );
    }

    private BankStatementLineResponse mapToLineResponse(BankStatementLine line) {
        return new BankStatementLineResponse(
            line.getId(),
            line.getStatement().getId(),
            line.getTransactionDate(),
            line.getValueDate(),
            line.getDescription(),
            line.getReference(),
            line.getAmount(),
            line.getReconciled(),
            line.getPayment() != null ? line.getPayment().getId() : null,
            line.getPayment() != null ? line.getPayment().getPaymentNumber() : null,
            line.getReconciledAt()
        );
    }

    private ReconciliationRuleResponse mapToRuleResponse(ReconciliationRule rule) {
        return new ReconciliationRuleResponse(
            rule.getId(),
            rule.getCompany().getId(),
            rule.getRuleName(),
            rule.getDateToleranceDays(),
            rule.getReferenceMatchType(),
            rule.getAllowManyToOne(),
            rule.getAllowOneToMany(),
            rule.getAllowSplits(),
            rule.getActive()
        );
    }

    private BankFeeRuleResponse mapToFeeRuleResponse(BankFeeRule rule) {
        return new BankFeeRuleResponse(
            rule.getId(),
            rule.getBankAccount().getId(),
            rule.getBankAccount().getAccountNumber(),
            rule.getChargeType(),
            rule.getRatePercent(),
            rule.getFixedAmount(),
            rule.getGlExpenseAccount().getId(),
            rule.getGlExpenseAccount().getAccountCode(),
            rule.getActive()
        );
    }
}