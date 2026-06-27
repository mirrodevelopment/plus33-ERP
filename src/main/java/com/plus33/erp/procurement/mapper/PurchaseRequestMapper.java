package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface PurchaseRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "requestNumber", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "convertedToPoAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "requestDate", ignore = true)
    PurchaseRequest toEntity(PurchaseRequestRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyCode", source = "company.code")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "supplierCode", source = "supplier.code")
    @Mapping(target = "requestedByUserId", source = "requestedBy.id")
    @Mapping(target = "requestedByUserName", source = "requestedBy.firstName")
    @Mapping(target = "submittedByUserId", source = "submittedBy.id")
    @Mapping(target = "submittedByUserName", source = "submittedBy.firstName")
    @Mapping(target = "approvedByUserId", source = "approvedBy.id")
    @Mapping(target = "approvedByUserName", source = "approvedBy.firstName")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "warehouseCode", source = "warehouse.code")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "storeCode", source = "store.code")
    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrder.orderNumber")
    PurchaseRequestResponse toResponse(PurchaseRequest entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "purchaseRequest", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "approvedQuantity", ignore = true)
    @Mapping(target = "dimensionSet", ignore = true)
    PurchaseRequestItem toItemEntity(PurchaseRequestItemRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "dimensionSetId", source = "dimensionSet.id")
    PurchaseRequestItemResponse toItemResponse(PurchaseRequestItem entity);

    List<PurchaseRequestItemResponse> toItemResponseList(List<PurchaseRequestItem> list);
}
