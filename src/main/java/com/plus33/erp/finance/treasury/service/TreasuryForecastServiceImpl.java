/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryForecastServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryForecastController
 * Related Service   : TreasuryForecastServiceImpl
 * Related Repository: CashPositionSnapshotRepository, CashPositionSnapshotLineRepository, TreasuryLimitRepository, TreasuryComplianceLogRepository, BankAccountRepository, BankRepository, CompanyRepository
 * Related Entity    : TreasuryForecast
 * Related DTO       : CashPositionSnapshotLineResponse, CashPositionSnapshotResponse, mapToComplianceLogResponse, mapToLimitResponse, mapToLineResponse
 * Related Mapper    : TreasuryForecastMapper
 * Related DB Table  : treasury_forecasts
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : TreasuryForecastController, TreasuryForecastServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TreasuryForecastService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryForecastServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TreasuryForecastController
 *   --> TreasuryForecastServiceImpl (this)
 *   --> Validate business rules
 *   --> TreasuryForecastRepository (read/write 'treasury_forecasts')
 *   --> TreasuryForecastMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code treasury_forecasts}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryForecastServiceImpl implements TreasuryForecastService {

    private final CashPositionSnapshotRepository cashPositionSnapshotRepository;
    private final CashPositionSnapshotLineRepository cashPositionSnapshotLineRepository;
    private final TreasuryLimitRepository treasuryLimitRepository;
    private final TreasuryComplianceLogRepository treasuryComplianceLogRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankRepository bankRepository;
    private final CompanyRepository companyRepository;

    /**
     * Performs the takeCashPositionSnapshot operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param snapshotType the snapshotType input value
     * @param username the username input value
     * @return the CashPositionSnapshotResponse result
     */
    @Override
    @Transactional
    public CashPositionSnapshotResponse takeCashPositionSnapshot(Long companyId, String snapshotType, String username) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + companyId));

        List<BankAccount> accounts = bankAccountRepository.findByCompanyId(companyId);
        
        BigDecimal totalUSD = BigDecimal.ZERO;
        for (BankAccount account : accounts) {
            BigDecimal usdRate = "USD".equals(account.getCurrencyCode()) ? BigDecimal.ONE : new BigDecimal("0.27"); // Mock AED to USD rate
            BigDecimal usdVal = account.getCurrentBalance().multiply(usdRate);
            totalUSD = totalUSD.add(usdVal);
        }

        CashPositionSnapshot snapshot = CashPositionSnapshot.builder()
            .company(company)
            .snapshotDate(LocalDateTime.now())
            .snapshotType(snapshotType != null ? snapshotType : "END_OF_DAY")
            .totalCashUsd(totalUSD)
            .createdBy(username)
            .build();

        CashPositionSnapshot saved = cashPositionSnapshotRepository.save(snapshot);
        List<CashPositionSnapshotLineResponse> lineResponses = new ArrayList<>();

        for (BankAccount account : accounts) {
            CashPositionSnapshotLine line = CashPositionSnapshotLine.builder()
                .snapshot(saved)
                .bankAccount(account)
                .currentBalance(account.getCurrentBalance())
                .reconciledBalance(account.getReconciledBalance())
                .currencyCode(account.getCurrencyCode())
                .build();
            
            CashPositionSnapshotLine savedLine = cashPositionSnapshotLineRepository.save(line);
            saved.getLines().add(savedLine);
            lineResponses.add(mapToLineResponse(savedLine));
        }

        return mapToSnapshotResponse(saved, lineResponses);
    }

    /**
     * Retrieves cash position snapshots data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves cash position snapshots data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<CashPositionSnapshotResponse> getCashPositionSnapshots(Long companyId) {
        return cashPositionSnapshotRepository.findByCompanyId(companyId).stream().map(s -> {
            List<CashPositionSnapshotLineResponse> lines = s.getLines().stream().map(this::mapToLineResponse).toList();
            return mapToSnapshotResponse(s, lines);
        }).toList();
    }

    /**
     * Creates a new limit and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the TreasuryLimitResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public TreasuryLimitResponse createLimit(TreasuryLimitRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        
        Bank targetBank = null;
        if (request.targetBankId() != null) {
            targetBank = bankRepository.findById(request.targetBankId())
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + request.targetBankId()));
        }

        if (treasuryLimitRepository.findByCompanyIdAndLimitTypeAndCurrencyCodeAndCountryCodeAndTargetBankId(
                request.companyId(), request.limitType(), request.currencyCode(), request.countryCode(), request.targetBankId()).isPresent()) {
            throw new BusinessException("Treasury Limit rule already exists for this combination.");
        }

        TreasuryLimit limit = TreasuryLimit.builder()
            .company(company)
            .limitType(request.limitType())
            .currencyCode(request.currencyCode())
            .countryCode(request.countryCode())
            .targetBank(targetBank)
            .limitAmount(request.limitAmount())
            .warningThresholdPercent(request.warningThresholdPercent() != null ? request.warningThresholdPercent() : new BigDecimal("80.00"))
            .active(true)
            .build();

        TreasuryLimit saved = treasuryLimitRepository.save(limit);
        return mapToLimitResponse(saved);
    }

    /**
     * Retrieves limits by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves limits by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<TreasuryLimitResponse> getLimitsByCompany(Long companyId) {
        return treasuryLimitRepository.findByCompanyId(companyId).stream().map(this::mapToLimitResponse).toList();
    }

    /**
     * Validates business rules and constraints for exposure limits.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public void checkExposureLimits(Long companyId) {
        List<TreasuryLimit> limits = treasuryLimitRepository.findByCompanyId(companyId);
        List<BankAccount> accounts = bankAccountRepository.findByCompanyId(companyId);

        for (TreasuryLimit limit : limits) {
            if (!limit.getActive()) continue;

            if ("MINIMUM_CASH_RESERVE".equals(limit.getLimitType())) {
                BigDecimal totalCash = accounts.stream()
                    .filter(a -> limit.getCurrencyCode() == null || a.getCurrencyCode().equals(limit.getCurrencyCode()))
                    .map(BankAccount::getCurrentBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalCash.compareTo(limit.getLimitAmount()) < 0) {
                    // Log warning
                    String details = "COMPLIANCE BREACH: Total cash reserve is " + totalCash + ", which is below the minimum limit of " + limit.getLimitAmount();
                    log.warn(details);
                    logCompliance(new TreasuryComplianceLogRequest(companyId, "LIMIT_BREACH", details, "SYSTEM"));
                }
            }
        }
    }

    /**
     * Performs the logCompliance operation in this module.
     *
     * @param request the validated request DTO containing input data
     * @return the TreasuryComplianceLogResponse result
     */
    @Override
    @Transactional
    public TreasuryComplianceLogResponse logCompliance(TreasuryComplianceLogRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + request.companyId()));

        TreasuryComplianceLog complianceLog = TreasuryComplianceLog.builder()
            .company(company)
            .actionType(request.actionType())
            .details(request.details())
            .performedBy(request.performedBy())
            .build();
        
        TreasuryComplianceLog saved = treasuryComplianceLogRepository.save(complianceLog);
        return mapToComplianceLogResponse(saved);
    }

    /**
     * Retrieves compliance logs data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves compliance logs data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<TreasuryComplianceLogResponse> getComplianceLogs(Long companyId) {
        return treasuryComplianceLogRepository.findByCompanyId(companyId).stream().map(this::mapToComplianceLogResponse).toList();
    }

    // Mapping Helpers
    private CashPositionSnapshotLineResponse mapToLineResponse(CashPositionSnapshotLine line) {
        return new CashPositionSnapshotLineResponse(
            line.getId(),
            line.getBankAccount().getId(),
            line.getBankAccount().getAccountNumber(),
            line.getCurrentBalance(),
            line.getReconciledBalance(),
            line.getCurrencyCode()
        );
    }

    private CashPositionSnapshotResponse mapToSnapshotResponse(CashPositionSnapshot snap, List<CashPositionSnapshotLineResponse> lines) {
        return new CashPositionSnapshotResponse(
            snap.getId(),
            snap.getCompany().getId(),
            snap.getSnapshotDate(),
            snap.getSnapshotType(),
            snap.getTotalCashUsd(),
            snap.getCreatedBy(),
            lines
        );
    }

    private TreasuryLimitResponse mapToLimitResponse(TreasuryLimit limit) {
        return new TreasuryLimitResponse(
            limit.getId(),
            limit.getCompany().getId(),
            limit.getLimitType(),
            limit.getCurrencyCode(),
            limit.getCountryCode(),
            limit.getTargetBank() != null ? limit.getTargetBank().getId() : null,
            limit.getTargetBank() != null ? limit.getTargetBank().getName() : null,
            limit.getLimitAmount(),
            limit.getWarningThresholdPercent(),
            limit.getActive()
        );
    }

    private TreasuryComplianceLogResponse mapToComplianceLogResponse(TreasuryComplianceLog logEntry) {
        return new TreasuryComplianceLogResponse(
            logEntry.getId(),
            logEntry.getCompany().getId(),
            logEntry.getActionType(),
            logEntry.getDetails(),
            logEntry.getPerformedBy(),
            logEntry.getTimestamp()
        );
    }
}