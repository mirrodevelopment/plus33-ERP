/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : SalesOrderServiceImpl.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository, CustomerRepository, CompanyRepository, ProductRepository, UserRepository
 * Related Entity    : SalesOrder
 * Related DTO       : PageResponse, SalesOrderItemRequest, SalesOrderRequest, SalesOrderResponse, SalesOrderSearchRequest
 * Related Mapper    : SalesOrderMapper
 * Related DB Table  : sales_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module, Organization Module, Security Module
 * Used By           : SalesOrderController, SalesOrderServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements SalesOrderService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.SalesOrder;
import com.plus33.erp.sales.entity.SalesOrderItem;
import com.plus33.erp.sales.entity.SalesOrderStatus;
import com.plus33.erp.sales.mapper.SalesOrderMapper;
import com.plus33.erp.sales.repository.CustomerRepository;
import com.plus33.erp.sales.repository.SalesOrderRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SalesOrderController
 *   --> SalesOrderServiceImpl (this)
 *   --> Validate business rules
 *   --> SalesOrderRepository (read/write 'sales_orders')
 *   --> SalesOrderMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code sales_orders}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Organization, Sales, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SalesOrderMapper salesOrderMapper;

    public SalesOrderServiceImpl(
            SalesOrderRepository salesOrderRepository,
            CustomerRepository customerRepository,
            CompanyRepository companyRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            SalesOrderMapper salesOrderMapper) {
        this.salesOrderRepository = salesOrderRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.salesOrderMapper = salesOrderMapper;
    }

    /**
     * Creates a new sales order and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the SalesOrderResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SalesOrderResponse createSalesOrder(SalesOrderRequest request) {
        // 1. Idempotency support: check if client reference ID is already processed
        Optional<SalesOrder> existing = salesOrderRepository.findByCompanyIdAndClientReferenceId(
                request.companyId(), request.clientReferenceId()
        );
        if (existing.isPresent()) {
            return salesOrderMapper.toResponse(existing.get());
        }

        // 2. Fetch Company & Customer
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.customerId()));

        // 3. Reject cross-company relationships
        if (!customer.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Customer does not belong to the requested company");
        }

        // 4. Validate customer status
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Customer is not active");
        }

        // 5. Check customer address snapshot requirements
        if (customer.getBillingAddress() == null || customer.getBillingAddress().isBlank() ||
            customer.getShippingAddress() == null || customer.getShippingAddress().isBlank()) {
            throw new BusinessException("Customer billing and shipping addresses must be configured");
        }

        // 6. Generate multi-tenant order number
        Long seqVal = salesOrderRepository.getNextSequenceValue();
        String orderNumber = String.format("SO-%d-%06d", LocalDate.now().getYear(), seqVal);

        User currentUser = getCurrentUser();

        // 7. Initialize SalesOrder header and snapshot fields
        SalesOrder salesOrder = SalesOrder.builder()
                .company(company)
                .customer(customer)
                .orderNumber(orderNumber)
                .clientReferenceId(request.clientReferenceId())
                .orderDate(LocalDate.now())
                .requestedDeliveryDate(request.requestedDeliveryDate())
                .currencyCode(customer.getCurrencyCode() != null ? customer.getCurrencyCode() : "INR")
                .paymentTermsDays(customer.getPaymentTermsDays())
                .billingAddress(customer.getBillingAddress())
                .shippingAddress(customer.getShippingAddress())
                .status(SalesOrderStatus.DRAFT)
                // Snapshot values
                .customerName(customer.getName())
                .customerCode(customer.getCode())
                .customerType(customer.getCustomerType().name())
                .pricingTier(customer.getPricingTier())
                .discountRate(customer.getDiscountRate() != null ? customer.getDiscountRate() : BigDecimal.ZERO)
                .taxProfile(customer.getTaxProfile().name())
                .orderedBy(currentUser)
                .build();

        // 8. Process items and calculate monetary sums
        processAndValidateItems(salesOrder, request.items());

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toResponse(saved);
    }

    /**
     * Updates an existing sales order record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the SalesOrderResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SalesOrderResponse updateSalesOrder(Long id, SalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + id));

        // Prevent updates after approval
        if (salesOrder.getStatus() != SalesOrderStatus.DRAFT) {
            throw new BusinessException("Only orders in DRAFT status can be updated");
        }

        if (!salesOrder.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Cannot change order company context");
        }

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + request.customerId()));

        if (!customer.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Customer does not belong to the requested company");
        }

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Customer is not active");
        }

        // Check if customer addresses are present
        if (customer.getBillingAddress() == null || customer.getBillingAddress().isBlank() ||
            customer.getShippingAddress() == null || customer.getShippingAddress().isBlank()) {
            throw new BusinessException("Customer billing and shipping addresses must be configured");
        }

        // Update basic snapshots
        salesOrder.setCustomer(customer);
        salesOrder.setCustomerName(customer.getName());
        salesOrder.setCustomerCode(customer.getCode());
        salesOrder.setCustomerType(customer.getCustomerType().name());
        salesOrder.setPricingTier(customer.getPricingTier());
        salesOrder.setDiscountRate(customer.getDiscountRate() != null ? customer.getDiscountRate() : BigDecimal.ZERO);
        salesOrder.setTaxProfile(customer.getTaxProfile().name());
        salesOrder.setPaymentTermsDays(customer.getPaymentTermsDays());
        salesOrder.setCurrencyCode(customer.getCurrencyCode() != null ? customer.getCurrencyCode() : "INR");
        salesOrder.setBillingAddress(customer.getBillingAddress());
        salesOrder.setShippingAddress(customer.getShippingAddress());
        salesOrder.setRequestedDeliveryDate(request.requestedDeliveryDate());

        // Process items & update totals
        salesOrder.getItems().clear();
        processAndValidateItems(salesOrder, request.items());

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toResponse(saved);
    }

    /**
     * Retrieves a single sales order by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the SalesOrderResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single sales order by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the SalesOrderResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public SalesOrderResponse getSalesOrderById(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + id));
        return salesOrderMapper.toResponse(salesOrder);
    }

    /**
     * Returns a filtered paginated list of sales orders records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of sales orders records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<SalesOrderResponse> searchSalesOrders(SalesOrderSearchRequest searchRequest, Pageable pageable) {
        Specification<SalesOrder> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }

            if (searchRequest.customerId() != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), searchRequest.customerId()));
            }

            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }

            if (searchRequest.customerType() != null && !searchRequest.customerType().isBlank()) {
                predicates.add(cb.equal(root.get("customerType"), searchRequest.customerType()));
            }

            if (searchRequest.creditOverride() != null) {
                predicates.add(cb.equal(root.get("creditOverride"), searchRequest.creditOverride()));
            }

            if (searchRequest.requestedDeliveryDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("requestedDeliveryDate"), searchRequest.requestedDeliveryDateFrom()));
            }

            if (searchRequest.requestedDeliveryDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("requestedDeliveryDate"), searchRequest.requestedDeliveryDateTo()));
            }

            if (searchRequest.createdBy() != null) {
                predicates.add(cb.equal(root.get("orderedBy").get("id"), searchRequest.createdBy()));
            }

            if (searchRequest.approvedBy() != null) {
                predicates.add(cb.equal(root.get("approvedBy").get("id"), searchRequest.approvedBy()));
            }

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("orderNumber")), searchPattern),
                        cb.like(cb.lower(root.get("customerName")), searchPattern),
                        cb.like(cb.lower(root.get("customerCode")), searchPattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<SalesOrder> page = salesOrderRepository.findAll(spec, pageable);
        List<SalesOrderResponse> content = page.getContent().stream()
                .map(salesOrderMapper::toResponse)
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
     * Submits the sales order for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SalesOrderResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SalesOrderResponse submitSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + id));

        if (salesOrder.getStatus() != SalesOrderStatus.DRAFT) {
            throw new BusinessException("Only DRAFT sales orders can be submitted");
        }

        User currentUser = getCurrentUser();

        salesOrder.setStatus(SalesOrderStatus.SUBMITTED);
        salesOrder.setSubmittedBy(currentUser);
        salesOrder.setSubmittedAt(LocalDateTime.now());

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toResponse(saved);
    }

    /**
     * Approves the sales order, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the SalesOrderResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SalesOrderResponse approveSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + id));

        if (salesOrder.getStatus() != SalesOrderStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED sales orders can be approved");
        }

        Customer customer = salesOrder.getCustomer();

        // Recheck customer status
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessException("Cannot approve order for inactive/suspended customer");
        }

        BigDecimal outstandingBalance = customer.getOutstandingBalance();
        BigDecimal orderTotal = salesOrder.getTotalAmount();
        BigDecimal creditLimit = customer.getCreditLimit();

        if (outstandingBalance.add(orderTotal).compareTo(creditLimit) > 0) {
            // Check override permission
            if (!hasOverridePermission()) {
                throw new BusinessException("Credit limit exceeded and user does not have permission to override");
            }
            salesOrder.setCreditOverride(true);
        }

        User currentUser = getCurrentUser();

        salesOrder.setStatus(SalesOrderStatus.APPROVED);
        salesOrder.setApprovedBy(currentUser);
        salesOrder.setApprovedAt(LocalDateTime.now());

        // Increment customer's outstanding balance
        customer.setOutstandingBalance(outstandingBalance.add(orderTotal));
        customerRepository.save(customer);

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toResponse(saved);
    }

    /**
     * Cancels the sales order and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the SalesOrderResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SalesOrderResponse cancelSalesOrder(Long id, String reason) {
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales order not found with ID: " + id));

        SalesOrderStatus status = salesOrder.getStatus();
        if (status != SalesOrderStatus.DRAFT && status != SalesOrderStatus.SUBMITTED && status != SalesOrderStatus.APPROVED) {
            throw new BusinessException("Cancellation is only allowed for DRAFT, SUBMITTED, or APPROVED orders");
        }

        Customer customer = salesOrder.getCustomer();
        User currentUser = getCurrentUser();

        // If approved, decrement customer's outstanding balance
        if (status == SalesOrderStatus.APPROVED) {
            customer.setOutstandingBalance(customer.getOutstandingBalance().subtract(salesOrder.getTotalAmount()));
            customerRepository.save(customer);
        }

        salesOrder.setStatus(SalesOrderStatus.CANCELLED);
        salesOrder.setCancelledBy(currentUser);
        salesOrder.setCancelledAt(LocalDateTime.now());
        salesOrder.setCancellationReason(reason);

        SalesOrder saved = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toResponse(saved);
    }

    private void processAndValidateItems(SalesOrder salesOrder, List<SalesOrderItemRequest> requests) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        Set<Long> productIds = new HashSet<>();

        for (SalesOrderItemRequest itemReq : requests) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));

            // Prevent duplicate products in the same order
            if (!productIds.add(product.getId())) {
                throw new BusinessException("Duplicate product lines detected for product ID: " + product.getId());
            }

            BigDecimal qty = itemReq.orderedQuantity();
            BigDecimal price = itemReq.unitPrice();
            BigDecimal discPct = itemReq.discountPercentage() != null ? itemReq.discountPercentage() : BigDecimal.ZERO;
            BigDecimal taxPct = itemReq.taxPercentage() != null ? itemReq.taxPercentage() : BigDecimal.ZERO;

            // Line calculations
            BigDecimal lineSubtotal = qty.multiply(price).setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineDiscount = lineSubtotal.multiply(discPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineNet = lineSubtotal.subtract(lineDiscount);
            BigDecimal lineTax = lineNet.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = lineNet.add(lineTax).setScale(2, RoundingMode.HALF_UP);

            subtotal = subtotal.add(lineSubtotal);
            discountAmount = discountAmount.add(lineDiscount);
            taxAmount = taxAmount.add(lineTax);

            SalesOrderItem item = SalesOrderItem.builder()
                    .product(product)
                    .orderedQuantity(qty)
                    .unitPrice(price)
                    .discountPercentage(discPct)
                    .taxPercentage(taxPct)
                    .lineTotal(lineTotal)
                    .build();

            salesOrder.addItem(item);
        }

        BigDecimal total = subtotal.subtract(discountAmount).add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        salesOrder.setSubtotal(subtotal);
        salesOrder.setDiscountAmount(discountAmount);
        salesOrder.setTaxAmount(taxAmount);
        salesOrder.setTotalAmount(total);
        salesOrder.setOutstandingAmount(total);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new BusinessException("User is not authenticated");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found with email: " + email));
    }

    private boolean hasOverridePermission() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("SALES_ORDER_OVERRIDE_CREDIT_LIMIT")
                        || role.equals("SALES_OVERRIDE_CREDIT_LIMIT")
                        || role.equals("CUSTOMER_OVERRIDE_CREDIT_LIMIT"));
    }
}