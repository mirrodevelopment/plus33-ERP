/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : SalesOrderService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderService, SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository
 * Related Entity    : SalesOrder
 * Related DTO       : PageResponse, SalesOrderRequest, SalesOrderResponse, SalesOrderSearchRequest, searchRequest
 * Related Mapper    : SalesOrderMapper
 * Related DB Table  : sales_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.SalesOrderRequest;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.dto.SalesOrderSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SalesOrderService {

    SalesOrderResponse createSalesOrder(SalesOrderRequest request);

    SalesOrderResponse updateSalesOrder(Long id, SalesOrderRequest request);

    SalesOrderResponse getSalesOrderById(Long id);

    PageResponse<SalesOrderResponse> searchSalesOrders(SalesOrderSearchRequest searchRequest, Pageable pageable);

    SalesOrderResponse submitSalesOrder(Long id);

    SalesOrderResponse approveSalesOrder(Long id);

    SalesOrderResponse cancelSalesOrder(Long id, String reason);
}
