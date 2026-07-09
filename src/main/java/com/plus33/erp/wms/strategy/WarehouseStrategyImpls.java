/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.strategy
 * File              : WarehouseStrategyImpls.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseStrategyImplsController
 * Related Service   : WarehouseStrategyImplsService, WarehouseStrategyImplsServiceImpl
 * Related Repository: WarehouseLocationRepository, LocationStockRepository, WarehouseLocationRepository, WarehouseLocationRepository, LocationStockRepository, WarehouseLocationRepository, LocationStockRepository, LocationStockRepository, WarehouseLocationRepository, WarehouseLocationRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository, LocationStockRepository
 * Related Entity    : WarehouseStrategyImpls
 * Related DTO       : N/A
 * Related Mapper    : WarehouseStrategyImplsMapper
 * Related DB Table  : warehouse_strategy_implss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.strategy;

import com.plus33.erp.wms.entity.AsnLine;
import com.plus33.erp.wms.entity.LocationStock;
import com.plus33.erp.wms.entity.WarehouseLocation;
import com.plus33.erp.wms.repository.LocationStockRepository;
import com.plus33.erp.wms.repository.WarehouseLocationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

// ============================================================
// PUT-AWAY STRATEGY IMPLEMENTATIONS
// ============================================================

/**
 * Nearest empty bin: finds the first active, receivable location
 * in the warehouse that has zero stock, ordered by pick_sequence.
 */
@Component
class NearestEmptyBinStrategy implements PutAwayStrategy {

    private final WarehouseLocationRepository locationRepo;
    private final LocationStockRepository stockRepo;

    NearestEmptyBinStrategy(WarehouseLocationRepository locationRepo,
                             LocationStockRepository stockRepo) {
        this.locationRepo = locationRepo;
        this.stockRepo = stockRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "NEAREST_EMPTY_BIN"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        return locationRepo.findEmptyReceivableLocations(warehouseId, companyId)
                .stream().findFirst();
    }
}

/**
 * Capacity-based: finds the receivable location with maximum remaining capacity
 * (maxPallets - current stock lines count), ordered descending.
 */
@Component
class CapacityBasedStrategy implements PutAwayStrategy {

    private final WarehouseLocationRepository locationRepo;

    CapacityBasedStrategy(WarehouseLocationRepository locationRepo) {
        this.locationRepo = locationRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "CAPACITY_BASED"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        return locationRepo.findLocationsWithMostCapacity(warehouseId, companyId)
                .stream().findFirst();
    }
}

/**
 * Zone-based: routes product to the zone that matches its ABC velocity class.
 * A-class → PICKING zone, B-class → BULK zone, C-class → BULK zone.
 */
@Component
class ZoneBasedStrategy implements PutAwayStrategy {

    private final WarehouseLocationRepository locationRepo;
    private final LocationStockRepository stockRepo;

    ZoneBasedStrategy(WarehouseLocationRepository locationRepo, LocationStockRepository stockRepo) {
        this.locationRepo = locationRepo;
        this.stockRepo = stockRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "ZONE_BASED"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        // Default to BULK zone; subclass can override with product velocity lookup
        return locationRepo.findByZoneTypeAndWarehouseId("BULK", warehouseId)
                .stream().findFirst();
    }
}

/**
 * ABC-class strategy: routes to locations matching the product's ABC class label.
 */
@Component
class AbcClassStrategy implements PutAwayStrategy {

    private final WarehouseLocationRepository locationRepo;
    private final LocationStockRepository stockRepo;

    AbcClassStrategy(WarehouseLocationRepository locationRepo, LocationStockRepository stockRepo) {
        this.locationRepo = locationRepo;
        this.stockRepo = stockRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "ABC_CLASS"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        // Resolve existing stock ABC class or default to 'C'
        String abcClass = stockRepo.findByCompanyIdAndProductId(companyId, asnLine.getProductId())
                .stream().findFirst()
                .map(LocationStock::getAbcClass)
                .orElse("C");
        return locationRepo.findByVelocityClassAndWarehouseId(abcClass, warehouseId)
                .stream().findFirst();
    }
}

/**
 * Fixed bin: always directs to the default fixed bin configured for this product.
 * Falls back to nearest empty bin if no fixed bin is configured.
 */
