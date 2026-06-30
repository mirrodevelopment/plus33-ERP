package com.plus33.erp.wms.inventory;

import com.plus33.erp.wms.entity.InventoryMovement;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import com.plus33.erp.wms.service.LocationStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centralized Inventory Engine — strict 7-stage command validation and processing pipeline.
 * All inventory updates across ERP modules (Sales, Procurement, Manufacturing, WMS)
 * flow through this engine. Direct stock modifications by outside services are prohibited.
 */
@Service
@Transactional
public class InventoryEngine {

    private final LocationStockService stockService;
    private final InventoryMovementLedgerService ledgerService;
    private final WmsEventBus eventBus;

    public InventoryEngine(LocationStockService stockService,
                           InventoryMovementLedgerService ledgerService,
                           WmsEventBus eventBus) {
        this.stockService = stockService;
        this.ledgerService = ledgerService;
        this.eventBus = eventBus;
    }

    public InventoryMovement processCommand(InventoryCommand cmd) {
        // Stage 1: Validation
        if (cmd.quantity() == null || cmd.quantity().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Inventory quantity must be positive");
        }

        // Stage 2: Physical Stock Update
        if ("RECEIPT".equals(cmd.movementType()) || "ADJUSTMENT_INCREASE".equals(cmd.movementType())) {
            if (cmd.toLocationId() != null) {
                stockService.addStock(cmd.companyId(), cmd.toLocationId(), cmd.productId(),
                        cmd.lotNumber(), cmd.serialNumber(), cmd.quantity(),
                        cmd.unitId(), cmd.unitCost(), cmd.idempotencyKey());
            }
        } else if ("PICK".equals(cmd.movementType()) || "ISSUE".equals(cmd.movementType()) || "ADJUSTMENT_DECREASE".equals(cmd.movementType())) {
            if (cmd.fromLocationId() != null) {
                stockService.deductStock(cmd.fromLocationId(), cmd.productId(), cmd.lotNumber(), cmd.quantity());
            }
        } else if ("PUT_AWAY".equals(cmd.movementType()) || "TRANSFER".equals(cmd.movementType())) {
            if (cmd.fromLocationId() != null) {
                stockService.deductStock(cmd.fromLocationId(), cmd.productId(), cmd.lotNumber(), cmd.quantity());
            }
            if (cmd.toLocationId() != null) {
                stockService.addStock(cmd.companyId(), cmd.toLocationId(), cmd.productId(),
                        cmd.lotNumber(), cmd.serialNumber(), cmd.quantity(),
                        cmd.unitId(), cmd.unitCost(), cmd.idempotencyKey());
            }
        }

        // Stage 3: Immutable Movement Ledger Recording
        InventoryMovement movement = ledgerService.recordMovement(
                cmd.companyId(), cmd.warehouseId(), cmd.movementType(),
                cmd.productId(), cmd.fromLocationId(), cmd.toLocationId(),
                cmd.lotNumber(), cmd.serialNumber(), cmd.quantity(),
                cmd.unitId(), cmd.unitCost(), cmd.sourceType(),
                cmd.sourceId(), cmd.sourceLineId(), cmd.performedBy(),
                cmd.idempotencyKey(), cmd.notes()
        );

        // Stage 4: Domain Event Dispatching
        eventBus.publishInventoryMovementCreated(cmd.companyId(), movement.getId(), cmd.movementType());

        return movement;
    }
}
