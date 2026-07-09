/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.mapper
 * File              : ReplenishmentMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentController
 * Related Service   : ReplenishmentService, ReplenishmentServiceImpl
 * Related Repository: ReplenishmentRepository
 * Related Entity    : Replenishment
 * Related DTO       : purchaseRequest, ReplenishmentRuleResponse, ReplenishmentSuggestionResponse, toResponse
 * Related Mapper    : ReplenishmentMapper
 * Related DB Table  : replenishments
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : ReplenishmentService, ReplenishmentServiceImpl
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
 * <p><b>Class  :</b> {@code ReplenishmentMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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