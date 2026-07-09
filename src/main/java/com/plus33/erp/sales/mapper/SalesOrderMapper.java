/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : SalesOrderMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderService, SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository
 * Related Entity    : SalesOrder
 * Related DTO       : SalesOrderItemResponse, SalesOrderResponse, toItemResponse, toResponse
 * Related Mapper    : SalesOrderMapper
 * Related DB Table  : sales_orders
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : SalesOrderService, SalesOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.sales.dto.SalesOrderItemResponse;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.entity.SalesOrder;
import com.plus33.erp.sales.entity.SalesOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface SalesOrderMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "orderedByUserId", source = "orderedBy.id")
    @Mapping(target = "orderedByUserName", source = "orderedBy.firstName")
    @Mapping(target = "submittedByUserId", source = "submittedBy.id")
    @Mapping(target = "submittedByUserName", source = "submittedBy.firstName")
    @Mapping(target = "approvedByUserId", source = "approvedBy.id")
    @Mapping(target = "approvedByUserName", source = "approvedBy.firstName")
    @Mapping(target = "cancelledByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancelledByUserName", source = "cancelledBy.firstName")
    SalesOrderResponse toResponse(SalesOrder entity);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    SalesOrderItemResponse toItemResponse(SalesOrderItem entity);

    List<SalesOrderItemResponse> toItemResponseList(List<SalesOrderItem> list);
}