package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface GoodsReceiptMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "receiptNumber", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "receivedBy", ignore = true)
    @Mapping(target = "receivedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "totalQuantity", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "cancelledBy", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "items", ignore = true)
    GoodsReceipt toEntity(GoodsReceiptRequest request);

    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrder.orderNumber")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "receivedByUserId", source = "receivedBy.id")
    @Mapping(target = "receivedByUserName", source = "receivedBy.firstName")
    @Mapping(target = "cancelledByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancelledByUserName", source = "cancelledBy.firstName")
    GoodsReceiptResponse toResponse(GoodsReceipt entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "goodsReceipt", ignore = true)
    @Mapping(target = "product", ignore = true)
    GoodsReceiptItem toItemEntity(GoodsReceiptItemRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    GoodsReceiptItemResponse toItemResponse(GoodsReceiptItem entity);

    List<GoodsReceiptItemResponse> toItemResponseList(List<GoodsReceiptItem> list);
}
