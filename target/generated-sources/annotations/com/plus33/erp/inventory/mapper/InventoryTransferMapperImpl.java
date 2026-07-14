package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.InventoryTransferItemResponse;
import com.plus33.erp.inventory.dto.InventoryTransferResponse;
import com.plus33.erp.inventory.entity.InventoryTransfer;
import com.plus33.erp.inventory.entity.InventoryTransferItem;
import com.plus33.erp.inventory.entity.InventoryTransferStatus;
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
    date = "2026-07-14T10:25:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class InventoryTransferMapperImpl implements InventoryTransferMapper {

    @Override
    public InventoryTransferResponse toResponse(InventoryTransfer entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long sourceWarehouseId = null;
        Long sourceStoreId = null;
        Long destWarehouseId = null;
        Long destStoreId = null;
        Long createdById = null;
        Long submittedById = null;
        Long approvedById = null;
        Long dispatchedById = null;
        Long receivedById = null;
        Long cancelledById = null;
        Long id = null;
        String transferNumber = null;
        InventoryTransferStatus status = null;
        UUID clientReferenceId = null;
        String remarks = null;
        LocalDateTime createdAt = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime dispatchedAt = null;
        LocalDateTime receivedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<InventoryTransferItemResponse> items = null;
        Long version = null;

        companyId = entityCompanyId( entity );
        sourceWarehouseId = entitySourceWarehouseId( entity );
        sourceStoreId = entitySourceStoreId( entity );
        destWarehouseId = entityDestWarehouseId( entity );
        destStoreId = entityDestStoreId( entity );
        createdById = entityCreatedById( entity );
        submittedById = entitySubmittedById( entity );
        approvedById = entityApprovedById( entity );
        dispatchedById = entityDispatchedById( entity );
        receivedById = entityReceivedById( entity );
        cancelledById = entityCancelledById( entity );
        id = entity.getId();
        transferNumber = entity.getTransferNumber();
        status = entity.getStatus();
        clientReferenceId = entity.getClientReferenceId();
        remarks = entity.getRemarks();
        createdAt = entity.getCreatedAt();
        submittedAt = entity.getSubmittedAt();
        approvedAt = entity.getApprovedAt();
        dispatchedAt = entity.getDispatchedAt();
        receivedAt = entity.getReceivedAt();
        cancelledAt = entity.getCancelledAt();
        cancellationReason = entity.getCancellationReason();
        items = inventoryTransferItemListToInventoryTransferItemResponseList( entity.getItems() );
        version = entity.getVersion();

        InventoryTransferResponse inventoryTransferResponse = new InventoryTransferResponse( id, transferNumber, companyId, sourceWarehouseId, sourceStoreId, destWarehouseId, destStoreId, status, clientReferenceId, remarks, createdById, createdAt, submittedById, submittedAt, approvedById, approvedAt, dispatchedById, dispatchedAt, receivedById, receivedAt, cancelledById, cancelledAt, cancellationReason, items, version );

        return inventoryTransferResponse;
    }

    @Override
    public InventoryTransferItemResponse toItemResponse(InventoryTransferItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        Long id = null;
        BigDecimal quantity = null;
        BigDecimal receivedQuantity = null;
        BigDecimal remainingQuantity = null;

        productId = entityProductId( entity );
        productName = entityProductName( entity );
        id = entity.getId();
        quantity = entity.getQuantity();
        receivedQuantity = entity.getReceivedQuantity();
        remainingQuantity = entity.getRemainingQuantity();

        InventoryTransferItemResponse inventoryTransferItemResponse = new InventoryTransferItemResponse( id, productId, productName, quantity, receivedQuantity, remainingQuantity );

        return inventoryTransferItemResponse;
    }

    private Long entityCompanyId(InventoryTransfer inventoryTransfer) {
        Company company = inventoryTransfer.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entitySourceWarehouseId(InventoryTransfer inventoryTransfer) {
        Warehouse sourceWarehouse = inventoryTransfer.getSourceWarehouse();
        if ( sourceWarehouse == null ) {
            return null;
        }
        return sourceWarehouse.getId();
    }

    private Long entitySourceStoreId(InventoryTransfer inventoryTransfer) {
        Store sourceStore = inventoryTransfer.getSourceStore();
        if ( sourceStore == null ) {
            return null;
        }
        return sourceStore.getId();
    }

    private Long entityDestWarehouseId(InventoryTransfer inventoryTransfer) {
        Warehouse destWarehouse = inventoryTransfer.getDestWarehouse();
        if ( destWarehouse == null ) {
            return null;
        }
        return destWarehouse.getId();
    }

    private Long entityDestStoreId(InventoryTransfer inventoryTransfer) {
        Store destStore = inventoryTransfer.getDestStore();
        if ( destStore == null ) {
            return null;
        }
        return destStore.getId();
    }

    private Long entityCreatedById(InventoryTransfer inventoryTransfer) {
        User createdBy = inventoryTransfer.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private Long entitySubmittedById(InventoryTransfer inventoryTransfer) {
        User submittedBy = inventoryTransfer.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private Long entityApprovedById(InventoryTransfer inventoryTransfer) {
        User approvedBy = inventoryTransfer.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private Long entityDispatchedById(InventoryTransfer inventoryTransfer) {
        User dispatchedBy = inventoryTransfer.getDispatchedBy();
        if ( dispatchedBy == null ) {
            return null;
        }
        return dispatchedBy.getId();
    }

    private Long entityReceivedById(InventoryTransfer inventoryTransfer) {
        User receivedBy = inventoryTransfer.getReceivedBy();
        if ( receivedBy == null ) {
            return null;
        }
        return receivedBy.getId();
    }

    private Long entityCancelledById(InventoryTransfer inventoryTransfer) {
        User cancelledBy = inventoryTransfer.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    protected List<InventoryTransferItemResponse> inventoryTransferItemListToInventoryTransferItemResponseList(List<InventoryTransferItem> list) {
        if ( list == null ) {
            return null;
        }

        List<InventoryTransferItemResponse> list1 = new ArrayList<InventoryTransferItemResponse>( list.size() );
        for ( InventoryTransferItem inventoryTransferItem : list ) {
            list1.add( toItemResponse( inventoryTransferItem ) );
        }

        return list1;
    }

    private Long entityProductId(InventoryTransferItem inventoryTransferItem) {
        Product product = inventoryTransferItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductName(InventoryTransferItem inventoryTransferItem) {
        Product product = inventoryTransferItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
