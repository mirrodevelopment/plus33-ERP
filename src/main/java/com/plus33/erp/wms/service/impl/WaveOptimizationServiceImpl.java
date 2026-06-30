package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.*;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.*;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import com.plus33.erp.wms.service.LocationStockService;
import com.plus33.erp.wms.strategy.WarehouseStrategyRegistry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Wave Optimization Service — creates waves, allocates stock using the picking
 * strategy, and manages the directed pick list lifecycle.
 *
 * <p>Concurrent picking guard: PickingWork uses {@code @Version} optimistic locking.
 * If two pickers claim the same task simultaneously, a
 * {@link ObjectOptimisticLockingFailureException} is thrown at flush time, which
 * the caller should catch and retry.</p>
 */
@Service
@Transactional
public class WaveOptimizationServiceImpl {

    private final WaveRepository waveRepo;
    private final PickingWorkRepository pickingWorkRepo;
    private final InventoryMovementLedgerService ledgerService;
    private final LocationStockService stockService;
    private final WarehouseStrategyRegistry strategyRegistry;
    private final WmsEventBus eventBus;

    public WaveOptimizationServiceImpl(WaveRepository waveRepo,
                                        PickingWorkRepository pickingWorkRepo,
                                        InventoryMovementLedgerService ledgerService,
                                        LocationStockService stockService,
                                        WarehouseStrategyRegistry strategyRegistry,
                                        WmsEventBus eventBus) {
        this.waveRepo = waveRepo;
        this.pickingWorkRepo = pickingWorkRepo;
        this.ledgerService = ledgerService;
        this.stockService = stockService;
        this.strategyRegistry = strategyRegistry;
        this.eventBus = eventBus;
    }

    public Wave createWave(Wave wave) {
        Wave saved = waveRepo.save(wave);
        eventBus.publishWaveCreated(wave.getCompanyId(), saved.getId(), wave.getWarehouseId());
        return saved;
    }

    /**
     * Adds a picking work item to a wave for a given source document line.
     */
    public PickingWork addPickingWork(Long waveId, Long companyId, String sourceType,
                                      Long sourceId, Long sourceLineId, Long productId,
                                      String lotNumber, String serialNumber, BigDecimal qty, Long unitId) {
        Wave wave = waveRepo.findById(waveId)
                .orElseThrow(() -> new EntityNotFoundException("Wave not found: " + waveId));

        // Allocate from best bin using the wave's picking strategy
        var strategy = strategyRegistry.pickingWithFallback(wave.getPickingStrategy(), "FEFO");
        PickingWork mockWork = buildMockPw(companyId, wave, sourceType, sourceId, sourceLineId,
                productId, lotNumber, serialNumber, qty, unitId);
        var candidates = strategy.allocate(mockWork, wave.getWarehouseId(), companyId);

        WarehouseLocation fromLocation = candidates.isEmpty() ? null : candidates.get(0).getLocation();
        if (fromLocation != null) {
            stockService.reserveStock(fromLocation.getId(), productId, lotNumber, qty);
        }

        mockWork.setFromLocation(fromLocation);
        mockWork.setStrategyUsed(wave.getPickingStrategy());
        return pickingWorkRepo.save(mockWork);
    }

    /**
     * Releases the wave — transitions status to RELEASED and sets the released timestamp.
     */
    public Wave releaseWave(Long waveId) {
        Wave wave = waveRepo.findById(waveId)
                .orElseThrow(() -> new EntityNotFoundException("Wave not found: " + waveId));
        wave.setStatus("RELEASED");
        wave.setReleasedAt(LocalDateTime.now());
        return waveRepo.save(wave);
    }

    /**
     * Confirms a picking work completion.
     * Records a PICK movement in the immutable ledger and deducts stock from the bin.
     */
    public PickingWork confirmPick(Long pickingWorkId, BigDecimal pickedQty, Long operatorId) {
        PickingWork pw = pickingWorkRepo.findById(pickingWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Picking work not found: " + pickingWorkId));

        if (pw.getFromLocation() != null) {
            stockService.deductStock(pw.getFromLocation().getId(), pw.getProductId(),
                    pw.getLotNumber(), pickedQty);

            ledgerService.recordMovement(pw.getCompanyId(),
                    pw.getWave().getWarehouseId(), "PICK",
                    pw.getProductId(), pw.getFromLocation().getId(),
                    pw.getToLocation() != null ? pw.getToLocation().getId() : null,
                    pw.getLotNumber(), pw.getSerialNumber(), pickedQty,
                    pw.getUnitId(), null, "PICKING_WORK", pickingWorkId, null,
                    operatorId, "PICK-" + pickingWorkId + "-" + System.currentTimeMillis(), null);
        }

        pw.setPickedQuantity(pw.getPickedQuantity().add(pickedQty));
        pw.setStartedAt(pw.getStartedAt() == null ? LocalDateTime.now() : pw.getStartedAt());

        boolean fullyPicked = pw.getPickedQuantity().compareTo(pw.getPickQuantity()) >= 0;
        pw.setStatus(fullyPicked ? "COMPLETED" : "PARTIALLY_PICKED");
        if (fullyPicked) {
            pw.setCompletedAt(LocalDateTime.now());
        }

        PickingWork saved = pickingWorkRepo.save(pw);
        if (fullyPicked) {
            eventBus.publishPickingCompleted(pw.getCompanyId(), pickingWorkId);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<PickingWork> getOpenPicksByWave(Long waveId) {
        return pickingWorkRepo.findOpenByWave(waveId);
    }

    private PickingWork buildMockPw(Long companyId, Wave wave, String sourceType, Long sourceId,
                                     Long sourceLineId, Long productId, String lotNumber,
                                     String serialNumber, BigDecimal qty, Long unitId) {
        PickingWork pw = new PickingWork();
        pw.setCompanyId(companyId);
        pw.setWave(wave);
        pw.setSourceType(sourceType);
        pw.setSourceId(sourceId);
        pw.setSourceLineId(sourceLineId);
        pw.setProductId(productId);
        pw.setLotNumber(lotNumber);
        pw.setSerialNumber(serialNumber);
        pw.setPickQuantity(qty);
        pw.setUnitId(unitId);
        pw.setStatus("PENDING");
        return pw;
    }
}
