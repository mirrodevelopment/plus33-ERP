package com.plus33.erp.procurement.mapper;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.procurement.dto.GoodsReceiptItemRequest;
import com.plus33.erp.procurement.dto.GoodsReceiptItemResponse;
import com.plus33.erp.procurement.dto.GoodsReceiptRequest;
import com.plus33.erp.procurement.dto.GoodsReceiptResponse;
import com.plus33.erp.procurement.entity.GoodsReceipt;
import com.plus33.erp.procurement.entity.GoodsReceiptItem;
import com.plus33.erp.procurement.entity.GoodsReceiptStatus;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.security.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:47+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class GoodsReceiptMapperImpl implements GoodsReceiptMapper {

    @Override
    public GoodsReceipt toEntity(GoodsReceiptRequest request) {
        if ( request == null ) {
            return null;
        }

        GoodsReceipt goodsReceipt = new GoodsReceipt();

        goodsReceipt.setRemarks( request.remarks() );
        goodsReceipt.setSupplierDeliveryNote( request.supplierDeliveryNote() );
        goodsReceipt.setSupplierInvoiceNumber( request.supplierInvoiceNumber() );
        goodsReceipt.setClientReferenceId( request.clientReferenceId() );

        return goodsReceipt;
    }

    @Override
    public GoodsReceiptResponse toResponse(GoodsReceipt entity) {
        if ( entity == null ) {
            return null;
        }

        Long purchaseOrderId = null;
        String purchaseOrderNumber = null;
        Long companyId = null;
        String companyName = null;
        Long warehouseId = null;
        String warehouseName = null;
        Long storeId = null;
        String storeName = null;
        Long receivedByUserId = null;
        String receivedByUserName = null;
        Long cancelledByUserId = null;
        String cancelledByUserName = null;
        Long id = null;
        String receiptNumber = null;
        LocalDateTime receivedAt = null;
        GoodsReceiptStatus status = null;
        String remarks = null;
        String supplierDeliveryNote = null;
        String supplierInvoiceNumber = null;
        UUID clientReferenceId = null;
        BigDecimal totalQuantity = null;
        BigDecimal totalAmount = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<GoodsReceiptItemResponse> items = null;

        purchaseOrderId = entityPurchaseOrderId( entity );
        purchaseOrderNumber = entityPurchaseOrderOrderNumber( entity );
        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        warehouseId = entityWarehouseId( entity );
        warehouseName = entityWarehouseName( entity );
        storeId = entityStoreId( entity );
        storeName = entityStoreName( entity );
        receivedByUserId = entityReceivedById( entity );
        receivedByUserName = entityReceivedByFirstName( entity );
        cancelledByUserId = entityCancelledById( entity );
        cancelledByUserName = entityCancelledByFirstName( entity );
        id = entity.getId();
        receiptNumber = entity.getReceiptNumber();
        receivedAt = entity.getReceivedAt();
        status = entity.getStatus();
        remarks = entity.getRemarks();
        supplierDeliveryNote = entity.getSupplierDeliveryNote();
        supplierInvoiceNumber = entity.getSupplierInvoiceNumber();
        clientReferenceId = entity.getClientReferenceId();
        totalQuantity = entity.getTotalQuantity();
        totalAmount = entity.getTotalAmount();
        cancelledAt = entity.getCancelledAt();
        cancellationReason = entity.getCancellationReason();
        items = toItemResponseList( entity.getItems() );

        GoodsReceiptResponse goodsReceiptResponse = new GoodsReceiptResponse( id, receiptNumber, purchaseOrderId, purchaseOrderNumber, companyId, companyName, warehouseId, warehouseName, storeId, storeName, receivedByUserId, receivedByUserName, receivedAt, status, remarks, supplierDeliveryNote, supplierInvoiceNumber, clientReferenceId, totalQuantity, totalAmount, cancelledByUserId, cancelledByUserName, cancelledAt, cancellationReason, items );

        return goodsReceiptResponse;
    }

    @Override
    public GoodsReceiptItem toItemEntity(GoodsReceiptItemRequest request) {
        if ( request == null ) {
            return null;
        }

        GoodsReceiptItem goodsReceiptItem = new GoodsReceiptItem();

        goodsReceiptItem.setReceivedQuantity( request.receivedQuantity() );
        goodsReceiptItem.setRemarks( request.remarks() );

        return goodsReceiptItem;
    }

    @Override
    public GoodsReceiptItemResponse toItemResponse(GoodsReceiptItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productCode = null;
        String productName = null;
        Long id = null;
        BigDecimal receivedQuantity = null;
        String remarks = null;

        productId = entityProductId( entity );
        productCode = entityProductCode( entity );
        productName = entityProductName( entity );
        id = entity.getId();
        receivedQuantity = entity.getReceivedQuantity();
        remarks = entity.getRemarks();

        GoodsReceiptItemResponse goodsReceiptItemResponse = new GoodsReceiptItemResponse( id, productId, productCode, productName, receivedQuantity, remarks );

        return goodsReceiptItemResponse;
    }

    @Override
    public List<GoodsReceiptItemResponse> toItemResponseList(List<GoodsReceiptItem> list) {
        if ( list == null ) {
            return null;
        }

        List<GoodsReceiptItemResponse> list1 = new ArrayList<GoodsReceiptItemResponse>( list.size() );
        for ( GoodsReceiptItem goodsReceiptItem : list ) {
            list1.add( toItemResponse( goodsReceiptItem ) );
        }

        return list1;
    }

    private Long entityPurchaseOrderId(GoodsReceipt goodsReceipt) {
        PurchaseOrder purchaseOrder = goodsReceipt.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getId();
    }

    private String entityPurchaseOrderOrderNumber(GoodsReceipt goodsReceipt) {
        PurchaseOrder purchaseOrder = goodsReceipt.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getOrderNumber();
    }

    private Long entityCompanyId(GoodsReceipt goodsReceipt) {
        Company company = goodsReceipt.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(GoodsReceipt goodsReceipt) {
        Company company = goodsReceipt.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private Long entityWarehouseId(GoodsReceipt goodsReceipt) {
        Warehouse warehouse = goodsReceipt.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseName(GoodsReceipt goodsReceipt) {
        Warehouse warehouse = goodsReceipt.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private Long entityStoreId(GoodsReceipt goodsReceipt) {
        Store store = goodsReceipt.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String entityStoreName(GoodsReceipt goodsReceipt) {
        Store store = goodsReceipt.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private Long entityReceivedById(GoodsReceipt goodsReceipt) {
        User receivedBy = goodsReceipt.getReceivedBy();
        if ( receivedBy == null ) {
            return null;
        }
        return receivedBy.getId();
    }

    private String entityReceivedByFirstName(GoodsReceipt goodsReceipt) {
        User receivedBy = goodsReceipt.getReceivedBy();
        if ( receivedBy == null ) {
            return null;
        }
        return receivedBy.getFirstName();
    }

    private Long entityCancelledById(GoodsReceipt goodsReceipt) {
        User cancelledBy = goodsReceipt.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String entityCancelledByFirstName(GoodsReceipt goodsReceipt) {
        User cancelledBy = goodsReceipt.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    private Long entityProductId(GoodsReceiptItem goodsReceiptItem) {
        Product product = goodsReceiptItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(GoodsReceiptItem goodsReceiptItem) {
        Product product = goodsReceiptItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(GoodsReceiptItem goodsReceiptItem) {
        Product product = goodsReceiptItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
