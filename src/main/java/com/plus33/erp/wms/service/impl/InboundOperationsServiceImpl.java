/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : InboundOperationsServiceImpl.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InboundOperationsController
 * Related Service   : InboundOperationsServiceImpl
 * Related Repository: AdvanceShippingNoticeRepository, PutAwayWorkRepository
 * Related Entity    : InboundOperations
 * Related DTO       : N/A
 * Related Mapper    : InboundOperationsMapper
 * Related DB Table  : inbound_operationss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InboundOperationsController, InboundOperationsServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements InboundOperationsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.*;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.*;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import com.plus33.erp.wms.service.LocationStockService;
import com.plus33.erp.wms.strategy.WarehouseStrategyRegistry;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Inbound Operations Service — handles ASN creation, arrival check-in,
 * and directed put-away task generation using the {@link WarehouseStrategyRegistry}.
 */
@Service
@Transactional
public class InboundOperationsServiceImpl {

    private final AdvanceShippingNoticeRepository asnRepo;
    private final PutAwayWorkRepository putAwayRepo;
    private final LocationStockService stockService;
    private final InventoryMovementLedgerService ledgerService;
    private final WarehouseStrategyRegistry strategyRegistry;
    private final WmsEventBus eventBus;

    public InboundOperationsServiceImpl(AdvanceShippingNoticeRepository asnRepo,
                                         PutAwayWorkRepository putAwayRepo,
                                         LocationStockService stockService,
                                         InventoryMovementLedgerService ledgerService,
                                         WarehouseStrategyRegistry strategyRegistry,
                                         WmsEventBus eventBus) {
        this.asnRepo = asnRepo;
        this.putAwayRepo = putAwayRepo;
        this.stockService = stockService;
        this.ledgerService = ledgerService;
        this.strategyRegistry = strategyRegistry;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new asn and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param asn the asn input value
     * @return the AdvanceShippingNotice result
     * @throws BusinessException if a business rule is violated
     */
    public AdvanceShippingNotice createAsn(AdvanceShippingNotice asn) {
        return asnRepo.save(asn);
    }

    /**
     * Validates business rules and constraints for in.
     *
     * @param asnId the asnId input value
     * @param dockDoorId the dockDoorId input value
     * @param checkinId the checkinId input value
     * @return the AdvanceShippingNotice result
     * @throws BusinessException if a business rule is violated
     */
    public AdvanceShippingNotice checkIn(Long asnId, Long dockDoorId, Long checkinId) {
        AdvanceShippingNotice asn = asnRepo.findById(asnId)
                .orElseThrow(() -> new EntityNotFoundException("ASN not found: " + asnId));
        asn.setStatus("ARRIVED");
        asn.setActualArrival(LocalDateTime.now());
        asn.setDockDoorId(dockDoorId);
        asn.setCheckinId(checkinId);
        AdvanceShippingNotice saved = asnRepo.save(asn);
        eventBus.publishAsnReceived(asn.getCompanyId(), asnId, asn.getWarehouseId());
        return saved;
    }

    /**
     * Generates a directed put-away task for each ASN line using the strategy registry.
     * Also receives stock into the staging (source) location.
     */
    public List<PutAwayWork> generatePutAwayTasks(Long asnId, String strategyKey,
                                                    Long stagingLocationId, Long receivedByUserId) {
        AdvanceShippingNotice asn = asnRepo.findById(asnId)
                .orElseThrow(() -> new EntityNotFoundException("ASN not found: " + asnId));

        var strategy = strategyRegistry.putAway(strategyKey);

        return asn.getLines().stream()
                .filter(line -> !"CANCELLED".equals(line.getStatus()))
                .map(line -> {
                    // Resolve target location via strategy
                    var targetLocation = strategy.resolve(line, asn.getWarehouseId(), asn.getCompanyId())
                            .orElse(null);

                    // Receive stock into staging location first
                    stockService.addStock(asn.getCompanyId(), stagingLocationId, line.getProductId(),
                            line.getLotNumber(), line.getSerialNumber(), line.getExpectedQty(),
                            line.getUnitId(), line.getUnitCost(),
                            "ASN-" + asnId + "-LINE-" + line.getId() + "-RECEIPT");

                    // Record RECEIPT movement in ledger
                    ledgerService.recordMovement(asn.getCompanyId(), asn.getWarehouseId(),
                            "RECEIPT", line.getProductId(), null, stagingLocationId,
                            line.getLotNumber(), line.getSerialNumber(), line.getExpectedQty(),
                            line.getUnitId(), line.getUnitCost(),
                            "ASN", asnId, line.getId(), receivedByUserId,
                            "RECV-" + asnId + "-" + line.getId(), null);

                    // Create put-away work ticket
                    PutAwayWork pw = new PutAwayWork();
                    pw.setCompanyId(asn.getCompanyId());
                    pw.setWarehouseId(asn.getWarehouseId());
                    pw.setAsnId(asnId);
                    pw.setAsnLineId(line.getId());
                    pw.setProductId(line.getProductId());
                    pw.setLotNumber(line.getLotNumber());
                    pw.setSerialNumber(line.getSerialNumber());
                    pw.setQuantity(line.getExpectedQty());
                    pw.setUnitId(line.getUnitId());
                    pw.setStrategyUsed(strategyKey);
                    if (targetLocation != null) {
                        pw.setTargetLocation(targetLocation);
                    }

                    line.setStatus("RECEIVED");
                    return putAwayRepo.save(pw);
                })
                .toList();
    }

    /**
     * Completes the put away workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param putAwayWorkId the putAwayWorkId input value
     * @param operatorId the operatorId input value
     * @return the PutAwayWork result
     */
    public PutAwayWork completePutAway(Long putAwayWorkId, Long operatorId) {
        PutAwayWork pw = putAwayRepo.findById(putAwayWorkId)
                .orElseThrow(() -> new EntityNotFoundException("Put-away work not found: " + putAwayWorkId));

        if (pw.getTargetLocation() != null) {
            // Move stock from staging to target bin
            stockService.addStock(pw.getCompanyId(), pw.getTargetLocation().getId(), pw.getProductId(),
                    pw.getLotNumber(), pw.getSerialNumber(), pw.getQuantity(), pw.getUnitId(),
                    null, "PA-" + putAwayWorkId + "-MOVE");

            // Record PUT_AWAY movement
            Long fromLocId = pw.getSourceLocation() != null ? pw.getSourceLocation().getId() : null;
            ledgerService.recordMovement(pw.getCompanyId(), pw.getWarehouseId(), "PUT_AWAY",
                    pw.getProductId(), fromLocId, pw.getTargetLocation().getId(),
                    pw.getLotNumber(), pw.getSerialNumber(), pw.getQuantity(),
                    pw.getUnitId(), null, "PUT_AWAY_WORK", putAwayWorkId, null,
                    operatorId, "PA-COMP-" + putAwayWorkId, null);
        }

        pw.setStatus("COMPLETED");
        pw.setCompletedAt(LocalDateTime.now());
        pw.setAssignedTo(operatorId);
        PutAwayWork saved = putAwayRepo.save(pw);
        eventBus.publishPutAwayCompleted(pw.getCompanyId(), putAwayWorkId,
                pw.getTargetLocation() != null ? pw.getTargetLocation().getId() : null);
        return saved;
    }

    /**
     * Retrieves by warehouse and status data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param warehouseId the warehouseId input value
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional(readOnly = true)
    public List<AdvanceShippingNotice> findByWarehouseAndStatus(Long companyId, Long warehouseId, String status) {
        return asnRepo.findByCompanyIdAndWarehouseIdAndStatus(companyId, warehouseId, status);
    }
}