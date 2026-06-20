package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

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
    PurchaseOrderItem toItemEntity(PurchaseOrderItemRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem entity);

    List<PurchaseOrderItemResponse> toItemResponseList(List<PurchaseOrderItem> list);
}
