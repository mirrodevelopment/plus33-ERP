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
