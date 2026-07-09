/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.mapper
 * File              : PurchaseOrderMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderController
 * Related Service   : PurchaseOrderService, PurchaseOrderServiceImpl
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : PurchaseOrder
 * Related DTO       : PurchaseOrderItemRequest, PurchaseOrderItemResponse, PurchaseOrderRequest, PurchaseOrderResponse, purchaseRequest
 * Related Mapper    : PurchaseOrderMapper
 * Related DB Table  : purchase_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : PurchaseOrderService, PurchaseOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Procurement Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface PurchaseOrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "purchaseRequest", ignore = true)
    @Mapping(target = "orderedBy", ignore = true)
    @Mapping(target = "issuedBy", ignore = true)
    @Mapping(target = "cancelledBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "issuedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "receivedPercentage", ignore = true)
    @Mapping(target = "subtotalAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "version", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyCode", source = "company.code")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierCode", source = "supplier.code")
    @Mapping(target = "purchaseRequestId", source = "purchaseRequest.id")
    @Mapping(target = "purchaseRequestNumber", source = "purchaseRequest.requestNumber")
    @Mapping(target = "orderedByUserId", source = "orderedBy.id")
    @Mapping(target = "orderedByUserName", source = "orderedBy.firstName")
    @Mapping(target = "issuedByUserId", source = "issuedBy.id")
    @Mapping(target = "issuedByUserName", source = "issuedBy.firstName")
    @Mapping(target = "cancelledByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancelledByUserName", source = "cancelledBy.firstName")
    PurchaseOrderResponse toResponse(PurchaseOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "receivedQuantity", ignore = true)
    @Mapping(target = "remainingQuantity", ignore = true)
    @Mapping(target = "invoicedQuantity", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "dimensionSet", ignore = true)
    PurchaseOrderItem toItemEntity(PurchaseOrderItemRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "dimensionSetId", source = "dimensionSet.id")
    PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem entity);

    List<PurchaseOrderItemResponse> toItemResponseList(List<PurchaseOrderItem> list);
}