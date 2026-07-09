/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : VanInventoryService.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: VanInventoryController
 * Related Service   : VanInventoryService
 * Related Repository: VanStockRepository, WorkOrderTaskRepository
 * Related Entity    : VanInventory
 * Related DTO       : N/A
 * Related Mapper    : VanInventoryMapper
 * Related DB Table  : van_inventorys
 * Related REST APIs : N/A
 * Depends On        : Wms Module
 * Used By           : VanInventoryController, VanInventoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements VanInventoryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.VanStock;
import com.plus33.erp.esm.entity.WorkOrderTask;
import com.plus33.erp.esm.repository.VanStockRepository;
import com.plus33.erp.esm.repository.WorkOrderTaskRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code VanInventoryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * VanInventoryController
 *   --> VanInventoryService (this)
 *   --> Validate business rules
 *   --> VanInventoryRepository (read/write 'van_inventorys')
 *   --> VanInventoryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code van_inventorys}</p>
 * <p><b>Module Deps      :</b> Esm, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class VanInventoryService {

    private final VanStockRepository vanStockRepository;
    private final WorkOrderTaskRepository taskRepository;
    private final InventoryMovementLedgerService wmsLedgerService;
    private final EsmEventBus eventBus;

    public VanInventoryService(VanStockRepository vanStockRepository,
                               WorkOrderTaskRepository taskRepository,
                               InventoryMovementLedgerService wmsLedgerService,
                               EsmEventBus eventBus) {
        this.vanStockRepository = vanStockRepository;
        this.taskRepository = taskRepository;
        this.wmsLedgerService = wmsLedgerService;
        this.eventBus = eventBus;
    }

    /**
     * Reserves parts resources (budget or stock) for downstream processing.
     *
     * @param workOrderId the workOrderId input value
     * @param productId the productId input value
     * @param quantity the quantity input value
     */
    @Transactional
    public void reserveParts(Long workOrderId, Long productId, BigDecimal quantity) {
        eventBus.publish("PartsReserved", 1L, workOrderId, "Reserved " + quantity + " units of product " + productId);
    }

    /**
     * Performs the transferToVan operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param vanId the vanId input value
     * @param productId the productId input value
     * @param quantity the quantity input value
     * @param unitId the unitId input value
     */
    @Transactional
    public void transferToVan(Long companyId, Long vanId, Long productId, BigDecimal quantity, Long unitId) {
        // Record movement in WMS ledger
        wmsLedgerService.recordMovement(
                companyId,
                1L, // Default warehouse ID
                "TRANSFER_OUT",
                productId,
                1L, // Source location ID
                vanId, // Destination location ID (represented by van ID)
                "LOT-DEFAULT",
                null,
                quantity,
                unitId,
                BigDecimal.TEN,
                "WORK_ORDER",
                vanId,
                null,
                1L,
                "TRANS-VAN-" + System.currentTimeMillis() + "-" + productId,
                "Transfer to van"
        );

        Optional<VanStock> stockOpt = vanStockRepository.findByCompanyIdAndVanIdAndProductId(companyId, vanId, productId);
        VanStock stock;
        if (stockOpt.isPresent()) {
            stock = stockOpt.get();
            stock.setQuantityOnHand(stock.getQuantityOnHand().add(quantity));
        } else {
            stock = new VanStock();
            stock.setCompanyId(companyId);
            stock.setVanId(vanId);
            stock.setProductId(productId);
            stock.setQuantityOnHand(quantity);
            stock.setUnitId(unitId);
        }
        vanStockRepository.save(stock);
    }

    /**
     * Performs the consumeParts operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param vanId the vanId input value
     * @param workOrderId the workOrderId input value
     * @param productId the productId input value
     * @param quantity the quantity input value
     * @param unitId the unitId input value
     */
    @Transactional
    public void consumeParts(Long companyId, Long vanId, Long workOrderId, Long productId, BigDecimal quantity, Long unitId) {
        VanStock stock = vanStockRepository.findByCompanyIdAndVanIdAndProductId(companyId, vanId, productId)
                .orElseThrow(() -> new IllegalArgumentException("No stock in van for product " + productId));

        if (stock.getQuantityOnHand().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Insufficient parts in van!");
        }

        stock.setQuantityOnHand(stock.getQuantityOnHand().subtract(quantity));
        vanStockRepository.save(stock);

        wmsLedgerService.recordMovement(
                companyId,
                1L,
                "CONSUMPTION",
                productId,
                vanId,
                null,
                "LOT-DEFAULT",
                null,
                quantity,
                unitId,
                BigDecimal.TEN,
                "WORK_ORDER",
                workOrderId,
                null,
                1L,
                "CONS-WO-" + System.currentTimeMillis() + "-" + productId,
                "Consumed in Work Order"
        );

        eventBus.publish("PartsConsumed", companyId, workOrderId, "Consumed " + quantity + " of product " + productId);
    }

    /**
     * Performs the returnParts operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param vanId the vanId input value
     * @param productId the productId input value
     * @param quantity the quantity input value
     * @param unitId the unitId input value
     */
    @Transactional
    public void returnParts(Long companyId, Long vanId, Long productId, BigDecimal quantity, Long unitId) {
        VanStock stock = vanStockRepository.findByCompanyIdAndVanIdAndProductId(companyId, vanId, productId)
                .orElseThrow(() -> new IllegalArgumentException("No stock in van for product " + productId));

        if (stock.getQuantityOnHand().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Insufficient parts to return!");
        }

        stock.setQuantityOnHand(stock.getQuantityOnHand().subtract(quantity));
        vanStockRepository.save(stock);

        wmsLedgerService.recordMovement(
                companyId,
                1L,
                "TRANSFER_IN",
                productId,
                vanId,
                1L,
                "LOT-DEFAULT",
                null,
                quantity,
                unitId,
                BigDecimal.TEN,
                "WORK_ORDER",
                vanId,
                null,
                1L,
                "RET-VAN-" + System.currentTimeMillis() + "-" + productId,
                "Returned parts to warehouse"
        );
    }
}