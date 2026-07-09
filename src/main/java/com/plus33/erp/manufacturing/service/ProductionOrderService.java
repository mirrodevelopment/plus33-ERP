/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : ProductionOrderService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderController
 * Related Service   : ProductionOrderService, ProductionOrderServiceImpl
 * Related Repository: ProductionOrderRepository
 * Related Entity    : ProductionOrder
 * Related DTO       : CompleteOperationRequest, CreateProductionOrderRequest, ProductionOrderDto, ProductionOrderOperationDto
 * Related Mapper    : ProductionOrderMapper
 * Related DB Table  : production_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionOrderService {
    ProductionOrderDto createProductionOrder(CreateProductionOrderRequest request);
    ProductionOrderDto getProductionOrderById(Long id);
    List<ProductionOrderDto> getProductionOrdersByCompany(Long companyId);
    ProductionOrderDto releaseProductionOrder(Long id);
    ProductionOrderOperationDto completeOperation(Long orderId, Long operationId, CompleteOperationRequest request);
    ProductionOrderDto completeProductionOrder(Long id);

    ProductionOrderDto startOrder(Long companyId, Long orderId, Long userId);
    ProductionOrderDto closeOrder(Long companyId, Long orderId, Long userId);
    ProductionOrderDto cancelOrder(Long companyId, Long orderId, String reason, Long userId);
    List<ProductionOrderDto> getOrdersByStatus(Long companyId, String status);
    List<ProductionOrderDto> getActiveOrders(Long companyId);
    List<ProductionOrderDto> getOrdersBySchedule(Long companyId, LocalDate from, LocalDate to);
}
