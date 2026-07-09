/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : PickListMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : PickListItemResponse, PickListResponse, toItemResponse, toResponse
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : PickListService, PickListServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.sales.dto.PickListItemResponse;
import com.plus33.erp.sales.dto.PickListResponse;
import com.plus33.erp.sales.entity.PickList;
import com.plus33.erp.sales.entity.PickListItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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