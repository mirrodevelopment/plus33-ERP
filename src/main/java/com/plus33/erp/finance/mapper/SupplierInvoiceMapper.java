package com.plus33.erp.finance.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.finance.entity.SupplierInvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface SupplierInvoiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "subtotalAmount", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "paidAmount", ignore = true)
    @Mapping(target = "outstandingBalance", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "journalEntry", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "paymentRun", ignore = true)
    SupplierInvoice toEntity(SupplierInvoiceRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "purchaseOrderId", source = "purchaseOrder.id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrder.orderNumber")
    @Mapping(target = "journalEntryId", source = "journalEntry.id")
    @Mapping(target = "journalEntryNumber", source = "journalEntry.entryNumber")
    @Mapping(target = "items", source = "items")
    SupplierInvoiceResponse toResponse(SupplierInvoice entity);

    List<SupplierInvoiceResponse> toResponseList(List<SupplierInvoice> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplierInvoice", ignore = true)
    @Mapping(target = "purchaseOrderItem", ignore = true)
    @Mapping(target = "goodsReceiptItem", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "netAmount", ignore = true)
    @Mapping(target = "taxAmount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    SupplierInvoiceItem toItemEntity(SupplierInvoiceItemRequest request);

    @Mapping(target = "purchaseOrderItemId", source = "purchaseOrderItem.id")
    @Mapping(target = "goodsReceiptItemId", source = "goodsReceiptItem.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productCode", source = "product.code")
    @Mapping(target = "productName", source = "product.name")
    SupplierInvoiceItemResponse toItemResponse(SupplierInvoiceItem entity);

    List<SupplierInvoiceItemResponse> toItemResponseList(List<SupplierInvoiceItem> list);
}
