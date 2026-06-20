package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.dto.WarehouseSearchRequest;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseRequest request);
    WarehouseResponse getWarehouseById(Long id);
    PageResponse<WarehouseResponse> searchWarehouses(WarehouseSearchRequest searchRequest, Pageable pageable);
    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);
    void deleteWarehouse(Long id);
    WarehouseResponse activateWarehouse(Long id);
    WarehouseResponse deactivateWarehouse(Long id);
}
