/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : PickListService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : CompletePickingRequest, PickListRequest, PickListResponse, ShipRequest
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.PickListStatus;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
