package com.plus33.erp.sales.service;

import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.PickListStatus;

import java.util.List;

public interface PickListService {

    PickListResponse createPickList(PickListRequest request);

    PickListResponse getPickListById(Long id);

    PickListResponse releasePickList(Long id);

    PickListResponse startPicking(Long id);

    PickListResponse completePicking(Long id, CompletePickingRequest request);

    PickListResponse packPickList(Long id);

    PickListResponse shipPickList(Long id, ShipRequest request);

    PickListResponse cancelPickList(Long id, String reason);

    List<PickListResponse> getPickListsBySalesOrderId(Long salesOrderId);

    List<PickListResponse> getPickListsByWarehouseId(Long warehouseId);

    List<PickListResponse> getPickListsByStoreId(Long storeId);

    List<PickListResponse> getPickListsByStatus(PickListStatus status);
}