@Component
class FixedBinStrategy implements PutAwayStrategy {

    private final LocationStockRepository stockRepo;
    private final WarehouseLocationRepository locationRepo;

    FixedBinStrategy(LocationStockRepository stockRepo, WarehouseLocationRepository locationRepo) {
        this.stockRepo = stockRepo;
        this.locationRepo = locationRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "FIXED_BIN"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        // Find the existing primary bin for this product (first existing stock location)
        return stockRepo.findByCompanyIdAndProductId(companyId, asnLine.getProductId())
                .stream().findFirst()
                .map(LocationStock::getLocation);
    }
}

/**
 * Velocity-based: routes A-velocity products to picking zone, others to bulk.
 */
@Component
class VelocityBasedStrategy implements PutAwayStrategy {

    private final WarehouseLocationRepository locationRepo;
    private final LocationStockRepository stockRepo;

    VelocityBasedStrategy(WarehouseLocationRepository locationRepo, LocationStockRepository stockRepo) {
        this.locationRepo = locationRepo;
        this.stockRepo = stockRepo;
    }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "VELOCITY_BASED"; }

    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param asnLine the asnLine input value
     * @param warehouseId the warehouseId input value
     * @param companyId owning company ID for multi-tenant data isolation
     * @return Optional containing the entity if found, empty if not
     */
    @Override
    public Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId) {
        String abcClass = stockRepo.findByCompanyIdAndProductId(companyId, asnLine.getProductId())
                .stream().findFirst()
                .map(LocationStock::getAbcClass)
                .orElse("C");
        String targetZoneType = "A".equals(abcClass) ? "PICKING" : "BULK";
        return locationRepo.findByZoneTypeAndWarehouseId(targetZoneType, warehouseId)
                .stream().findFirst();
    }
}

// ============================================================
// PICKING STRATEGY IMPLEMENTATIONS
// ============================================================

/**
 * FEFO (First Expired, First Out): allocates bins with earliest expiry date first.
 * Required for food, pharma, and any lot-controlled perishable goods.
 */
@Component
class FefoPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    FefoPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "FEFO"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductFefo(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * FIFO (First In, First Out): allocates oldest receipt date bins first.
 */
@Component
class FifoPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    FifoPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "FIFO"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductFifo(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * LIFO (Last In, First Out): allocates most recently received bins first.
 */
@Component
class LifoPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    LifoPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "LIFO"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductLifo(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * Batch picking: consolidates multiple order lines to a single pick tour.
 * Returns all available bins ordered by pick sequence (aisle walk optimization).
 */
@Component
class BatchPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    BatchPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "BATCH"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductByPickSequence(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * Cluster picking: same as batch but scoped to a defined zone.
 */
@Component
class ClusterPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    ClusterPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "CLUSTER"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductByPickSequence(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * Zone picking: picks only from the PICKING zone, then falls back to BULK.
 */
@Component
class ZonePickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    ZonePickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "ZONE"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        return stockRepo.findPickableByProductInPickingZone(companyId, pw.getProductId(), warehouseId);
    }
}

/**
 * Serial-controlled picking: ensures serial-number-tracked items are picked
 * individually and verified against the source serial number.
 */
@Component
class SerialControlledPickingStrategy implements PickingStrategy {

    private final LocationStockRepository stockRepo;

    SerialControlledPickingStrategy(LocationStockRepository stockRepo) { this.stockRepo = stockRepo; }

    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    /**
     * Performs the strategyKey operation in this module.
     *
     * @return the result string value
     */
    @Override
    public String strategyKey() { return "SERIAL_CONTROLLED"; }

    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    /**
     * Reserves wms resources (budget or stock) for downstream processing.
     *
     * @return List of matching records
     */
    @Override
    public List<LocationStock> allocate(com.plus33.erp.wms.entity.PickingWork pw,
                                         Long warehouseId, Long companyId) {
        if (pw.getSerialNumber() != null) {
            return stockRepo.findByCompanyIdAndProductIdAndSerialNumber(
                    companyId, pw.getProductId(), pw.getSerialNumber());
        }
        return stockRepo.findPickableByProductFifo(companyId, pw.getProductId(), warehouseId);
    }
}