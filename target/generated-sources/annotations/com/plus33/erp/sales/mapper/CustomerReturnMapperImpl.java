package com.plus33.erp.sales.mapper;

import com.plus33.erp.inventory.entity.InventoryLot;
import com.plus33.erp.inventory.entity.InventorySerial;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.sales.dto.CustomerReturnItemResponse;
import com.plus33.erp.sales.dto.CustomerReturnResponse;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerInvoiceItem;
import com.plus33.erp.sales.entity.CustomerReturn;
import com.plus33.erp.sales.entity.CustomerReturnItem;
import com.plus33.erp.sales.entity.CustomerReturnStatus;
import com.plus33.erp.sales.entity.InspectionResult;
import com.plus33.erp.sales.entity.ReturnReason;
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
    date = "2026-07-13T18:32:46+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class CustomerReturnMapperImpl implements CustomerReturnMapper {

    @Override
    public CustomerReturnResponse toResponse(CustomerReturn customerReturn) {
        if ( customerReturn == null ) {
            return null;
        }

        Long companyId = null;
        Long customerId = null;
        String customerName = null;
        String customerCode = null;
        Long salesOrderId = null;
        String salesOrderNumber = null;
        Long customerInvoiceId = null;
        String customerInvoiceNumber = null;
        Long warehouseId = null;
        String warehouseName = null;
        Long storeId = null;
        String storeName = null;
        Long createdById = null;
        String createdByName = null;
        Long approvedById = null;
        String approvedByName = null;
        Long receivedById = null;
        String receivedByName = null;
        Long inspectedById = null;
        String inspectedByName = null;
        Long closedById = null;
        String closedByName = null;
        Long cancelledById = null;
        String cancelledByName = null;
        Long id = null;
        String returnNumber = null;
        UUID clientReferenceId = null;
        CustomerReturnStatus status = null;
        ReturnReason reason = null;
        String remarks = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime receivedAt = null;
        LocalDateTime inspectedAt = null;
        LocalDateTime closedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<CustomerReturnItemResponse> items = null;
        Long version = null;

        companyId = customerReturnCompanyId( customerReturn );
        customerId = customerReturnCustomerId( customerReturn );
        customerName = customerReturnCustomerName( customerReturn );
        customerCode = customerReturnCustomerCode( customerReturn );
        salesOrderId = customerReturnSalesOrderId( customerReturn );
        salesOrderNumber = customerReturnSalesOrderOrderNumber( customerReturn );
        customerInvoiceId = customerReturnCustomerInvoiceId( customerReturn );
        customerInvoiceNumber = customerReturnCustomerInvoiceInvoiceNumber( customerReturn );
        warehouseId = customerReturnWarehouseId( customerReturn );
        warehouseName = customerReturnWarehouseName( customerReturn );
        storeId = customerReturnStoreId( customerReturn );
        storeName = customerReturnStoreName( customerReturn );
        createdById = customerReturnCreatedById( customerReturn );
        createdByName = customerReturnCreatedByFirstName( customerReturn );
        approvedById = customerReturnApprovedById( customerReturn );
        approvedByName = customerReturnApprovedByFirstName( customerReturn );
        receivedById = customerReturnReceivedById( customerReturn );
        receivedByName = customerReturnReceivedByFirstName( customerReturn );
        inspectedById = customerReturnInspectedById( customerReturn );
        inspectedByName = customerReturnInspectedByFirstName( customerReturn );
        closedById = customerReturnClosedById( customerReturn );
        closedByName = customerReturnClosedByFirstName( customerReturn );
        cancelledById = customerReturnCancelledById( customerReturn );
        cancelledByName = customerReturnCancelledByFirstName( customerReturn );
        id = customerReturn.getId();
        returnNumber = customerReturn.getReturnNumber();
        clientReferenceId = customerReturn.getClientReferenceId();
        status = customerReturn.getStatus();
        reason = customerReturn.getReason();
        remarks = customerReturn.getRemarks();
        createdAt = customerReturn.getCreatedAt();
        updatedAt = customerReturn.getUpdatedAt();
        approvedAt = customerReturn.getApprovedAt();
        receivedAt = customerReturn.getReceivedAt();
        inspectedAt = customerReturn.getInspectedAt();
        closedAt = customerReturn.getClosedAt();
        cancelledAt = customerReturn.getCancelledAt();
        cancellationReason = customerReturn.getCancellationReason();
        items = customerReturnItemListToCustomerReturnItemResponseList( customerReturn.getItems() );
        version = customerReturn.getVersion();

        CustomerReturnResponse customerReturnResponse = new CustomerReturnResponse( id, companyId, customerId, customerName, customerCode, salesOrderId, salesOrderNumber, customerInvoiceId, customerInvoiceNumber, warehouseId, warehouseName, storeId, storeName, returnNumber, clientReferenceId, status, reason, remarks, createdById, createdByName, approvedById, approvedByName, receivedById, receivedByName, inspectedById, inspectedByName, closedById, closedByName, cancelledById, cancelledByName, createdAt, updatedAt, approvedAt, receivedAt, inspectedAt, closedAt, cancelledAt, cancellationReason, items, version );

        return customerReturnResponse;
    }

    @Override
    public CustomerReturnItemResponse toItemResponse(CustomerReturnItem item) {
        if ( item == null ) {
            return null;
        }

        Long salesOrderItemId = null;
        Long customerInvoiceItemId = null;
        Long productId = null;
        String productName = null;
        String productCode = null;
        Long lotId = null;
        String lotNumber = null;
        Long serialId = null;
        String serialNumber = null;
        Long id = null;
        BigDecimal quantity = null;
        InspectionResult inspectionResult = null;
        String inspectionNotes = null;

        salesOrderItemId = itemSalesOrderItemId( item );
        customerInvoiceItemId = itemCustomerInvoiceItemId( item );
        productId = itemProductId( item );
        productName = itemProductName( item );
        productCode = itemProductCode( item );
        lotId = itemLotId( item );
        lotNumber = itemLotLotNumber( item );
        serialId = itemSerialId( item );
        serialNumber = itemSerialSerialNumber( item );
        id = item.getId();
        quantity = item.getQuantity();
        inspectionResult = item.getInspectionResult();
        inspectionNotes = item.getInspectionNotes();

        CustomerReturnItemResponse customerReturnItemResponse = new CustomerReturnItemResponse( id, salesOrderItemId, customerInvoiceItemId, productId, productName, productCode, quantity, inspectionResult, inspectionNotes, lotId, lotNumber, serialId, serialNumber );

        return customerReturnItemResponse;
    }

    private Long customerReturnCompanyId(CustomerReturn customerReturn) {
        Company company = customerReturn.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long customerReturnCustomerId(CustomerReturn customerReturn) {
        Customer customer = customerReturn.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String customerReturnCustomerName(CustomerReturn customerReturn) {
        Customer customer = customerReturn.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    private String customerReturnCustomerCode(CustomerReturn customerReturn) {
        Customer customer = customerReturn.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getCode();
    }

    private Long customerReturnSalesOrderId(CustomerReturn customerReturn) {
        SalesOrder salesOrder = customerReturn.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getId();
    }

    private String customerReturnSalesOrderOrderNumber(CustomerReturn customerReturn) {
        SalesOrder salesOrder = customerReturn.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getOrderNumber();
    }

    private Long customerReturnCustomerInvoiceId(CustomerReturn customerReturn) {
        CustomerInvoice customerInvoice = customerReturn.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getId();
    }

    private String customerReturnCustomerInvoiceInvoiceNumber(CustomerReturn customerReturn) {
        CustomerInvoice customerInvoice = customerReturn.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getInvoiceNumber();
    }

    private Long customerReturnWarehouseId(CustomerReturn customerReturn) {
        Warehouse warehouse = customerReturn.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String customerReturnWarehouseName(CustomerReturn customerReturn) {
        Warehouse warehouse = customerReturn.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getName();
    }

    private Long customerReturnStoreId(CustomerReturn customerReturn) {
        Store store = customerReturn.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getId();
    }

    private String customerReturnStoreName(CustomerReturn customerReturn) {
        Store store = customerReturn.getStore();
        if ( store == null ) {
            return null;
        }
        return store.getName();
    }

    private Long customerReturnCreatedById(CustomerReturn customerReturn) {
        User createdBy = customerReturn.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String customerReturnCreatedByFirstName(CustomerReturn customerReturn) {
        User createdBy = customerReturn.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getFirstName();
    }

    private Long customerReturnApprovedById(CustomerReturn customerReturn) {
        User approvedBy = customerReturn.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private String customerReturnApprovedByFirstName(CustomerReturn customerReturn) {
        User approvedBy = customerReturn.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getFirstName();
    }

    private Long customerReturnReceivedById(CustomerReturn customerReturn) {
        User receivedBy = customerReturn.getReceivedBy();
        if ( receivedBy == null ) {
            return null;
        }
        return receivedBy.getId();
    }

    private String customerReturnReceivedByFirstName(CustomerReturn customerReturn) {
        User receivedBy = customerReturn.getReceivedBy();
        if ( receivedBy == null ) {
            return null;
        }
        return receivedBy.getFirstName();
    }

    private Long customerReturnInspectedById(CustomerReturn customerReturn) {
        User inspectedBy = customerReturn.getInspectedBy();
        if ( inspectedBy == null ) {
            return null;
        }
        return inspectedBy.getId();
    }

    private String customerReturnInspectedByFirstName(CustomerReturn customerReturn) {
        User inspectedBy = customerReturn.getInspectedBy();
        if ( inspectedBy == null ) {
            return null;
        }
        return inspectedBy.getFirstName();
    }

    private Long customerReturnClosedById(CustomerReturn customerReturn) {
        User closedBy = customerReturn.getClosedBy();
        if ( closedBy == null ) {
            return null;
        }
        return closedBy.getId();
    }

    private String customerReturnClosedByFirstName(CustomerReturn customerReturn) {
        User closedBy = customerReturn.getClosedBy();
        if ( closedBy == null ) {
            return null;
        }
        return closedBy.getFirstName();
    }

    private Long customerReturnCancelledById(CustomerReturn customerReturn) {
        User cancelledBy = customerReturn.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String customerReturnCancelledByFirstName(CustomerReturn customerReturn) {
        User cancelledBy = customerReturn.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    protected List<CustomerReturnItemResponse> customerReturnItemListToCustomerReturnItemResponseList(List<CustomerReturnItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CustomerReturnItemResponse> list1 = new ArrayList<CustomerReturnItemResponse>( list.size() );
        for ( CustomerReturnItem customerReturnItem : list ) {
            list1.add( toItemResponse( customerReturnItem ) );
        }

        return list1;
    }

    private Long itemSalesOrderItemId(CustomerReturnItem customerReturnItem) {
        SalesOrderItem salesOrderItem = customerReturnItem.getSalesOrderItem();
        if ( salesOrderItem == null ) {
            return null;
        }
        return salesOrderItem.getId();
    }

    private Long itemCustomerInvoiceItemId(CustomerReturnItem customerReturnItem) {
        CustomerInvoiceItem customerInvoiceItem = customerReturnItem.getCustomerInvoiceItem();
        if ( customerInvoiceItem == null ) {
            return null;
        }
        return customerInvoiceItem.getId();
    }

    private Long itemProductId(CustomerReturnItem customerReturnItem) {
        Product product = customerReturnItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String itemProductName(CustomerReturnItem customerReturnItem) {
        Product product = customerReturnItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String itemProductCode(CustomerReturnItem customerReturnItem) {
        Product product = customerReturnItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private Long itemLotId(CustomerReturnItem customerReturnItem) {
        InventoryLot lot = customerReturnItem.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getId();
    }

    private String itemLotLotNumber(CustomerReturnItem customerReturnItem) {
        InventoryLot lot = customerReturnItem.getLot();
        if ( lot == null ) {
            return null;
        }
        return lot.getLotNumber();
    }

    private Long itemSerialId(CustomerReturnItem customerReturnItem) {
        InventorySerial serial = customerReturnItem.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getId();
    }

    private String itemSerialSerialNumber(CustomerReturnItem customerReturnItem) {
        InventorySerial serial = customerReturnItem.getSerial();
        if ( serial == null ) {
            return null;
        }
        return serial.getSerialNumber();
    }
}
