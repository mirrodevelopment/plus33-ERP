/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.mapper
 * File              : InventoryTransferMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferController
 * Related Service   : InventoryTransferService, InventoryTransferServiceImpl
 * Related Repository: InventoryTransferRepository
 * Related Entity    : InventoryTransfer
 * Related DTO       : InventoryTransferItemResponse, InventoryTransferResponse, toItemResponse, toResponse
 * Related Mapper    : InventoryTransferMapper
 * Related DB Table  : inventory_transfers
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : InventoryTransferService, InventoryTransferServiceImpl
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
 * <p><b>Class  :</b> {@code InventoryTransferMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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