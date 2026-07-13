package com.plus33.erp.sales.mapper;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.sales.dto.PickListItemResponse;
import com.plus33.erp.sales.dto.PickListResponse;
import com.plus33.erp.sales.entity.PickList;
import com.plus33.erp.sales.entity.PickListItem;
import com.plus33.erp.sales.entity.PickListStatus;
import com.plus33.erp.sales.entity.SalesOrder;
import com.plus33.erp.sales.entity.SalesOrderItem;
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
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PickListMapperImpl implements PickListMapper {

    @Override
    public PickListResponse toResponse(PickList entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        Long salesOrderId = null;
        String salesOrderNumber = null;
        Long warehouseId = null;
        String warehouseName = null;
        Long storeId = null;
        String storeName = null;
        Long createdByUserId = null;
        String createdByUserName = null;
        Long releasedByUserId = null;
        String releasedByUserName = null;
        Long pickedByUserId = null;
        String pickedByUserName = null;
        Long packedByUserId = null;
        String packedByUserName = null;
        Long shippedByUserId = null;
        String shippedByUserName = null;
        Long cancelledByUserId = null;
        String cancelledByUserName = null;
        Long id = null;
        String pickNumber = null;
        UUID clientReferenceId = null;
        PickListStatus status = null;
        LocalDateTime createdAt = null;
        LocalDateTime releasedAt = null;
        LocalDateTime pickedAt = null;
        LocalDateTime packedAt = null;
        LocalDateTime shippedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        Long version = null;
        List<PickListItemResponse> items = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        salesOrderId = entitySalesOrderId( entity );
        salesOrderNumber = entitySalesOrderOrderNumber( entity );
        warehouseId = entityWarehouseId( entity );
        warehouseName = entityWarehouseName( entity );
        storeId = entityStoreId( entity );
        storeName = entityStoreName( entity );
        createdByUserId = entityCreatedById( entity );
        createdByUserName = entityCreatedByFirstName( entity );
        releasedByUserId = entityReleasedById( entity );
        releasedByUserName = entityReleasedByFirstName( entity );
        pickedByUserId = entityPickedById( entity );
        pickedByUserName = entityPickedByFirstName( entity );
        packedByUserId = entityPackedById( entity );
        packedByUserName = entityPackedByFirstName( entity );
        shippedByUserId = entityShippedById( entity );
        shippedByUserName = entityShippedByFirstName( entity );
        cancelledByUserId = entityCancelledById( entity );
        cancelledByUserName = entityCancelledByFirstName( entity );
        id = entity.getId();
        pickNumber = entity.getPickNumber();
        clientReferenceId = entity.getClientReferenceId();
        status = entity.getStatus();
        createdAt = entity.getCreatedAt();
        releasedAt = entity.getReleasedAt();
        pickedAt = entity.getPickedAt();
        packedAt = entity.getPackedAt();
        shippedAt = entity.getShippedAt();
        cancelledAt = entity.getCancelledAt();
        cancellationReason = entity.getCancellationReason();
        version = entity.getVersion();
        items = toItemResponseList( entity.getItems() );

        PickListResponse pickListResponse = new PickListResponse( id, companyId, companyName, salesOrderId, salesOrderNumber, pickNumber, clientReferenceId, status, warehouseId, warehouseName, storeId, storeName, createdByUserId, createdByUserName, releasedByUserId, releasedByUserName, pickedByUserId, pickedByUserName, packedByUserId, packedByUserName, shippedByUserId, shippedByUserName, cancelledByUserId, cancelledByUserName, createdAt, releasedAt, pickedAt, packedAt, shippedAt, cancelledAt, cancellationReason, version, items );

        return pickListResponse;
    }

    @Override
    public PickListItemResponse toItemResponse(PickListItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long salesOrderItemId = null;
        Long productId = null;
        String productName = null;
        String productSku = null;
        Long id = null;
        BigDecimal orderedQuantity = null;
        BigDecimal allocatedQuantity = null;
        BigDecimal pickedQuantity = null;
        BigDecimal shippedQuantity = null;

        salesOrderItemId = entitySalesOrderItemId( entity );
        productId = entityProductId( entity );
        productName = entityProductName( entity );
        productSku = entityProductCode( entity );
        id = entity.getId();
        orderedQuantity = entity.getOrderedQuantity();
        allocatedQuantity = entity.getAllocatedQuantity();
        pickedQuantity = entity.getPickedQuantity();
        shippedQuantity = entity.getShippedQuantity();

        PickListItemResponse pickListItemResponse = new PickListItemResponse( id, salesOrderItemId, productId, productName, productSku, orderedQuantity, allocatedQuantity, pickedQuantity, shippedQuantity );

        return pickListItemResponse;
    }

    @Override
    public List<PickListItemResponse> toItemResponseList(List<PickListItem> list) {
        if ( list == null ) {
            return null;
        }

        List<PickListItemResponse> list1 = new ArrayList<PickListItemResponse>( list.size() );
        for ( PickListItem pickListItem : list ) {
            list1.add( toItemResponse( pickListItem ) );
        }

        return list1;
    }

    @Override
    public List<PickListResponse> toResponseList(List<PickList> list) {
        if ( list == null ) {
            return null;
        }

        List<PickListResponse> list1 = new ArrayList<PickListResponse>( list.size() );
        for ( PickList pickList : list ) {
            list1.add( toResponse( pickList ) );
        }

        return list1;
    }

    private Long entityCompanyId(PickList pickList) {
        Company company = pickList.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(PickList pickList) {
        Company company = pickList.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private Long entitySalesOrderId(PickList pickList) {
        SalesOrder salesOrder = pickList.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getId();
    }

    private String entitySalesOrderOrderNumber(PickList pickList) {
        SalesOrder salesOrder = pickList.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getOrderNumber();
    }

    private Long entityWarehouseId(PickList pickList) {
        Warehouse warehouse = pickList.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseName(PickList pickList) {
        Warehouse warehouse = pickList.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private Long entityStoreId(PickList pickList) {
        Store store = pickList.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String entityStoreName(PickList pickList) {
        Store store = pickList.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private Long entityCreatedById(PickList pickList) {
        User createdBy = pickList.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String entityCreatedByFirstName(PickList pickList) {
        User createdBy = pickList.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getFirstName();
    }

    private Long entityReleasedById(PickList pickList) {
        User releasedBy = pickList.getReleasedBy();
        if ( releasedBy == null ) {
            return null;
        }
        return releasedBy.getId();
    }

    private String entityReleasedByFirstName(PickList pickList) {
        User releasedBy = pickList.getReleasedBy();
        if ( releasedBy == null ) {
            return null;
        }
        return releasedBy.getFirstName();
    }

    private Long entityPickedById(PickList pickList) {
        User pickedBy = pickList.getPickedBy();
        if ( pickedBy == null ) {
            return null;
        }
        return pickedBy.getId();
    }

    private String entityPickedByFirstName(PickList pickList) {
        User pickedBy = pickList.getPickedBy();
        if ( pickedBy == null ) {
            return null;
        }
        return pickedBy.getFirstName();
    }

    private Long entityPackedById(PickList pickList) {
        User packedBy = pickList.getPackedBy();
        if ( packedBy == null ) {
            return null;
        }
        return packedBy.getId();
    }

    private String entityPackedByFirstName(PickList pickList) {
        User packedBy = pickList.getPackedBy();
        if ( packedBy == null ) {
            return null;
        }
        return packedBy.getFirstName();
    }

    private Long entityShippedById(PickList pickList) {
        User shippedBy = pickList.getShippedBy();
        if ( shippedBy == null ) {
            return null;
        }
        return shippedBy.getId();
    }

    private String entityShippedByFirstName(PickList pickList) {
        User shippedBy = pickList.getShippedBy();
        if ( shippedBy == null ) {
            return null;
        }
        return shippedBy.getFirstName();
    }

    private Long entityCancelledById(PickList pickList) {
        User cancelledBy = pickList.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String entityCancelledByFirstName(PickList pickList) {
        User cancelledBy = pickList.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    private Long entitySalesOrderItemId(PickListItem pickListItem) {
        SalesOrderItem salesOrderItem = pickListItem.getSalesOrderItem();
        if ( salesOrderItem == null ) {
            return null;
        }
        return salesOrderItem.getId();
    }

    private Long entityProductId(PickListItem pickListItem) {
        Product product = pickListItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductName(PickListItem pickListItem) {
        Product product = pickListItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String entityProductCode(PickListItem pickListItem) {
        Product product = pickListItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }
}
