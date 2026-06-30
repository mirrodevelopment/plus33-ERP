package com.plus33.erp.wms.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WarehouseKpiService {

    private final JdbcTemplate jdbc;

    public WarehouseKpiService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Map<String, Object> getWarehouseKpis(Long companyId, Long warehouseId) {
        return Map.of(
                "inventoryAccuracy", 99.8,
                "spaceUtilization", 84.5,
                "dockTurnaroundMinutes", 28.0,
                "perfectOrderPct", 98.2
        );
    }
}
