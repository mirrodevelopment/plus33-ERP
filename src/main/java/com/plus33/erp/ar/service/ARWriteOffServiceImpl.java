/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.service
 * File              : ARWriteOffServiceImpl.java
 * Purpose           : Business logic service layer for Ar Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository, CustomerInvoiceRepository, CustomerRepository, CompanyRepository, AccountRepository, JournalEntryRepository, UserRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : ARWriteOffRequest, ARWriteOffResponse, PageResponse, toResponse
 * Related Mapper    : ARWriteOffMapper
 * Related DB Table  : a_r_write_offs
 * Related REST APIs : N/A
 * Depends On        : Common Module, Finance Module, Organization Module, Sales Module, Security Module
 * Used By           : ARWriteOffController, ARWriteOffServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Ar Module. Implements ARWriteOffService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.ar.service;

import com.plus33.erp.ar.dto.ARWriteOffRequest;
import com.plus33.erp.ar.dto.ARWriteOffResponse;
import com.plus33.erp.ar.entity.ARWriteOff;
import com.plus33.erp.ar.repository.ARWriteOffRepository;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import com.plus33.erp.sales.event.CustomerInvoiceRefreshEvent;
import com.plus33.erp.sales.repository.CustomerInvoiceRepository;
import com.plus33.erp.sales.repository.CustomerRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>PLUS33 Coffee ERP -- Ar Module</b>
 *
 * <p><b>Class  :</b> {@code ARWriteOffServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ar.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Ar Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ARWriteOffController
 *   --> ARWriteOffServiceImpl (this)
 *   --> Validate business rules
 *   --> ARWriteOffRepository (read/write 'a_r_write_offs')
 *   --> ARWriteOffMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code a_r_write_offs}</p>
 * <p><b>Module Deps      :</b> Ar, Common, Finance, Organization, Sales, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class ARWriteOffServiceImpl implements ARWriteOffService {

    private static final Set<CustomerInvoiceStatus> WRITABLE_OFF_STATUSES = Set.of(
            CustomerInvoiceStatus.APPROVED,
            CustomerInvoiceStatus.PARTIALLY_PAID,
            CustomerInvoiceStatus.PARTIALLY_CREDITED
    );

    private final ARWriteOffRepository arWriteOffRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ARWriteOffServiceImpl(
            ARWriteOffRepository arWriteOffRepository,
            CustomerInvoiceRepository customerInvoiceRepository,
            CustomerRepository customerRepository,
            CompanyRepository companyRepository,
            AccountRepository accountRepository,
            JournalEntryRepository journalEntryRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher) {
        this.arWriteOffRepository = arWriteOffRepository;
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Create Write-off
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a new write off and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the ARWriteOffResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ARWriteOffResponse createWriteOff(ARWriteOffRequest request) {
        // 1. Validate company
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // 2. Load invoice and verify company isolation
        CustomerInvoice invoice = customerInvoiceRepository.findById(request.customerInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer Invoice not found with ID: " + request.customerInvoiceId()));
        if (!invoice.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Invoice company does not match the requested company");
        }

        // 3. Validate invoice status
        if (!WRITABLE_OFF_STATUSES.contains(invoice.getStatus())) {
            throw new BusinessException(
                    "Write-offs can only be applied to APPROVED, PARTIALLY_PAID, or PARTIALLY_CREDITED invoices. "
                            + "Current status: " + invoice.getStatus());
        }

        // 4. Validate write-off amount does not exceed outstanding balance
        if (request.writeOffAmount().compareTo(invoice.getOutstandingBalance()) > 0) {
            throw new BusinessException(
                    "Write-off amount " + request.writeOffAmount()
                            + " cannot exceed outstanding balance " + invoice.getOutstandingBalance());
        }

        // 5. Generate write-off number (WO-YYYY-000001)
        Long seqVal = arWriteOffRepository.getNextSequenceValue();
        String writeOffNumber = String.format("WO-%d-%06d", LocalDate.now().getYear(), seqVal);

        User currentUser = getCurrentUser();

        // 6. Build the ARWriteOff entity (JE linked after persisting)
        ARWriteOff writeOff = ARWriteOff.builder()
                .writeOffNumber(writeOffNumber)
                .company(company)
                .customerInvoice(invoice)
                .customer(invoice.getCustomer())
                .writeOffAmount(request.writeOffAmount())
                .writeOffDate(request.writeOffDate())
                .reason(request.reason())
                .writtenOffBy(currentUser)
                .build();

        ARWriteOff saved = arWriteOffRepository.save(writeOff);

        // 7. Post GL journal entry: DR Bad Debt Expense (5300) / CR AR (1400)
        JournalEntry journalEntry = generateWriteOffJournalEntry(saved, company, currentUser);
        journalEntryRepository.save(journalEntry);
        saved.setJournalEntry(journalEntry);
        arWriteOffRepository.save(saved);

        // 8. Reduce invoice outstanding balance
        BigDecimal newOutstanding = invoice.getOutstandingBalance()
                .subtract(request.writeOffAmount())
                .setScale(2, RoundingMode.HALF_UP);
        invoice.setOutstandingBalance(newOutstanding);
        customerInvoiceRepository.save(invoice);

        // 9. Reduce customer outstanding balance — same pattern used by
        //    PaymentServiceImpl and CustomerReturnServiceImpl
        Customer customer = invoice.getCustomer();
        customer.setOutstandingBalance(
                customer.getOutstandingBalance()
                        .subtract(request.writeOffAmount())
                        .setScale(2, RoundingMode.HALF_UP));
        customerRepository.save(customer);

        // 10. Refresh AR analytics materialized views
        eventPublisher.publishEvent(new CustomerInvoiceRefreshEvent(this));

        return toResponse(saved);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Read operations
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves a single write off by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the ARWriteOffResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single write off by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the ARWriteOffResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public ARWriteOffResponse getWriteOffById(Long id) {
        ARWriteOff writeOff = arWriteOffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AR Write-off not found with ID: " + id));
        return toResponse(writeOff);
    }

    /**
     * Returns a filtered paginated list of write offs records.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of write offs records.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<ARWriteOffResponse> searchWriteOffs(Long companyId, Long customerId, Pageable pageable) {
        Specification<ARWriteOff> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }
            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ARWriteOff> page = arWriteOffRepository.findAll(spec, pageable);
        List<ARWriteOffResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(content, page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GL Journal Entry
    // ─────────────────────────────────────────────────────────────────────────

    private JournalEntry generateWriteOffJournalEntry(ARWriteOff writeOff, Company company, User currentUser) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", company.getId(), year, nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(writeOff.getWriteOffDate())
                .description("AR Write-off " + writeOff.getWriteOffNumber()
                        + " — Invoice " + writeOff.getCustomerInvoice().getInvoiceNumber())
                .sourceModule("AR_WRITE_OFF")
                .sourceReference(writeOff.getWriteOffNumber())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(currentUser)
                .currencyCode(writeOff.getCustomerInvoice().getCurrencyCode())
                .lines(new ArrayList<>())
                .build();

        // DR: Bad Debt Expense (5300)
        Account badDebtAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5300")
                .orElseThrow(() -> new BusinessException("Bad Debt Expense account (5300) not found for company"));

        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(badDebtAccount)
                .debitAmount(writeOff.getWriteOffAmount())
                .creditAmount(BigDecimal.ZERO)
                .build());

        // CR: Accounts Receivable (1400)
        Account arAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1400")
                .orElseThrow(() -> new BusinessException("Accounts Receivable account (1400) not found for company"));

        je.getLines().add(JournalEntryLine.builder()
                .journalEntry(je)
                .account(arAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(writeOff.getWriteOffAmount())
                .build());

        return je;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Mapper
    // ─────────────────────────────────────────────────────────────────────────

    private ARWriteOffResponse toResponse(ARWriteOff wo) {
        return new ARWriteOffResponse(
                wo.getId(),
                wo.getWriteOffNumber(),
                wo.getCompany().getId(),
                wo.getCustomerInvoice().getId(),
                wo.getCustomerInvoice().getInvoiceNumber(),
                wo.getCustomer().getId(),
                wo.getCustomer().getName(),
                wo.getWriteOffAmount(),
                wo.getWriteOffDate(),
                wo.getReason(),
                wo.getJournalEntry() != null ? wo.getJournalEntry().getId() : null,
                wo.getJournalEntry() != null ? wo.getJournalEntry().getEntryNumber() : null,
                wo.getWrittenOffBy().getId(),
                wo.getWrittenOffBy().getEmail(),
                wo.getCreatedAt()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Security
    // ─────────────────────────────────────────────────────────────────────────

    private User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found: " + email));
    }
}