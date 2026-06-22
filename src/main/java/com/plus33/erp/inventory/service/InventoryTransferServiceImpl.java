package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.event.InventoryTransferRefreshEvent;
import com.plus33.erp.inventory.mapper.InventoryTransferMapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class InventoryTransferServiceImpl implements InventoryTransferService {

    private final InventoryTransferRepository inventoryTransferRepository;
    private final InventoryTransferItemRepository inventoryTransferItemRepository;
    private final InventoryStockRepository inventoryStockRepository;
    private final StockMovementRepository stockMovementRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryTransferMapper inventoryTransferMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryTraceabilityService inventoryTraceabilityService;

    public InventoryTransferServiceImpl(
            InventoryTransferRepository inventoryTransferRepository,
            InventoryTransferItemRepository inventoryTransferItemRepository,
            InventoryStockRepository inventoryStockRepository,
            StockMovementRepository stockMovementRepository,
            CompanyRepository companyRepository,
            WarehouseRepository warehouseRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            InventoryTransferMapper inventoryTransferMapper,
            ApplicationEventPublisher eventPublisher,
            InventoryTraceabilityService inventoryTraceabilityService) {
        this.inventoryTransferRepository = inventoryTransferRepository;
        this.inventoryTransferItemRepository = inventoryTransferItemRepository;
        this.inventoryStockRepository = inventoryStockRepository;
        this.stockMovementRepository = stockMovementRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryTransferMapper = inventoryTransferMapper;
        this.eventPublisher = eventPublisher;
        this.inventoryTraceabilityService = inventoryTraceabilityService;
    }

    @Override
    @Transactional
    public IdempotentCreateResult<InventoryTransferResponse> createTransfer(InventoryTransferRequest request) {
        // Idempotency check
        Optional<InventoryTransfer> existing = inventoryTransferRepository.findByClientReferenceId(request.clientReferenceId());
        if (existing.isPresent()) {
            InventoryTransfer transfer = existing.get();
            validateReplayPayload(transfer, request);
            return new IdempotentCreateResult<>(inventoryTransferMapper.toResponse(transfer), false);
        }

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        if (!company.getActive()) {
            throw new BusinessException("Company is inactive");
        }

        InventoryTransfer transfer = new InventoryTransfer();
        transfer.setCompany(company);
        transfer.setClientReferenceId(request.clientReferenceId());
        transfer.setRemarks(request.remarks());
        transfer.setStatus(InventoryTransferStatus.DRAFT);
        transfer.setCreatedBy(getCurrentUser());

        setLocationsAndValidate(transfer, request.sourceWarehouseId(), request.sourceStoreId(),
                request.destWarehouseId(), request.destStoreId());

        List<InventoryTransferItem> items = new ArrayList<>();
        Set<Long> productIds = new HashSet<>();

        for (InventoryTransferItemRequest itemReq : request.items()) {
            if (!productIds.add(itemReq.productId())) {
                throw new BusinessException("Duplicate product ID in items list: " + itemReq.productId());
            }

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Product " + product.getName() + " is inactive");
            }

            InventoryTransferItem item = new InventoryTransferItem();
            item.setInventoryTransfer(transfer);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setReceivedQuantity(BigDecimal.ZERO);
            item.setRemainingQuantity(itemReq.quantity());
            items.add(item);
        }

        transfer.setItems(items);
        transfer.setTransferNumber(generateTransferNumber());

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);
        return new IdempotentCreateResult<>(inventoryTransferMapper.toResponse(saved), true);
    }

    @Override
    @Transactional
    public InventoryTransferResponse updateTransfer(Long id, InventoryTransferUpdateRequest request) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() != InventoryTransferStatus.DRAFT) {
            throw new BusinessException("Only DRAFT transfers can be updated. Current status: " + transfer.getStatus());
        }

        transfer.setRemarks(request.remarks());
        setLocationsAndValidate(transfer, request.sourceWarehouseId(), request.sourceStoreId(),
                request.destWarehouseId(), request.destStoreId());

        // Recreate items
        transfer.getItems().clear();
        Set<Long> productIds = new HashSet<>();

        for (InventoryTransferItemRequest itemReq : request.items()) {
            if (!productIds.add(itemReq.productId())) {
                throw new BusinessException("Duplicate product ID in items list: " + itemReq.productId());
            }

            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemReq.productId()));
            if (!product.getActive()) {
                throw new BusinessException("Product " + product.getName() + " is inactive");
            }

            InventoryTransferItem item = new InventoryTransferItem();
            item.setInventoryTransfer(transfer);
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setReceivedQuantity(BigDecimal.ZERO);
            item.setRemainingQuantity(itemReq.quantity());
            transfer.getItems().add(item);
        }

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);
        return inventoryTransferMapper.toResponse(saved);
    }

    @Override
    public InventoryTransferResponse getTransferById(Long id) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));
        return inventoryTransferMapper.toResponse(transfer);
    }

    @Override
    public PageResponse<InventoryTransferResponse> searchTransfers(InventoryTransferSearchRequest searchRequest, Pageable pageable) {
        Specification<InventoryTransfer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.status() != null) {
                predicates.add(cb.equal(root.get("status"), searchRequest.status()));
            }
            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }
            if (searchRequest.sourceWarehouseId() != null) {
                predicates.add(cb.equal(root.get("sourceWarehouse").get("id"), searchRequest.sourceWarehouseId()));
            }
            if (searchRequest.sourceStoreId() != null) {
                predicates.add(cb.equal(root.get("sourceStore").get("id"), searchRequest.sourceStoreId()));
            }
            if (searchRequest.destWarehouseId() != null) {
                predicates.add(cb.equal(root.get("destWarehouse").get("id"), searchRequest.destWarehouseId()));
            }
            if (searchRequest.destStoreId() != null) {
                predicates.add(cb.equal(root.get("destStore").get("id"), searchRequest.destStoreId()));
            }
            if (searchRequest.transferNumber() != null && !searchRequest.transferNumber().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("transferNumber")), "%" + searchRequest.transferNumber().toLowerCase() + "%"));
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
            if (searchRequest.productId() != null) {
                predicates.add(cb.equal(root.join("items").get("product").get("id"), searchRequest.productId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<InventoryTransfer> page = inventoryTransferRepository.findAll(spec, pageable);
        List<InventoryTransferResponse> content = page.getContent().stream()
                .map(inventoryTransferMapper::toResponse)
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
    public InventoryTransferResponse submitTransfer(Long id) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() != InventoryTransferStatus.DRAFT) {
            throw new BusinessException("Only DRAFT transfers can be submitted. Current status: " + transfer.getStatus());
        }

        if (transfer.getItems().isEmpty()) {
            throw new BusinessException("Transfer must contain at least one line item");
        }

        transfer.setStatus(InventoryTransferStatus.SUBMITTED);
        transfer.setSubmittedBy(getCurrentUser());
        transfer.setSubmittedAt(LocalDateTime.now());

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);
        return inventoryTransferMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryTransferResponse approveTransfer(Long id) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() != InventoryTransferStatus.SUBMITTED) {
            throw new BusinessException("Only SUBMITTED transfers can be approved. Current status: " + transfer.getStatus());
        }

        User currentUser = getCurrentUser();

        // Stock reservation logic
        for (InventoryTransferItem item : transfer.getItems()) {
            InventoryStock stock = getOrLockStock(item.getProduct().getId(),
                    transfer.getSourceWarehouse() != null ? transfer.getSourceWarehouse().getId() : null,
                    transfer.getSourceStore() != null ? transfer.getSourceStore().getId() : null,
                    true);

            BigDecimal available = stock.getQuantity().subtract(stock.getReservedQuantity());
            if (available.compareTo(item.getQuantity()) < 0) {
                throw new BusinessException("Insufficient stock for product " + item.getProduct().getName()
                        + ". Available: " + available + ", Requested: " + item.getQuantity());
            }

            stock.setReservedQuantity(stock.getReservedQuantity().add(item.getQuantity()));
            inventoryStockRepository.save(stock);

            createStockMovement(item.getProduct(), transfer.getSourceWarehouse(), transfer.getSourceStore(),
                    item.getQuantity(), transfer, "TRANSFER_RESERVE", currentUser);
        }

        transfer.setStatus(InventoryTransferStatus.APPROVED);
        transfer.setApprovedBy(currentUser);
        transfer.setApprovedAt(LocalDateTime.now());

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);
        eventPublisher.publishEvent(new InventoryTransferRefreshEvent(this));

        return inventoryTransferMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryTransferResponse dispatchTransfer(Long id) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() != InventoryTransferStatus.APPROVED) {
            throw new BusinessException("Only APPROVED transfers can be dispatched. Current status: " + transfer.getStatus());
        }

        User currentUser = getCurrentUser();

        // Source stock deduction
        for (InventoryTransferItem item : transfer.getItems()) {
            InventoryStock stock = getOrLockStock(item.getProduct().getId(),
                    transfer.getSourceWarehouse() != null ? transfer.getSourceWarehouse().getId() : null,
                    transfer.getSourceStore() != null ? transfer.getSourceStore().getId() : null,
                    true);

            // Safety checks
            if (stock.getQuantity().compareTo(item.getQuantity()) < 0) {
                throw new BusinessException("Database state error: stock quantity is less than dispatch quantity for product: " + item.getProduct().getName());
            }
            if (stock.getReservedQuantity().compareTo(item.getQuantity()) < 0) {
                throw new BusinessException("Database state error: reserved quantity is less than dispatch quantity for product: " + item.getProduct().getName());
            }

            stock.setQuantity(stock.getQuantity().subtract(item.getQuantity()));
            stock.setReservedQuantity(stock.getReservedQuantity().subtract(item.getQuantity()));
            inventoryStockRepository.save(stock);

            createStockMovement(item.getProduct(), transfer.getSourceWarehouse(), transfer.getSourceStore(),
                    item.getQuantity(), transfer, "TRANSFER_OUT", currentUser);
        }

        transfer.setStatus(InventoryTransferStatus.IN_TRANSIT);
        transfer.setDispatchedBy(currentUser);
        transfer.setDispatchedAt(LocalDateTime.now());

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);

        // Record trace events
        for (InventoryTransferItem item : saved.getItems()) {
            inventoryTraceabilityService.recordTraceEvent(
                    saved.getCompany().getId(),
                    item.getProduct().getId(),
                    null, // lotId
                    null, // serialId
                    saved.getSourceWarehouse() != null ? saved.getSourceWarehouse().getId() : null,
                    saved.getSourceStore() != null ? saved.getSourceStore().getId() : null,
                    com.plus33.erp.inventory.entity.InventoryTraceEventType.TRANSFER_OUT,
                    item.getQuantity().negate(),
                    com.plus33.erp.inventory.entity.InventoryTraceReferenceType.INVENTORY_TRANSFER,
                    saved.getId(),
                    saved.getTransferNumber(),
                    "Dispatched via Transfer #" + saved.getTransferNumber()
            );
        }

        eventPublisher.publishEvent(new InventoryTransferRefreshEvent(this));

        return inventoryTransferMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryTransferResponse receiveTransfer(Long id) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() != InventoryTransferStatus.IN_TRANSIT) {
            throw new BusinessException("Only IN_TRANSIT transfers can be received. Current status: " + transfer.getStatus());
        }

        User currentUser = getCurrentUser();

        // Destination stock increment
        for (InventoryTransferItem item : transfer.getItems()) {
            InventoryStock destStock = getOrLockStock(item.getProduct().getId(),
                    transfer.getDestWarehouse() != null ? transfer.getDestWarehouse().getId() : null,
                    transfer.getDestStore() != null ? transfer.getDestStore().getId() : null,
                    true);

            destStock.setQuantity(destStock.getQuantity().add(item.getQuantity()));
            inventoryStockRepository.save(destStock);

            item.setReceivedQuantity(item.getQuantity());
            item.setRemainingQuantity(BigDecimal.ZERO);
            inventoryTransferItemRepository.save(item);

            createStockMovement(item.getProduct(), transfer.getDestWarehouse(), transfer.getDestStore(),
                    item.getQuantity(), transfer, "TRANSFER_IN", currentUser);
        }

        // V24.1 transitions IN_TRANSIT -> RECEIVED -> CLOSED automatically
        transfer.setStatus(InventoryTransferStatus.CLOSED);
        transfer.setReceivedBy(currentUser);
        transfer.setReceivedAt(LocalDateTime.now());

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);

        // Record trace events
        for (InventoryTransferItem item : saved.getItems()) {
            inventoryTraceabilityService.recordTraceEvent(
                    saved.getCompany().getId(),
                    item.getProduct().getId(),
                    null, // lotId
                    null, // serialId
                    saved.getDestWarehouse() != null ? saved.getDestWarehouse().getId() : null,
                    saved.getDestStore() != null ? saved.getDestStore().getId() : null,
                    com.plus33.erp.inventory.entity.InventoryTraceEventType.TRANSFER_IN,
                    item.getQuantity(),
                    com.plus33.erp.inventory.entity.InventoryTraceReferenceType.INVENTORY_TRANSFER,
                    saved.getId(),
                    saved.getTransferNumber(),
                    "Received via Transfer #" + saved.getTransferNumber()
            );
        }

        eventPublisher.publishEvent(new InventoryTransferRefreshEvent(this));

        return inventoryTransferMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryTransferResponse cancelTransfer(Long id, String reason) {
        InventoryTransfer transfer = inventoryTransferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory Transfer not found with ID: " + id));

        if (transfer.getStatus() == InventoryTransferStatus.IN_TRANSIT ||
            transfer.getStatus() == InventoryTransferStatus.RECEIVED ||
            transfer.getStatus() == InventoryTransferStatus.CLOSED) {
            throw new BusinessException("Cannot cancel transfer after dispatch has occurred. Current status: " + transfer.getStatus());
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessException("Cancellation reason is required");
        }

        User currentUser = getCurrentUser();

        // Release reservations if approved
        if (transfer.getStatus() == InventoryTransferStatus.APPROVED) {
            for (InventoryTransferItem item : transfer.getItems()) {
                InventoryStock stock = getOrLockStock(item.getProduct().getId(),
                        transfer.getSourceWarehouse() != null ? transfer.getSourceWarehouse().getId() : null,
                        transfer.getSourceStore() != null ? transfer.getSourceStore().getId() : null,
                        true);

                if (stock.getReservedQuantity().compareTo(item.getQuantity()) < 0) {
                    throw new BusinessException("Database state error: reserved quantity is less than release quantity for product: " + item.getProduct().getName());
                }

                stock.setReservedQuantity(stock.getReservedQuantity().subtract(item.getQuantity()));
                inventoryStockRepository.save(stock);

                createStockMovement(item.getProduct(), transfer.getSourceWarehouse(), transfer.getSourceStore(),
                        item.getQuantity(), transfer, "TRANSFER_RELEASE", currentUser);
            }
        }

        transfer.setStatus(InventoryTransferStatus.CANCELLED);
        transfer.setCancelledBy(currentUser);
        transfer.setCancelledAt(LocalDateTime.now());
        transfer.setCancellationReason(reason);

        InventoryTransfer saved = inventoryTransferRepository.save(transfer);
        eventPublisher.publishEvent(new InventoryTransferRefreshEvent(this));

        return inventoryTransferMapper.toResponse(saved);
    }

    private void validateReplayPayload(InventoryTransfer transfer, InventoryTransferRequest request) {
        if (!transfer.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Idempotent replay mismatch: company ID does not match");
        }
        if (request.sourceWarehouseId() != null && (transfer.getSourceWarehouse() == null || !transfer.getSourceWarehouse().getId().equals(request.sourceWarehouseId()))) {
            throw new BusinessException("Idempotent replay mismatch: source warehouse mismatch");
        }
        if (request.sourceStoreId() != null && (transfer.getSourceStore() == null || !transfer.getSourceStore().getId().equals(request.sourceStoreId()))) {
            throw new BusinessException("Idempotent replay mismatch: source store mismatch");
        }
        if (request.destWarehouseId() != null && (transfer.getDestWarehouse() == null || !transfer.getDestWarehouse().getId().equals(request.destWarehouseId()))) {
            throw new BusinessException("Idempotent replay mismatch: dest warehouse mismatch");
        }
        if (request.destStoreId() != null && (transfer.getDestStore() == null || !transfer.getDestStore().getId().equals(request.destStoreId()))) {
            throw new BusinessException("Idempotent replay mismatch: dest store mismatch");
        }
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

    private void createStockMovement(Product product, Warehouse warehouse, Store store, BigDecimal quantity,
                                     InventoryTransfer transfer, String movementType, User user) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setStore(store);
        movement.setMovementType(movementType);
        movement.setQuantity(quantity);
        movement.setReferenceNo(transfer.getTransferNumber());
        movement.setReferenceType(StockMovementReferenceType.INVENTORY_TRANSFER);
        movement.setReferenceId(transfer.getId());
        movement.setReferenceNumber(transfer.getTransferNumber());
        movement.setCreatedBy(user);
        stockMovementRepository.save(movement);
    }

    private void setLocationsAndValidate(InventoryTransfer transfer, Long srcWhId, Long srcStId, Long destWhId, Long destStId) {
        // XOR checks
        if ((srcWhId != null && srcStId != null) || (srcWhId == null && srcStId == null)) {
            throw new BusinessException("Source must be either a Warehouse or a Store");
        }
        if ((destWhId != null && destStId != null) || (destWhId == null && destStId == null)) {
            throw new BusinessException("Destination must be either a Warehouse or a Store");
        }

        // Store to Warehouse prohibition
        if (srcStId != null && destWhId != null) {
            throw new BusinessException("Store-to-Warehouse transfers are prohibited");
        }

        // Inequality checks
        if (srcWhId != null && destWhId != null && srcWhId.equals(destWhId)) {
            throw new BusinessException("Source and destination warehouses cannot be the same");
        }
        if (srcStId != null && destStId != null && srcStId.equals(destStId)) {
            throw new BusinessException("Source and destination stores cannot be the same");
        }

        Warehouse srcWh = srcWhId != null ? warehouseRepository.findById(srcWhId)
                .orElseThrow(() -> new ResourceNotFoundException("Source Warehouse not found with ID: " + srcWhId)) : null;
        Store srcSt = srcStId != null ? storeRepository.findById(srcStId)
                .orElseThrow(() -> new ResourceNotFoundException("Source Store not found with ID: " + srcStId)) : null;

        Warehouse destWh = destWhId != null ? warehouseRepository.findById(destWhId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination Warehouse not found with ID: " + destWhId)) : null;
        Store destSt = destStId != null ? storeRepository.findById(destStId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination Store not found with ID: " + destStId)) : null;

        // Company match validation
        Long srcCompId = srcWh != null ? srcWh.getRegion().getCompany().getId() 
                : (srcSt != null ? srcSt.getRegion().getCompany().getId() : null);
        Long destCompId = destWh != null ? destWh.getRegion().getCompany().getId() 
                : (destSt != null ? destSt.getRegion().getCompany().getId() : null);

        if (srcCompId == null || destCompId == null || !srcCompId.equals(transfer.getCompany().getId()) || !destCompId.equals(transfer.getCompany().getId())) {
            throw new BusinessException("Source and destination locations must belong to the transfer company");
        }

        transfer.setSourceWarehouse(srcWh);
        transfer.setSourceStore(srcSt);
        transfer.setDestWarehouse(destWh);
        transfer.setDestStore(destSt);
    }

    private synchronized String generateTransferNumber() {
        Long seqVal = inventoryTransferRepository.getNextSequenceValue();
        return String.format("TRF-%d-%06d", LocalDate.now().getYear(), seqVal);
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
