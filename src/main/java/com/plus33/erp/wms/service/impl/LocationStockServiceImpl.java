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

    @Override
    public void releaseReservation(Long locationId, Long productId, String lotNumber, BigDecimal quantity) {
        stockRepo.findForUpdateByLocationAndProductAndLot(locationId, productId, lotNumber)
                .ifPresent(stock -> {
                    BigDecimal newReserved = stock.getReservedQuantity().subtract(quantity);
                    stock.setReservedQuantity(newReserved.max(BigDecimal.ZERO));
                    stockRepo.save(stock);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAvailableStock(Long companyId, Long productId) {
        return stockRepo.sumAvailableByProduct(companyId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationStock> getStockByLocation(Long locationId) {
        return stockRepo.findByLocationId(locationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationStock> getStockByProduct(Long companyId, Long productId) {
        return stockRepo.findByCompanyIdAndProductId(companyId, productId);
    }
}
