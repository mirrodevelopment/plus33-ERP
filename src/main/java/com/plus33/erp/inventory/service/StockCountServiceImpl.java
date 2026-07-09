/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : StockCountServiceImpl.java
 * Purpose           : Business logic service layer for Inventory Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountServiceImpl
 * Related Repository: StockCountRepository, InventoryAdjustmentRepository, InventoryStockRepository, CompanyRepository, WarehouseRepository, StoreRepository, ProductRepository, UserRepository
 * Related Entity    : StockCount
 * Related DTO       : PageResponse, searchRequest, StockCountItemCountRequest, StockCountItemResponse, StockCountRequest
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module, Security Module
 * Used By           : StockCountController, StockCountServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Inventory Module. Implements StockCountService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.event.InventoryAdjustmentRefreshEvent;
import com.plus33.erp.inventory.mapper.StockCountMapper;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
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
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Inventory Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * StockCountController
 *   --> StockCountServiceImpl (this)
 *   --> Validate business rules
 *   --> StockCountRepository (read/write 'stock_counts')
 *   --> StockCountMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code stock_counts}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class StockCountServiceImpl implements StockCountService {

    private final StockCountRepository stockCountRepository;
    private final InventoryAdjustmentRepository inventoryAdjustmentRepository;
    private final InventoryAdjustmentService inventoryAdjustmentService;
    private final InventoryStockRepository inventoryStockRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockCountMapper stockCountMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryTraceabilityService inventoryTraceabilityService;

    public StockCountServiceImpl(
            StockCountRepository stockCountRepository,
            InventoryAdjustmentRepository inventoryAdjustmentRepository,
            InventoryAdjustmentService inventoryAdjustmentService,
            InventoryStockRepository inventoryStockRepository,
            CompanyRepository companyRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            StockCountMapper stockCountMapper,
            ApplicationEventPublisher eventPublisher,
            InventoryTraceabilityService inventoryTraceabilityService) {
        this.stockCountRepository = stockCountRepository;
        this.inventoryAdjustmentRepository = inventoryAdjustmentRepository;
        this.inventoryAdjustmentService = inventoryAdjustmentService;
        this.inventoryStockRepository = inventoryStockRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.stockCountMapper = stockCountMapper;
        this.eventPublisher = eventPublisher;
        this.inventoryTraceabilityService = inventoryTraceabilityService;
    }

    /**
     * Creates a new count and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the IdempotentCreateResult result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public IdempotentCreateResult<StockCountResponse> createCount(StockCountRequest request) {
        // Idempotency check
        Optional<StockCount> existing = stockCountRepository.findByClientReferenceId(request.clientReferenceId());
        if (existing.isPresent()) {
            StockCount count = existing.get();
            validateReplayPayload(count, request);
            return new IdempotentCreateResult<>(toResponse(count), false);
        }

        // XOR location checks
        if ((request.warehouseId() != null && request.storeId() != null) ||
            (request.warehouseId() == null && request.storeId() == null)) {
            throw new BusinessException("Stock count must reference exactly one location (Warehouse or Store)");
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        // Validate location belongs to company
        Warehouse warehouse = null;
        Store store = null;
        Long locCompanyId;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            locCompanyId = warehouse.getRegion().getCompany().getId();
        } else {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            locCompanyId = store.getRegion().getCompany().getId();
        }

        if (!locCompanyId.equals(company.getId())) {
            throw new BusinessException("Referenced location must belong to the stock count company");
        }

        // Pessimistic lock active counts at this location
        List<StockCount> activeCounts = stockCountRepository.findActiveCountsForLocationWithLock(
                Arrays.asList(StockCountStatus.DRAFT, StockCountStatus.ASSIGNED, StockCountStatus.IN_PROGRESS,
                              StockCountStatus.SUBMITTED, StockCountStatus.REJECTED, StockCountStatus.APPROVED),
                request.warehouseId(), request.storeId()
        );

        // Overlapping product set check
        boolean hasOverlap = false;
        if (!activeCounts.isEmpty()) {
            if (request.countType() == StockCountType.FULL) {
                hasOverlap = true;
            } else {
                for (StockCount active : activeCounts) {
                    if (active.getCountType() == StockCountType.FULL) {
                        hasOverlap = true;
                        break;
                    }
                    // Both are CYCLE, check intersection of products
                    Set<Long> activeProductIds = new HashSet<>();
                    for (StockCountItem item : active.getItems()) {
                        activeProductIds.add(item.getProduct().getId());
                    }
                    if (request.cycleProductIds() != null) {
                        for (Long pid : request.cycleProductIds()) {
                            if (activeProductIds.contains(pid)) {
                                hasOverlap = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (hasOverlap) {
            throw new BusinessException("An active stock count already exists for this location and overlapping product set");
        }

        // Resolve products to be counted
        List<Product> products = new ArrayList<>();
        if (request.countType() == StockCountType.FULL) {
            List<InventoryStock> stocks = request.warehouseId() != null
                    ? inventoryStockRepository.findByWarehouseId(request.warehouseId())
                    : inventoryStockRepository.findByStoreId(request.storeId());
            for (InventoryStock stock : stocks) {
                Product p = stock.getProduct();
                if (Boolean.TRUE.equals(p.getActive())) {
                    products.add(p);
                }
            }
        } else {
            if (request.cycleProductIds() == null || request.cycleProductIds().isEmpty()) {
                throw new BusinessException("Cycle product list cannot be empty for cycle counts");
            }
            Set<Long> uniquePids = new HashSet<>(request.cycleProductIds());
            for (Long pid : uniquePids) {
                Product p = productRepository.findById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + pid));
                if (!Boolean.TRUE.equals(p.getActive())) {
                    throw new BusinessException("Product " + p.getName() + " is inactive");
                }
                products.add(p);
            }
        }

        BigDecimal threshold = request.approvalThresholdPercentage() != null
                ? request.approvalThresholdPercentage()
                : BigDecimal.valueOf(5.00);

        StockCount count = new StockCount();
        count.setCompany(company);
        count.setWarehouse(warehouse);
        count.setStore(store);
        count.setStatus(StockCountStatus.DRAFT);
        count.setCountType(request.countType());
        count.setBlindCount(request.blindCount() == null || request.blindCount());
        count.setApprovalThresholdPercentage(threshold);
        count.setClientReferenceId(request.clientReferenceId());
        count.setRemarks(request.remarks());
        count.setCreatedBy(getCurrentUser());
        count.setRecountNumber(0);
        count.setCountNumber(generateCountNumber());

        List<StockCountItem> items = new ArrayList<>();
        for (Product p : products) {
            StockCountItem item = new StockCountItem();
            item.setStockCount(count);
            item.setProduct(p);
            item.setSystemQuantity(BigDecimal.ZERO);
            item.setReservedQuantity(BigDecimal.ZERO);
            item.setAvailableQuantity(BigDecimal.ZERO);
            items.add(item);
        }
        count.setItems(items);

        StockCount saved = stockCountRepository.save(count);
        return new IdempotentCreateResult<>(toResponse(saved), true);
    }

    /**
     * Updates an existing count record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the StockCountResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public StockCountResponse updateCount(Long id, StockCountUpdateRequest request) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.DRAFT) {
            throw new BusinessException("Only DRAFT counts can be updated. Current status: " + count.getStatus());
        }

        // XOR location checks
        if ((request.warehouseId() != null && request.storeId() != null) ||
            (request.warehouseId() == null && request.storeId() == null)) {
            throw new BusinessException("Stock count must reference exactly one location (Warehouse or Store)");
        }

        Warehouse warehouse = null;
        Store store = null;
        Long locCompanyId;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            locCompanyId = warehouse.getRegion().getCompany().getId();
        } else {
            store = storeRepository.findById(request.storeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.storeId()));
            locCompanyId = store.getRegion().getCompany().getId();
        }

        if (!locCompanyId.equals(count.getCompany().getId())) {
            throw new BusinessException("Referenced location must belong to the stock count company");
        }

        // Lock active counts at location
        List<StockCount> activeCounts = stockCountRepository.findActiveCountsForLocationWithLock(
                Arrays.asList(StockCountStatus.DRAFT, StockCountStatus.ASSIGNED, StockCountStatus.IN_PROGRESS,
                              StockCountStatus.SUBMITTED, StockCountStatus.REJECTED, StockCountStatus.APPROVED),
                request.warehouseId(), request.storeId()
        );

        boolean hasOverlap = false;
        if (!activeCounts.isEmpty()) {
            if (request.countType() == StockCountType.FULL) {
                hasOverlap = true;
            } else {
                for (StockCount active : activeCounts) {
                    if (active.getId().equals(count.getId())) {
                        continue;
                    }
                    if (active.getCountType() == StockCountType.FULL) {
                        hasOverlap = true;
                        break;
                    }
                    Set<Long> activeProductIds = new HashSet<>();
                    for (StockCountItem item : active.getItems()) {
                        activeProductIds.add(item.getProduct().getId());
                    }
                    if (request.cycleProductIds() != null) {
                        for (Long pid : request.cycleProductIds()) {
                            if (activeProductIds.contains(pid)) {
                                hasOverlap = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (hasOverlap) {
            throw new BusinessException("An active stock count already exists for this location and overlapping product set");
        }

        List<Product> products = new ArrayList<>();
        if (request.countType() == StockCountType.FULL) {
            List<InventoryStock> stocks = request.warehouseId() != null
                    ? inventoryStockRepository.findByWarehouseId(request.warehouseId())
                    : inventoryStockRepository.findByStoreId(request.storeId());
            for (InventoryStock stock : stocks) {
                Product p = stock.getProduct();
                if (Boolean.TRUE.equals(p.getActive())) {
                    products.add(p);
                }
            }
        } else {
            if (request.cycleProductIds() == null || request.cycleProductIds().isEmpty()) {
                throw new BusinessException("Cycle product list cannot be empty for cycle counts");
            }
            Set<Long> uniquePids = new HashSet<>(request.cycleProductIds());
            for (Long pid : uniquePids) {
                Product p = productRepository.findById(pid)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + pid));
                if (!Boolean.TRUE.equals(p.getActive())) {
                    throw new BusinessException("Product " + p.getName() + " is inactive");
                }
                products.add(p);
            }
        }

        count.setWarehouse(warehouse);
        count.setStore(store);
        count.setCountType(request.countType());
        count.setBlindCount(request.blindCount() == null || request.blindCount());
        if (request.approvalThresholdPercentage() != null) {
            count.setApprovalThresholdPercentage(request.approvalThresholdPercentage());
        }
        count.setRemarks(request.remarks());

        // Recreate items
        count.getItems().clear();
        for (Product p : products) {
            StockCountItem item = new StockCountItem();
            item.setStockCount(count);
            item.setProduct(p);
            item.setSystemQuantity(BigDecimal.ZERO);
            item.setReservedQuantity(BigDecimal.ZERO);
            item.setAvailableQuantity(BigDecimal.ZERO);
            count.getItems().add(item);
        }

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Retrieves a single count by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single count by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public StockCountResponse getCountById(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));
        return toResponse(count);
    }

    /**
     * Returns a filtered paginated list of counts records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of counts records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<StockCountResponse> searchCounts(StockCountSearchRequest searchRequest, Pageable pageable) {
        Specification<StockCount> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.warehouseId() != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), searchRequest.warehouseId()));
            }
            if (searchRequest.storeId() != null) {
                predicates.add(cb.equal(root.get("store").get("id"), searchRequest.storeId()));
            }
            if (searchRequest.countType() != null) {
                predicates.add(cb.equal(root.get("countType"), searchRequest.countType()));
            }
            if (searchRequest.countNumber() != null && !searchRequest.countNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("countNumber")), "%" + searchRequest.countNumber().toLowerCase() + "%"));
            }
            if (searchRequest.clientReferenceId() != null) {
                predicates.add(cb.equal(root.get("clientReferenceId"), searchRequest.clientReferenceId()));
            }
            if (searchRequest.createdAtFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), searchRequest.createdAtFrom().atStartOfDay()));
            }
            if (searchRequest.createdAtTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), searchRequest.createdAtTo().plusDays(1).atStartOfDay()));
            }
            if (searchRequest.createdBy() != null) {
                predicates.add(cb.equal(root.get("createdBy").get("id"), searchRequest.createdBy()));
            }
            if (searchRequest.assignedTo() != null) {
                predicates.add(cb.equal(root.get("assignedTo").get("id"), searchRequest.assignedTo()));
            }
            if (searchRequest.productId() != null) {
                predicates.add(cb.equal(root.join("items").get("product").get("id"), searchRequest.productId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<StockCount> page = stockCountRepository.findAll(spec, pageable);
        List<StockCountResponse> content = page.getContent().stream()
                .map(this::toResponse)
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
     * Performs the assignCount operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param userId authenticated user identifier
     * @return the StockCountResponse result
     */
    @Override
    @Transactional
    public StockCountResponse assignCount(Long id, Long userId) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.DRAFT && count.getStatus() != StockCountStatus.ASSIGNED) {
            throw new BusinessException("Cannot assign count in current status: " + count.getStatus());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BusinessException("User is inactive");
        }

        count.setAssignedTo(user);
        count.setAssignedBy(getCurrentUser());
        count.setAssignedAt(LocalDateTime.now());
        count.setStatus(StockCountStatus.ASSIGNED);

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Performs the startCount operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     */
    @Override
    @Transactional
    public StockCountResponse startCount(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() == StockCountStatus.IN_PROGRESS) {
            return toResponse(count); // Already started (idempotent)
        }

        if (count.getStatus() != StockCountStatus.ASSIGNED) {
            throw new BusinessException("Only ASSIGNED counts can be started. Current status: " + count.getStatus());
        }

        count.setStatus(StockCountStatus.IN_PROGRESS);
        count.setStartedBy(getCurrentUser());
        count.setStartedAt(LocalDateTime.now());

        // Snapshot current stock levels
        for (StockCountItem item : count.getItems()) {
            InventoryStock stock = getOrLockStock(
                    item.getProduct().getId(),
                    count.getWarehouse() != null ? count.getWarehouse().getId() : null,
                    count.getStore() != null ? count.getStore().getId() : null,
                    true
            );
            item.setSystemQuantity(stock.getQuantity());
            item.setReservedQuantity(stock.getReservedQuantity());
            item.setAvailableQuantity(stock.getQuantity().subtract(stock.getReservedQuantity()));
        }

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Submits the count for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the StockCountResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public StockCountResponse submitCount(Long id, StockCountSubmitRequest request) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.IN_PROGRESS) {
            throw new BusinessException("Only IN_PROGRESS counts can be submitted. Current status: " + count.getStatus());
        }

        Map<Long, BigDecimal> countedMap = new HashMap<>();
        for (StockCountItemCountRequest itemReq : request.items()) {
            if (itemReq.countedQuantity() == null || itemReq.countedQuantity().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Counted quantity must be non-negative");
            }
            countedMap.put(itemReq.productId(), itemReq.countedQuantity());
        }

        // Ensure all items are counted
        for (StockCountItem item : count.getItems()) {
            BigDecimal counted = countedMap.get(item.getProduct().getId());
            if (counted == null) {
                throw new BusinessException("Missing counted quantity for product ID: " + item.getProduct().getId());
            }
            item.setCountedQuantity(counted);
            item.setVariance(counted.subtract(item.getSystemQuantity()));
        }

        // Determine if approval is required based on threshold percentage
        boolean approvalRequired = false;
        for (StockCountItem item : count.getItems()) {
            BigDecimal systemVal = item.getSystemQuantity();
            BigDecimal countedVal = item.getCountedQuantity();
            BigDecimal varianceVal = item.getVariance();
            BigDecimal varPct;

            if (systemVal.compareTo(BigDecimal.ZERO) == 0) {
                varPct = varianceVal.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(100.00);
            } else {
                varPct = countedVal.subtract(systemVal).abs()
                        .divide(systemVal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100.00));
            }

            if (varPct.compareTo(count.getApprovalThresholdPercentage()) > 0) {
                approvalRequired = true;
            }
        }

        count.setStatus(StockCountStatus.SUBMITTED);
        count.setApprovalRequired(approvalRequired);
        count.setSubmittedBy(getCurrentUser());
        count.setSubmittedAt(LocalDateTime.now());

        if (!approvalRequired) {
            count.setStatus(StockCountStatus.APPROVED);
            count.setApprovedBy(getCurrentUser());
            count.setApprovedAt(LocalDateTime.now());
            createAdjustmentForVariances(count);
            recordStockCountTraceEvents(count);
        }

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Performs the rejectCount operation in this module.
     *
     * @param id the unique database ID of the resource
     * @param reason the reason input value
     * @return the StockCountResponse result
     */
    @Override
    @Transactional
    public StockCountResponse rejectCount(Long id, String reason) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED counts can be rejected. Current status: " + count.getStatus());
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Rejection reason is required");
        }

        count.setStatus(StockCountStatus.REJECTED);
        count.setRejectionReason(reason);

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Performs the reopenCount operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     */
    @Override
    @Transactional
    public StockCountResponse reopenCount(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.REJECTED) {
            throw new BusinessException("Only REJECTED counts can be reopened. Current status: " + count.getStatus());
        }

        count.setStatus(StockCountStatus.IN_PROGRESS);
        count.setRecountNumber(count.getRecountNumber() + 1);
        count.setRejectionReason(null);

        // Reset counted quantities and variances but preserve original system snapshots
        for (StockCountItem item : count.getItems()) {
            item.setCountedQuantity(null);
            item.setVariance(null);
        }

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Approves the count, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public StockCountResponse approveCount(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED counts can be approved. Current status: " + count.getStatus());
        }

        count.setStatus(StockCountStatus.APPROVED);
        count.setApprovedBy(getCurrentUser());
        count.setApprovedAt(LocalDateTime.now());

        createAdjustmentForVariances(count);
        recordStockCountTraceEvents(count);

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Posts count entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public StockCountResponse postCount(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.APPROVED) {
            throw new BusinessException("Only APPROVED counts can be posted. Current status: " + count.getStatus());
        }

        count.setStatus(StockCountStatus.POSTED);
        count.setPostedBy(getCurrentUser());
        count.setPostedAt(LocalDateTime.now());

        if (count.getAdjustment() != null) {
            // Apply physical stock updates and publish refresh events
            inventoryAdjustmentService.postAdjustment(count.getAdjustment().getId());
        } else {
            // No variances/adjustment, publish refresh directly
            eventPublisher.publishEvent(new InventoryAdjustmentRefreshEvent(this));
        }

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    /**
     * Completes the count workflow and finalizes the record status.
     *
     * @param id the unique database ID of the resource
     * @return the StockCountResponse result
     */
    @Override
    @Transactional
    public StockCountResponse closeCount(Long id) {
        StockCount count = stockCountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock Count not found with ID: " + id));

        if (count.getStatus() != StockCountStatus.POSTED) {
            throw new BusinessException("Only POSTED counts can be closed. Current status: " + count.getStatus());
        }

        count.setStatus(StockCountStatus.CLOSED);
        count.setClosedBy(getCurrentUser());
        count.setClosedAt(LocalDateTime.now());

        StockCount saved = stockCountRepository.save(count);
        return toResponse(saved);
    }

    private void createAdjustmentForVariances(StockCount count) {
        if (count.getAdjustment() != null) {
            return; // Already populated
        }

        List<StockCountItem> varianceItems = count.getItems().stream()
                .filter(item -> item.getVariance() != null && item.getVariance().compareTo(BigDecimal.ZERO) != 0)
                .toList();

        if (varianceItems.isEmpty()) {
            return; // No variance to adjust
        }

        InventoryAdjustment adjustment = new InventoryAdjustment();
        adjustment.setCompany(count.getCompany());
        adjustment.setWarehouse(count.getWarehouse());
        adjustment.setStore(count.getStore());
        adjustment.setAdjustmentType(InventoryAdjustmentType.STOCK_COUNT_VARIANCE);
        adjustment.setStatus(InventoryAdjustmentStatus.APPROVED);
        adjustment.setClientReferenceId(UUID.randomUUID());
        adjustment.setRemarks("Created automatically from Stock Count: " + count.getCountNumber());
        adjustment.setCreatedBy(getCurrentUser());
        adjustment.setApprovedBy(getCurrentUser());
        adjustment.setApprovedAt(LocalDateTime.now());

        List<InventoryAdjustmentItem> adjItems = new ArrayList<>();
        for (StockCountItem sci : varianceItems) {
            InventoryAdjustmentItem adjItem = new InventoryAdjustmentItem();
            adjItem.setInventoryAdjustment(adjustment);
            adjItem.setProduct(sci.getProduct());
            adjItem.setQuantity(sci.getVariance());
            adjItems.add(adjItem);
        }
        adjustment.setItems(adjItems);
        adjustment.setAdjustmentNumber(generateAdjustmentNumber());

        InventoryAdjustment savedAdjustment = inventoryAdjustmentRepository.save(adjustment);
        count.setAdjustment(savedAdjustment);
    }

    private InventoryStock getOrLockStock(Long productId, Long warehouseId, Long storeId, boolean lock) {
        if (warehouseId != null) {
            return lock
                    ? inventoryStockRepository.findWithPessimisticWriteByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseGet(() -> createDefaultStock(productId, warehouseId, null))
                    : inventoryStockRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                    .orElseGet(() -> createDefaultStock(productId, warehouseId, null));
        } else if (storeId != null) {
            return lock
                    ? inventoryStockRepository.findWithPessimisticWriteByProductIdAndStoreId(productId, storeId)
                    .orElseGet(() -> createDefaultStock(productId, null, storeId))
                    : inventoryStockRepository.findByProductIdAndStoreId(productId, storeId)
                    .orElseGet(() -> createDefaultStock(productId, null, storeId));
        }
        throw new BusinessException("Valid location ID must be specified");
    }

    private InventoryStock createDefaultStock(Long productId, Long warehouseId, Long storeId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        InventoryStock stock = new InventoryStock();
        stock.setProduct(product);
        if (warehouseId != null) {
            stock.setWarehouse(warehouseRepository.getReferenceById(warehouseId));
        }
        if (storeId != null) {
            stock.setStore(storeRepository.getReferenceById(storeId));
        }
        stock.setQuantity(BigDecimal.ZERO);
        stock.setReservedQuantity(BigDecimal.ZERO);
        return inventoryStockRepository.save(stock);
    }

    private synchronized String generateAdjustmentNumber() {
        Long seqVal = inventoryAdjustmentRepository.getNextSequenceValue();
        return String.format("ADJ-%d-%06d", LocalDate.now().getYear(), seqVal);
    }

    private synchronized String generateCountNumber() {
        Long seqVal = stockCountRepository.getNextSequenceValue();
        return String.format("CNT-%d-%06d", LocalDate.now().getYear(), seqVal);
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

    private void validateReplayPayload(StockCount count, StockCountRequest request) {
        if (!count.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Idempotent replay mismatch: company ID does not match");
        }
        if (request.warehouseId() != null && (count.getWarehouse() == null || !count.getWarehouse().getId().equals(request.warehouseId()))) {
            throw new BusinessException("Idempotent replay mismatch: warehouse mismatch");
        }
        if (request.storeId() != null && (count.getStore() == null || !count.getStore().getId().equals(request.storeId()))) {
            throw new BusinessException("Idempotent replay mismatch: store mismatch");
        }
        if (count.getCountType() != request.countType()) {
            throw new BusinessException("Idempotent replay mismatch: count type mismatch");
        }
    }

    private StockCountResponse toResponse(StockCount entity) {
        StockCountResponse raw = stockCountMapper.toResponse(entity);
        if (entity.isBlindCount() && (
                entity.getStatus() == StockCountStatus.DRAFT ||
                entity.getStatus() == StockCountStatus.ASSIGNED ||
                entity.getStatus() == StockCountStatus.IN_PROGRESS
        )) {
            List<StockCountItemResponse> maskedItems = raw.items().stream()
                    .map(item -> new StockCountItemResponse(
                            item.id(),
                            item.productId(),
                            item.productCode(),
                            item.productName(),
                            null, // Hide systemQuantity
                            null, // Hide reservedQuantity
                            null, // Hide availableQuantity
                            item.countedQuantity(),
                            null, // Hide variance
                            item.version()
                    ))
                    .toList();
            return new StockCountResponse(
                    raw.id(),
                    raw.countNumber(),
                    raw.companyId(),
                    raw.warehouseId(),
                    raw.storeId(),
                    raw.status(),
                    raw.countType(),
                    raw.blindCount(),
                    raw.assignedToId(),
                    raw.assignedToName(),
                    raw.adjustmentId(),
                    raw.approvalRequired(),
                    raw.approvalThresholdPercentage(),
                    raw.clientReferenceId(),
                    raw.remarks(),
                    raw.rejectionReason(),
                    raw.recountNumber(),
                    raw.createdById(),
                    raw.createdAt(),
                    raw.assignedById(),
                    raw.assignedAt(),
                    raw.startedById(),
                    raw.startedAt(),
                    raw.submittedById(),
                    raw.submittedAt(),
                    raw.approvedById(),
                    raw.approvedAt(),
                    raw.postedById(),
                    raw.postedAt(),
                    raw.closedById(),
                    raw.closedAt(),
                    maskedItems,
                    raw.version()
            );
        }
        return raw;
    }

    private void recordStockCountTraceEvents(StockCount count) {
        for (StockCountItem item : count.getItems()) {
            if (item.getVariance() != null && item.getVariance().compareTo(BigDecimal.ZERO) != 0) {
                inventoryTraceabilityService.recordTraceEvent(
                        count.getCompany().getId(),
                        item.getProduct().getId(),
                        null,
                        null,
                        count.getWarehouse() != null ? count.getWarehouse().getId() : null,
                        count.getStore() != null ? count.getStore().getId() : null,
                        com.plus33.erp.inventory.entity.InventoryTraceEventType.COUNT_VARIANCE,
                        item.getVariance(),
                        com.plus33.erp.inventory.entity.InventoryTraceReferenceType.STOCK_COUNT,
                        count.getId(),
                        count.getCountNumber(),
                        "Variance: " + item.getVariance() + " (Counted: " + item.getCountedQuantity() + ", System: " + item.getSystemQuantity() + ")"
                );
            }
        }
    }
}