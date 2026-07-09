/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service
 * File              : LocationStockService.java
 * Purpose           : Service interface contract defining the API for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LocationStockController
 * Related Service   : LocationStockService, LocationStockServiceImpl
 * Related Repository: LocationStockRepository
 * Related Entity    : LocationStock
 * Related DTO       : N/A
 * Related Mapper    : LocationStockMapper
 * Related DB Table  : location_stocks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.service;

import com.plus33.erp.wms.entity.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code LocationStockService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service}</p>
 * <p><b>Layer  :</b> Component of Wms Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
