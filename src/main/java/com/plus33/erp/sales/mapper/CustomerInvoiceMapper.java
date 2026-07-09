/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : CustomerInvoiceMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceService, CustomerInvoiceServiceImpl
 * Related Repository: CustomerInvoiceRepository
 * Related Entity    : CustomerInvoice
 * Related DTO       : CustomerInvoiceItemResponse, CustomerInvoiceResponse, toItemResponse, toResponse
 * Related Mapper    : CustomerInvoiceMapper
 * Related DB Table  : customer_invoices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerInvoiceService, CustomerInvoiceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.sales.dto.CustomerInvoiceItemResponse;
import com.plus33.erp.sales.dto.CustomerInvoiceResponse;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerInvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(componentModel = "spring")
public interface CustomerInvoiceMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerCode", source = "customer.code")
    @Mapping(target = "salesOrderId", source = "salesOrder.id")
    @Mapping(target = "salesOrderNumber", source = "salesOrder.orderNumber")
    @Mapping(target = "journalEntryId", source = "journalEntry.id")
    @Mapping(target = "journalEntryNumber", source = "journalEntry.entryNumber")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.firstName")
    @Mapping(target = "submittedById", source = "submittedBy.id")
    @Mapping(target = "submittedByName", source = "submittedBy.firstName")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "approvedByName", source = "approvedBy.firstName")
    @Mapping(target = "cancelledById", source = "cancelledBy.id")
    @Mapping(target = "cancelledByName", source = "cancelledBy.firstName")
    CustomerInvoiceResponse toResponse(CustomerInvoice invoice);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productSku", source = "product.code")
    CustomerInvoiceItemResponse toItemResponse(CustomerInvoiceItem item);
}