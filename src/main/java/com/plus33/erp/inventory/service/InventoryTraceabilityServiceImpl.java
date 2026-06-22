package com.plus33.erp.inventory.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import com.plus33.erp.inventory.event.InventoryTraceabilityRefreshEvent;
import com.plus33.erp.inventory.mapper.InventoryTraceabilityMapper;
import com.plus33.erp.inventory.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryTraceabilityServiceImpl implements InventoryTraceabilityService {

    private final InventoryLotRepository inventoryLotRepository;
    private final InventorySerialRepository inventorySerialRepository;
    private final InventoryTraceEventRepository inventoryTraceEventRepository;
    private final InventoryRecallRepository inventoryRecallRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final InventoryTraceabilityMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public InventoryLotResponse createLot(InventoryLotRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.manufacturedDate() != null && request.expiryDate().isBefore(request.manufacturedDate())) {
            throw new BusinessException("Expiry date cannot be before manufactured date");
        }

        Optional<InventoryLot> existing = inventoryLotRepository.findByCompanyIdAndProductIdAndLotNumber(
                request.companyId(), request.productId(), request.lotNumber());
        if (existing.isPresent()) {
            throw new BusinessException("Lot number already exists for this product");
        }

        InventoryLot lot = InventoryLot.builder()
                .company(company)
                .product(product)
                .lotNumber(request.lotNumber())
                .expiryDate(request.expiryDate())
                .manufacturedDate(request.manufacturedDate())
                .status(request.status() != null ? request.status() : InventoryLotStatus.ACTIVE)
                .build();

        lot = inventoryLotRepository.save(lot);

        eventPublisher.publishEvent(new InventoryTraceabilityRefreshEvent(this));

        return mapper.toResponse(lot);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryLotResponse getLotById(Long id) {
        InventoryLot lot = inventoryLotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));
        return mapper.toResponse(lot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryLotResponse> getAllLots() {
        return inventoryLotRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventorySerialResponse> getAllSerials() {
        return inventorySerialRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void transitionSerialStatus(Long serialId, InventorySerialStatus newStatus, Long warehouseId, Long storeId) {
        InventorySerial serial = inventorySerialRepository.findById(serialId)
                .orElseThrow(() -> new ResourceNotFoundException("Serial not found"));
        InventorySerialStatus oldStatus = serial.getStatus();

        if (oldStatus == newStatus) {
            updateSerialLocation(serial, warehouseId, storeId);
            inventorySerialRepository.save(serial);
            return;
        }

        boolean allowed = false;
        if (oldStatus == InventorySerialStatus.IN_STOCK) {
            allowed = (newStatus == InventorySerialStatus.TRANSIT ||
                       newStatus == InventorySerialStatus.SOLD ||
                       newStatus == InventorySerialStatus.RECALLED ||
                       newStatus == InventorySerialStatus.QUARANTINED);
        } else if (oldStatus == InventorySerialStatus.TRANSIT) {
            allowed = (newStatus == InventorySerialStatus.IN_STOCK);
        } else if (oldStatus == InventorySerialStatus.QUARANTINED) {
            allowed = (newStatus == InventorySerialStatus.IN_STOCK);
        }

        if (!allowed) {
            throw new BusinessException("Invalid serial status transition from " + oldStatus + " to " + newStatus);
        }

        serial.setStatus(newStatus);
        updateSerialLocation(serial, warehouseId, storeId);
        inventorySerialRepository.save(serial);

        eventPublisher.publishEvent(new InventoryTraceabilityRefreshEvent(this));
    }

    private void updateSerialLocation(InventorySerial serial, Long warehouseId, Long storeId) {
        if (warehouseId != null && storeId != null) {
            throw new BusinessException("Serial cannot be assigned to both a warehouse and a store");
        }
        if (warehouseId != null) {
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));
            serial.setWarehouse(warehouse);
            serial.setStore(null);
        } else if (storeId != null) {
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
            serial.setStore(store);
            serial.setWarehouse(null);
        } else {
            serial.setWarehouse(null);
            serial.setStore(null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTraceEventResponse> getProductTrace(Long productId) {
        return inventoryTraceEventRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTraceEventResponse> getLotTrace(String lotNumber) {
        return inventoryTraceEventRepository.findByLotLotNumberOrderByCreatedAtDesc(lotNumber).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryTraceEventResponse> getSerialTrace(String serialNumber) {
        return inventoryTraceEventRepository.findBySerialSerialNumberOrderByCreatedAtDesc(serialNumber).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InventoryRecallResponse createRecall(InventoryRecallRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        InventoryLot lot = null;
        if (request.lotId() != null) {
            lot = inventoryLotRepository.findById(request.lotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lot not found"));
            if (!lot.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Recall company must match target lot company");
            }
            if (inventoryRecallRepository.existsByLotIdAndStatus(request.lotId(), InventoryRecallStatus.ACTIVE)) {
                throw new BusinessException("Active recall already exists for this lot");
            }
        }

        InventorySerial serial = null;
        if (request.serialId() != null) {
            serial = inventorySerialRepository.findById(request.serialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Serial not found"));
            if (!serial.getCompany().getId().equals(company.getId())) {
                throw new BusinessException("Recall company must match target serial company");
            }
            if (inventoryRecallRepository.existsBySerialIdAndStatus(request.serialId(), InventoryRecallStatus.ACTIVE)) {
                throw new BusinessException("Active recall already exists for this serial");
            }
        }

        if (lot != null && serial != null) {
            throw new BusinessException("Recall cannot be assigned to both a lot and a serial");
        }

        User recalledBy = getCurrentUser()
                .orElseThrow(() -> new BusinessException("Authenticated user required to create recall"));

        Long seq = inventoryRecallRepository.getNextSequenceValue();
        String recallNumber = String.format("REC-%s-%04d", 
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
                seq);

        InventoryRecall recall = InventoryRecall.builder()
                .company(company)
                .product(product)
                .lot(lot)
                .serial(serial)
                .recallNumber(recallNumber)
                .recallReason(request.recallReason())
                .recallReferenceNumber(request.recallReferenceNumber())
                .status(InventoryRecallStatus.ACTIVE)
                .recalledBy(recalledBy)
                .recalledAt(LocalDateTime.now())
                .build();

        recall = inventoryRecallRepository.save(recall);

        // Quarantine lot and associated serials
        if (lot != null) {
            lot.setStatus(InventoryLotStatus.RECALLED);
            inventoryLotRepository.save(lot);

            recordTraceEventInternal(company, product, lot, null, null, null, 
                    InventoryTraceEventType.RECALL, BigDecimal.ZERO, 
                    InventoryTraceReferenceType.INVENTORY_RECALL, recall.getId(), recall.getRecallNumber(), 
                    "Lot recalled: " + request.recallReason(), recalledBy);

            List<InventorySerial> serials = inventorySerialRepository.findByLotId(lot.getId());
            for (InventorySerial s : serials) {
                s.setStatus(InventorySerialStatus.RECALLED);
                inventorySerialRepository.save(s);

                recordTraceEventInternal(company, product, lot, s, s.getWarehouse(), s.getStore(), 
                        InventoryTraceEventType.RECALL, BigDecimal.ONE, 
                        InventoryTraceReferenceType.INVENTORY_RECALL, recall.getId(), recall.getRecallNumber(), 
                        "Serial recalled via lot: " + request.recallReason(), recalledBy);
            }
        }

        if (serial != null) {
            serial.setStatus(InventorySerialStatus.RECALLED);
            inventorySerialRepository.save(serial);

            recordTraceEventInternal(company, product, serial.getLot(), serial, serial.getWarehouse(), serial.getStore(), 
                    InventoryTraceEventType.RECALL, BigDecimal.ONE, 
                    InventoryTraceReferenceType.INVENTORY_RECALL, recall.getId(), recall.getRecallNumber(), 
                    "Serial recalled: " + request.recallReason(), recalledBy);
        }

        // General product recall without specific lot/serial
        if (lot == null && serial == null) {
            recordTraceEventInternal(company, product, null, null, null, null, 
                    InventoryTraceEventType.RECALL, BigDecimal.ZERO, 
                    InventoryTraceReferenceType.INVENTORY_RECALL, recall.getId(), recall.getRecallNumber(), 
                    "General product recalled: " + request.recallReason(), recalledBy);
        }

        eventPublisher.publishEvent(new InventoryTraceabilityRefreshEvent(this));

        return mapper.toResponse(recall);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryRecallResponse> getAllRecalls() {
        return inventoryRecallRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryLotResponse> getLotsForFefoAllocation(Long productId) {
        return inventoryLotRepository.findByProductIdAndStatusOrderByExpiryDateAsc(productId, InventoryLotStatus.ACTIVE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 15 1 * * *")
    @Override
    @Transactional
    public void processExpiredLots() {
        List<InventoryLot> expiredLots = inventoryLotRepository.findActiveExpiredLots(InventoryLotStatus.ACTIVE, LocalDate.now());
        for (InventoryLot lot : expiredLots) {
            lot.setStatus(InventoryLotStatus.EXPIRED);
            inventoryLotRepository.save(lot);

            recordTraceEventInternal(lot.getCompany(), lot.getProduct(), lot, null, null, null, 
                    InventoryTraceEventType.EXPIRED, BigDecimal.ZERO, 
                    InventoryTraceReferenceType.INVENTORY_RECALL, lot.getId(), lot.getLotNumber(), 
                    "System auto-expired batch", null);
        }
        if (!expiredLots.isEmpty()) {
            eventPublisher.publishEvent(new InventoryTraceabilityRefreshEvent(this));
        }
    }

    @Override
    @Transactional
    public void recordTraceEvent(
            Long companyId,
            Long productId,
            Long lotId,
            Long serialId,
            Long warehouseId,
            Long storeId,
            InventoryTraceEventType eventType,
            BigDecimal quantity,
            InventoryTraceReferenceType referenceType,
            Long referenceId,
            String referenceNumber,
            String notes
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        InventoryLot lot = lotId != null ? inventoryLotRepository.findById(lotId).orElse(null) : null;
        InventorySerial serial = serialId != null ? inventorySerialRepository.findById(serialId).orElse(null) : null;
        Warehouse warehouse = warehouseId != null ? warehouseRepository.findById(warehouseId).orElse(null) : null;
        Store store = storeId != null ? storeRepository.findById(storeId).orElse(null) : null;

        User createdBy = getCurrentUser().orElse(null);

        recordTraceEventInternal(company, product, lot, serial, warehouse, store, 
                eventType, quantity, referenceType, referenceId, referenceNumber, notes, createdBy);
    }

    private void recordTraceEventInternal(
            Company company,
            Product product,
            InventoryLot lot,
            InventorySerial serial,
            Warehouse warehouse,
            Store store,
            InventoryTraceEventType eventType,
            BigDecimal quantity,
            InventoryTraceReferenceType referenceType,
            Long referenceId,
            String referenceNumber,
            String notes,
            User createdBy
    ) {
        InventoryTraceEvent traceEvent = InventoryTraceEvent.builder()
                .company(company)
                .product(product)
                .lot(lot)
                .serial(serial)
                .warehouse(warehouse)
                .store(store)
                .eventType(eventType)
                .quantity(quantity)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .referenceNumber(referenceNumber)
                .notes(notes)
                .createdBy(createdBy)
                .build();

        inventoryTraceEventRepository.save(traceEvent);
    }

    private Optional<User> getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return Optional.empty();
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(email)) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }
}
