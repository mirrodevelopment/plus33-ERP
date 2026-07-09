/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : WarehouseService.java
 * Purpose           : Service interface contract defining the API for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseService, WarehouseServiceImpl
 * Related Repository: WarehouseRepository
 * Related Entity    : Warehouse
 * Related DTO       : PageResponse, searchRequest, WarehouseRequest, WarehouseResponse, WarehouseSearchRequest
 * Related Mapper    : WarehouseMapper
 * Related DB Table  : warehouses
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Organization Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.dto.WarehouseSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseRequest request);
    WarehouseResponse getWarehouseById(Long id);
    PageResponse<WarehouseResponse> searchWarehouses(WarehouseSearchRequest searchRequest, Pageable pageable);
    WarehouseResponse updateWarehouse(Long id, WarehouseRequest request);
    void deleteWarehouse(Long id);
    WarehouseResponse activateWarehouse(Long id);
    WarehouseResponse deactivateWarehouse(Long id);
}
