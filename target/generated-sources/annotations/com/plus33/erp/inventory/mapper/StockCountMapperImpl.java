package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.StockCountItemResponse;
import com.plus33.erp.inventory.dto.StockCountResponse;
import com.plus33.erp.inventory.entity.InventoryAdjustment;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.StockCount;
import com.plus33.erp.inventory.entity.StockCountItem;
import com.plus33.erp.inventory.entity.StockCountStatus;
import com.plus33.erp.inventory.entity.StockCountType;
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
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class StockCountMapperImpl implements StockCountMapper {

    @Override
    public StockCountResponse toResponse(StockCount entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long warehouseId = null;
        Long storeId = null;
        Long assignedToId = null;
        String assignedToName = null;
        Long adjustmentId = null;
        Long createdById = null;
        Long assignedById = null;
        Long startedById = null;
        Long submittedById = null;
        Long approvedById = null;
        Long postedById = null;
        Long closedById = null;
        Long id = null;
        String countNumber = null;
        StockCountStatus status = null;
        StockCountType countType = null;
        boolean blindCount = false;
        boolean approvalRequired = false;
        BigDecimal approvalThresholdPercentage = null;
        UUID clientReferenceId = null;
        String remarks = null;
        String rejectionReason = null;
        int recountNumber = 0;
        LocalDateTime createdAt = null;
        LocalDateTime assignedAt = null;
        LocalDateTime startedAt = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime postedAt = null;
        LocalDateTime closedAt = null;
        List<StockCountItemResponse> items = null;
        Long version = null;

        companyId = entityCompanyId( entity );
        warehouseId = entityWarehouseId( entity );
        storeId = entityStoreId( entity );
        assignedToId = entityAssignedToId( entity );
        assignedToName = entityAssignedToEmail( entity );
        adjustmentId = entityAdjustmentId( entity );
        createdById = entityCreatedById( entity );
        assignedById = entityAssignedById( entity );
        startedById = entityStartedById( entity );
        submittedById = entitySubmittedById( entity );
        approvedById = entityApprovedById( entity );
        postedById = entityPostedById( entity );
        closedById = entityClosedById( entity );
        id = entity.getId();
        countNumber = entity.getCountNumber();
        status = entity.getStatus();
        countType = entity.getCountType();
        blindCount = entity.isBlindCount();
        approvalRequired = entity.isApprovalRequired();
        approvalThresholdPercentage = entity.getApprovalThresholdPercentage();
        clientReferenceId = entity.getClientReferenceId();
        remarks = entity.getRemarks();
        rejectionReason = entity.getRejectionReason();
        recountNumber = entity.getRecountNumber();
        createdAt = entity.getCreatedAt();
        assignedAt = entity.getAssignedAt();
        startedAt = entity.getStartedAt();
        submittedAt = entity.getSubmittedAt();
        approvedAt = entity.getApprovedAt();
        postedAt = entity.getPostedAt();
        closedAt = entity.getClosedAt();
        items = stockCountItemListToStockCountItemResponseList( entity.getItems() );
        version = entity.getVersion();

        StockCountResponse stockCountResponse = new StockCountResponse( id, countNumber, companyId, warehouseId, storeId, status, countType, blindCount, assignedToId, assignedToName, adjustmentId, approvalRequired, approvalThresholdPercentage, clientReferenceId, remarks, rejectionReason, recountNumber, createdById, createdAt, assignedById, assignedAt, startedById, startedAt, submittedById, submittedAt, approvedById, approvedAt, postedById, postedAt, closedById, closedAt, items, version );

        return stockCountResponse;
    }

    @Override
    public StockCountItemResponse toItemResponse(StockCountItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productCode = null;
        String productName = null;
        Long id = null;
        BigDecimal systemQuantity = null;
        BigDecimal reservedQuantity = null;
        BigDecimal availableQuantity = null;
        BigDecimal countedQuantity = null;
        BigDecimal variance = null;
        Long version = null;

        productId = entityProductId( entity );
        productCode = entityProductCode( entity );
        productName = entityProductName( entity );
        id = entity.getId();
        systemQuantity = entity.getSystemQuantity();
        reservedQuantity = entity.getReservedQuantity();
        availableQuantity = entity.getAvailableQuantity();
        countedQuantity = entity.getCountedQuantity();
        variance = entity.getVariance();
        version = entity.getVersion();

        StockCountItemResponse stockCountItemResponse = new StockCountItemResponse( id, productId, productCode, productName, systemQuantity, reservedQuantity, availableQuantity, countedQuantity, variance, version );

        return stockCountItemResponse;
    }

    private Long entityCompanyId(StockCount stockCount) {
        Company company = stockCount.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityWarehouseId(StockCount stockCount) {
        Warehouse warehouse = stockCount.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private Long entityStoreId(StockCount stockCount) {
        Store store = stockCount.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private Long entityAssignedToId(StockCount stockCount) {
        User assignedTo = stockCount.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        return assignedTo.getId();
    }

    private String entityAssignedToEmail(StockCount stockCount) {
        User assignedTo = stockCount.getAssignedTo();
        if ( assignedTo == null ) {
            return null;
        }
        return assignedTo.getEmail();
    }

    private Long entityAdjustmentId(StockCount stockCount) {
        InventoryAdjustment adjustment = stockCount.getAdjustment();
        if ( adjustment == null ) {
            return null;
        }
        return adjustment.getId();
    }

    private Long entityCreatedById(StockCount stockCount) {
        User createdBy = stockCount.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private Long entityAssignedById(StockCount stockCount) {
        User assignedBy = stockCount.getAssignedBy();
        if ( assignedBy == null ) {
            return null;
        }
        return assignedBy.getId();
    }

    private Long entityStartedById(StockCount stockCount) {
        User startedBy = stockCount.getStartedBy();
        if ( startedBy == null ) {
            return null;
        }
        return startedBy.getId();
    }

    private Long entitySubmittedById(StockCount stockCount) {
        User submittedBy = stockCount.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private Long entityApprovedById(StockCount stockCount) {
        User approvedBy = stockCount.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private Long entityPostedById(StockCount stockCount) {
        User postedBy = stockCount.getPostedBy();
        if ( postedBy == null ) {
            return null;
        }
        return postedBy.getId();
    }

    private Long entityClosedById(StockCount stockCount) {
        User closedBy = stockCount.getClosedBy();
        if ( closedBy == null ) {
            return null;
        }
        return closedBy.getId();
    }

    protected List<StockCountItemResponse> stockCountItemListToStockCountItemResponseList(List<StockCountItem> list) {
        if ( list == null ) {
            return null;
        }

        List<StockCountItemResponse> list1 = new ArrayList<StockCountItemResponse>( list.size() );
        for ( StockCountItem stockCountItem : list ) {
            list1.add( toItemResponse( stockCountItem ) );
        }

        return list1;
    }

    private Long entityProductId(StockCountItem stockCountItem) {
        Product product = stockCountItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(StockCountItem stockCountItem) {
        Product product = stockCountItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(StockCountItem stockCountItem) {
        Product product = stockCountItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
