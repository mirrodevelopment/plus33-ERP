package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface StockCountMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "assignedToId", source = "assignedTo.id")
    @Mapping(target = "assignedToName", source = "assignedTo.email")
    @Mapping(target = "adjustmentId", source = "adjustment.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "assignedById", source = "assignedBy.id")
    @Mapping(target = "startedById", source = "startedBy.id")
    @Mapping(target = "submittedById", source = "submittedBy.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "postedById", source = "postedBy.id")
    @Mapping(target = "closedById", source = "closedBy.id")
    StockCountResponse toResponse(StockCount entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    StockCountItemResponse toItemResponse(StockCountItem entity);
}
