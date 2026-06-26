package com.plus33.erp.sales.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.sales.dto.PickListItemResponse;
import com.plus33.erp.sales.dto.PickListResponse;
import com.plus33.erp.sales.entity.PickList;
import com.plus33.erp.sales.entity.PickListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface PickListMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "salesOrderId", source = "salesOrder.id")
    @Mapping(target = "salesOrderNumber", source = "salesOrder.orderNumber")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "createdByUserId", source = "createdBy.id")
    @Mapping(target = "createdByUserName", source = "createdBy.firstName")
    @Mapping(target = "releasedByUserId", source = "releasedBy.id")
    @Mapping(target = "releasedByUserName", source = "releasedBy.firstName")
    @Mapping(target = "pickedByUserId", source = "pickedBy.id")
    @Mapping(target = "pickedByUserName", source = "pickedBy.firstName")
    @Mapping(target = "packedByUserId", source = "packedBy.id")
    @Mapping(target = "packedByUserName", source = "packedBy.firstName")
    @Mapping(target = "shippedByUserId", source = "shippedBy.id")
    @Mapping(target = "shippedByUserName", source = "shippedBy.firstName")
    @Mapping(target = "cancelledByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancelledByUserName", source = "cancelledBy.firstName")
    PickListResponse toResponse(PickList entity);

    @Mapping(target = "salesOrderItemId", source = "salesOrderItem.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.code")
    PickListItemResponse toItemResponse(PickListItem entity);

    List<PickListItemResponse> toItemResponseList(List<PickListItem> list);

    List<PickListResponse> toResponseList(List<PickList> list);
}
