package com.plus33.erp.wms.service;

import com.plus33.erp.wms.entity.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LocationStockService {
    LocationStock addStock(Long companyId, Long locationId, Long productId, String lotNumber,
                           String serialNumber, BigDecimal quantity, Long unitId, BigDecimal unitCost,
                           String idempotencyKey);
    void deductStock(Long locationId, Long productId, String lotNumber, BigDecimal quantity);
    void reserveStock(Long locationId, Long productId, String lotNumber, BigDecimal quantity);
    void releaseReservation(Long locationId, Long productId, String lotNumber, BigDecimal quantity);
    BigDecimal getAvailableStock(Long companyId, Long productId);
    List<LocationStock> getStockByLocation(Long locationId);
    List<LocationStock> getStockByProduct(Long companyId, Long productId);
}
