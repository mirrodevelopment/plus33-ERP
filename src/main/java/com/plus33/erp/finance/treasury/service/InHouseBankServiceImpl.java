/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : InHouseBankServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InHouseBankController
 * Related Service   : InHouseBankServiceImpl
 * Related Repository: InHouseBankAccountRepository, IntercompanyLoanRepository, InternalSettlementRepository, CompanyRepository, AccountRepository
 * Related Entity    : InHouseBank
 * Related DTO       : InHouseBankAccountRequest, InHouseBankAccountResponse, IntercompanyLoanRequest, IntercompanyLoanResponse, InternalSettlementRequest
 * Related Mapper    : InHouseBankMapper
 * Related DB Table  : in_house_banks
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : InHouseBankController, InHouseBankServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements InHouseBankService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InHouseBankServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * InHouseBankController
 *   --> InHouseBankServiceImpl (this)
 *   --> Validate business rules
 *   --> InHouseBankRepository (read/write 'in_house_banks')
 *   --> InHouseBankMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code in_house_banks}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InHouseBankServiceImpl implements InHouseBankService {

    private final InHouseBankAccountRepository inHouseBankAccountRepository;
    private final IntercompanyLoanRepository intercompanyLoanRepository;
    private final InternalSettlementRepository internalSettlementRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;

    /**
     * Creates a new in house account and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the InHouseBankAccountResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public InHouseBankAccountResponse createInHouseAccount(InHouseBankAccountRequest request) {
        Company sub = companyRepository.findById(request.subsidiaryCompanyId())
            .orElseThrow(() -> new ResourceNotFoundException("Subsidiary Company not found with ID: " + request.subsidiaryCompanyId()));
        Account glAccount = accountRepository.findById(request.glAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("GL Account not found with ID: " + request.glAccountId()));

        if (inHouseBankAccountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            throw new BusinessException("In-House Account number already exists: " + request.accountNumber());
        }

        InHouseBankAccount account = InHouseBankAccount.builder()
            .subsidiaryCompany(sub)
            .accountNumber(request.accountNumber())
            .currencyCode(request.currencyCode() != null ? request.currencyCode() : "AED")
            .currentBalance(BigDecimal.ZERO)
            .glAccount(glAccount)
            .active(true)
            .build();
        InHouseBankAccount saved = inHouseBankAccountRepository.save(account);
        return mapToIhbResponse(saved);
    }

    /**
     * Retrieves a single in house account by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the InHouseBankAccountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single in house account by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the InHouseBankAccountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public InHouseBankAccountResponse getInHouseAccountById(Long id) {
        InHouseBankAccount account = inHouseBankAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("In-House Account not found with ID: " + id));
        return mapToIhbResponse(account);
    }

    /**
     * Retrieves in house account by subsidiary data from the database.
     *
     * @param subsidiaryCompanyId the subsidiaryCompanyId input value
     * @return the InHouseBankAccountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves in house account by subsidiary data from the database.
     *
     * @param subsidiaryCompanyId the subsidiaryCompanyId input value
     * @return the InHouseBankAccountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public InHouseBankAccountResponse getInHouseAccountBySubsidiary(Long subsidiaryCompanyId) {
        InHouseBankAccount account = inHouseBankAccountRepository.findBySubsidiaryCompanyId(subsidiaryCompanyId)
            .orElseThrow(() -> new ResourceNotFoundException("In-House Account not found for Subsidiary: " + subsidiaryCompanyId));
        return mapToIhbResponse(account);
    }

    /**
     * Creates a new loan and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the numeric result value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public IntercompanyLoanResponse createLoan(IntercompanyLoanRequest request) {
        Company lender = companyRepository.findById(request.lenderCompanyId())
            .orElseThrow(() -> new ResourceNotFoundException("Lender Company not found: " + request.lenderCompanyId()));
        Company borrower = companyRepository.findById(request.borrowerCompanyId())
            .orElseThrow(() -> new ResourceNotFoundException("Borrower Company not found: " + request.borrowerCompanyId()));

        if (request.lenderCompanyId().equals(request.borrowerCompanyId())) {
            throw new BusinessException("Lender and Borrower companies must be distinct.");
        }

        IntercompanyLoan loan = IntercompanyLoan.builder()
            .lenderCompany(lender)
            .borrowerCompany(borrower)
            .principalAmount(request.principalAmount())
            .interestRate(request.interestRate())
            .startDate(request.startDate())
            .maturityDate(request.maturityDate())
            .interestAccrued(BigDecimal.ZERO)
            .status("ACTIVE")
            .build();
        IntercompanyLoan saved = intercompanyLoanRepository.save(loan);
        return mapToLoanResponse(saved);
    }

    /**
     * Retrieves a single loan by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single loan by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public IntercompanyLoanResponse getLoanById(Long id) {
        IntercompanyLoan loan = intercompanyLoanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Intercompany Loan not found with ID: " + id));
        return mapToLoanResponse(loan);
    }

    /**
     * Retrieves loans by lender data from the database.
     *
     * @param lenderCompanyId the lenderCompanyId input value
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves loans by lender data from the database.
     *
     * @param lenderCompanyId the lenderCompanyId input value
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<IntercompanyLoanResponse> getLoansByLender(Long lenderCompanyId) {
        return intercompanyLoanRepository.findByLenderCompanyId(lenderCompanyId).stream().map(this::mapToLoanResponse).toList();
    }

    /**
     * Retrieves loans by borrower data from the database.
     *
     * @param borrowerCompanyId the borrowerCompanyId input value
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves loans by borrower data from the database.
     *
     * @param borrowerCompanyId the borrowerCompanyId input value
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<IntercompanyLoanResponse> getLoansByBorrower(Long borrowerCompanyId) {
        return intercompanyLoanRepository.findByBorrowerCompanyId(borrowerCompanyId).stream().map(this::mapToLoanResponse).toList();
    }

    /**
     * Performs the accrueIntercompanyLoanInterest operation in this module.
     *
     * @param loanId the loanId input value
     */
    @Override
    @Transactional
    public void accrueIntercompanyLoanInterest(Long loanId) {
        IntercompanyLoan loan = intercompanyLoanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Intercompany Loan not found with ID: " + loanId));

        if (!"ACTIVE".equals(loan.getStatus())) return;

        // Simple daily interest accrual calculation: Principal * Rate% / 365
        BigDecimal rateFraction = loan.getInterestRate().divide(BigDecimal.valueOf(100.00), 6, RoundingMode.HALF_UP);
        BigDecimal dailyInterest = loan.getPrincipalAmount().multiply(rateFraction)
            .divide(BigDecimal.valueOf(365.00), 2, RoundingMode.HALF_UP);
        
        loan.setInterestAccrued(loan.getInterestAccrued().add(dailyInterest));
        intercompanyLoanRepository.save(loan);
    }

    /**
     * Creates a new internal settlement and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the numeric result value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public InternalSettlementResponse createInternalSettlement(InternalSettlementRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        InHouseBankAccount source = inHouseBankAccountRepository.findById(request.sourceIhbAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Source IHB Account not found: " + request.sourceIhbAccountId()));
        InHouseBankAccount target = inHouseBankAccountRepository.findById(request.targetIhbAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Target IHB Account not found: " + request.targetIhbAccountId()));

        if (source.getCurrentBalance().compareTo(request.amount()) < 0) {
            throw new BusinessException("Insufficient balance in source In-House Account. Available: " + source.getCurrentBalance());
        }

        // Perform settlement transfer
        source.setCurrentBalance(source.getCurrentBalance().subtract(request.amount()));
        target.setCurrentBalance(target.getCurrentBalance().add(request.amount()));

        inHouseBankAccountRepository.save(source);
        inHouseBankAccountRepository.save(target);

        InternalSettlement settlement = InternalSettlement.builder()
            .company(company)
            .sourceIhbAccount(source)
            .targetIhbAccount(target)
            .amount(request.amount())
            .settlementDate(request.settlementDate())
            .referenceNumber(request.referenceNumber())
            .notes(request.notes())
            .build();
        InternalSettlement saved = internalSettlementRepository.save(settlement);
        return mapToSettlementResponse(saved);
    }

    /**
     * Retrieves internal settlements data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves internal settlements data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<InternalSettlementResponse> getInternalSettlements(Long companyId) {
        return internalSettlementRepository.findByCompanyId(companyId).stream().map(this::mapToSettlementResponse).toList();
    }

    // Mapping Helpers
    private InHouseBankAccountResponse mapToIhbResponse(InHouseBankAccount account) {
        return new InHouseBankAccountResponse(
            account.getId(),
            account.getSubsidiaryCompany().getId(),
            account.getSubsidiaryCompany().getName(),
            account.getAccountNumber(),
            account.getCurrencyCode(),
            account.getCurrentBalance(),
            account.getGlAccount().getId(),
            account.getActive()
        );
    }

    private IntercompanyLoanResponse mapToLoanResponse(IntercompanyLoan loan) {
        return new IntercompanyLoanResponse(
            loan.getId(),
            loan.getLenderCompany().getId(),
            loan.getLenderCompany().getName(),
            loan.getBorrowerCompany().getId(),
            loan.getBorrowerCompany().getName(),
            loan.getPrincipalAmount(),
            loan.getInterestRate(),
            loan.getStartDate(),
            loan.getMaturityDate(),
            loan.getInterestAccrued(),
            loan.getStatus()
        );
    }

    private InternalSettlementResponse mapToSettlementResponse(InternalSettlement settle) {
        return new InternalSettlementResponse(
            settle.getId(),
            settle.getCompany().getId(),
            settle.getSourceIhbAccount().getId(),
            settle.getSourceIhbAccount().getSubsidiaryCompany().getName(),
            settle.getTargetIhbAccount().getId(),
            settle.getTargetIhbAccount().getSubsidiaryCompany().getName(),
            settle.getAmount(),
            settle.getSettlementDate(),
            settle.getReferenceNumber(),
            settle.getNotes()
        );
    }
}