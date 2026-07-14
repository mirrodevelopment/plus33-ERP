package com.plus33.erp.sales.mapper;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.sales.dto.SalesOrderItemResponse;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.SalesOrder;
import com.plus33.erp.sales.entity.SalesOrderItem;
import com.plus33.erp.sales.entity.SalesOrderStatus;
import com.plus33.erp.security.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:47+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class SalesOrderMapperImpl implements SalesOrderMapper {

    @Override
    public SalesOrderResponse toResponse(SalesOrder entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        Long customerId = null;
        Long orderedByUserId = null;
        String orderedByUserName = null;
        Long submittedByUserId = null;
        String submittedByUserName = null;
        Long approvedByUserId = null;
        String approvedByUserName = null;
        Long cancelledByUserId = null;
        String cancelledByUserName = null;
        Long id = null;
        String orderNumber = null;
        UUID clientReferenceId = null;
        LocalDate orderDate = null;
        LocalDate requestedDeliveryDate = null;
        String currencyCode = null;
        Integer paymentTermsDays = null;
        String billingAddress = null;
        String shippingAddress = null;
        SalesOrderStatus status = null;
        String customerName = null;
        String customerCode = null;
        String customerType = null;
        String pricingTier = null;
        BigDecimal discountRate = null;
        String taxProfile = null;
        BigDecimal subtotal = null;
        BigDecimal discountAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal totalAmount = null;
        BigDecimal outstandingAmount = null;
        Boolean creditOverride = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Long version = null;
        List<SalesOrderItemResponse> items = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        customerId = entityCustomerId( entity );
        orderedByUserId = entityOrderedById( entity );
        orderedByUserName = entityOrderedByFirstName( entity );
        submittedByUserId = entitySubmittedById( entity );
        submittedByUserName = entitySubmittedByFirstName( entity );
        approvedByUserId = entityApprovedById( entity );
        approvedByUserName = entityApprovedByFirstName( entity );
        cancelledByUserId = entityCancelledById( entity );
        cancelledByUserName = entityCancelledByFirstName( entity );
        id = entity.getId();
        orderNumber = entity.getOrderNumber();
        clientReferenceId = entity.getClientReferenceId();
        orderDate = entity.getOrderDate();
        requestedDeliveryDate = entity.getRequestedDeliveryDate();
        currencyCode = entity.getCurrencyCode();
        paymentTermsDays = entity.getPaymentTermsDays();
        billingAddress = entity.getBillingAddress();
        shippingAddress = entity.getShippingAddress();
        status = entity.getStatus();
        customerName = entity.getCustomerName();
        customerCode = entity.getCustomerCode();
        customerType = entity.getCustomerType();
        pricingTier = entity.getPricingTier();
        discountRate = entity.getDiscountRate();
        taxProfile = entity.getTaxProfile();
        subtotal = entity.getSubtotal();
        discountAmount = entity.getDiscountAmount();
        taxAmount = entity.getTaxAmount();
        totalAmount = entity.getTotalAmount();
        outstandingAmount = entity.getOutstandingAmount();
        creditOverride = entity.getCreditOverride();
        submittedAt = entity.getSubmittedAt();
        approvedAt = entity.getApprovedAt();
        cancelledAt = entity.getCancelledAt();
        cancellationReason = entity.getCancellationReason();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        version = entity.getVersion();
        items = toItemResponseList( entity.getItems() );

        SalesOrderResponse salesOrderResponse = new SalesOrderResponse( id, companyId, companyName, customerId, orderNumber, clientReferenceId, orderDate, requestedDeliveryDate, currencyCode, paymentTermsDays, billingAddress, shippingAddress, status, customerName, customerCode, customerType, pricingTier, discountRate, taxProfile, subtotal, discountAmount, taxAmount, totalAmount, outstandingAmount, creditOverride, orderedByUserId, orderedByUserName, submittedByUserId, submittedByUserName, approvedByUserId, approvedByUserName, cancelledByUserId, cancelledByUserName, submittedAt, approvedAt, cancelledAt, cancellationReason, createdAt, updatedAt, version, items );

        return salesOrderResponse;
    }

    @Override
    public SalesOrderItemResponse toItemResponse(SalesOrderItem entity) {
        if ( entity == null ) {
            return null;
        }

        Long productId = null;
        String productCode = null;
        String productName = null;
        Long id = null;
        BigDecimal orderedQuantity = null;
        BigDecimal unitPrice = null;
        BigDecimal discountPercentage = null;
        BigDecimal taxPercentage = null;
        BigDecimal lineTotal = null;
        Long version = null;

        productId = entityProductId( entity );
        productCode = entityProductCode( entity );
        productName = entityProductName( entity );
        id = entity.getId();
        orderedQuantity = entity.getOrderedQuantity();
        unitPrice = entity.getUnitPrice();
        discountPercentage = entity.getDiscountPercentage();
        taxPercentage = entity.getTaxPercentage();
        lineTotal = entity.getLineTotal();
        version = entity.getVersion();

        SalesOrderItemResponse salesOrderItemResponse = new SalesOrderItemResponse( id, productId, productCode, productName, orderedQuantity, unitPrice, discountPercentage, taxPercentage, lineTotal, version );

        return salesOrderItemResponse;
    }

    @Override
    public List<SalesOrderItemResponse> toItemResponseList(List<SalesOrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<SalesOrderItemResponse> list1 = new ArrayList<SalesOrderItemResponse>( list.size() );
        for ( SalesOrderItem salesOrderItem : list ) {
            list1.add( toItemResponse( salesOrderItem ) );
        }

        return list1;
    }

    private Long entityCompanyId(SalesOrder salesOrder) {
        Company company = salesOrder.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(SalesOrder salesOrder) {
        Company company = salesOrder.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private Long entityCustomerId(SalesOrder salesOrder) {
        Customer customer = salesOrder.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private Long entityOrderedById(SalesOrder salesOrder) {
        User orderedBy = salesOrder.getOrderedBy();
        if ( orderedBy == null ) {
            return null;
        }
        return orderedBy.getId();
    }

    private String entityOrderedByFirstName(SalesOrder salesOrder) {
        User orderedBy = salesOrder.getOrderedBy();
        if ( orderedBy == null ) {
            return null;
        }
        return orderedBy.getFirstName();
    }

    private Long entitySubmittedById(SalesOrder salesOrder) {
        User submittedBy = salesOrder.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private String entitySubmittedByFirstName(SalesOrder salesOrder) {
        User submittedBy = salesOrder.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getFirstName();
    }

    private Long entityApprovedById(SalesOrder salesOrder) {
        User approvedBy = salesOrder.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private String entityApprovedByFirstName(SalesOrder salesOrder) {
        User approvedBy = salesOrder.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getFirstName();
    }

    private Long entityCancelledById(SalesOrder salesOrder) {
        User cancelledBy = salesOrder.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String entityCancelledByFirstName(SalesOrder salesOrder) {
        User cancelledBy = salesOrder.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    private Long entityProductId(SalesOrderItem salesOrderItem) {
        Product product = salesOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String entityProductCode(SalesOrderItem salesOrderItem) {
        Product product = salesOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }

    private String entityProductName(SalesOrderItem salesOrderItem) {
        Product product = salesOrderItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
