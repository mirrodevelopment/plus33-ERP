/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : PurchaseOrderService.java
 * Purpose           : Service interface contract defining the API for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderController
 * Related Service   : PurchaseOrderService, PurchaseOrderServiceImpl
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : PurchaseOrder
 * Related DTO       : PageResponse, PurchaseOrderRequest, PurchaseOrderResponse, PurchaseOrderSearchRequest, searchRequest
 * Related Mapper    : PurchaseOrderMapper
 * Related DB Table  : purchase_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Procurement Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderRequest request);
    PurchaseOrderResponse getPurchaseOrderById(Long id);
    PageResponse<PurchaseOrderResponse> searchPurchaseOrders(PurchaseOrderSearchRequest searchRequest, Pageable pageable);
    PurchaseOrderResponse updatePurchaseOrder(Long id, PurchaseOrderRequest request);
    void deletePurchaseOrder(Long id);
    PurchaseOrderResponse approvePurchaseOrder(Long id);
    PurchaseOrderResponse cancelPurchaseOrder(Long id, String reason);
    PurchaseOrderResponse closePurchaseOrder(Long id);
}
