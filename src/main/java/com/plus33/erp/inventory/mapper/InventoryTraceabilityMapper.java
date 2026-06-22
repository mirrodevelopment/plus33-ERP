package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface InventoryTraceabilityMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    InventoryLotResponse toResponse(InventoryLot entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "lotId", source = "lot.id")
    @Mapping(target = "lotNumber", source = "lot.lotNumber")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "storeId", source = "store.id")
    InventorySerialResponse toResponse(InventorySerial entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "lotId", source = "lot.id")
    @Mapping(target = "lotNumber", source = "lot.lotNumber")
    @Mapping(target = "serialId", source = "serial.id")
    @Mapping(target = "serialNumber", source = "serial.serialNumber")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    InventoryTraceEventResponse toResponse(InventoryTraceEvent entity);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "lotId", source = "lot.id")
    @Mapping(target = "lotNumber", source = "lot.lotNumber")
    @Mapping(target = "serialId", source = "serial.id")
    @Mapping(target = "serialNumber", source = "serial.serialNumber")
    @Mapping(target = "recalledById", source = "recalledBy.id")
    InventoryRecallResponse toResponse(InventoryRecall entity);
}
