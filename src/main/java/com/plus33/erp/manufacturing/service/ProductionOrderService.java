package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.util.List;

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
