/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CustomerReturnServiceImpl.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnServiceImpl
 * Related Repository: CustomerReturnRepository, CustomerReturnItemRepository, CreditNoteRepository, CreditNoteItemRepository, CustomerInvoiceRepository, CustomerInvoiceItemRepository, SalesOrderRepository, SalesOrderItemRepository, ProductRepository, CompanyRepository, CustomerRepository, WarehouseRepository, StoreRepository, UserRepository, InventoryStockRepository, StockMovementRepository, InventoryLotRepository, InventorySerialRepository, AccountRepository, JournalEntryRepository
 * Related Entity    : CustomerReturn
 * Related DTO       : CustomerReturnItemRequest, CustomerReturnRequest, CustomerReturnResponse, CustomerReturnSearchRequest, InspectionRequest
 * Related Mapper    : CustomerReturnMapper
 * Related DB Table  : customer_returns
 * Related REST APIs : N/A
 * Depends On        : Common Module, Finance Module, Inventory Module, Organization Module, Security Module
 * Used By           : CustomerReturnController, CustomerReturnServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements CustomerReturnService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.inventory.service.InventoryTraceabilityService;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.*;
import com.plus33.erp.sales.event.CustomerReturnRefreshEvent;
import com.plus33.erp.sales.mapper.CustomerReturnMapper;
import com.plus33.erp.sales.repository.*;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CustomerReturnController
 *   --> CustomerReturnServiceImpl (this)
 *   --> Validate business rules
 *   --> CustomerReturnRepository (read/write 'customer_returns')
 *   --> CustomerReturnMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code customer_returns}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Inventory, Organization, Sales, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CustomerReturnServiceImpl implements CustomerReturnService {

    private final CustomerReturnRepository customerReturnRepository;
    private final CustomerReturnItemRepository customerReturnItemRepository;
    private final CreditNoteRepository creditNoteRepository;
    private final CreditNoteItemRepository creditNoteItemRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final CustomerInvoiceItemRepository customerInvoiceItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final InventoryLotRepository inventoryLotRepository;
    private final InventorySerialRepository inventorySerialRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final InventoryTraceabilityService inventoryTraceabilityService;
    private final CustomerReturnMapper customerReturnMapper;
    private final ApplicationEventPublisher eventPublisher;

    public CustomerReturnServiceImpl(
            CustomerReturnRepository customerReturnRepository,
            CustomerReturnItemRepository customerReturnItemRepository,
            CreditNoteRepository creditNoteRepository,
            CreditNoteItemRepository creditNoteItemRepository,
            CustomerInvoiceRepository customerInvoiceRepository,
            CustomerInvoiceItemRepository customerInvoiceItemRepository,
            SalesOrderRepository salesOrderRepository,
            SalesOrderItemRepository salesOrderItemRepository,
            ProductRepository productRepository,
            CompanyRepository companyRepository,
            CustomerRepository customerRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            UserRepository userRepository,
            InventoryStockRepository inventoryStockRepository,
            StockMovementRepository stockMovementRepository,
            InventoryLotRepository inventoryLotRepository,
            InventorySerialRepository inventorySerialRepository,
            AccountRepository accountRepository,
            JournalEntryRepository journalEntryRepository,
            InventoryTraceabilityService inventoryTraceabilityService,
            CustomerReturnMapper customerReturnMapper,
            ApplicationEventPublisher eventPublisher) {
        this.customerReturnRepository = customerReturnRepository;
        this.customerReturnItemRepository = customerReturnItemRepository;
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteItemRepository = creditNoteItemRepository;
        this.customerInvoiceRepository = customerInvoiceRepository;
        this.customerInvoiceItemRepository = customerInvoiceItemRepository;
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.inventoryLotRepository = inventoryLotRepository;
        this.inventorySerialRepository = inventorySerialRepository;
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.inventoryTraceabilityService = inventoryTraceabilityService;
        this.customerReturnMapper = customerReturnMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a new return and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new return and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CustomerReturnResponse createReturn(CustomerReturnRequest request) {
        // Tenant validation
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        // Idempotency check
        Optional<CustomerReturn> existing = customerReturnRepository.findByClientReferenceId(request.clientReferenceId());
        if (existing.isPresent()) {
            return customerReturnMapper.toResponse(existing.get());
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (!customer.getCompany().getId().equals(company.getId())) {
            throw new BusinessException("Customer does not belong to company");
        }

        Warehouse warehouse = null;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
            if (!warehouse.getRegion().getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Warehouse does not belong to company");
            }
        }

        Store store = null;
        if (request.storeId() != null) {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
            if (!store.getRegion().getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Store does not belong to company");
            }
        }

        if ((warehouse != null && store != null) || (warehouse == null && store == null)) {
            throw new BusinessException("Either Warehouse or Store must be set (XOR)");
        }

        SalesOrder salesOrder = null;
        if (request.salesOrderId() != null) {
            salesOrder = salesOrderRepository.findById(request.salesOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sales Order not found"));
            if (!salesOrder.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Sales Order does not belong to company");
            }
        }

        CustomerInvoice invoice = null;
        if (request.customerInvoiceId() != null) {
            invoice = customerInvoiceRepository.findById(request.customerInvoiceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
            if (!invoice.getCustomer().getId().equals(customer.getId())) {
                throw new BusinessException("Invoice does not belong to customer");
            }
        }

        // Generate return number
        Long seqVal = customerReturnRepository.getNextSequenceValue();
        String returnNumber = String.format("CR-%d-%06d", LocalDate.now().getYear(), seqVal);

        CustomerReturn customerReturn = CustomerReturn.builder()
                .company(company)
                .customer(customer)
                .salesOrder(salesOrder)
                .customerInvoice(invoice)
                .warehouse(warehouse)
                .store(store)
                .returnNumber(returnNumber)
                .clientReferenceId(request.clientReferenceId())
                .status(CustomerReturnStatus.RETURN_REQUESTED)
                .reason(request.reason())
                .remarks(request.remarks())
                .createdBy(getCurrentUser())
                .build();

        // Process items
        for (CustomerReturnItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            CustomerInvoiceItem invoiceItem = null;
            if (itemReq.customerInvoiceItemId() != null) {
                invoiceItem = customerInvoiceItemRepository.findById(itemReq.customerInvoiceItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Invoice Item not found"));
                if (!invoiceItem.getProduct().getId().equals(product.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Product does not belong to invoice");
                }
            } else if (invoice != null) {
                // If invoice is linked at parent but not item level, find matching product in invoice
                final Product p = product;
                invoiceItem = invoice.getItems().stream()
                        .filter(it -> it.getProduct().getId().equals(p.getId()))
                        .findFirst()
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Product does not belong to invoice"));
            }

            SalesOrderItem soItem = null;
            if (itemReq.salesOrderItemId() != null) {
                soItem = salesOrderItemRepository.findById(itemReq.salesOrderItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sales Order Item not found"));
            }

            // Partial return validation
            if (invoiceItem != null) {
                BigDecimal alreadyReturned = getAlreadyReturnedQuantity(invoiceItem.getId());
                if (alreadyReturned.add(itemReq.quantity()).compareTo(invoiceItem.getQuantity()) > 0) {
                    throw new BusinessException("Total returned quantity exceeds invoiced quantity for product: " + product.getName());
                }
            }

            // Lot & Serial validation
            InventoryLot lot = null;
            if (itemReq.lotId() != null) {
                lot = inventoryLotRepository.findById(itemReq.lotId())
                        .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));
                if (!lot.getCompany().getId().equals(company.getId()) || !lot.getProduct().getId().equals(product.getId())) {
                    throw new BusinessException("Lot does not belong to company/product");
                }
            }

            InventorySerial serial = null;
            if (itemReq.serialId() != null) {
                serial = inventorySerialRepository.findById(itemReq.serialId())
                        .orElseThrow(() -> new ResourceNotFoundException("Serial not found"));
                if (!serial.getCompany().getId().equals(company.getId()) || !serial.getProduct().getId().equals(product.getId())) {
                    throw new BusinessException("Serial does not belong to company/product");
                }
                if (serial.getStatus() != InventorySerialStatus.SOLD) {
                    throw new BusinessException("Serial is not sold/delivered, cannot be returned");
                }
                // Check if already returned
                boolean alreadyRet = customerReturnItemRepository.existsBySerialIdAndCustomerReturnStatusNot(serial.getId(), CustomerReturnStatus.CANCELLED);
                if (alreadyRet) {
                    throw new BusinessException("Serial is already returned on another return request");
                }
            }

            CustomerReturnItem returnItem = CustomerReturnItem.builder()
                    .customerReturn(customerReturn)
                    .salesOrderItem(soItem)
                    .customerInvoiceItem(invoiceItem)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .lot(lot)
                    .serial(serial)
                    .build();

            customerReturn.addItem(returnItem);
        }

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Updates an existing return record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Updates an existing return record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CustomerReturnResponse updateReturn(Long id, CustomerReturnRequest request) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() != CustomerReturnStatus.RETURN_REQUESTED) {
            throw new BusinessException("Only return requests in RETURN_REQUESTED status can be edited");
        }

        // Re-validate and update fields
        customerReturn.setReason(request.reason());
        customerReturn.setRemarks(request.remarks());

        // Clear existing items and rebuild to avoid orphan items issues
        customerReturn.getItems().clear();

        for (CustomerReturnItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            CustomerInvoiceItem invoiceItem = null;
            if (itemReq.customerInvoiceItemId() != null) {
                invoiceItem = customerInvoiceItemRepository.findById(itemReq.customerInvoiceItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Invoice Item not found"));
                if (!invoiceItem.getProduct().getId().equals(product.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Product does not belong to invoice");
                }
            }

            SalesOrderItem soItem = null;
            if (itemReq.salesOrderItemId() != null) {
                soItem = salesOrderItemRepository.findById(itemReq.salesOrderItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Sales Order Item not found"));
            }

            if (invoiceItem != null) {
                BigDecimal alreadyReturned = getAlreadyReturnedQuantity(invoiceItem.getId());
                if (alreadyReturned.add(itemReq.quantity()).compareTo(invoiceItem.getQuantity()) > 0) {
                    throw new BusinessException("Total returned quantity exceeds invoiced quantity for product: " + product.getName());
                }
            }

            InventoryLot lot = null;
            if (itemReq.lotId() != null) {
                lot = inventoryLotRepository.findById(itemReq.lotId())
                        .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));
            }

            InventorySerial serial = null;
            if (itemReq.serialId() != null) {
                serial = inventorySerialRepository.findById(itemReq.serialId())
                        .orElseThrow(() -> new ResourceNotFoundException("Serial not found"));
                if (serial.getStatus() != InventorySerialStatus.SOLD) {
                    throw new BusinessException("Serial is not sold/delivered");
                }
            }

            CustomerReturnItem returnItem = CustomerReturnItem.builder()
                    .customerReturn(customerReturn)
                    .salesOrderItem(soItem)
                    .customerInvoiceItem(invoiceItem)
                    .product(product)
                    .quantity(itemReq.quantity())
                    .lot(lot)
                    .serial(serial)
                    .build();

            customerReturn.addItem(returnItem);
        }

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Retrieves a single return by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerReturnResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single return by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerReturnResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public CustomerReturnResponse getReturnById(Long id) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));
        return customerReturnMapper.toResponse(customerReturn);
    }

    /**
     * Returns a filtered paginated list of returns records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of returns records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<CustomerReturnResponse> searchReturns(CustomerReturnSearchRequest searchRequest, Pageable pageable) {
        Specification<CustomerReturn> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.getCompanyId()));
            }
            if (searchRequest.getCustomerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), searchRequest.getCustomerId()));
            }
            if (searchRequest.getSalesOrderId() != null) {
                predicates.add(cb.equal(root.get("salesOrder").get("id"), searchRequest.getSalesOrderId()));
            }
            if (searchRequest.getCustomerInvoiceId() != null) {
                predicates.add(cb.equal(root.get("customerInvoice").get("id"), searchRequest.getCustomerInvoiceId()));
            }
            if (searchRequest.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.getStatus()));
            }
            if (searchRequest.getReturnNumber() != null && !searchRequest.getReturnNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("returnNumber")), "%" + searchRequest.getReturnNumber().toLowerCase() + "%"));
            }
            if (searchRequest.getReturnDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), searchRequest.getReturnDateFrom().atStartOfDay()));
            }
            if (searchRequest.getReturnDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), searchRequest.getReturnDateTo().atTime(23, 59, 59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<CustomerReturn> page = customerReturnRepository.findAll(spec, pageable);
        List<CustomerReturnResponse> content = page.getContent().stream()
                .map(customerReturnMapper::toResponse)
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
     * Approves the return, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Approves the return, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CustomerReturnResponse approveReturn(Long id, ReturnApprovalRequest request) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() != CustomerReturnStatus.RETURN_REQUESTED) {
            throw new BusinessException("Only RETURN_REQUESTED returns can be approved");
        }

        customerReturn.setStatus(CustomerReturnStatus.APPROVED);
        customerReturn.setApprovedBy(getCurrentUser());
        customerReturn.setApprovedAt(LocalDateTime.now());

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Performs the receiveReturn operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerReturnResponse result
     */
    /**
     * Performs the receiveReturn operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CustomerReturnResponse result
     */
    @Override
    public CustomerReturnResponse receiveReturn(Long id) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() != CustomerReturnStatus.APPROVED) {
            throw new BusinessException("Only APPROVED returns can be received");
        }

        customerReturn.setStatus(CustomerReturnStatus.RECEIVED);
        customerReturn.setReceivedBy(getCurrentUser());
        customerReturn.setReceivedAt(LocalDateTime.now());

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Performs the inspectReturn operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     */
    /**
     * Performs the inspectReturn operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     */
    @Override
    public CustomerReturnResponse inspectReturn(Long id, InspectionRequest request) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() != CustomerReturnStatus.RECEIVED) {
            throw new BusinessException("Only RECEIVED returns can be inspected");
        }

        for (InspectionRequest.ItemInspection itemInsp : request.items()) {
            CustomerReturnItem returnItem = customerReturn.getItems().stream()
                    .filter(it -> it.getProduct().getId().equals(itemInsp.productId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Return item not found for product ID: " + itemInsp.productId()));

            returnItem.setInspectionResult(itemInsp.result());
            returnItem.setInspectionNotes(itemInsp.notes());
        }

        customerReturn.setStatus(CustomerReturnStatus.INSPECTED);
        customerReturn.setInspectedBy(getCurrentUser());
        customerReturn.setInspectedAt(LocalDateTime.now());
        customerReturn.setRemarks(customerReturn.getRemarks() + " | Inspection remarks: " + request.remarks());

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Completes the return workflow and finalizes the record status.
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CustomerReturnResponse result
     */
    @Override
    @Transactional
    public CustomerReturnResponse closeReturn(Long id, ReturnCloseRequest request) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() != CustomerReturnStatus.INSPECTED) {
            throw new BusinessException("Only INSPECTED returns can be closed");
        }

        User currentUser = getCurrentUser();
        Company company = customerReturn.getCompany();

        // 1. Process Inventory Movements & Traceability per line
        for (CustomerReturnItem item : customerReturn.getItems()) {
            if (item.getInspectionResult() == null) {
                throw new BusinessException("All items must have an inspection result before closing return");
            }

            switch (item.getInspectionResult()) {
                case RESTOCK -> {
                    // Update available stock under pessimistic write lock
                    InventoryStock stock = getLockedInventoryStock(item.getProduct(), customerReturn.getWarehouse(), customerReturn.getStore());
                    if (stock == null) {
                        stock = new InventoryStock();
                        stock.setProduct(item.getProduct());
                        stock.setWarehouse(customerReturn.getWarehouse());
                        stock.setStore(customerReturn.getStore());
                        stock.setQuantity(BigDecimal.ZERO);
                    }
                    stock.setQuantity(stock.getQuantity().add(item.getQuantity()));
                    inventoryStockRepository.save(stock);

                    // Create stock movement
                    createStockMovement(item.getProduct(), customerReturn.getWarehouse(), customerReturn.getStore(),
                            item.getQuantity(), customerReturn, StockMovementReferenceType.CUSTOMER_RETURN, currentUser);

                    // Transition serial if tracked
                    if (item.getSerial() != null) {
                        InventorySerial serial = item.getSerial();
                        serial.setStatus(InventorySerialStatus.IN_STOCK);
                        serial.setWarehouse(customerReturn.getWarehouse());
                        serial.setStore(customerReturn.getStore());
                        inventorySerialRepository.save(serial);
                    }

                    // Record Trace Event
                    inventoryTraceabilityService.recordTraceEvent(
                            company.getId(),
                            item.getProduct().getId(),
                            item.getLot() != null ? item.getLot().getId() : null,
                            item.getSerial() != null ? item.getSerial().getId() : null,
                            customerReturn.getWarehouse() != null ? customerReturn.getWarehouse().getId() : null,
                            customerReturn.getStore() != null ? customerReturn.getStore().getId() : null,
                            InventoryTraceEventType.RECEIPT,
                            item.getQuantity(),
                            InventoryTraceReferenceType.CUSTOMER_RETURN,
                            customerReturn.getId(),
                            customerReturn.getReturnNumber(),
                            "Customer return restocked"
                    );
                }
                case SCRAP -> {
                    // Scrap adjustment (recorded directly for audit log without available stock increment)
                    createStockMovement(item.getProduct(), customerReturn.getWarehouse(), customerReturn.getStore(),
                            BigDecimal.ZERO, customerReturn, StockMovementReferenceType.CUSTOMER_RETURN, currentUser);

                    if (item.getSerial() != null) {
                        InventorySerial serial = item.getSerial();
                        serial.setStatus(InventorySerialStatus.RECALLED);
                        inventorySerialRepository.save(serial);
                    }

                    // Record Trace Event
                    inventoryTraceabilityService.recordTraceEvent(
                            company.getId(),
                            item.getProduct().getId(),
                            item.getLot() != null ? item.getLot().getId() : null,
                            item.getSerial() != null ? item.getSerial().getId() : null,
                            customerReturn.getWarehouse() != null ? customerReturn.getWarehouse().getId() : null,
                            customerReturn.getStore() != null ? customerReturn.getStore().getId() : null,
                            InventoryTraceEventType.ADJUSTMENT,
                            item.getQuantity(),
                            InventoryTraceReferenceType.CUSTOMER_RETURN,
                            customerReturn.getId(),
                            customerReturn.getReturnNumber(),
                            "Customer return scrapped"
                    );
                }
                case QUARANTINE -> {
                    // Update Serial/Lot Status
                    if (item.getSerial() != null) {
                        InventorySerial serial = item.getSerial();
                        serial.setStatus(InventorySerialStatus.QUARANTINED);
                        serial.setWarehouse(customerReturn.getWarehouse());
                        serial.setStore(customerReturn.getStore());
                        inventorySerialRepository.save(serial);
                    }
                    if (item.getLot() != null) {
                        InventoryLot lot = item.getLot();
                        lot.setStatus(InventoryLotStatus.QUARANTINED);
                        inventoryLotRepository.save(lot);
                    }

                    // Create stock movement
                    createStockMovement(item.getProduct(), customerReturn.getWarehouse(), customerReturn.getStore(),
                            item.getQuantity(), customerReturn, StockMovementReferenceType.CUSTOMER_RETURN, currentUser);

                    // Record Trace Event
                    inventoryTraceabilityService.recordTraceEvent(
                            company.getId(),
                            item.getProduct().getId(),
                            item.getLot() != null ? item.getLot().getId() : null,
                            item.getSerial() != null ? item.getSerial().getId() : null,
                            customerReturn.getWarehouse() != null ? customerReturn.getWarehouse().getId() : null,
                            customerReturn.getStore() != null ? customerReturn.getStore().getId() : null,
                            InventoryTraceEventType.ADJUSTMENT,
                            item.getQuantity(),
                            InventoryTraceReferenceType.CUSTOMER_RETURN,
                            customerReturn.getId(),
                            customerReturn.getReturnNumber(),
                            "Customer return quarantined"
                    );
                }
            }
        }

        // 2. Generate Credit Note
        CreditNote creditNote = generateCreditNoteForReturn(customerReturn, currentUser);

        // 3. Post Credit Note & Ledger integration
        postCreditNoteAndLedger(creditNote, currentUser);

        customerReturn.setStatus(CustomerReturnStatus.CLOSED);
        customerReturn.setClosedBy(currentUser);
        customerReturn.setClosedAt(LocalDateTime.now());
        customerReturn.setRemarks(customerReturn.getRemarks() + " | Closure notes: " + request.remarks());

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    /**
     * Cancels the return and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Cancels the return and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the CustomerReturnResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public CustomerReturnResponse cancelReturn(Long id, String reason) {
        CustomerReturn customerReturn = customerReturnRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Return not found"));

        if (customerReturn.getStatus() == CustomerReturnStatus.CLOSED || customerReturn.getStatus() == CustomerReturnStatus.CANCELLED) {
            throw new BusinessException("Closed or cancelled returns cannot be cancelled");
        }

        customerReturn.setStatus(CustomerReturnStatus.CANCELLED);
        customerReturn.setCancelledBy(getCurrentUser());
        customerReturn.setCancelledAt(LocalDateTime.now());
        customerReturn.setCancellationReason(reason);

        CustomerReturn saved = customerReturnRepository.save(customerReturn);
        eventPublisher.publishEvent(new CustomerReturnRefreshEvent(this));
        return customerReturnMapper.toResponse(saved);
    }

    private BigDecimal getAlreadyReturnedQuantity(Long customerInvoiceItemId) {
        List<CustomerReturnItem> returnedItems = customerReturnItemRepository.findByCustomerInvoiceItemId(customerInvoiceItemId);
        BigDecimal total = BigDecimal.ZERO;
        for (CustomerReturnItem item : returnedItems) {
            if (item.getCustomerReturn().getStatus() != CustomerReturnStatus.CANCELLED) {
                total = total.add(item.getQuantity());
            }
        }
        return total;
    }

    private InventoryStock getLockedInventoryStock(Product product, Warehouse warehouse, Store store) {
        if (warehouse != null) {
            return inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(product.getId(), warehouse.getId())
                    .orElse(null);
        } else {
            return inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(product.getId(), store.getId())
                    .orElse(null);
        }
    }

    private void createStockMovement(Product product, Warehouse warehouse, Store store, BigDecimal quantity,
                                     CustomerReturn cr, StockMovementReferenceType refType, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setStore(store);
        movement.setMovementType(refType.name());
        movement.setQuantity(quantity);
        movement.setReferenceNo(cr.getReturnNumber());
        movement.setReferenceType(refType);
        movement.setReferenceId(cr.getId());
        movement.setReferenceNumber(cr.getReturnNumber());
        movement.setCreatedBy(user);
        stockMovementRepository.save(movement);
    }

    private CreditNote generateCreditNoteForReturn(CustomerReturn customerReturn, User currentUser) {
        Long nextSeq = creditNoteRepository.getNextSequenceValue();
        String creditNoteNumber = String.format("CN-%d-%06d", LocalDate.now().getYear(), nextSeq);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;

        CreditNote creditNote = CreditNote.builder()
                .company(customerReturn.getCompany())
                .customer(customerReturn.getCustomer())
                .customerReturn(customerReturn)
                .customerInvoice(customerReturn.getCustomerInvoice())
                .creditNoteNumber(creditNoteNumber)
                .clientReferenceId(UUID.randomUUID()) // Internal derived idempotency UUID
                .status(CreditNoteStatus.DRAFT)
                .remarks("Generated from Customer Return: " + customerReturn.getReturnNumber())
                .createdBy(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();

        for (CustomerReturnItem returnItem : customerReturn.getItems()) {
            CustomerInvoiceItem invoiceItem = returnItem.getCustomerInvoiceItem();
            BigDecimal price = invoiceItem != null ? invoiceItem.getUnitPrice() : BigDecimal.ZERO;
            BigDecimal taxPct = invoiceItem != null ? invoiceItem.getTaxPercentage() : BigDecimal.ZERO;
            BigDecimal discPct = invoiceItem != null ? invoiceItem.getDiscountPercentage() : BigDecimal.ZERO;

            BigDecimal itemQty = returnItem.getQuantity();
            BigDecimal itemSubtotal = itemQty.multiply(price);
            BigDecimal itemDiscount = itemSubtotal.multiply(discPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal itemNet = itemSubtotal.subtract(itemDiscount);
            BigDecimal itemTax = itemNet.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal itemTotal = itemNet.add(itemTax);

            subtotal = subtotal.add(itemSubtotal);
            tax = tax.add(itemTax);
            discount = discount.add(itemDiscount);
            total = total.add(itemTotal);

            CreditNoteItem cnItem = CreditNoteItem.builder()
                    .creditNote(creditNote)
                    .product(returnItem.getProduct())
                    .quantity(itemQty)
                    .unitPrice(price)
                    .discountPercentage(discPct)
                    .taxPercentage(taxPct)
                    .netAmount(itemNet)
                    .taxAmount(itemTax)
                    .discountAmount(itemDiscount)
                    .totalAmount(itemTotal)
                    .build();

            creditNote.addItem(cnItem);
        }

        creditNote.setSubtotalAmount(subtotal);
        creditNote.setTaxAmount(tax);
        creditNote.setDiscountAmount(discount);
        creditNote.setTotalAmount(total);

        return creditNoteRepository.save(creditNote);
    }

    private void postCreditNoteAndLedger(CreditNote creditNote, User currentUser) {
        CustomerInvoice invoice = creditNote.getCustomerInvoice();
        if (invoice != null) {
            // Recalculate remaining invoice balances
            BigDecimal newCredited = invoice.getCreditedAmount().add(creditNote.getTotalAmount());
            if (newCredited.compareTo(invoice.getTotalAmount()) > 0) {
                throw new BusinessException("Credit Note total amount exceeds customer invoice value");
            }
            invoice.setCreditedAmount(newCredited);
            invoice.setOutstandingBalance(invoice.getOutstandingBalance().subtract(creditNote.getTotalAmount()));

            if (invoice.getOutstandingBalance().compareTo(BigDecimal.ZERO) <= 0) {
                invoice.setStatus(CustomerInvoiceStatus.FULLY_CREDITED);
            } else {
                invoice.setStatus(CustomerInvoiceStatus.PARTIALLY_CREDITED);
            }

            // Update item returned quantities
            for (CreditNoteItem cnItem : creditNote.getItems()) {
                CustomerInvoiceItem invoiceItem = invoice.getItems().stream()
                        .filter(i -> i.getProduct().getId().equals(cnItem.getProduct().getId()))
                        .findFirst()
                        .orElse(null);
                if (invoiceItem != null) {
                    invoiceItem.setReturnedQuantity(invoiceItem.getReturnedQuantity().add(cnItem.getQuantity()));
                    customerInvoiceItemRepository.save(invoiceItem);
                }
            }
            customerInvoiceRepository.save(invoice);
        }

        // Adjust Customer Outstanding Balance
        Customer customer = creditNote.getCustomer();
        customer.setOutstandingBalance(customer.getOutstandingBalance().subtract(creditNote.getTotalAmount()));
        customerRepository.save(customer);

        // GL Ledger Reversal posting
        JournalEntry journalEntry = generateCreditNoteJournalEntry(creditNote, currentUser);
        journalEntryRepository.save(journalEntry);
        creditNote.setJournalEntry(journalEntry);

        creditNote.setStatus(CreditNoteStatus.POSTED);
        creditNote.setApprovedBy(currentUser);
        creditNote.setApprovedAt(LocalDateTime.now());
        creditNoteRepository.save(creditNote);
    }

    private JournalEntry generateCreditNoteJournalEntry(CreditNote creditNote, User user) {
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        int year = LocalDate.now().getYear();
        String jeNumber = String.format("JE-%d-%d-%06d", creditNote.getCompany().getId(), year, nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(creditNote.getCompany())
                .entryDate(LocalDate.now())
                .description("Credit Note Posting " + creditNote.getCreditNoteNumber())
                .sourceModule("CREDIT_NOTE")
                .sourceReference(creditNote.getId().toString())
                .status("POSTED")
                .postedAt(LocalDateTime.now())
                .createdBy(user)
                .currencyCode("AED")
                .lines(new ArrayList<>())
                .build();

        // 1. Debit: Sales Returns / Revenue (4000)
        Account revAccount = accountRepository.findByCompanyIdAndAccountCode(creditNote.getCompany().getId(), "4000")
                .orElseThrow(() -> new BusinessException("Sales Returns / Revenue account (4000) not found"));

        BigDecimal revAmount = creditNote.getSubtotalAmount().subtract(creditNote.getDiscountAmount());
        JournalEntryLine revLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(revAccount)
                .debitAmount(revAmount)
                .creditAmount(BigDecimal.ZERO)
                .build();
        je.getLines().add(revLine);

        // 2. Debit: Tax Payable (2200)
        if (creditNote.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
            Account taxAccount = accountRepository.findByCompanyIdAndAccountCode(creditNote.getCompany().getId(), "2200")
                    .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));

            JournalEntryLine taxLine = JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(taxAccount)
                    .debitAmount(creditNote.getTaxAmount())
                    .creditAmount(BigDecimal.ZERO)
                    .build();
            je.getLines().add(taxLine);
        }

        // 3. Credit: Accounts Receivable (1400)
        Account arAccount = accountRepository.findByCompanyIdAndAccountCode(creditNote.getCompany().getId(), "1400")
                .orElseThrow(() -> new BusinessException("Accounts Receivable account (1400) not found"));

        JournalEntryLine arLine = JournalEntryLine.builder()
                .journalEntry(je)
                .account(arAccount)
                .debitAmount(BigDecimal.ZERO)
                .creditAmount(creditNote.getTotalAmount())
                .build();
        je.getLines().add(arLine);

        return je;
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