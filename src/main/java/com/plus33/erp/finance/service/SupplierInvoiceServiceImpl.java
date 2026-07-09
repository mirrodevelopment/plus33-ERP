/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : SupplierInvoiceServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceServiceImpl
 * Related Repository: SupplierInvoiceRepository, CompanyRepository, SupplierRepository, PurchaseOrderRepository, PurchaseOrderItemRepository, GoodsReceiptItemRepository, AccountRepository, JournalEntryRepository, UserRepository, BudgetDimensionSetRepository
 * Related Entity    : SupplierInvoice
 * Related DTO       : BudgetDimensionSetRequest, BudgetReservationRequest, PageResponse, searchRequest, SupplierInvoiceItemRequest
 * Related Mapper    : SupplierInvoiceMapper
 * Related DB Table  : supplier_invoices
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Organization Module, Procurement Module, Security Module
 * Used By           : SupplierInvoiceController, SupplierInvoiceServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements SupplierInvoiceService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.mapper.SupplierInvoiceMapper;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceItemRepository;
import com.plus33.erp.finance.repository.SupplierInvoiceRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.GoodsReceiptItemRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderItemRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
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
import com.plus33.erp.finance.event.SupplierInvoiceRefreshEvent;
import com.plus33.erp.finance.tax.service.TaxCalculationEngine;
import com.plus33.erp.finance.tax.service.TaxJournalService;
import com.plus33.erp.finance.tax.dto.TaxCalculationRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationLineRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.finance.tax.dto.TaxCalculationLineResult;
import com.plus33.erp.finance.tax.dto.TaxComponentResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.plus33.erp.finance.budget.repository.BudgetDimensionSetRepository;
import com.plus33.erp.finance.budget.service.BudgetService;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SupplierInvoiceController
 *   --> SupplierInvoiceServiceImpl (this)
 *   --> Validate business rules
 *   --> SupplierInvoiceRepository (read/write 'supplier_invoices')
 *   --> SupplierInvoiceMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code supplier_invoices}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Inventory, Organization, Procurement, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SupplierInvoiceServiceImpl implements SupplierInvoiceService {

    private final SupplierInvoiceRepository supplierInvoiceRepository;
    private final CompanyRepository companyRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final GoodsReceiptItemRepository goodsReceiptItemRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final SupplierInvoiceMapper supplierInvoiceMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final BudgetDimensionSetRepository budgetDimensionSetRepository;
    private final BudgetService budgetService;
    private final TaxCalculationEngine taxCalculationEngine;
    private final TaxJournalService taxJournalService;

    public SupplierInvoiceServiceImpl(
            SupplierInvoiceRepository supplierInvoiceRepository,
            CompanyRepository companyRepository,
            SupplierRepository supplierRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderItemRepository purchaseOrderItemRepository,
            GoodsReceiptItemRepository goodsReceiptItemRepository,
            AccountRepository accountRepository,
            JournalEntryRepository journalEntryRepository,
            UserRepository userRepository,
            SupplierInvoiceMapper supplierInvoiceMapper,
            ApplicationEventPublisher eventPublisher,
            BudgetDimensionSetRepository budgetDimensionSetRepository,
            BudgetService budgetService,
            TaxCalculationEngine taxCalculationEngine,
            TaxJournalService taxJournalService) {
        this.supplierInvoiceRepository = supplierInvoiceRepository;
        this.companyRepository = companyRepository;
        this.supplierRepository = supplierRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.goodsReceiptItemRepository = goodsReceiptItemRepository;
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.userRepository = userRepository;
        this.supplierInvoiceMapper = supplierInvoiceMapper;
        this.eventPublisher = eventPublisher;
        this.budgetDimensionSetRepository = budgetDimensionSetRepository;
        this.budgetService = budgetService;
        this.taxCalculationEngine = taxCalculationEngine;
        this.taxJournalService = taxJournalService;
    }

    /**
     * Creates a new invoice and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the SupplierInvoiceResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse createInvoice(SupplierInvoiceRequest request) {
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

        // Validate unique invoice number per supplier
        Optional<SupplierInvoice> existing = supplierInvoiceRepository.findBySupplierIdAndInvoiceNumber(request.getSupplierId(), request.getInvoiceNumber());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Duplicate invoice number for this supplier: " + request.getInvoiceNumber());
        }

        // Validate Purchase Order
        PurchaseOrder purchaseOrder = null;
        if (request.getPurchaseOrderId() != null) {
            purchaseOrder = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order not found with ID: " + request.getPurchaseOrderId()));
            if (!purchaseOrder.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Invoice company does not match purchase order company");
            }
            if (!purchaseOrder.getSupplier().getId().equals(supplier.getId())) {
                throw new BusinessException("Invoice supplier does not match purchase order supplier");
            }
        }

        SupplierInvoice invoice = SupplierInvoice.builder()
                .company(company)
                .supplier(supplier)
                .purchaseOrder(purchaseOrder)
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .dueDate(request.getDueDate())
                .currencyCode(request.getCurrencyCode() != null ? request.getCurrencyCode() : "AED")
                .status(SupplierInvoiceStatus.DRAFT)
                .build();

        List<SupplierInvoiceItem> items = processItems(request.getItems(), invoice, request.getPurchaseOrderId());
        invoice.setItems(items);

        calculateTotals(invoice);

        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Updates an existing invoice record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the SupplierInvoiceResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse updateInvoice(Long id, SupplierInvoiceUpdateRequest request) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.DRAFT) {
            throw new BusinessException("Only DRAFT invoices can be updated. Current status: " + invoice.getStatus());
        }

        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setCurrencyCode(request.getCurrencyCode() != null ? request.getCurrencyCode() : "AED");

        // Clear existing items and process new ones.
        // The uk_supplier_invoice_item_po_item unique constraint is DEFERRABLE INITIALLY DEFERRED
        // (see migration V41), so Hibernate can INSERT new items before orphan-removal DELETEs
        // are flushed within the same transaction without violating the constraint.
        invoice.getItems().clear();
        List<SupplierInvoiceItem> items = processItems(request.getItems(), invoice, invoice.getPurchaseOrder() != null ? invoice.getPurchaseOrder().getId() : null);
        invoice.getItems().addAll(items);

        calculateTotals(invoice);

        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Retrieves a single invoice by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single invoice by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public SupplierInvoiceResponse getInvoiceById(Long id) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));
        return supplierInvoiceMapper.toResponse(invoice);
    }

    /**
     * Returns a filtered paginated list of invoices records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of invoices records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<SupplierInvoiceResponse> searchInvoices(SupplierInvoiceSearchRequest searchRequest, Pageable pageable) {
        Specification<SupplierInvoice> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getInvoiceNumber() != null && !searchRequest.getInvoiceNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("invoiceNumber")), "%" + searchRequest.getInvoiceNumber().toLowerCase() + "%"));
            }
            if (searchRequest.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.getCompanyId()));
            }
            if (searchRequest.getSupplierId() != null) {
                predicates.add(cb.equal(root.get("supplier").get("id"), searchRequest.getSupplierId()));
            }
            if (searchRequest.getPurchaseOrderId() != null) {
                predicates.add(cb.equal(root.get("purchaseOrder").get("id"), searchRequest.getPurchaseOrderId()));
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

        Page<SupplierInvoice> page = supplierInvoiceRepository.findAll(spec, pageable);
        List<SupplierInvoiceResponse> content = page.getContent().stream()
                .map(supplierInvoiceMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /**
     * Approves the invoice, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse approveInvoice(Long id) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED invoices can be approved. Current status: " + invoice.getStatus());
        }

        // 1. Re-validate quantity checks and update PO invoiced quantities
        for (SupplierInvoiceItem item : invoice.getItems()) {
            PurchaseOrderItem poItem = item.getPurchaseOrderItem();
            BigDecimal remaining = poItem.getReceivedQuantity().subtract(poItem.getInvoicedQuantity());
            if (item.getQuantity().compareTo(remaining) > 0) {
                throw new BusinessException("Invoice quantity " + item.getQuantity()
                        + " exceeds remaining received quantity " + remaining
                        + " for product: " + poItem.getProduct().getName());
            }
            poItem.setInvoicedQuantity(poItem.getInvoicedQuantity().add(item.getQuantity()));
            purchaseOrderItemRepository.save(poItem);
        }

        // 2. Generate and post Journal Entry
        JournalEntry journalEntry = generateJournalEntry(invoice);
        journalEntryRepository.save(journalEntry);

        invoice.setJournalEntry(journalEntry);
        invoice.setStatus(SupplierInvoiceStatus.APPROVED);

        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);

        // Transition PO reservations to actual consumptions
        for (SupplierInvoiceItem item : saved.getItems()) {
            if (item.getDimensionSet() != null && item.getPurchaseOrderItem() != null) {
                try {
                    budgetService.consumeReservation(
                        saved.getCompany().getId(),
                        "PROCUREMENT_PO",
                        item.getPurchaseOrderItem().getId(),
                        item.getTotalAmount(),
                        saved.getInvoiceNumber()
                    );
                } catch (Exception e) {
                    log.error("Failed to transition budget reservation to consumption for PO item ID: {}", item.getPurchaseOrderItem().getId(), e);
                    throw e;
                }
            }
        }

        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Cancels the invoice and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse cancelInvoice(Long id) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() == SupplierInvoiceStatus.CANCELLED) {
            throw new BusinessException("Supplier Invoice is already cancelled");
        }

        if (invoice.getStatus() == SupplierInvoiceStatus.PAID || invoice.getStatus() == SupplierInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("Cannot cancel an invoice with allocated payments. Paid amount: " + invoice.getPaidAmount());
        }

        if (invoice.getStatus() == SupplierInvoiceStatus.APPROVED) {
            // Revert invoiced quantities on PO items
            for (SupplierInvoiceItem item : invoice.getItems()) {
                PurchaseOrderItem poItem = item.getPurchaseOrderItem();
                poItem.setInvoicedQuantity(poItem.getInvoicedQuantity().subtract(item.getQuantity()));
                purchaseOrderItemRepository.save(poItem);
            }

            // Generate reversing journal entry
            if (invoice.getJournalEntry() != null) {
                JournalEntry originalJE = invoice.getJournalEntry();
                JournalEntry reversalJE = generateReversalJournalEntry(originalJE);
                journalEntryRepository.save(reversalJE);

                originalJE.setReversalEntry(reversalJE);
                journalEntryRepository.save(originalJE);
            }

            // Revert consumptions and restore PO reservations
            for (SupplierInvoiceItem item : invoice.getItems()) {
                if (item.getDimensionSet() != null && item.getPurchaseOrderItem() != null) {
                    try {
                        budgetService.releaseConsumption("PROCUREMENT_PO", item.getPurchaseOrderItem().getId());

                        // Recreate PO reservation
                        PurchaseOrderItem poItem = item.getPurchaseOrderItem();
                        String accountCode = isInventoryProduct(poItem.getProduct()) ? "1300" : "5200";
                        Account account = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), accountCode)
                            .orElseThrow(() -> new BusinessException("Account not found: " + accountCode));

                        BigDecimal amount = poItem.getOrderedQuantity().multiply(poItem.getUnitPrice());

                        com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest dimReq = new com.plus33.erp.finance.budget.dto.BudgetDimensionSetRequest(
                            poItem.getDimensionSet().getDepartment() != null ? poItem.getDimensionSet().getDepartment().getId() : null,
                            poItem.getDimensionSet().getCostCenter() != null ? poItem.getDimensionSet().getCostCenter().getId() : null,
                            poItem.getDimensionSet().getProject() != null ? poItem.getDimensionSet().getProject().getId() : null,
                            poItem.getDimensionSet().getWarehouse() != null ? poItem.getDimensionSet().getWarehouse().getId() : null,
                            poItem.getDimensionSet().getAssetCategory() != null ? poItem.getDimensionSet().getAssetCategory().getId() : null,
                            poItem.getDimensionSet().getRegion() != null ? poItem.getDimensionSet().getRegion().getId() : null,
                            poItem.getDimensionSet().getStore() != null ? poItem.getDimensionSet().getStore().getId() : null
                        );

                        com.plus33.erp.finance.budget.dto.BudgetReservationRequest resReq = new com.plus33.erp.finance.budget.dto.BudgetReservationRequest(
                            account.getId(),
                            dimReq,
                            LocalDate.now(),
                            amount,
                            "PROCUREMENT_PO",
                            poItem.getId(),
                            poItem.getPurchaseOrder().getOrderNumber(),
                            LocalDate.now().plusDays(30)
                        );
                        budgetService.createReservation(invoice.getCompany().getId(), resReq);
                    } catch (Exception e) {
                        log.warn("Failed to revert consumption / restore PO reservation for item: {}", item.getId(), e);
                    }
                }
            }
        }

        invoice.setStatus(SupplierInvoiceStatus.CANCELLED);
        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Reserves payment resources (budget or stock) for downstream processing.
     *
     * @param id the unique database ID of the resource
     * @param amount the amount input value
     * @return the SupplierInvoiceResponse result
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse allocatePayment(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Allocation amount must be greater than zero");
        }

        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.APPROVED && invoice.getStatus() != SupplierInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("Payments can only be allocated to APPROVED or PARTIALLY_PAID invoices");
        }

        if (amount.compareTo(invoice.getOutstandingBalance()) > 0) {
            throw new BusinessException("Payment amount cannot exceed outstanding balance. Outstanding: " + invoice.getOutstandingBalance() + ", Allocation request: " + amount);
        }

        invoice.setPaidAmount(invoice.getPaidAmount().add(amount).setScale(2, RoundingMode.HALF_UP));
        invoice.setOutstandingBalance(invoice.getOutstandingBalance().subtract(amount).setScale(2, RoundingMode.HALF_UP));

        if (invoice.getOutstandingBalance().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(SupplierInvoiceStatus.PAID);
        } else {
            invoice.setStatus(SupplierInvoiceStatus.PARTIALLY_PAID);
        }

        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Performs the deallocatePayment operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param amount the amount input value
     * @return the SupplierInvoiceResponse result
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse deallocatePayment(Long id, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Deallocation amount must be greater than zero");
        }

        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.PAID && invoice.getStatus() != SupplierInvoiceStatus.PARTIALLY_PAID) {
            throw new BusinessException("Payments can only be deallocated from PAID or PARTIALLY_PAID invoices");
        }

        if (amount.compareTo(invoice.getPaidAmount()) > 0) {
            throw new BusinessException("Deallocation amount cannot exceed paid amount. Paid amount: " + invoice.getPaidAmount() + ", Deallocation request: " + amount);
        }

        invoice.setPaidAmount(invoice.getPaidAmount().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        invoice.setOutstandingBalance(invoice.getOutstandingBalance().add(amount).setScale(2, RoundingMode.HALF_UP));

        if (invoice.getOutstandingBalance().compareTo(invoice.getTotalAmount()) == 0) {
            invoice.setStatus(SupplierInvoiceStatus.APPROVED);
        } else {
            invoice.setStatus(SupplierInvoiceStatus.PARTIALLY_PAID);
        }

        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    private List<SupplierInvoiceItem> processItems(List<SupplierInvoiceItemRequest> itemRequests, SupplierInvoice invoice, Long purchaseOrderId) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new BusinessException("Supplier invoice must contain at least one item");
        }

        List<SupplierInvoiceItem> items = new ArrayList<>();
        Set<Long> poItemIds = new HashSet<>();

        List<TaxCalculationLineRequest> taxLineReqs = new ArrayList<>();
        for (int i = 0; i < itemRequests.size(); i++) {
            SupplierInvoiceItemRequest req = itemRequests.get(i);

            PurchaseOrderItem poItem = purchaseOrderItemRepository.findById(req.getPurchaseOrderItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Item not found with ID: " + req.getPurchaseOrderItemId()));

            BigDecimal lineSubtotal = req.getQuantity().multiply(req.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineNet = lineSubtotal.subtract(req.getDiscountAmount() != null ? req.getDiscountAmount() : BigDecimal.ZERO);

            Product product = poItem.getProduct();

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
                .documentType("PURCHASE_INVOICE")
                .supplierId(invoice.getSupplier().getId())
                .lines(taxLineReqs)
                .build();

        try {
            taxCalculationEngine.calculateTax(taxReq);
        } catch (Exception e) {
            // Fall back to manual calculation if rules or engine are not configured
        }

        for (int i = 0; i < itemRequests.size(); i++) {
            SupplierInvoiceItemRequest itemReq = itemRequests.get(i);
            if (!poItemIds.add(itemReq.getPurchaseOrderItemId())) {
                throw new BusinessException("Duplicate purchase order item in invoice request: " + itemReq.getPurchaseOrderItemId());
            }

            PurchaseOrderItem poItem = purchaseOrderItemRepository.findById(itemReq.getPurchaseOrderItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Purchase Order Item not found with ID: " + itemReq.getPurchaseOrderItemId()));

            Product product = poItem.getProduct();

            if (purchaseOrderId != null && !poItem.getPurchaseOrder().getId().equals(purchaseOrderId)) {
                throw new BusinessException("Purchase order item " + itemReq.getPurchaseOrderItemId() + " does not belong to the selected Purchase Order: " + purchaseOrderId);
            }

            GoodsReceiptItem grItem = null;
            if (itemReq.getGoodsReceiptItemId() != null) {
                grItem = goodsReceiptItemRepository.findById(itemReq.getGoodsReceiptItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Goods Receipt Item not found with ID: " + itemReq.getGoodsReceiptItemId()));
                if (grItem.getGoodsReceipt().getStatus() == GoodsReceiptStatus.CANCELLED) {
                    throw new BusinessException("Cannot invoice items from cancelled Goods Receipt: " + grItem.getGoodsReceipt().getReceiptNumber());
                }
                if (purchaseOrderId != null && !grItem.getGoodsReceipt().getPurchaseOrder().getId().equals(purchaseOrderId)) {
                    throw new BusinessException("Goods receipt item does not belong to the selected Purchase Order: " + purchaseOrderId);
                }
                if (!grItem.getProduct().getId().equals(product.getId())) {
                    throw new BusinessException("Goods receipt item product does not match purchase order item product");
                }
            }

            BigDecimal remaining = poItem.getReceivedQuantity().subtract(poItem.getInvoicedQuantity());
            BigDecimal qty = itemReq.getQuantity();

            if (qty.compareTo(remaining) > 0) {
                throw new BusinessException("Invoice quantity " + qty
                        + " exceeds remaining received quantity " + remaining
                        + " for product: " + product.getName());
            }

            BigDecimal netAmount = qty.multiply(itemReq.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal taxAmount = netAmount.multiply(itemReq.getTaxRate()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal discountAmount = itemReq.getDiscountAmount().setScale(2, RoundingMode.HALF_UP);
            BigDecimal totalAmount = netAmount.add(taxAmount).subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

            SupplierInvoiceItem item = SupplierInvoiceItem.builder()
                    .supplierInvoice(invoice)
                    .purchaseOrderItem(poItem)
                    .goodsReceiptItem(grItem)
                    .product(product)
                    .quantity(qty)
                    .unitPrice(itemReq.getUnitPrice().setScale(2, RoundingMode.HALF_UP))
                    .netAmount(netAmount)
                    .taxRate(itemReq.getTaxRate())
                    .taxAmount(taxAmount)
                    .discountAmount(discountAmount)
                    .totalAmount(totalAmount)
                    .build();

            if (itemReq.getDimensionSetId() != null) {
                item.setDimensionSet(budgetDimensionSetRepository.findById(itemReq.getDimensionSetId()).orElse(null));
            }

            items.add(item);
        }

        return items;
    }

    private void calculateTotals(SupplierInvoice invoice) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        for (SupplierInvoiceItem item : invoice.getItems()) {
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

    private JournalEntry generateJournalEntry(SupplierInvoice invoice) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", invoice.getCompany().getId(), year, nextSeq);

        User currentUser = getCurrentUser();

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(invoice.getCompany())
                .entryDate(invoice.getInvoiceDate())
                .description("Supplier Invoice " + invoice.getInvoiceNumber())
                .sourceModule("SUPPLIER_INVOICE")
                .sourceReference(invoice.getId().toString())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(currentUser)
                .currencyCode(invoice.getCurrencyCode())
                .lines(new ArrayList<>())
                .build();

        // Dynamic tax calculation to determine posting accounts
        TaxCalculationRequest taxReq = TaxCalculationRequest.builder()
                .companyId(invoice.getCompany().getId())
                .transactionDate(invoice.getInvoiceDate())
                .documentType("PURCHASE_INVOICE")
                .documentId(invoice.getId())
                .supplierId(invoice.getSupplier().getId())
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
            // Fall back to manual/stored values on calculation error
        }

        // Debits: Net expenses/inventory per item + non-recoverable tax
        for (int i = 0; i < invoice.getItems().size(); i++) {
            SupplierInvoiceItem item = invoice.getItems().get(i);
            BigDecimal itemNet = item.getNetAmount().subtract(item.getDiscountAmount());
            BigDecimal itemDebit = itemNet;

            if (taxResult != null) {
                TaxCalculationLineResult lineRes = taxResult.getLines().get(i);
                for (TaxComponentResult comp : lineRes.getTaxComponents()) {
                    BigDecimal compTax = comp.getTaxAmount();
                    if (compTax.compareTo(BigDecimal.ZERO) > 0) {
                        if (comp.getReverseChargeAccountId() == null && !comp.isRecoverable()) {
                            // Non-recoverable tax: capitalize/expense it by adding to item debit
                            itemDebit = itemDebit.add(compTax);
                        }
                    }
                }
            }

            String accountCode = isInventoryProduct(item.getProduct()) ? "1300" : "5200";
            Account account = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), accountCode)
                    .orElseThrow(() -> new BusinessException("Account not found for code " + accountCode + " in company " + invoice.getCompany().getId()));

            JournalEntryLine debitLine = JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(account)
                    .debitAmount(itemDebit)
                    .creditAmount(BigDecimal.ZERO)
                    .build();

            je.getLines().add(debitLine);
        }

        // Add tax journal entries using TaxJournalService
        if (taxResult != null) {
            taxJournalService.createTaxJournalLines(je, invoice.getCompany(), taxResult, true);
        } else {
            // Fallback: post tax directly to 2200
            BigDecimal totalTax = invoice.getTaxAmount();
            if (totalTax.compareTo(BigDecimal.ZERO) > 0) {
                Account fallbackTaxAcc = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), "2200")
                        .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));
                JournalEntryLine taxLine = JournalEntryLine.builder()
                        .journalEntry(je)
                        .account(fallbackTaxAcc)
                        .debitAmount(totalTax)
                        .creditAmount(BigDecimal.ZERO)
                        .build();
                je.getLines().add(taxLine);
            }
        }

        BigDecimal totalDebits = je.getLines().stream().map(JournalEntryLine::getDebitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalOtherCredits = je.getLines().stream().map(JournalEntryLine::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal apCreditAmount = totalDebits.subtract(totalOtherCredits);

        Account apAccount = accountRepository.findByCompanyIdAndAccountCode(invoice.getCompany().getId(), "2100")
                .orElseThrow(() -> new BusinessException("Accounts Payable account (2100) not found in company " + invoice.getCompany().getId()));

        JournalEntryLine creditLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(apAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(apCreditAmount)
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
                .sourceModule("SUPPLIER_INVOICE")
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

    private boolean isInventoryProduct(Product product) {
        if (product.getProductType() == null) {
            return true; // Default to inventory product
        }
        String type = product.getProductType().toUpperCase();
        return !type.equals("SERVICE") && !type.equals("EXPENSE");
    }

    private User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            // For testing environments without security context configured, fetch a default system admin or return null
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }

    /**
     * Submits the invoice for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse submitInvoice(Long id) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.DRAFT) {
            throw new BusinessException("Only DRAFT invoices can be submitted. Current status: " + invoice.getStatus());
        }

        invoice.setStatus(SupplierInvoiceStatus.SUBMITTED);
        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }

    /**
     * Permanently voids the invoice. This action cannot be undone.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SupplierInvoiceResponse result
     */
    @Override
    @Transactional
    public SupplierInvoiceResponse voidInvoice(Long id) {
        SupplierInvoice invoice = supplierInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier Invoice not found with ID: " + id));

        if (invoice.getStatus() != SupplierInvoiceStatus.DRAFT && invoice.getStatus() != SupplierInvoiceStatus.SUBMITTED) {
            throw new BusinessException("Only DRAFT or SUBMITTED invoices can be voided. Current status: " + invoice.getStatus());
        }

        invoice.setStatus(SupplierInvoiceStatus.VOID);
        SupplierInvoice saved = supplierInvoiceRepository.save(invoice);
        eventPublisher.publishEvent(new SupplierInvoiceRefreshEvent(this));
        return supplierInvoiceMapper.toResponse(saved);
    }
}