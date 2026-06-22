package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface InventoryAdjustmentMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "submittedById", source = "submittedBy.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "postedById", source = "postedBy.id")
    @Mapping(target = "cancelledById", source = "cancelledBy.id")
    InventoryAdjustmentResponse toResponse(InventoryAdjustment entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    InventoryAdjustmentItemResponse toItemResponse(InventoryAdjustmentItem entity);
}
