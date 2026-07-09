/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : ManufacturingAnalyticsService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingAnalyticsController
 * Related Service   : ManufacturingAnalyticsService, ManufacturingAnalyticsServiceImpl
 * Related Repository: ManufacturingAnalyticsRepository
 * Related Entity    : ManufacturingAnalytics
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingAnalyticsMapper
 * Related DB Table  : manufacturing_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service;

import java.util.List;
import java.util.Map;

public interface ManufacturingAnalyticsService {
    List<Map<String, Object>> getProductionDashboard(Long companyId);
    List<Map<String, Object>> getWipValuation(Long companyId);
    List<Map<String, Object>> getOeeSummary(Long workCenterId);
    List<Map<String, Object>> getManufacturingVariances(Long companyId);
    void refreshMaterializedViews();
}
