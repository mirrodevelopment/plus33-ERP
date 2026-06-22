package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface InventoryTransferMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "sourceWarehouseId", source = "sourceWarehouse.id")
    @Mapping(target = "sourceStoreId", source = "sourceStore.id")
    @Mapping(target = "destWarehouseId", source = "destWarehouse.id")
    @Mapping(target = "destStoreId", source = "destStore.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "submittedById", source = "submittedBy.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "dispatchedById", source = "dispatchedBy.id")
    @Mapping(target = "receivedById", source = "receivedBy.id")
    @Mapping(target = "cancelledById", source = "cancelledBy.id")
    InventoryTransferResponse toResponse(InventoryTransfer entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    InventoryTransferItemResponse toItemResponse(InventoryTransferItem entity);
}
