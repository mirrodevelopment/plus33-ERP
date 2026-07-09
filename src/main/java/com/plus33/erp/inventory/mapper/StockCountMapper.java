/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.mapper
 * File              : StockCountMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : StockCountItemResponse, StockCountResponse, toItemResponse, toResponse
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : StockCountService, StockCountServiceImpl
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
 * <p><b>Class  :</b> {@code StockCountMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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