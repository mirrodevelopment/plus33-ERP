package com.plus33.erp.procurement.mapper;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.procurement.dto.PurchaseRequestItemRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestItemResponse;
import com.plus33.erp.procurement.dto.PurchaseRequestRequest;
import com.plus33.erp.procurement.dto.PurchaseRequestResponse;
import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import com.plus33.erp.procurement.entity.PurchaseRequestItem;
import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
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
    date = "2026-07-14T10:25:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PurchaseRequestMapperImpl implements PurchaseRequestMapper {

    @Override
    public PurchaseRequest toEntity(PurchaseRequestRequest request) {
        if ( request == null ) {
            return null;
        }

        PurchaseRequest purchaseRequest = new PurchaseRequest();

        purchaseRequest.setRequiredDate( request.requiredDate() );
        purchaseRequest.setNotes( request.notes() );

        return purchaseRequest;
    }

    @Override
    public PurchaseRequestResponse toResponse(PurchaseRequest entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        String companyCode = null;
        Long supplierId = null;
        String supplierName = null;
        String supplierCode = null;
        Long requestedByUserId = null;
        String requestedByUserName = null;
        Long submittedByUserId = null;
        String submittedByUserName = null;
        Long approvedByUserId = null;
        String approvedByUserName = null;
        Long warehouseId = null;
        String warehouseName = null;
        String warehouseCode = null;
        Long storeId = null;
        String storeName = null;
        String storeCode = null;
        Long purchaseOrderId = null;
        String purchaseOrderNumber = null;
        Long id = null;
        String requestNumber = null;
        PurchaseRequestStatus status = null;
        LocalDate requestDate = null;
        LocalDate requiredDate = null;
        String notes = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime rejectedAt = null;
        LocalDateTime cancelledAt = null;
        LocalDateTime convertedToPoAt = null;
        String rejectionReason = null;
        String cancellationReason = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        List<PurchaseRequestItemResponse> items = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        companyCode = entityCompanyCode( entity );
        supplierId = entitySupplierId( entity );
        supplierName = entitySupplierName( entity );
        supplierCode = entitySupplierCode( entity );
        requestedByUserId = entityRequestedById( entity );
        requestedByUserName = entityRequestedByFirstName( entity );
        submittedByUserId = entitySubmittedById( entity );
        submittedByUserName = entitySubmittedByFirstName( entity );
        approvedByUserId = entityApprovedById( entity );
        approvedByUserName = entityApprovedByFirstName( entity );
        warehouseId = entityWarehouseId( entity );
        warehouseName = entityWarehouseName( entity );
        warehouseCode = entityWarehouseCode( entity );
        storeId = entityStoreId( entity );
        storeName = entityStoreName( entity );
        storeCode = entityStoreCode( entity );
        purchaseOrderId = entityPurchaseOrderId( entity );
        purchaseOrderNumber = entityPurchaseOrderOrderNumber( entity );
        id = entity.getId();
        requestNumber = entity.getRequestNumber();
        status = entity.getStatus();
        requestDate = entity.getRequestDate();
        requiredDate = entity.getRequiredDate();
        notes = entity.getNotes();
        submittedAt = entity.getSubmittedAt();
        approvedAt = entity.getApprovedAt();
        rejectedAt = entity.getRejectedAt();
        cancelledAt = entity.getCancelledAt();
        convertedToPoAt = entity.getConvertedToPoAt();
        rejectionReason = entity.getRejectionReason();
        cancellationReason = entity.getCancellationReason();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        items = toItemResponseList( entity.getItems() );

        PurchaseRequestResponse purchaseRequestResponse = new PurchaseRequestResponse( id, requestNumber, companyId, companyName, companyCode, supplierId, supplierName, supplierCode, requestedByUserId, requestedByUserName, submittedByUserId, submittedByUserName, approvedByUserId, approvedByUserName, warehouseId, warehouseName, warehouseCode, storeId, storeName, storeCode, status, requestDate, requiredDate, notes, submittedAt, approvedAt, rejectedAt, cancelledAt, convertedToPoAt, rejectionReason, cancellationReason, purchaseOrderId, purchaseOrderNumber, createdAt, updatedAt, items );

        return purchaseRequestResponse;
    }

    @Override
    public PurchaseRequestItem toItemEntity(PurchaseRequestItemRequest request) {
        if ( request == null ) {
            return null;
        }

        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();

        purchaseRequestItem.setRequestedQuantity( request.requestedQuantity() );
        purchaseRequestItem.setUnitOfMeasure( request.unitOfMeasure() );
        purchaseRequestItem.setRemarks( request.remarks() );

        return purchaseRequestItem;
    }

    @Override
    public PurchaseRequestItemResponse toItemResponse(PurchaseRequestItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productCode = null;
        String productName = null;
        Long dimensionSetId = null;
        Long id = null;
        BigDecimal requestedQuantity = null;
        BigDecimal approvedQuantity = null;
        String unitOfMeasure = null;
        String remarks = null;

        productId = entityProductId( entity );
        productCode = entityProductCode( entity );
        productName = entityProductName( entity );
        dimensionSetId = entityDimensionSetId( entity );
        id = entity.getId();
        requestedQuantity = entity.getRequestedQuantity();
        approvedQuantity = entity.getApprovedQuantity();
        unitOfMeasure = entity.getUnitOfMeasure();
        remarks = entity.getRemarks();

        PurchaseRequestItemResponse purchaseRequestItemResponse = new PurchaseRequestItemResponse( id, productId, productCode, productName, requestedQuantity, approvedQuantity, unitOfMeasure, remarks, dimensionSetId );

        return purchaseRequestItemResponse;
    }

    @Override
    public List<PurchaseRequestItemResponse> toItemResponseList(List<PurchaseRequestItem> list) {
        if ( list == null ) {
            return null;
        }

        List<PurchaseRequestItemResponse> list1 = new ArrayList<PurchaseRequestItemResponse>( list.size() );
        for ( PurchaseRequestItem purchaseRequestItem : list ) {
            list1.add( toItemResponse( purchaseRequestItem ) );
        }

        return list1;
    }

    private Long entityCompanyId(PurchaseRequest purchaseRequest) {
        Company company = purchaseRequest.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(PurchaseRequest purchaseRequest) {
        Company company = purchaseRequest.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private String entityCompanyCode(PurchaseRequest purchaseRequest) {
        Company company = purchaseRequest.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getCode();
    }

    private Long entitySupplierId(PurchaseRequest purchaseRequest) {
        Supplier supplier = purchaseRequest.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierName(PurchaseRequest purchaseRequest) {
        Supplier supplier = purchaseRequest.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private String entitySupplierCode(PurchaseRequest purchaseRequest) {
        Supplier supplier = purchaseRequest.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getCode();
    }

    private Long entityRequestedById(PurchaseRequest purchaseRequest) {
        User requestedBy = purchaseRequest.getRequestedBy();
        if ( requestedBy == null ) {
            return null;
        }
        return requestedBy.getId();
    }

    private String entityRequestedByFirstName(PurchaseRequest purchaseRequest) {
        User requestedBy = purchaseRequest.getRequestedBy();
        if ( requestedBy == null ) {
            return null;
        }
        return requestedBy.getFirstName();
    }

    private Long entitySubmittedById(PurchaseRequest purchaseRequest) {
        User submittedBy = purchaseRequest.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private String entitySubmittedByFirstName(PurchaseRequest purchaseRequest) {
        User submittedBy = purchaseRequest.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getFirstName();
    }

    private Long entityApprovedById(PurchaseRequest purchaseRequest) {
        User approvedBy = purchaseRequest.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private String entityApprovedByFirstName(PurchaseRequest purchaseRequest) {
        User approvedBy = purchaseRequest.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getFirstName();
    }

    private Long entityWarehouseId(PurchaseRequest purchaseRequest) {
        Warehouse warehouse = purchaseRequest.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseName(PurchaseRequest purchaseRequest) {
        Warehouse warehouse = purchaseRequest.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private String entityWarehouseCode(PurchaseRequest purchaseRequest) {
        Warehouse warehouse = purchaseRequest.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getCode();
    }

    private Long entityStoreId(PurchaseRequest purchaseRequest) {
        Store store = purchaseRequest.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String entityStoreName(PurchaseRequest purchaseRequest) {
        Store store = purchaseRequest.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private String entityStoreCode(PurchaseRequest purchaseRequest) {
        Store store = purchaseRequest.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getCode();
    }

    private Long entityPurchaseOrderId(PurchaseRequest purchaseRequest) {
        PurchaseOrder purchaseOrder = purchaseRequest.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getId();
    }

    private String entityPurchaseOrderOrderNumber(PurchaseRequest purchaseRequest) {
        PurchaseOrder purchaseOrder = purchaseRequest.getPurchaseOrder();
        if ( purchaseOrder == null ) {
            return null;
        }
        return purchaseOrder.getOrderNumber();
    }

    private Long entityProductId(PurchaseRequestItem purchaseRequestItem) {
        Product product = purchaseRequestItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(PurchaseRequestItem purchaseRequestItem) {
        Product product = purchaseRequestItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(PurchaseRequestItem purchaseRequestItem) {
        Product product = purchaseRequestItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long entityDimensionSetId(PurchaseRequestItem purchaseRequestItem) {
        BudgetDimensionSet dimensionSet = purchaseRequestItem.getDimensionSet();
        if ( dimensionSet == null ) {
            return null;
        }
        return dimensionSet.getId();
    }
}
