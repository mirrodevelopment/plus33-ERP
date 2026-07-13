package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.ReplenishmentRuleResponse;
import com.plus33.erp.inventory.dto.ReplenishmentSuggestionResponse;
import com.plus33.erp.inventory.entity.InventoryTransfer;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ReplenishmentEvaluationSource;
import com.plus33.erp.inventory.entity.ReplenishmentRule;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestion;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.procurement.entity.PurchaseRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:46+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class ReplenishmentMapperImpl implements ReplenishmentMapper {

    @Override
    public ReplenishmentRuleResponse toResponse(ReplenishmentRule entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long productId = null;
        String productName = null;
        Long warehouseId = null;
        String warehouseName = null;
        Long storeId = null;
        String storeName = null;
        Long id = null;
        BigDecimal minQuantity = null;
        BigDecimal maxQuantity = null;
        BigDecimal reorderPoint = null;
        BigDecimal reorderQuantity = null;
        int leadTimeDays = 0;
        boolean active = false;
        UUID clientReferenceId = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Long version = null;

        companyId = entityCompanyId( entity );
        productId = entityProductId( entity );
        productName = entityProductName( entity );
        warehouseId = entityWarehouseId( entity );
        warehouseName = entityWarehouseName( entity );
        storeId = entityStoreId( entity );
        storeName = entityStoreName( entity );
        id = entity.getId();
        minQuantity = entity.getMinQuantity();
        maxQuantity = entity.getMaxQuantity();
        reorderPoint = entity.getReorderPoint();
        reorderQuantity = entity.getReorderQuantity();
        leadTimeDays = entity.getLeadTimeDays();
        active = entity.isActive();
        clientReferenceId = entity.getClientReferenceId();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        version = entity.getVersion();

        ReplenishmentRuleResponse replenishmentRuleResponse = new ReplenishmentRuleResponse( id, companyId, productId, productName, warehouseId, warehouseName, storeId, storeName, minQuantity, maxQuantity, reorderPoint, reorderQuantity, leadTimeDays, active, clientReferenceId, createdAt, updatedAt, version );

        return replenishmentRuleResponse;
    }

    @Override
    public ReplenishmentSuggestionResponse toResponse(ReplenishmentSuggestion entity) {
        if ( entity == null ) {
            return null;
        }

        Long ruleId = null;
        Long companyId = null;
        Long productId = null;
        String productName = null;
        Long warehouseId = null;
        String warehouseName = null;
        Long storeId = null;
        String storeName = null;
        Long purchaseRequestId = null;
        String purchaseRequestNumber = null;
        Long transferId = null;
        String transferNumber = null;
        Long id = null;
        BigDecimal currentQuantity = null;
        BigDecimal reservedQuantity = null;
        BigDecimal availableQuantity = null;
        BigDecimal suggestedQuantity = null;
        ReplenishmentSuggestionStatus status = null;
        ReplenishmentEvaluationSource evaluationSource = null;
        UUID clientReferenceId = null;
        String notes = null;
        LocalDateTime evaluatedAt = null;
        LocalDateTime acknowledgedAt = null;
        LocalDateTime orderedAt = null;
        LocalDateTime fulfilledAt = null;
        LocalDateTime cancelledAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Long version = null;

        ruleId = entityRuleId( entity );
        companyId = entityCompanyId1( entity );
        productId = entityProductId1( entity );
        productName = entityProductName1( entity );
        warehouseId = entityWarehouseId1( entity );
        warehouseName = entityWarehouseName1( entity );
        storeId = entityStoreId1( entity );
        storeName = entityStoreName1( entity );
        purchaseRequestId = entityPurchaseRequestId( entity );
        purchaseRequestNumber = entityPurchaseRequestRequestNumber( entity );
        transferId = entityTransferId( entity );
        transferNumber = entityTransferTransferNumber( entity );
        id = entity.getId();
        currentQuantity = entity.getCurrentQuantity();
        reservedQuantity = entity.getReservedQuantity();
        availableQuantity = entity.getAvailableQuantity();
        suggestedQuantity = entity.getSuggestedQuantity();
        status = entity.getStatus();
        evaluationSource = entity.getEvaluationSource();
        clientReferenceId = entity.getClientReferenceId();
        notes = entity.getNotes();
        evaluatedAt = entity.getEvaluatedAt();
        acknowledgedAt = entity.getAcknowledgedAt();
        orderedAt = entity.getOrderedAt();
        fulfilledAt = entity.getFulfilledAt();
        cancelledAt = entity.getCancelledAt();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        version = entity.getVersion();

        ReplenishmentSuggestionResponse replenishmentSuggestionResponse = new ReplenishmentSuggestionResponse( id, ruleId, companyId, productId, productName, warehouseId, warehouseName, storeId, storeName, currentQuantity, reservedQuantity, availableQuantity, suggestedQuantity, status, evaluationSource, purchaseRequestId, purchaseRequestNumber, transferId, transferNumber, clientReferenceId, notes, evaluatedAt, acknowledgedAt, orderedAt, fulfilledAt, cancelledAt, createdAt, updatedAt, version );

        return replenishmentSuggestionResponse;
    }

    private Long entityCompanyId(ReplenishmentRule replenishmentRule) {
        Company company = replenishmentRule.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId(ReplenishmentRule replenishmentRule) {
        Product product = replenishmentRule.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductName(ReplenishmentRule replenishmentRule) {
        Product product = replenishmentRule.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long entityWarehouseId(ReplenishmentRule replenishmentRule) {
        Warehouse warehouse = replenishmentRule.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseName(ReplenishmentRule replenishmentRule) {
        Warehouse warehouse = replenishmentRule.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private Long entityStoreId(ReplenishmentRule replenishmentRule) {
        Store store = replenishmentRule.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String entityStoreName(ReplenishmentRule replenishmentRule) {
        Store store = replenishmentRule.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private Long entityRuleId(ReplenishmentSuggestion replenishmentSuggestion) {
        ReplenishmentRule rule = replenishmentSuggestion.getRule();
        if ( rule == null ) {
            return null;
        }
        return rule.getId();
    }

    private Long entityCompanyId1(ReplenishmentSuggestion replenishmentSuggestion) {
        Company company = replenishmentSuggestion.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId1(ReplenishmentSuggestion replenishmentSuggestion) {
        Product product = replenishmentSuggestion.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductName1(ReplenishmentSuggestion replenishmentSuggestion) {
        Product product = replenishmentSuggestion.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Long entityWarehouseId1(ReplenishmentSuggestion replenishmentSuggestion) {
        Warehouse warehouse = replenishmentSuggestion.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseName1(ReplenishmentSuggestion replenishmentSuggestion) {
        Warehouse warehouse = replenishmentSuggestion.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private Long entityStoreId1(ReplenishmentSuggestion replenishmentSuggestion) {
        Store store = replenishmentSuggestion.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String entityStoreName1(ReplenishmentSuggestion replenishmentSuggestion) {
        Store store = replenishmentSuggestion.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private Long entityPurchaseRequestId(ReplenishmentSuggestion replenishmentSuggestion) {
        PurchaseRequest purchaseRequest = replenishmentSuggestion.getPurchaseRequest();
        if ( purchaseRequest == null ) {
            return null;
        }
        return purchaseRequest.getId();
    }

    private String entityPurchaseRequestRequestNumber(ReplenishmentSuggestion replenishmentSuggestion) {
        PurchaseRequest purchaseRequest = replenishmentSuggestion.getPurchaseRequest();
        if ( purchaseRequest == null ) {
            return null;
        }
        return purchaseRequest.getRequestNumber();
    }

    private Long entityTransferId(ReplenishmentSuggestion replenishmentSuggestion) {
        InventoryTransfer transfer = replenishmentSuggestion.getTransfer();
        if ( transfer == null ) {
            return null;
        }
        return transfer.getId();
    }

    private String entityTransferTransferNumber(ReplenishmentSuggestion replenishmentSuggestion) {
        InventoryTransfer transfer = replenishmentSuggestion.getTransfer();
        if ( transfer == null ) {
            return null;
        }
        return transfer.getTransferNumber();
    }
}
