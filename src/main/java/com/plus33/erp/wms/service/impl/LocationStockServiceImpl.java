/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : LocationStockServiceImpl.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LocationStockController
 * Related Service   : LocationStockServiceImpl
 * Related Repository: LocationStockRepository, WarehouseLocationRepository
 * Related Entity    : LocationStock
 * Related DTO       : N/A
 * Related Mapper    : LocationStockMapper
 * Related DB Table  : location_stocks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LocationStockController, LocationStockServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements LocationStockService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.LocationStock;
import com.plus33.erp.wms.entity.WarehouseLocation;
import com.plus33.erp.wms.repository.LocationStockRepository;
import com.plus33.erp.wms.repository.WarehouseLocationRepository;
import com.plus33.erp.wms.service.LocationStockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Manages bin-level stock with:
 * - Pessimistic WRITE locking for concurrent updates to the same bin
 * - Optimistic version (@Version) guard on PickingWork / LocationStock for race detection
 */
@Service
@Transactional
public class LocationStockServiceImpl implements LocationStockService {

    private final LocationStockRepository stockRepo;
    private final WarehouseLocationRepository locationRepo;

    public LocationStockServiceImpl(LocationStockRepository stockRepo,
                                     WarehouseLocationRepository locationRepo) {
        this.stockRepo = stockRepo;
        this.locationRepo = locationRepo;
    }

    /**
     * Creates a new stock and persists it to the database.
     *
     * @return the LocationStock result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new stock and persists it to the database.
     *
     * @return the LocationStock result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public LocationStock addStock(Long companyId, Long locationId, Long productId,
                                   String lotNumber, String serialNumber, BigDecimal quantity,
                                   Long unitId, BigDecimal unitCost, String idempotencyKey) {
        WarehouseLocation location = locationRepo.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse location not found: " + locationId));

        // Find or create the bin-level stock record
        var optExisting = stockRepo.findForUpdateByLocationAndProductAndLot(
                locationId, productId, lotNumber);

        LocationStock stock = optExisting.orElseGet(() -> {
            LocationStock s = new LocationStock();
            s.setCompanyId(companyId);
            s.setLocation(location);
            s.setProductId(productId);
            s.setLotNumber(lotNumber);
            s.setSerialNumber(serialNumber);
            s.setQuantity(BigDecimal.ZERO);
            s.setReservedQuantity(BigDecimal.ZERO);
            s.setReceiptDate(LocalDate.now());
            s.setUnitCost(unitCost);
            return s;
        });

        stock.setQuantity(stock.getQuantity().add(quantity));
        if (unitCost != null) {
            stock.setUnitCost(unitCost);
        }
        return stockRepo.save(stock);
    }

    /**
     * Performs the deductStock operation in this module.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    /**
     * Performs the deductStock operation in this module.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    @Override
    public void deductStock(Long locationId, Long productId, String lotNumber, BigDecimal quantity) {
        LocationStock stock = stockRepo.findForUpdateByLocationAndProductAndLot(
                        locationId, productId, lotNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No stock found at location " + locationId + " for product " + productId));

        if (stock.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalStateException(
                    "Insufficient stock at location " + locationId + ": available=" +
                    stock.getQuantity() + ", requested=" + quantity);
        }
        stock.setQuantity(stock.getQuantity().subtract(quantity));
        // Release corresponding reserved quantity
        BigDecimal newReserved = stock.getReservedQuantity().subtract(quantity);
        stock.setReservedQuantity(newReserved.max(BigDecimal.ZERO));
        stockRepo.save(stock);
    }

    /**
     * Reserves stock resources (budget or stock) for downstream processing.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    /**
     * Reserves stock resources (budget or stock) for downstream processing.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    @Override
    public void reserveStock(Long locationId, Long productId, String lotNumber, BigDecimal quantity) {
        LocationStock stock = stockRepo.findForUpdateByLocationAndProductAndLot(
                        locationId, productId, lotNumber)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No stock at location " + locationId + " for product " + productId));

        BigDecimal available = stock.getQuantity().subtract(stock.getReservedQuantity());
        if (available.compareTo(quantity) < 0) {
            throw new IllegalStateException(
                    "Insufficient available stock at location " + locationId +
                    ": available=" + available + ", requested=" + quantity);
        }
        stock.setReservedQuantity(stock.getReservedQuantity().add(quantity));
        stockRepo.save(stock);
    }

    /**
     * Releases previously reserved reservation resources back to the available pool.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    /**
     * Releases previously reserved reservation resources back to the available pool.
     *
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param lotNumber the lotNumber input value
     * @param quantity the quantity input value
     */
    @Override
    public void releaseReservation(Long locationId, Long productId, String lotNumber, BigDecimal quantity) {
        stockRepo.findForUpdateByLocationAndProductAndLot(locationId, productId, lotNumber)
                .ifPresent(stock -> {
                    BigDecimal newReserved = stock.getReservedQuantity().subtract(quantity);
                    stock.setReservedQuantity(newReserved.max(BigDecimal.ZERO));
                    stockRepo.save(stock);
                });
    }

    /**
     * Retrieves available stock data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param productId the productId input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableStock(Long companyId, Long productId) {
        return stockRepo.sumAvailableByProduct(companyId, productId);
    }

    /**
     * Retrieves stock by location data from the database.
     *
     * @param locationId the locationId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationStock> getStockByLocation(Long locationId) {
        return stockRepo.findByLocationId(locationId);
    }

    /**
     * Retrieves stock by product data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param productId the productId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<LocationStock> getStockByProduct(Long companyId, Long productId) {
        return stockRepo.findByCompanyIdAndProductId(companyId, productId);
    }
}