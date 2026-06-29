package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.event.CustomerInvoiceRefreshEvent;
import com.plus33.erp.sales.mapper.CustomerInvoiceMapper;
import com.plus33.erp.sales.repository.*;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.finance.tax.service.TaxCalculationEngine;
import com.plus33.erp.finance.tax.service.TaxJournalService;
import com.plus33.erp.finance.tax.dto.TaxCalculationRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationLineRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.finance.tax.dto.TaxCalculationLineResult;
import com.plus33.erp.finance.tax.dto.TaxComponentResult;
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
import java.util.*;

@Service
@Transactional(readOnly = true)
public class CustomerInvoiceServiceImpl implements CustomerInvoiceService {

    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final CustomerInvoiceItemRepository customerInvoiceItemRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ProductRepository productRepository;
    private final PickListItemRepository pickListItemRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final CustomerInvoiceMapper customerInvoiceMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final TaxCalculationEngine taxCalculationEngine;
    private final TaxJournalService taxJournalService;

    public CustomerInvoiceServiceImpl(
            CustomerInvoiceRepository customerInvoiceRepository,
            CustomerInvoiceItemRepository customerInvoiceItemRepository,
            CompanyRepository companyRepository,
            CustomerRepository customerRepository,
            SalesOrderRepository salesOrderRepository,
            SalesOrderItemRepository salesOrderItemRepository,
            ProductRepository productRepository,
            PickListItemRepository pickListItemRepository,
            AccountRepository accountRepository,
            JournalEntryRepository journalEntryRepository,
            UserRepository userRepository,
            CustomerInvoiceMapper customerInvoiceMapper,
            ApplicationEventPublisher eventPublisher,
            TaxCalculationEngine taxCalculationEngine,
            TaxJournalService taxJournalService) {
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.customerInvoiceItemRepository = customerInvoiceItemRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.productRepository = productRepository;
        this.pickListItemRepository = pickListItemRepository;
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.userRepository = userRepository;
        this.customerInvoiceMapper = customerInvoiceMapper;
        this.eventPublisher = eventPublisher;
        this.taxCalculationEngine = taxCalculationEngine;
        this.taxJournalService = taxJournalService;
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse createInvoice(CustomerInvoiceRequest request) {
        // Validate Date range
        if (request.dueDate() != null && request.dueDate().isBefore(request.invoiceDate())) {
            throw new BusinessException("Due date cannot be before invoice date");
        }

        // Validate Company
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // Validate Customer
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.customerId()));
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Customer is inactive");
        }
        if (!customer.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Customer company does not match invoice company");
        }

        // Idempotency support
        Optional<CustomerInvoice> existing = customerInvoiceRepository.findByCompanyIdAndClientReferenceId(company.getId(), request.clientReferenceId());
        if (existing.isPresent()) {
            return customerInvoiceMapper.toResponse(existing.get());
        }

        // Validate Sales Order
        SalesOrder salesOrder = null;
        if (request.salesOrderId() != null) {
            salesOrder = salesOrderRepository.findById(request.salesOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found with ID: " + request.salesOrderId()));
            if (!salesOrder.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Invoice company does not match sales order company");
            }
            if (!salesOrder.getCustomer().getId().equals(customer.getId())) {
                throw new BusinessException("Invoice customer does not match sales order customer");
            }
        }

        // Generate Invoice Number
        Long seqVal = customerInvoiceRepository.getNextSequenceValue();
        String invoiceNumber = String.format("INV-%d-%06d", LocalDate.now().getYear(), seqVal);

        CustomerInvoice invoice = CustomerInvoice.builder()
                .company(company)
                .customer(customer)
                .salesOrder(salesOrder)
                .invoiceNumber(invoiceNumber)
                .clientReferenceId(request.clientReferenceId())
                .invoiceDate(request.invoiceDate())
                .dueDate(request.dueDate())
                .currencyCode(request.currencyCode() != null ? request.currencyCode() : "AED")
                .status(CustomerInvoiceStatus.DRAFT)
                .createdBy(getCurrentUser())
                .build();

        List<CustomerInvoiceItem> items = processItems(request.items(), invoice, request.salesOrderId());
        invoice.setItems(items);

        calculateTotals(invoice);

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse updateInvoice(Long id, CustomerInvoiceUpdateRequest request) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.DRAFT) {
            throw new BusinessException("Only DRAFT invoices can be updated");
        }

        if (request.dueDate() != null && request.dueDate().isBefore(request.invoiceDate())) {
            throw new BusinessException("Due date cannot be before invoice date");
        }

        invoice.setInvoiceDate(request.invoiceDate());
        invoice.setDueDate(request.dueDate());
        invoice.setCurrencyCode(request.currencyCode() != null ? request.currencyCode() : "AED");

        // Clear existing and add new items
        invoice.getItems().clear();
        List<CustomerInvoiceItem> items = processItems(request.items(), invoice, invoice.getSalesOrder() != null ? invoice.getSalesOrder().getId() : null);
        invoice.getItems().addAll(items);

        calculateTotals(invoice);

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    public CustomerInvoiceResponse getInvoiceById(Long id) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));
        return customerInvoiceMapper.toResponse(invoice);
    }

    @Override
    public PageResponse<CustomerInvoiceResponse> searchInvoices(CustomerInvoiceSearchRequest searchRequest, Pageable pageable) {
        Specification<CustomerInvoice> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getInvoiceNumber() != null && !searchRequest.getInvoiceNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("invoiceNumber")), "%" + searchRequest.getInvoiceNumber().toLowerCase() + "%"));
            }
            if (searchRequest.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.getCompanyId()));
            }
            if (searchRequest.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), searchRequest.getCustomerId()));
            }
            if (searchRequest.getSalesOrderId() != null) {
                predicates.add(cb.equal(root.get("salesOrder").get("id"), searchRequest.getSalesOrderId()));
            }
            if (searchRequest.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
            }
            if (searchRequest.getInvoiceDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("invoiceDate"), searchRequest.getInvoiceDateFrom()));
            }
            if (searchRequest.getInvoiceDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("invoiceDate"), searchRequest.getInvoiceDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<CustomerInvoice> page = customerInvoiceRepository.findAll(spec, pageable);
        List<CustomerInvoiceResponse> content = page.getContent().stream()
                .map(customerInvoiceMapper::toResponse)
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
    public CustomerInvoiceResponse submitInvoice(Long id) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.DRAFT) {
            throw new BusinessException("Only DRAFT invoices can be submitted");
        }

        invoice.setStatus(CustomerInvoiceStatus.SUBMITTED);
        invoice.setSubmittedBy(getCurrentUser());
        invoice.setSubmittedAt(LocalDateTime.now());

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse approveInvoice(Long id) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED invoices can be approved");
        }

        if (invoice.getJournalEntry() != null) {
            throw new BusinessException("Invoice is already linked to a journal entry");
        }

        // Validate Accounts Existence
        Long companyId = invoice.getCompany().getId();
        accountRepository.findByCompanyIdAndAccountCode(companyId, "1400")
                .orElseThrow(() -> new BusinessException("Required Accounts Receivable account (1400) is missing for this company"));
        accountRepository.findByCompanyIdAndAccountCode(companyId, "4000")
                .orElseThrow(() -> new BusinessException("Required Revenue account (4000) is missing for this company"));
        if (invoice.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            accountRepository.findByCompanyIdAndAccountCode(companyId, "2200")
                    .orElseThrow(() -> new BusinessException("Required Tax Payable account (2200) is missing for this company"));
        }

        // Re-validate quantities and increment invoiced quantities on Sales Order items
        for (CustomerInvoiceItem item : invoice.getItems()) {
            if (item.getSalesOrderItem() != null) {
                SalesOrderItem soItem = item.getSalesOrderItem();
                BigDecimal remaining = soItem.getFulfilledQuantity().subtract(soItem.getInvoicedQuantity());
                if (item.getQuantity().compareTo(remaining) > 0) {
                    throw new BusinessException("Invoiced quantity " + item.getQuantity() 
                            + " exceeds remaining fulfillable quantity " + remaining 
                            + " for product: " + item.getProduct().getName());
                }
                soItem.setInvoicedQuantity(soItem.getInvoicedQuantity().add(item.getQuantity()));
                salesOrderItemRepository.save(soItem);
            }
        }

        // Generate and post Journal Entry
        JournalEntry journalEntry = generateJournalEntry(invoice);
        journalEntryRepository.save(journalEntry);
        invoice.setJournalEntry(journalEntry);

        // Adjust Customer Outstanding Balance if Ad-hoc Invoice
        if (invoice.getSalesOrder() == null) {
            Customer customer = invoice.getCustomer();
            customer.setOutstandingBalance(customer.getOutstandingBalance().add(invoice.getTotalAmount()));
            customerRepository.save(customer);
        }

        invoice.setStatus(CustomerInvoiceStatus.APPROVED);
        invoice.setApprovedBy(getCurrentUser());
        invoice.setApprovedAt(LocalDateTime.now());

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new CustomerInvoiceRefreshEvent(this));
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse cancelInvoice(Long id, String reason) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() == CustomerInvoiceStatus.PAID) {
            throw new BusinessException("PAID invoices cannot be cancelled");
        }
        if (invoice.getStatus() == CustomerInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("PARTIALLY_PAID invoices require deallocation of all payments before cancellation");
        }
        if (invoice.getStatus() != CustomerInvoiceStatus.APPROVED) {
            throw new BusinessException("Only APPROVED invoices can be cancelled");
        }

        // Revert invoiced quantities on Sales Order items
        for (CustomerInvoiceItem item : invoice.getItems()) {
            if (item.getSalesOrderItem() != null) {
                SalesOrderItem soItem = item.getSalesOrderItem();
                soItem.setInvoicedQuantity(soItem.getInvoicedQuantity().subtract(item.getQuantity()));
                salesOrderItemRepository.save(soItem);
            }
        }

        // Revert Customer Outstanding Balance if Ad-hoc
        if (invoice.getSalesOrder() == null) {
            Customer customer = invoice.getCustomer();
            customer.setOutstandingBalance(customer.getOutstandingBalance().subtract(invoice.getTotalAmount()));
            customerRepository.save(customer);
        }

        // Generate Reversing Journal Entry
        if (invoice.getJournalEntry() != null) {
            JournalEntry originalJE = invoice.getJournalEntry();
            JournalEntry reversalJE = generateReversalJournalEntry(originalJE);
            journalEntryRepository.save(reversalJE);

            originalJE.setReversalEntry(reversalJE);
            journalEntryRepository.save(originalJE);
        }

        invoice.setStatus(CustomerInvoiceStatus.CANCELLED);
        invoice.setCancelledBy(getCurrentUser());
        invoice.setCancelledAt(LocalDateTime.now());
        invoice.setCancellationReason(reason);

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new CustomerInvoiceRefreshEvent(this));
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse voidInvoice(Long id) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.DRAFT && invoice.getStatus() != CustomerInvoiceStatus.SUBMITTED) {
            throw new BusinessException("Only DRAFT or SUBMITTED invoices can be voided");
        }

        invoice.setStatus(CustomerInvoiceStatus.VOID);
        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse allocatePayment(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Allocation amount must be greater than zero");
        }

        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.APPROVED && invoice.getStatus() != CustomerInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("Payments can only be allocated to APPROVED or PARTIALLY_PAID invoices");
        }

        if (amount.compareTo(invoice.getOutstandingBalance()) > 0) {
            throw new BusinessException("Allocation amount " + amount + " cannot exceed outstanding balance " + invoice.getOutstandingBalance());
        }

        invoice.setPaidAmount(invoice.getPaidAmount().add(amount).setScale(2, RoundingMode.HALF_UP));
        invoice.setOutstandingBalance(invoice.getOutstandingBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));

        if (invoice.getOutstandingBalance().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(CustomerInvoiceStatus.PAID);
        } else {
            invoice.setStatus(CustomerInvoiceStatus.PARTIALLY_PAID);
        }

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new CustomerInvoiceRefreshEvent(this));
        return customerInvoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CustomerInvoiceResponse deallocatePayment(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deallocation amount must be greater than zero");
        }

        CustomerInvoice invoice = customerInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Invoice not found with ID: " + id));

        if (invoice.getStatus() != CustomerInvoiceStatus.PAID && invoice.getStatus() != CustomerInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("Payments can only be deallocated from PAID or PARTIALLY_PAID invoices");
        }

        if (amount.compareTo(invoice.getPaidAmount()) > 0) {
            throw new BusinessException("Deallocation amount " + amount + " cannot exceed paid amount " + invoice.getPaidAmount());
        }

        invoice.setPaidAmount(invoice.getPaidAmount().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        invoice.setOutstandingBalance(invoice.getOutstandingBalance().add(amount).setScale(2, RoundingMode.HALF_UP));

        if (invoice.getOutstandingBalance().compareTo(invoice.getTotalAmount()) == 0) {
            invoice.setStatus(CustomerInvoiceStatus.APPROVED);
        } else {
            invoice.setStatus(CustomerInvoiceStatus.PARTIALLY_PAID);
        }

        CustomerInvoice saved = customerInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new CustomerInvoiceRefreshEvent(this));
        return customerInvoiceMapper.toResponse(saved);
    }

    private List<CustomerInvoiceItem> processItems(List<CustomerInvoiceItemRequest> requests, CustomerInvoice invoice, Long salesOrderId) {
        if (requests == null || requests.isEmpty()) {
            throw new BusinessException("Customer invoice must contain at least one item");
        }

        List<CustomerInvoiceItem> items = new ArrayList<>();
        Set<Long> productIds = new HashSet<>();

        List<TaxCalculationLineRequest> taxLineReqs = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            CustomerInvoiceItemRequest req = requests.get(i);
            BigDecimal lineSubtotal = req.quantity().multiply(req.unitPrice()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineDiscount = lineSubtotal.multiply(req.discountPercentage() != null ? req.discountPercentage() : BigDecimal.ZERO)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineNet = lineSubtotal.subtract(lineDiscount);

            Product product = productRepository.findById(req.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + req.productId()));

            taxLineReqs.add(TaxCalculationLineRequest.builder()
                    .lineId((long) i)
                    .productTaxCategory(product.getCategory() != null ? product.getCategory().getCode() : "STANDARD")
                    .amount(lineNet)
                    .taxInclusive(false)
                    .build());
        }

        TaxCalculationRequest taxReq = TaxCalculationRequest.builder()
                .companyId(invoice.getCompany().getId())
                .transactionDate(invoice.getInvoiceDate())
                .documentType("SALES_INVOICE")
                .customerId(invoice.getCustomer().getId())
                .customerTaxProfile(invoice.getCustomer().getTaxProfile() != null ? invoice.getCustomer().getTaxProfile().name() : "STANDARD")
                .lines(taxLineReqs)
                .build();

        TaxCalculationResult taxResult = null;
        try {
            taxResult = taxCalculationEngine.calculateTax(taxReq);
        } catch (Exception e) {
            // Fall back to manual calculation if rules or engine are not configured
        }

        for (int i = 0; i < requests.size(); i++) {
            CustomerInvoiceItemRequest req = requests.get(i);
            if (!productIds.add(req.productId())) {
                throw new BusinessException("Duplicate product lines detected in invoice: " + req.productId());
            }

            Product product = productRepository.findById(req.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + req.productId()));

            SalesOrderItem soItem = null;
            if (req.salesOrderItemId() != null) {
                soItem = salesOrderItemRepository.findById(req.salesOrderItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sales Order Item not found with ID: " + req.salesOrderItemId()));

                if (salesOrderId != null && !soItem.getSalesOrder().getId().equals(salesOrderId)) {
                    throw new BusinessException("Sales Order Item does not belong to the selected Sales Order");
                }
                if (!soItem.getProduct().getId().equals(product.getId())) {
                    throw new BusinessException("Sales Order Item product does not match requested product");
                }
            }

            PickListItem pickItem = null;
            if (req.pickListItemId() != null) {
                pickItem = pickListItemRepository.findById(req.pickListItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Pick List Item not found with ID: " + req.pickListItemId()));
                if (!pickItem.getProduct().getId().equals(product.getId())) {
                    throw new BusinessException("Pick List Item product does not match requested product");
                }
            }

            BigDecimal qty = req.quantity();
            BigDecimal price = req.unitPrice();
            BigDecimal discPct = req.discountPercentage() != null ? req.discountPercentage() : BigDecimal.ZERO;

            BigDecimal lineSubtotal = qty.multiply(price).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineDiscount = lineSubtotal.multiply(discPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineNet = lineSubtotal.subtract(lineDiscount);

            BigDecimal lineTax;
            BigDecimal taxPct;
            if (taxResult != null) {
                TaxCalculationLineResult lineTaxRes = taxResult.getLines().get(i);
                lineTax = lineTaxRes.getTaxAmount();
                if (!lineTaxRes.getTaxComponents().isEmpty()) {
                    taxPct = lineTaxRes.getTaxComponents().get(0).getRatePercent();
                } else {
                    taxPct = BigDecimal.ZERO;
                }
            } else {
                taxPct = req.taxPercentage() != null ? req.taxPercentage() : BigDecimal.ZERO;
                lineTax = lineNet.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }
            BigDecimal lineTotal = lineNet.add(lineTax).setScale(2, RoundingMode.HALF_UP);

            if (soItem != null) {
                BigDecimal remaining = soItem.getFulfilledQuantity().subtract(soItem.getInvoicedQuantity());
                if (qty.compareTo(remaining) > 0) {
                    throw new BusinessException("Invoiced quantity " + qty + " exceeds remaining fulfillable quantity " + remaining + " for product: " + product.getName());
                }
            }

            CustomerInvoiceItem item = CustomerInvoiceItem.builder()
                    .customerInvoice(invoice)
                    .salesOrderItem(soItem)
                    .pickListItem(pickItem)
                    .product(product)
                    .quantity(qty)
                    .unitPrice(price)
                    .discountPercentage(discPct)
                    .taxPercentage(taxPct)
                    .netAmount(lineSubtotal)
                    .discountAmount(lineDiscount)
                    .taxAmount(lineTax)
                    .totalAmount(lineTotal)
                    .build();

            items.add(item);
        }

        return items;
    }

    private void calculateTotals(CustomerInvoice invoice) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (CustomerInvoiceItem item : invoice.getItems()) {
            subtotal = subtotal.add(item.getNetAmount());
            tax = tax.add(item.getTaxAmount());
            discount = discount.add(item.getDiscountAmount());
            total = total.add(item.getTotalAmount());
        }

        invoice.setSubtotalAmount(subtotal.setScale(2, RoundingMode.HALF_UP));
        invoice.setTaxAmount(tax.setScale(2, RoundingMode.HALF_UP));
        invoice.setDiscountAmount(discount.setScale(2, RoundingMode.HALF_UP));
        invoice.setTotalAmount(total.setScale(2, RoundingMode.HALF_UP));
        invoice.setOutstandingBalance(total.subtract(invoice.getPaidAmount()).setScale(2, RoundingMode.HALF_UP));
    }

    private JournalEntry generateJournalEntry(CustomerInvoice invoice) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", invoice.getCompany().getId(), year, nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(invoice.getCompany())
                .entryDate(invoice.getInvoiceDate())
                .description("Customer Invoice " + invoice.getInvoiceNumber())
                .sourceModule("CUSTOMER_INVOICE")
                .sourceReference(invoice.getId().toString())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(getCurrentUser())
                .currencyCode(invoice.getCurrencyCode())
                .lines(new ArrayList<>())
                .build();

        // Debit: Accounts Receivable (1400)
        Account arAccount = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), "1400")
                .orElseThrow(() -> new BusinessException("Accounts Receivable account (1400) not found"));

        JournalEntryLine arLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(arAccount)
                .debitAmount(invoice.getTotalAmount())
                .creditAmount(BigDecimal.ZERO)
                .build();
        je.getLines().add(arLine);

        // Credit: Revenue (4000)
        Account revAccount = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), "4000")
                .orElseThrow(() -> new BusinessException("Revenue account (4000) not found"));

        BigDecimal revAmount = invoice.getSubtotalAmount().subtract(invoice.getDiscountAmount());
        JournalEntryLine revLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(revAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(revAmount)
                .build();
        je.getLines().add(revLine);

        // Delegate tax postings to centralized TaxJournalService
        TaxCalculationRequest taxReq = TaxCalculationRequest.builder()
                .companyId(invoice.getCompany().getId())
                .transactionDate(invoice.getInvoiceDate())
                .documentType("SALES_INVOICE")
                .documentId(invoice.getId())
                .customerId(invoice.getCustomer().getId())
                .customerTaxProfile(invoice.getCustomer().getTaxProfile() != null ? invoice.getCustomer().getTaxProfile().name() : "STANDARD")
                .lines(invoice.getItems().stream().map(item -> {
                    BigDecimal lineSubtotal = item.getQuantity().multiply(item.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal lineNet = lineSubtotal.subtract(item.getDiscountAmount());
                    return TaxCalculationLineRequest.builder()
                            .lineId(item.getId())
                            .productTaxCategory(item.getProduct().getCategory() != null ? item.getProduct().getCategory().getCode() : "STANDARD")
                            .amount(lineNet)
                            .taxInclusive(false)
                            .build();
                }).toList())
                .build();

        TaxCalculationResult taxResult = null;
        try {
            taxResult = taxCalculationEngine.calculateTax(taxReq);
        } catch (Exception e) {
            // Fall back to manual calculation if rules or engine are not configured
        }

        if (taxResult != null) {
            taxJournalService.createTaxJournalLines(je, invoice.getCompany(), taxResult, false);
        } else {
            // Fallback to hardcoded 2200 credit
            if (invoice.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
                Account taxAccount = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), "2200")
                        .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));
                JournalEntryLine taxLine = JournalEntryLine.builder()
                        .journalEntry(je)
                        .account(taxAccount)
                        .debitAmount(BigDecimal.ZERO)
                        .creditAmount(invoice.getTaxAmount())
                        .build();
                je.getLines().add(taxLine);
            }
        }

        return je;
    }

    private JournalEntry generateReversalJournalEntry(JournalEntry originalJE) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", originalJE.getCompany().getId(), year, nextSeq);

        JournalEntry reversalJE = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(originalJE.getCompany())
                .entryDate(LocalDate.now())
                .description("Reversal of Journal Entry " + originalJE.getEntryNumber())
                .sourceModule("CUSTOMER_INVOICE")
                .sourceReference(originalJE.getSourceReference())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(getCurrentUser())
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
