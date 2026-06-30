package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.InventoryMovement;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.InventoryMovementRepository;
import com.plus33.erp.wms.repository.WarehouseLocationRepository;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Records immutable inventory movement ledger entries.
 * Every call to {@code recordMovement} produces exactly one {@link InventoryMovement} row.
 * Idempotency keys prevent duplicate rows on retry — if a key already exists,
 * the existing record is returned without modification.
 */
@Service
@Transactional
public class InventoryMovementLedgerServiceImpl implements InventoryMovementLedgerService {

    private final InventoryMovementRepository movementRepo;
    private final WarehouseLocationRepository locationRepo;
    private final WmsEventBus eventBus;

    public InventoryMovementLedgerServiceImpl(InventoryMovementRepository movementRepo,
                                               WarehouseLocationRepository locationRepo,
                                               WmsEventBus eventBus) {
        this.movementRepo = movementRepo;
        this.locationRepo = locationRepo;
        this.eventBus = eventBus;
    }

    @Override
    public InventoryMovement recordMovement(Long companyId, Long warehouseId, String movementType,
                                             Long productId, Long fromLocationId, Long toLocationId,
                                             String lotNumber, String serialNumber, BigDecimal quantity,
                                             Long unitId, BigDecimal unitCost, String sourceType,
                                             Long sourceId, Long sourceLineId, Long performedBy,
                                             String idempotencyKey, String notes) {
        // Idempotency: return existing record if key already used
        if (idempotencyKey != null) {
            var existing = movementRepo.findByCompanyIdAndIdempotencyKey(companyId, idempotencyKey);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        InventoryMovement movement = new InventoryMovement();
        movement.setCompanyId(companyId);
        movement.setWarehouseId(warehouseId);
        movement.setMovementType(movementType);
        movement.setProductId(productId);
        movement.setLotNumber(lotNumber);
        movement.setSerialNumber(serialNumber);
        movement.setQuantity(quantity);
        movement.setUnitId(unitId);
        movement.setUnitCost(unitCost);
        movement.setSourceType(sourceType);
        movement.setSourceId(sourceId);
        movement.setSourceLineId(sourceLineId);
        movement.setPerformedBy(performedBy);
        movement.setIdempotencyKey(idempotencyKey);
        movement.setNotes(notes);

        if (unitCost != null) {
            movement.setTotalCost(unitCost.multiply(quantity));
        }

        if (fromLocationId != null) {
            locationRepo.findById(fromLocationId).ifPresent(movement::setFromLocation);
        }
        if (toLocationId != null) {
            locationRepo.findById(toLocationId).ifPresent(movement::setToLocation);
        }

        InventoryMovement saved = movementRepo.save(movement);
        eventBus.publishInventoryMovementCreated(companyId, saved.getId(), movementType);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> findByProduct(Long companyId, Long productId) {
        return movementRepo.findByCompanyIdAndProductIdOrderByMovementAtDesc(companyId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryMovement> findBySourceDocument(String sourceType, Long sourceId) {
        return movementRepo.findBySourceDocument(sourceType, sourceId);
    }
}
