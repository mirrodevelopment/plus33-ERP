package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.InventoryLotResponse;
import com.plus33.erp.inventory.dto.InventoryRecallResponse;
import com.plus33.erp.inventory.dto.InventorySerialResponse;
import com.plus33.erp.inventory.dto.InventoryTraceEventResponse;
import com.plus33.erp.inventory.entity.InventoryLot;
import com.plus33.erp.inventory.entity.InventoryLotStatus;
import com.plus33.erp.inventory.entity.InventoryRecall;
import com.plus33.erp.inventory.entity.InventoryRecallStatus;
import com.plus33.erp.inventory.entity.InventorySerial;
import com.plus33.erp.inventory.entity.InventorySerialStatus;
import com.plus33.erp.inventory.entity.InventoryTraceEvent;
import com.plus33.erp.inventory.entity.InventoryTraceEventType;
import com.plus33.erp.inventory.entity.InventoryTraceReferenceType;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:46+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class InventoryTraceabilityMapperImpl implements InventoryTraceabilityMapper {

    @Override
    public InventoryLotResponse toResponse(InventoryLot entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long productId = null;
        Long id = null;
        String lotNumber = null;
        LocalDate expiryDate = null;
        LocalDate manufacturedDate = null;
        InventoryLotStatus status = null;
        Long version = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId( entity );
        productId = entityProductId( entity );
        id = entity.getId();
        lotNumber = entity.getLotNumber();
        expiryDate = entity.getExpiryDate();
        manufacturedDate = entity.getManufacturedDate();
        status = entity.getStatus();
        version = entity.getVersion();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        InventoryLotResponse inventoryLotResponse = new InventoryLotResponse( id, companyId, productId, lotNumber, expiryDate, manufacturedDate, status, version, createdAt, updatedAt );

        return inventoryLotResponse;
    }

    @Override
    public InventorySerialResponse toResponse(InventorySerial entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long productId = null;
        Long lotId = null;
        String lotNumber = null;
        Long warehouseId = null;
        Long storeId = null;
        Long id = null;
        String serialNumber = null;
        InventorySerialStatus status = null;
        Long version = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId1( entity );
        productId = entityProductId1( entity );
        lotId = entityLotId( entity );
        lotNumber = entityLotLotNumber( entity );
        warehouseId = entityWarehouseId( entity );
        storeId = entityStoreId( entity );
        id = entity.getId();
        serialNumber = entity.getSerialNumber();
        status = entity.getStatus();
        version = entity.getVersion();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        InventorySerialResponse inventorySerialResponse = new InventorySerialResponse( id, companyId, productId, lotId, lotNumber, serialNumber, warehouseId, storeId, status, version, createdAt, updatedAt );

        return inventorySerialResponse;
    }

    @Override
    public InventoryTraceEventResponse toResponse(InventoryTraceEvent entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long productId = null;
        Long lotId = null;
        String lotNumber = null;
        Long serialId = null;
        String serialNumber = null;
        Long warehouseId = null;
        Long storeId = null;
        Long createdById = null;
        Long id = null;
        InventoryTraceEventType eventType = null;
        BigDecimal quantity = null;
        InventoryTraceReferenceType referenceType = null;
        Long referenceId = null;
        String referenceNumber = null;
        String notes = null;
        LocalDateTime createdAt = null;

        companyId = entityCompanyId2( entity );
        productId = entityProductId2( entity );
        lotId = entityLotId1( entity );
        lotNumber = entityLotLotNumber1( entity );
        serialId = entitySerialId( entity );
        serialNumber = entitySerialSerialNumber( entity );
        warehouseId = entityWarehouseId1( entity );
        storeId = entityStoreId1( entity );
        createdById = entityCreatedById( entity );
        id = entity.getId();
        eventType = entity.getEventType();
        quantity = entity.getQuantity();
        referenceType = entity.getReferenceType();
        referenceId = entity.getReferenceId();
        referenceNumber = entity.getReferenceNumber();
        notes = entity.getNotes();
        createdAt = entity.getCreatedAt();

        InventoryTraceEventResponse inventoryTraceEventResponse = new InventoryTraceEventResponse( id, companyId, productId, lotId, lotNumber, serialId, serialNumber, warehouseId, storeId, eventType, quantity, referenceType, referenceId, referenceNumber, notes, createdById, createdAt );

        return inventoryTraceEventResponse;
    }

    @Override
    public InventoryRecallResponse toResponse(InventoryRecall entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long productId = null;
        Long lotId = null;
        String lotNumber = null;
        Long serialId = null;
        String serialNumber = null;
        Long recalledById = null;
        Long id = null;
        String recallNumber = null;
        String recallReason = null;
        String recallReferenceNumber = null;
        InventoryRecallStatus status = null;
        LocalDateTime recalledAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId3( entity );
        productId = entityProductId3( entity );
        lotId = entityLotId2( entity );
        lotNumber = entityLotLotNumber2( entity );
        serialId = entitySerialId1( entity );
        serialNumber = entitySerialSerialNumber1( entity );
        recalledById = entityRecalledById( entity );
        id = entity.getId();
        recallNumber = entity.getRecallNumber();
        recallReason = entity.getRecallReason();
        recallReferenceNumber = entity.getRecallReferenceNumber();
        status = entity.getStatus();
        recalledAt = entity.getRecalledAt();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        InventoryRecallResponse inventoryRecallResponse = new InventoryRecallResponse( id, companyId, productId, lotId, lotNumber, serialId, serialNumber, recallNumber, recallReason, recallReferenceNumber, status, recalledById, recalledAt, createdAt, updatedAt );

        return inventoryRecallResponse;
    }

    private Long entityCompanyId(InventoryLot inventoryLot) {
        Company company = inventoryLot.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId(InventoryLot inventoryLot) {
        Product product = inventoryLot.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityCompanyId1(InventorySerial inventorySerial) {
        Company company = inventorySerial.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId1(InventorySerial inventorySerial) {
        Product product = inventorySerial.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityLotId(InventorySerial inventorySerial) {
        InventoryLot lot = inventorySerial.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getId();
    }

    private String entityLotLotNumber(InventorySerial inventorySerial) {
        InventoryLot lot = inventorySerial.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getLotNumber();
    }

    private Long entityWarehouseId(InventorySerial inventorySerial) {
        Warehouse warehouse = inventorySerial.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private Long entityStoreId(InventorySerial inventorySerial) {
        Store store = inventorySerial.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private Long entityCompanyId2(InventoryTraceEvent inventoryTraceEvent) {
        Company company = inventoryTraceEvent.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId2(InventoryTraceEvent inventoryTraceEvent) {
        Product product = inventoryTraceEvent.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityLotId1(InventoryTraceEvent inventoryTraceEvent) {
        InventoryLot lot = inventoryTraceEvent.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getId();
    }

    private String entityLotLotNumber1(InventoryTraceEvent inventoryTraceEvent) {
        InventoryLot lot = inventoryTraceEvent.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getLotNumber();
    }

    private Long entitySerialId(InventoryTraceEvent inventoryTraceEvent) {
        InventorySerial serial = inventoryTraceEvent.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getId();
    }

    private String entitySerialSerialNumber(InventoryTraceEvent inventoryTraceEvent) {
        InventorySerial serial = inventoryTraceEvent.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getSerialNumber();
    }

    private Long entityWarehouseId1(InventoryTraceEvent inventoryTraceEvent) {
        Warehouse warehouse = inventoryTraceEvent.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private Long entityStoreId1(InventoryTraceEvent inventoryTraceEvent) {
        Store store = inventoryTraceEvent.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private Long entityCreatedById(InventoryTraceEvent inventoryTraceEvent) {
        User createdBy = inventoryTraceEvent.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private Long entityCompanyId3(InventoryRecall inventoryRecall) {
        Company company = inventoryRecall.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityProductId3(InventoryRecall inventoryRecall) {
        Product product = inventoryRecall.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityLotId2(InventoryRecall inventoryRecall) {
        InventoryLot lot = inventoryRecall.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getId();
    }

    private String entityLotLotNumber2(InventoryRecall inventoryRecall) {
        InventoryLot lot = inventoryRecall.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getLotNumber();
    }

    private Long entitySerialId1(InventoryRecall inventoryRecall) {
        InventorySerial serial = inventoryRecall.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getId();
    }

    private String entitySerialSerialNumber1(InventoryRecall inventoryRecall) {
        InventorySerial serial = inventoryRecall.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getSerialNumber();
    }

    private Long entityRecalledById(InventoryRecall inventoryRecall) {
        User recalledBy = inventoryRecall.getRecalledBy();
        if ( recalledBy == null ) {
            return null;
        }
        return recalledBy.getId();
    }
}
