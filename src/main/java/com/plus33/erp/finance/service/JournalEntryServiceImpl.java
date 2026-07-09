/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : JournalEntryServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository, CompanyRepository, AccountRepository, UserRepository, BudgetDimensionSetRepository
 * Related Entity    : JournalEntry
 * Related DTO       : BudgetDimensionSetRequest, JournalEntryLineRequest, JournalEntryRequest, JournalEntryResponse, toResponse
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entrys
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module, Security Module
 * Used By           : JournalEntryController, JournalEntryServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements JournalEntryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.budget.repository.BudgetDimensionSetRepository;
import com.plus33.erp.finance.budget.service.BudgetService;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.mapper.JournalEntryMapper;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * JournalEntryController
 *   --> JournalEntryServiceImpl (this)
 *   --> Validate business rules
 *   --> JournalEntryRepository (read/write 'journal_entrys')
 *   --> JournalEntryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code journal_entrys}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BudgetDimensionSetRepository budgetDimensionSetRepository;
    private final BudgetService budgetService;
    private final JournalEntryMapper journalEntryMapper;

    /**
     * Creates a new journal entry and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the JournalEntryResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public JournalEntryResponse createJournalEntry(JournalEntryRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        User currentUser = getCurrentUser();

        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        JournalEntry je = journalEntryMapper.toEntity(request);
        je.setCompany(company);
        je.setCreatedBy(currentUser);
        je.setSourceModule("GENERAL_GL");
        je.setStatus("DRAFT");
        je.setLines(new ArrayList<>());

        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = request.entryDate().getYear();
        String entryNumber = String.format("JE-%d-%d-%06d", company.getId(), year, nextSeq);
        je.setEntryNumber(entryNumber);

        for (JournalEntryLineRequest lineReq : request.lines()) {
            Account account = accountRepository.findById(lineReq.accountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + lineReq.accountId()));

            JournalEntryLine line = journalEntryMapper.toLineEntity(lineReq);
            line.setAccount(account);
            line.setJournalEntry(je);

            if (lineReq.dimensionSetId() != null) {
                line.setDimensionSet(budgetDimensionSetRepository.findById(lineReq.dimensionSetId()).orElse(null));
            }

            je.getLines().add(line);
            totalDebits = totalDebits.add(lineReq.debitAmount());
            totalCredits = totalCredits.add(lineReq.creditAmount());
        }

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new BusinessException("Debits must equal Credits. Difference: " + totalDebits.subtract(totalCredits).abs());
        }

        JournalEntry saved = journalEntryRepository.save(je);
        return journalEntryMapper.toResponse(saved);
    }

    /**
     * Posts journal entry entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the JournalEntryResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public JournalEntryResponse postJournalEntry(Long id) {
        JournalEntry je = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal Entry not found with ID: " + id));

        if (!"DRAFT".equals(je.getStatus())) {
            throw new BusinessException("Only DRAFT journal entries can be posted");
        }

        // Validate and Consume Budget for each debit line mapping dimension sets
        for (JournalEntryLine line : je.getLines()) {
            if (line.getDimensionSet() != null && line.getDebitAmount().compareTo(BigDecimal.ZERO) > 0) {
                try {
                    com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest dimReq = new com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest(
                        line.getDimensionSet().getDepartment() != null ? line.getDimensionSet().getDepartment().getId() : null,
                        line.getDimensionSet().getCostCenter() != null ? line.getDimensionSet().getCostCenter().getId() : null,
                        line.getDimensionSet().getProject() != null ? line.getDimensionSet().getProject().getId() : null,
                        line.getDimensionSet().getWarehouse() != null ? line.getDimensionSet().getWarehouse().getId() : null,
                        line.getDimensionSet().getAssetCategory() != null ? line.getDimensionSet().getAssetCategory().getId() : null,
                        line.getDimensionSet().getRegion() != null ? line.getDimensionSet().getRegion().getId() : null,
                        line.getDimensionSet().getStore() != null ? line.getDimensionSet().getStore().getId() : null
                    );

                    budgetService.createDirectConsumption(
                        je.getCompany().getId(),
                        line.getAccount().getId(),
                        dimReq,
                        line.getDebitAmount(),
                        "GENERAL_GL",
                        line.getId(),
                        je.getEntryNumber(),
                        je.getEntryDate()
                    );
                } catch (Exception e) {
                    log.error("Failed to post GL entry: Budget validation failed for line ID: {}", line.getId(), e);
                    throw e;
                }
            }
        }

        je.setStatus("POSTED");
        je.setPostedAt(LocalDateTime.now());
        JournalEntry saved = journalEntryRepository.save(je);
        return journalEntryMapper.toResponse(saved);
    }

    /**
     * Retrieves a single journal entry by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the JournalEntryResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single journal entry by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the JournalEntryResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public JournalEntryResponse getJournalEntryById(Long id) {
        JournalEntry je = journalEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Journal Entry not found with ID: " + id));
        return journalEntryMapper.toResponse(je);
    }

    /**
     * Retrieves journal entries by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves journal entries by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<JournalEntryResponse> getJournalEntriesByCompany(Long companyId) {
        List<JournalEntry> list = journalEntryRepository.findAll().stream()
                .filter(je -> je.getCompany().getId().equals(companyId))
                .toList();
        return journalEntryMapper.toResponseList(list);
    }

    private User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }
}