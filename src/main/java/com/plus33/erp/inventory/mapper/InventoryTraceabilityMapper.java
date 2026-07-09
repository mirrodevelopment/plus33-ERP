/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.mapper
 * File              : InventoryTraceabilityMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceabilityController
 * Related Service   : InventoryTraceabilityService, InventoryTraceabilityServiceImpl
 * Related Repository: InventoryTraceabilityRepository
 * Related Entity    : InventoryTraceability
 * Related DTO       : InventoryLotResponse, InventoryRecallResponse, InventorySerialResponse, InventoryTraceEventResponse, toResponse
 * Related Mapper    : InventoryTraceabilityMapper
 * Related DB Table  : inventory_traceabilitys
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : InventoryTraceabilityService, InventoryTraceabilityServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Inventory Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTraceabilityMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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