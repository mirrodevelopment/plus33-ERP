package com.plus33.erp.finance.mapper;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import com.plus33.erp.finance.dto.SupplierInvoiceItemRequest;
import com.plus33.erp.finance.dto.SupplierInvoiceItemResponse;
import com.plus33.erp.finance.dto.SupplierInvoiceRequest;
import com.plus33.erp.finance.dto.SupplierInvoiceResponse;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.finance.entity.SupplierInvoiceItem;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.procurement.entity.GoodsReceiptItem;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import com.plus33.erp.procurement.entity.Supplier;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:47+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class SupplierInvoiceMapperImpl implements SupplierInvoiceMapper {

    @Override
    public SupplierInvoice toEntity(SupplierInvoiceRequest request) {
        if ( request == null ) {
            return null;
        }

        SupplierInvoice.SupplierInvoiceBuilder supplierInvoice = SupplierInvoice.builder();

        supplierInvoice.invoiceNumber( request.getInvoiceNumber() );
        supplierInvoice.invoiceDate( request.getInvoiceDate() );
        supplierInvoice.dueDate( request.getDueDate() );
        supplierInvoice.currencyCode( request.getCurrencyCode() );

        return supplierInvoice.build();
    }

    @Override
    public SupplierInvoiceResponse toResponse(SupplierInvoice entity) {
        if ( entity == null ) {
            return null;
        }

        SupplierInvoiceResponse.SupplierInvoiceResponseBuilder supplierInvoiceResponse = SupplierInvoiceResponse.builder();

        supplierInvoiceResponse.companyId( entityCompanyId( entity ) );
        supplierInvoiceResponse.companyName( entityCompanyName( entity ) );
        supplierInvoiceResponse.supplierId( entitySupplierId( entity ) );
        supplierInvoiceResponse.supplierName( entitySupplierName( entity ) );
        supplierInvoiceResponse.purchaseOrderId( entityPurchaseOrderId( entity ) );
        supplierInvoiceResponse.purchaseOrderNumber( entityPurchaseOrderOrderNumber( entity ) );
        supplierInvoiceResponse.journalEntryId( entityJournalEntryId( entity ) );
        supplierInvoiceResponse.journalEntryNumber( entityJournalEntryEntryNumber( entity ) );
        supplierInvoiceResponse.items( toItemResponseList( entity.getItems() ) );
        supplierInvoiceResponse.id( entity.getId() );
        supplierInvoiceResponse.invoiceNumber( entity.getInvoiceNumber() );
        supplierInvoiceResponse.invoiceDate( entity.getInvoiceDate() );
        supplierInvoiceResponse.dueDate( entity.getDueDate() );
        supplierInvoiceResponse.subtotalAmount( entity.getSubtotalAmount() );
        supplierInvoiceResponse.taxAmount( entity.getTaxAmount() );
        supplierInvoiceResponse.discountAmount( entity.getDiscountAmount() );
        supplierInvoiceResponse.totalAmount( entity.getTotalAmount() );
        supplierInvoiceResponse.paidAmount( entity.getPaidAmount() );
        supplierInvoiceResponse.outstandingBalance( entity.getOutstandingBalance() );
        supplierInvoiceResponse.status( entity.getStatus() );
        supplierInvoiceResponse.currencyCode( entity.getCurrencyCode() );
        supplierInvoiceResponse.createdAt( entity.getCreatedAt() );
        supplierInvoiceResponse.updatedAt( entity.getUpdatedAt() );

        return supplierInvoiceResponse.build();
    }

    @Override
    public List<SupplierInvoiceResponse> toResponseList(List<SupplierInvoice> list) {
        if ( list == null ) {
            return null;
        }

        List<SupplierInvoiceResponse> list1 = new ArrayList<SupplierInvoiceResponse>( list.size() );
        for ( SupplierInvoice supplierInvoice : list ) {
            list1.add( toResponse( supplierInvoice ) );
        }

        return list1;
    }

    @Override
    public SupplierInvoiceItem toItemEntity(SupplierInvoiceItemRequest request) {
        if ( request == null ) {
            return null;
        }

        SupplierInvoiceItem.SupplierInvoiceItemBuilder supplierInvoiceItem = SupplierInvoiceItem.builder();

        supplierInvoiceItem.quantity( request.getQuantity() );
        supplierInvoiceItem.unitPrice( request.getUnitPrice() );
        supplierInvoiceItem.taxRate( request.getTaxRate() );
        supplierInvoiceItem.discountAmount( request.getDiscountAmount() );

        return supplierInvoiceItem.build();
    }

    @Override
    public SupplierInvoiceItemResponse toItemResponse(SupplierInvoiceItem entity) {
        if ( entity == null ) {
            return null;
        }

        SupplierInvoiceItemResponse.SupplierInvoiceItemResponseBuilder supplierInvoiceItemResponse = SupplierInvoiceItemResponse.builder();

        supplierInvoiceItemResponse.purchaseOrderItemId( entityPurchaseOrderItemId( entity ) );
        supplierInvoiceItemResponse.goodsReceiptItemId( entityGoodsReceiptItemId( entity ) );
        supplierInvoiceItemResponse.productId( entityProductId( entity ) );
        supplierInvoiceItemResponse.productCode( entityProductCode( entity ) );
        supplierInvoiceItemResponse.productName( entityProductName( entity ) );
        supplierInvoiceItemResponse.dimensionSetId( entityDimensionSetId( entity ) );
        supplierInvoiceItemResponse.id( entity.getId() );
        supplierInvoiceItemResponse.quantity( entity.getQuantity() );
        supplierInvoiceItemResponse.unitPrice( entity.getUnitPrice() );
        supplierInvoiceItemResponse.netAmount( entity.getNetAmount() );
        supplierInvoiceItemResponse.taxRate( entity.getTaxRate() );
        supplierInvoiceItemResponse.taxAmount( entity.getTaxAmount() );
        supplierInvoiceItemResponse.discountAmount( entity.getDiscountAmount() );
        supplierInvoiceItemResponse.totalAmount( entity.getTotalAmount() );

        return supplierInvoiceItemResponse.build();
    }

    @Override
    public List<SupplierInvoiceItemResponse> toItemResponseList(List<SupplierInvoiceItem> list) {
        if ( list == null ) {
            return null;
        }

        List<SupplierInvoiceItemResponse> list1 = new ArrayList<SupplierInvoiceItemResponse>( list.size() );
        for ( SupplierInvoiceItem supplierInvoiceItem : list ) {
            list1.add( toItemResponse( supplierInvoiceItem ) );
        }

        return list1;
    }

    private Long entityCompanyId(SupplierInvoice supplierInvoice) {
        Company company = supplierInvoice.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(SupplierInvoice supplierInvoice) {
        Company company = supplierInvoice.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private Long entitySupplierId(SupplierInvoice supplierInvoice) {
        Supplier supplier = supplierInvoice.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierName(SupplierInvoice supplierInvoice) {
        Supplier supplier = supplierInvoice.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private Long entityPurchaseOrderId(SupplierInvoice supplierInvoice) {
        PurchaseOrder purchaseOrder = supplierInvoice.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getId();
    }

    private String entityPurchaseOrderOrderNumber(SupplierInvoice supplierInvoice) {
        PurchaseOrder purchaseOrder = supplierInvoice.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getOrderNumber();
    }

    private Long entityJournalEntryId(SupplierInvoice supplierInvoice) {
        JournalEntry journalEntry = supplierInvoice.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getId();
    }

    private String entityJournalEntryEntryNumber(SupplierInvoice supplierInvoice) {
        JournalEntry journalEntry = supplierInvoice.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getEntryNumber();
    }

    private Long entityPurchaseOrderItemId(SupplierInvoiceItem supplierInvoiceItem) {
        PurchaseOrderItem purchaseOrderItem = supplierInvoiceItem.getPurchaseOrderItem();
        if ( purchaseOrderItem == null ) {
            return null;
        }
        return purchaseOrderItem.getId();
    }

    private Long entityGoodsReceiptItemId(SupplierInvoiceItem supplierInvoiceItem) {
        GoodsReceiptItem goodsReceiptItem = supplierInvoiceItem.getGoodsReceiptItem();
        if ( goodsReceiptItem == null ) {
            return null;
        }
        return goodsReceiptItem.getId();
    }

    private Long entityProductId(SupplierInvoiceItem supplierInvoiceItem) {
        Product product = supplierInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(SupplierInvoiceItem supplierInvoiceItem) {
        Product product = supplierInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(SupplierInvoiceItem supplierInvoiceItem) {
        Product product = supplierInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long entityDimensionSetId(SupplierInvoiceItem supplierInvoiceItem) {
        BudgetDimensionSet dimensionSet = supplierInvoiceItem.getDimensionSet();
        if ( dimensionSet == null ) {
            return null;
        }
        return dimensionSet.getId();
    }
}
