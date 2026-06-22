package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.SalesOrderRequest;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.dto.SalesOrderSearchRequest;
import org.springframework.data.domain.Pageable;

public interface SalesOrderService {

    SalesOrderResponse createSalesOrder(SalesOrderRequest request);

    SalesOrderResponse updateSalesOrder(Long id, SalesOrderRequest request);

    SalesOrderResponse getSalesOrderById(Long id);

    PageResponse<SalesOrderResponse> searchSalesOrders(SalesOrderSearchRequest searchRequest, Pageable pageable);

    SalesOrderResponse submitSalesOrder(Long id);

    SalesOrderResponse approveSalesOrder(Long id);

    SalesOrderResponse cancelSalesOrder(Long id, String reason);
}
