package com.plus33.erp.dashboard.dto;

public class DashboardScope {
    private final Long storeId;
    private final Long warehouseId;
    private final Long regionId;
    private final String userRole;
    private final String username;

    public DashboardScope(Long storeId, Long warehouseId, Long regionId, String userRole, String username) {
        this.storeId = storeId;
        this.warehouseId = warehouseId;
        this.regionId = regionId;
        this.userRole = userRole;
        this.username = username;
    }

    public Long getStoreId() { return storeId; }
    public Long getWarehouseId() { return warehouseId; }
    public Long getRegionId() { return regionId; }
    public String getUserRole() { return userRole; }
    public String getUsername() { return username; }
}
