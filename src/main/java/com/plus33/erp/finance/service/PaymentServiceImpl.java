package com.plus33.erp.finance.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.mapper.PaymentMapper;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.finance.repository.PaymentAllocationRepository;
import com.plus33.erp.finance.repository.PaymentRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.plus33.erp.analytics.event.ProcurementRefreshEvent;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final SupplierInvoiceRepository supplierInvoiceRepository;
    private final SupplierInvoiceService supplierInvoiceService;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            SupplierInvoiceRepository supplierInvoiceRepository,
            SupplierInvoiceService supplierInvoiceService,
            CompanyRepository companyRepository,
            SupplierRepository supplierRepository,
            AccountRepository accountRepository,
            JournalEntryRepository journalEntryRepository,
            UserRepository userRepository,
            PaymentMapper paymentMapper,
            ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.supplierInvoiceRepository = supplierInvoiceRepository;
        this.supplierInvoiceService = supplierInvoiceService;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.userRepository = userRepository;
        this.paymentMapper = paymentMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        // Validate Company
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.getCompanyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // Validate Supplier
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + request.getSupplierId()));
        if (!supplier.getActive()) {
            throw new BusinessException("Supplier is inactive");
        }
        if (!supplier.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Supplier must belong to the same company");
        }

        // Validate duplicate reference number per company
        if (request.getReferenceNumber() != null && !request.getReferenceNumber().isBlank()) {
            Optional<Payment> dup = paymentRepository.findByCompanyIdAndReferenceNumber(request.getCompanyId(), request.getReferenceNumber());
            if (dup.isPresent()) {
                throw new DuplicateResourceException("Duplicate payment reference number in this company: " + request.getReferenceNumber());
            }
        }

        // Validate total allocations equals payment amount
        BigDecimal totalAllocations = BigDecimal.ZERO;
        for (PaymentAllocationRequest allocReq : request.getAllocations()) {
            totalAllocations = totalAllocations.add(allocReq.getAllocatedAmount());
        }
        if (totalAllocations.compareTo(request.getAmount()) != 0) {
            throw new BusinessException("Total allocated amount (" + totalAllocations + ") must equal payment amount (" + request.getAmount() + ")");
        }

        // Create Payment entity
        Long seqVal = paymentRepository.getNextSequenceValue();
        String paymentNumber = String.format("PAY-%d-%06d", LocalDate.now().getYear(), seqVal);

        Payment payment = Payment.builder()
                .paymentNumber(paymentNumber)
                .company(company)
                .supplier(supplier)
                .paymentDate(request.getPaymentDate())
                .paymentMethod(request.getPaymentMethod().toUpperCase())
                .paymentType("PAYABLE")
                .amount(request.getAmount())
                .referenceNumber(request.getReferenceNumber())
                .currencyCode(request.getCurrencyCode() != null ? request.getCurrencyCode() : "AED")
                .status(PaymentStatus.COMPLETED)
                .createdBy(getCurrentUser())
                .allocations(new ArrayList<>())
                .build();

        // Process allocations
        for (PaymentAllocationRequest allocReq : request.getAllocations()) {
            SupplierInvoice invoice = supplierInvoiceRepository.findById(allocReq.getSupplierInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + allocReq.getSupplierInvoiceId()));

            if (!invoice.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Invoice company does not match payment company");
            }
            if (!invoice.getSupplier().getId().equals(supplier.getId())) {
                throw new BusinessException("Invoice supplier does not match payment supplier");
            }
            if (invoice.getStatus() != SupplierInvoiceStatus.APPROVED && invoice.getStatus() != SupplierInvoiceStatus.PARTIALLY_PAID) {
                throw new BusinessException("Only APPROVED or PARTIALLY_PAID invoices can receive payments. Invoice ID " + invoice.getId() + " is in status " + invoice.getStatus());
            }
            if (allocReq.getAllocatedAmount().compareTo(invoice.getOutstandingBalance()) > 0) {
                throw new BusinessException("Allocation amount " + allocReq.getAllocatedAmount() + " cannot exceed outstanding balance " + invoice.getOutstandingBalance() + " for invoice ID " + invoice.getId());
            }

            // Apply allocation via Invoice Service
            supplierInvoiceService.allocatePayment(invoice.getId(), allocReq.getAllocatedAmount());

            PaymentAllocation allocation = PaymentAllocation.builder()
                    .payment(payment)
                    .supplierInvoice(invoice)
                    .allocatedAmount(allocReq.getAllocatedAmount())
                    .build();

            payment.getAllocations().add(allocation);
        }

        // Generate posted journal entry
        JournalEntry journalEntry = generateJournalEntry(payment);
        journalEntryRepository.save(journalEntry);

        payment.setJournalEntry(journalEntry);
        Payment saved = paymentRepository.save(payment);
        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));
        return paymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));
        return paymentMapper.toResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponse> searchPayments(PaymentSearchRequest searchRequest, Pageable pageable) {
        Specification<Payment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getPaymentNumber() != null && !searchRequest.getPaymentNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("paymentNumber")), "%" + searchRequest.getPaymentNumber().toLowerCase() + "%"));
            }
            if (searchRequest.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.getCompanyId()));
            }
            if (searchRequest.getSupplierId() != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), searchRequest.getSupplierId()));
            }
            if (searchRequest.getPaymentMethod() != null && !searchRequest.getPaymentMethod().isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("paymentMethod")), searchRequest.getPaymentMethod().toUpperCase()));
            }
            if (searchRequest.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
            }
            if (searchRequest.getPaymentDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("paymentDate"), searchRequest.getPaymentDateFrom()));
            }
            if (searchRequest.getPaymentDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("paymentDate"), searchRequest.getPaymentDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Payment> page = paymentRepository.findAll(spec, pageable);
        List<PaymentResponse> content = page.getContent().stream()
                .map(paymentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    @Transactional
    public PaymentResponse cancelPayment(Long id, PaymentCancelRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessException("Only COMPLETED payments can be cancelled. Current status: " + payment.getStatus());
        }

        // 1. Revert allocations
        for (PaymentAllocation allocation : payment.getAllocations()) {
            supplierInvoiceService.deallocatePayment(allocation.getSupplierInvoice().getId(), allocation.getAllocatedAmount());
        }

        // 2. Revert Journal Entry
        if (payment.getJournalEntry() != null) {
            JournalEntry originalJE = payment.getJournalEntry();
            JournalEntry reversalJE = generateReversalJournalEntry(originalJE);
            journalEntryRepository.save(reversalJE);

            originalJE.setReversalEntry(reversalJE);
            journalEntryRepository.save(originalJE);
        }

        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setCancelledAt(LocalDateTime.now());
        payment.setCancelledBy(getCurrentUser());
        payment.setCancellationReason(request.getReason());

        Payment saved = paymentRepository.save(payment);
        eventPublisher.publishEvent(new ProcurementRefreshEvent(this));
        return paymentMapper.toResponse(saved);
    }

    private JournalEntry generateJournalEntry(Payment payment) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", payment.getCompany().getId(), year, nextSeq);

        User currentUser = getCurrentUser();

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(payment.getCompany())
                .entryDate(payment.getPaymentDate())
                .description("Supplier Payment " + payment.getPaymentNumber())
                .sourceModule("SUPPLIER_PAYMENT")
                .sourceReference(payment.getPaymentNumber())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(currentUser)
                .currencyCode(payment.getCurrencyCode())
                .lines(new ArrayList<>())
                .build();

        // Debit: Accounts Payable (2100)
        Account apAccount = accountRepository.findByCompanyIdAndAccountCode(payment.getCompany().getId(), "2100")
                .orElseThrow(() -> new BusinessException("Accounts Payable account (2100) not found in company " + payment.getCompany().getId()));

        JournalEntryLine debitLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(apAccount)
                .debitAmount(payment.getAmount())
                .creditAmount(BigDecimal.ZERO)
                .build();

        je.getLines().add(debitLine);

        // Credit: Cash (1100) or Bank (1200)
        String creditAccountCode = payment.getPaymentMethod().equalsIgnoreCase("CASH") ? "1100" : "1200";
        Account creditAccount = accountRepository.findByCompanyIdAndAccountCode(payment.getCompany().getId(), creditAccountCode)
                .orElseThrow(() -> new BusinessException("Payment account (" + creditAccountCode + ") not found in company " + payment.getCompany().getId()));

        JournalEntryLine creditLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(creditAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(payment.getAmount())
                .build();

        je.getLines().add(creditLine);

        return je;
    }

    private JournalEntry generateReversalJournalEntry(JournalEntry originalJE) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", originalJE.getCompany().getId(), year, nextSeq);

        User currentUser = getCurrentUser();

        JournalEntry reversalJE = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(originalJE.getCompany())
                .entryDate(LocalDate.now())
                .description("Reversal of Journal Entry " + originalJE.getEntryNumber())
                .sourceModule("SUPPLIER_PAYMENT")
                .sourceReference(originalJE.getSourceReference())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(currentUser)
                .currencyCode(originalJE.getCurrencyCode())
                .lines(new ArrayList<>())
                .build();

        for (JournalEntryLine originalLine : originalJE.getLines()) {
            JournalEntryLine reversalLine = JournalEntryLine.builder()
                    .journalEntry(reversalJE)
                    .account(originalLine.getAccount())
                    .debitAmount(originalLine.getCreditAmount())
                    .creditAmount(originalLine.getDebitAmount())
                    .build();

            reversalJE.getLines().add(reversalLine);
        }

        return reversalJE;
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
