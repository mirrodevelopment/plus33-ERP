package com.plus33.erp.procurement.mapper;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.procurement.dto.PurchaseOrderItemRequest;
import com.plus33.erp.procurement.dto.PurchaseOrderItemResponse;
import com.plus33.erp.procurement.dto.PurchaseOrderRequest;
import com.plus33.erp.procurement.dto.PurchaseOrderResponse;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.entity.PurchaseOrderItem;
import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.security.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:49+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PurchaseOrderMapperImpl implements PurchaseOrderMapper {

    @Override
    public PurchaseOrder toEntity(PurchaseOrderRequest request) {
        if ( request == null ) {
            return null;
        }

        PurchaseOrder purchaseOrder = new PurchaseOrder();

        purchaseOrder.setExpectedDeliveryDate( request.expectedDeliveryDate() );
        purchaseOrder.setNotes( request.notes() );
        purchaseOrder.setTaxAmount( request.taxAmount() );
        purchaseOrder.setDiscountAmount( request.discountAmount() );
        purchaseOrder.setCurrencyCode( request.currencyCode() );

        return purchaseOrder;
    }

    @Override
    public PurchaseOrderResponse toResponse(PurchaseOrder entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        String companyCode = null;
        Long supplierId = null;
        String supplierName = null;
        String supplierCode = null;
        Long purchaseRequestId = null;
        String purchaseRequestNumber = null;
        Long orderedByUserId = null;
        String orderedByUserName = null;
        Long issuedByUserId = null;
        String issuedByUserName = null;
        Long cancelledByUserId = null;
        String cancelledByUserName = null;
        Long id = null;
        String orderNumber = null;
        LocalDate expectedDeliveryDate = null;
        PurchaseOrderStatus status = null;
        String notes = null;
        LocalDateTime issuedAt = null;
        LocalDateTime cancelledAt = null;
        LocalDateTime receivedAt = null;
        LocalDateTime closedAt = null;
        String cancellationReason = null;
        BigDecimal receivedPercentage = null;
        BigDecimal subtotalAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal totalAmount = null;
        String currencyCode = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        List<PurchaseOrderItemResponse> items = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        companyCode = entityCompanyCode( entity );
        supplierId = entitySupplierId( entity );
        supplierName = entitySupplierName( entity );
        supplierCode = entitySupplierCode( entity );
        purchaseRequestId = entityPurchaseRequestId( entity );
        purchaseRequestNumber = entityPurchaseRequestRequestNumber( entity );
        orderedByUserId = entityOrderedById( entity );
        orderedByUserName = entityOrderedByFirstName( entity );
        issuedByUserId = entityIssuedById( entity );
        issuedByUserName = entityIssuedByFirstName( entity );
        cancelledByUserId = entityCancelledById( entity );
        cancelledByUserName = entityCancelledByFirstName( entity );
        id = entity.getId();
        orderNumber = entity.getOrderNumber();
        expectedDeliveryDate = entity.getExpectedDeliveryDate();
        status = entity.getStatus();
        notes = entity.getNotes();
        issuedAt = entity.getIssuedAt();
        cancelledAt = entity.getCancelledAt();
        receivedAt = entity.getReceivedAt();
        closedAt = entity.getClosedAt();
        cancellationReason = entity.getCancellationReason();
        receivedPercentage = entity.getReceivedPercentage();
        subtotalAmount = entity.getSubtotalAmount();
        taxAmount = entity.getTaxAmount();
        discountAmount = entity.getDiscountAmount();
        totalAmount = entity.getTotalAmount();
        currencyCode = entity.getCurrencyCode();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        items = toItemResponseList( entity.getItems() );

        PurchaseOrderResponse purchaseOrderResponse = new PurchaseOrderResponse( id, orderNumber, companyId, companyName, companyCode, supplierId, supplierName, supplierCode, purchaseRequestId, purchaseRequestNumber, orderedByUserId, orderedByUserName, issuedByUserId, issuedByUserName, cancelledByUserId, cancelledByUserName, expectedDeliveryDate, status, notes, issuedAt, cancelledAt, receivedAt, closedAt, cancellationReason, receivedPercentage, subtotalAmount, taxAmount, discountAmount, totalAmount, currencyCode, createdAt, updatedAt, items );

        return purchaseOrderResponse;
    }

    @Override
    public PurchaseOrderItem toItemEntity(PurchaseOrderItemRequest request) {
        if ( request == null ) {
            return null;
        }

        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();

        purchaseOrderItem.setOrderedQuantity( request.orderedQuantity() );
        purchaseOrderItem.setUnitPrice( request.unitPrice() );
        purchaseOrderItem.setRemarks( request.remarks() );

        return purchaseOrderItem;
    }

    @Override
    public PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productCode = null;
        String productName = null;
        Long dimensionSetId = null;
        Long id = null;
        BigDecimal orderedQuantity = null;
        BigDecimal unitPrice = null;
        BigDecimal receivedQuantity = null;
        BigDecimal remainingQuantity = null;
        String remarks = null;

        productId = entityProductId( entity );
        productCode = entityProductCode( entity );
        productName = entityProductName( entity );
        dimensionSetId = entityDimensionSetId( entity );
        id = entity.getId();
        orderedQuantity = entity.getOrderedQuantity();
        unitPrice = entity.getUnitPrice();
        receivedQuantity = entity.getReceivedQuantity();
        remainingQuantity = entity.getRemainingQuantity();
        remarks = entity.getRemarks();

        PurchaseOrderItemResponse purchaseOrderItemResponse = new PurchaseOrderItemResponse( id, productId, productCode, productName, orderedQuantity, unitPrice, receivedQuantity, remainingQuantity, remarks, dimensionSetId );

        return purchaseOrderItemResponse;
    }

    @Override
    public List<PurchaseOrderItemResponse> toItemResponseList(List<PurchaseOrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<PurchaseOrderItemResponse> list1 = new ArrayList<PurchaseOrderItemResponse>( list.size() );
        for ( PurchaseOrderItem purchaseOrderItem : list ) {
            list1.add( toItemResponse( purchaseOrderItem ) );
        }

        return list1;
    }

    private Long entityCompanyId(PurchaseOrder purchaseOrder) {
        Company company = purchaseOrder.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(PurchaseOrder purchaseOrder) {
        Company company = purchaseOrder.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private String entityCompanyCode(PurchaseOrder purchaseOrder) {
        Company company = purchaseOrder.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getCode();
    }

    private Long entitySupplierId(PurchaseOrder purchaseOrder) {
        Supplier supplier = purchaseOrder.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierName(PurchaseOrder purchaseOrder) {
        Supplier supplier = purchaseOrder.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private String entitySupplierCode(PurchaseOrder purchaseOrder) {
        Supplier supplier = purchaseOrder.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getCode();
    }

    private Long entityPurchaseRequestId(PurchaseOrder purchaseOrder) {
        PurchaseRequest purchaseRequest = purchaseOrder.getPurchaseRequest();
        if ( purchaseRequest == null ) {
            return null;
        }
        return purchaseRequest.getId();
    }

    private String entityPurchaseRequestRequestNumber(PurchaseOrder purchaseOrder) {
        PurchaseRequest purchaseRequest = purchaseOrder.getPurchaseRequest();
        if ( purchaseRequest == null ) {
            return null;
        }
        return purchaseRequest.getRequestNumber();
    }

    private Long entityOrderedById(PurchaseOrder purchaseOrder) {
        User orderedBy = purchaseOrder.getOrderedBy();
        if ( orderedBy == null ) {
            return null;
        }
        return orderedBy.getId();
    }

    private String entityOrderedByFirstName(PurchaseOrder purchaseOrder) {
        User orderedBy = purchaseOrder.getOrderedBy();
        if ( orderedBy == null ) {
            return null;
        }
        return orderedBy.getFirstName();
    }

    private Long entityIssuedById(PurchaseOrder purchaseOrder) {
        User issuedBy = purchaseOrder.getIssuedBy();
        if ( issuedBy == null ) {
            return null;
        }
        return issuedBy.getId();
    }

    private String entityIssuedByFirstName(PurchaseOrder purchaseOrder) {
        User issuedBy = purchaseOrder.getIssuedBy();
        if ( issuedBy == null ) {
            return null;
        }
        return issuedBy.getFirstName();
    }

    private Long entityCancelledById(PurchaseOrder purchaseOrder) {
        User cancelledBy = purchaseOrder.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String entityCancelledByFirstName(PurchaseOrder purchaseOrder) {
        User cancelledBy = purchaseOrder.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    private Long entityProductId(PurchaseOrderItem purchaseOrderItem) {
        Product product = purchaseOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(PurchaseOrderItem purchaseOrderItem) {
        Product product = purchaseOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(PurchaseOrderItem purchaseOrderItem) {
        Product product = purchaseOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long entityDimensionSetId(PurchaseOrderItem purchaseOrderItem) {
        BudgetDimensionSet dimensionSet = purchaseOrderItem.getDimensionSet();
        if ( dimensionSet == null ) {
            return null;
        }
        return dimensionSet.getId();
    }
}
