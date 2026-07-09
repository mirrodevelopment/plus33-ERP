/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : CustomerReturnMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnService, CustomerReturnServiceImpl
 * Related Repository: CustomerReturnRepository
 * Related Entity    : CustomerReturn
 * Related DTO       : CustomerReturnItemResponse, CustomerReturnResponse, toItemResponse, toResponse
 * Related Mapper    : CustomerReturnMapper
 * Related DB Table  : customer_returns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerReturnService, CustomerReturnServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.sales.dto.CustomerReturnItemResponse;
import com.plus33.erp.sales.dto.CustomerReturnResponse;
import com.plus33.erp.sales.entity.CustomerReturn;
import com.plus33.erp.sales.entity.CustomerReturnItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(componentModel = "spring")
public interface CustomerReturnMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerCode", source = "customer.code")
    @Mapping(target = "salesOrderId", source = "salesOrder.id")
    @Mapping(target = "salesOrderNumber", source = "salesOrder.orderNumber")
    @Mapping(target = "customerInvoiceId", source = "customerInvoice.id")
    @Mapping(target = "customerInvoiceNumber", source = "customerInvoice.invoiceNumber")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "storeId", source = "store.id")
    @Mapping(target = "storeName", source = "store.name")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.firstName")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "approvedByName", source = "approvedBy.firstName")
    @Mapping(target = "receivedById", source = "receivedBy.id")
    @Mapping(target = "receivedByName", source = "receivedBy.firstName")
    @Mapping(target = "inspectedById", source = "inspectedBy.id")
    @Mapping(target = "inspectedByName", source = "inspectedBy.firstName")
    @Mapping(target = "closedById", source = "closedBy.id")
    @Mapping(target = "closedByName", source = "closedBy.firstName")
    @Mapping(target = "cancelledById", source = "cancelledBy.id")
    @Mapping(target = "cancelledByName", source = "cancelledBy.firstName")
    CustomerReturnResponse toResponse(CustomerReturn customerReturn);

    @Mapping(target = "salesOrderItemId", source = "salesOrderItem.id")
    @Mapping(target = "customerInvoiceItemId", source = "customerInvoiceItem.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "lotId", source = "lot.id")
    @Mapping(target = "lotNumber", source = "lot.lotNumber")
    @Mapping(target = "serialId", source = "serial.id")
    @Mapping(target = "serialNumber", source = "serial.serialNumber")
    CustomerReturnItemResponse toItemResponse(CustomerReturnItem item);
}