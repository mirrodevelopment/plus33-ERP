package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.InventoryAdjustmentItemResponse;
import com.plus33.erp.inventory.dto.InventoryAdjustmentResponse;
import com.plus33.erp.inventory.entity.InventoryAdjustment;
import com.plus33.erp.inventory.entity.InventoryAdjustmentItem;
import com.plus33.erp.inventory.entity.InventoryAdjustmentStatus;
import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
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
    date = "2026-07-14T10:25:45+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class InventoryAdjustmentMapperImpl implements InventoryAdjustmentMapper {

    @Override
    public InventoryAdjustmentResponse toResponse(InventoryAdjustment entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long warehouseId = null;
        Long storeId = null;
        Long createdById = null;
        Long submittedById = null;
        Long approvedById = null;
        Long postedById = null;
        Long cancelledById = null;
        Long id = null;
        String adjustmentNumber = null;
        InventoryAdjustmentType adjustmentType = null;
        InventoryAdjustmentStatus status = null;
        UUID clientReferenceId = null;
        String remarks = null;
        LocalDateTime createdAt = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime postedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<InventoryAdjustmentItemResponse> items = null;
        Long version = null;

        companyId = entityCompanyId( entity );
        warehouseId = entityWarehouseId( entity );
        storeId = entityStoreId( entity );
        createdById = entityCreatedById( entity );
        submittedById = entitySubmittedById( entity );
        approvedById = entityApprovedById( entity );
        postedById = entityPostedById( entity );
        cancelledById = entityCancelledById( entity );
        id = entity.getId();
        adjustmentNumber = entity.getAdjustmentNumber();
        adjustmentType = entity.getAdjustmentType();
        status = entity.getStatus();
        clientReferenceId = entity.getClientReferenceId();
        remarks = entity.getRemarks();
        createdAt = entity.getCreatedAt();
        submittedAt = entity.getSubmittedAt();
        approvedAt = entity.getApprovedAt();
        postedAt = entity.getPostedAt();
        cancelledAt = entity.getCancelledAt();
        cancellationReason = entity.getCancellationReason();
        items = inventoryAdjustmentItemListToInventoryAdjustmentItemResponseList( entity.getItems() );
        version = entity.getVersion();

        InventoryAdjustmentResponse inventoryAdjustmentResponse = new InventoryAdjustmentResponse( id, adjustmentNumber, companyId, warehouseId, storeId, adjustmentType, status, clientReferenceId, remarks, createdById, createdAt, submittedById, submittedAt, approvedById, approvedAt, postedById, postedAt, cancelledById, cancelledAt, cancellationReason, items, version );

        return inventoryAdjustmentResponse;
    }

    @Override
    public InventoryAdjustmentItemResponse toItemResponse(InventoryAdjustmentItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        Long id = null;
        BigDecimal quantity = null;

        productId = entityProductId( entity );
        productName = entityProductName( entity );
        id = entity.getId();
        quantity = entity.getQuantity();

        InventoryAdjustmentItemResponse inventoryAdjustmentItemResponse = new InventoryAdjustmentItemResponse( id, productId, productName, quantity );

        return inventoryAdjustmentItemResponse;
    }

    private Long entityCompanyId(InventoryAdjustment inventoryAdjustment) {
        Company company = inventoryAdjustment.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityWarehouseId(InventoryAdjustment inventoryAdjustment) {
        Warehouse warehouse = inventoryAdjustment.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private Long entityStoreId(InventoryAdjustment inventoryAdjustment) {
        Store store = inventoryAdjustment.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private Long entityCreatedById(InventoryAdjustment inventoryAdjustment) {
        User createdBy = inventoryAdjustment.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private Long entitySubmittedById(InventoryAdjustment inventoryAdjustment) {
        User submittedBy = inventoryAdjustment.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private Long entityApprovedById(InventoryAdjustment inventoryAdjustment) {
        User approvedBy = inventoryAdjustment.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private Long entityPostedById(InventoryAdjustment inventoryAdjustment) {
        User postedBy = inventoryAdjustment.getPostedBy();
        if ( postedBy == null ) {
            return null;
        }
        return postedBy.getId();
    }

    private Long entityCancelledById(InventoryAdjustment inventoryAdjustment) {
        User cancelledBy = inventoryAdjustment.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    protected List<InventoryAdjustmentItemResponse> inventoryAdjustmentItemListToInventoryAdjustmentItemResponseList(List<InventoryAdjustmentItem> list) {
        if ( list == null ) {
            return null;
        }

        List<InventoryAdjustmentItemResponse> list1 = new ArrayList<InventoryAdjustmentItemResponse>( list.size() );
        for ( InventoryAdjustmentItem inventoryAdjustmentItem : list ) {
            list1.add( toItemResponse( inventoryAdjustmentItem ) );
        }

        return list1;
    }

    private Long entityProductId(InventoryAdjustmentItem inventoryAdjustmentItem) {
        Product product = inventoryAdjustmentItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductName(InventoryAdjustmentItem inventoryAdjustmentItem) {
        Product product = inventoryAdjustmentItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
