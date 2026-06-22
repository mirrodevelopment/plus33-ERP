package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface ReplenishmentMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    ReplenishmentRuleResponse toResponse(ReplenishmentRule entity);

    @Mapping(target = "ruleId", source = "rule.id")
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "purchaseRequestId", source = "purchaseRequest.id")
    @Mapping(target = "purchaseRequestNumber", source = "purchaseRequest.requestNumber")
    @Mapping(target = "transferId", source = "transfer.id")
    @Mapping(target = "transferNumber", source = "transfer.transferNumber")
    ReplenishmentSuggestionResponse toResponse(ReplenishmentSuggestion entity);
}
