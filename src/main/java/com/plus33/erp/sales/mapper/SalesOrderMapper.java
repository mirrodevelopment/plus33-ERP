package com.plus33.erp.sales.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.sales.dto.SalesOrderItemResponse;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.entity.SalesOrder;
import com.plus33.erp.sales.entity.SalesOrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
