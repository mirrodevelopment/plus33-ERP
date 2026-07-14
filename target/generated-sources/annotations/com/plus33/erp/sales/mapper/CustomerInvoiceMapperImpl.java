package com.plus33.erp.sales.mapper;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.sales.dto.CustomerInvoiceItemResponse;
import com.plus33.erp.sales.dto.CustomerInvoiceResponse;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerInvoiceItem;
import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import com.plus33.erp.sales.entity.SalesOrder;
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
    date = "2026-07-14T10:25:49+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class CustomerInvoiceMapperImpl implements CustomerInvoiceMapper {

    @Override
    public CustomerInvoiceResponse toResponse(CustomerInvoice invoice) {
        if ( invoice == null ) {
            return null;
        }

        Long companyId = null;
        Long customerId = null;
        String customerName = null;
        String customerCode = null;
        Long salesOrderId = null;
        String salesOrderNumber = null;
        Long journalEntryId = null;
        String journalEntryNumber = null;
        Long createdById = null;
        String createdByName = null;
        Long submittedById = null;
        String submittedByName = null;
        Long approvedById = null;
        String approvedByName = null;
        Long cancelledById = null;
        String cancelledByName = null;
        Long id = null;
        String invoiceNumber = null;
        UUID clientReferenceId = null;
        LocalDate invoiceDate = null;
        LocalDate dueDate = null;
        BigDecimal subtotalAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal totalAmount = null;
        BigDecimal paidAmount = null;
        BigDecimal outstandingBalance = null;
        CustomerInvoiceStatus status = null;
        String currencyCode = null;
        LocalDateTime createdAt = null;
        LocalDateTime submittedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<CustomerInvoiceItemResponse> items = null;
        Long version = null;

        companyId = invoiceCompanyId( invoice );
        customerId = invoiceCustomerId( invoice );
        customerName = invoiceCustomerName( invoice );
        customerCode = invoiceCustomerCode( invoice );
        salesOrderId = invoiceSalesOrderId( invoice );
        salesOrderNumber = invoiceSalesOrderOrderNumber( invoice );
        journalEntryId = invoiceJournalEntryId( invoice );
        journalEntryNumber = invoiceJournalEntryEntryNumber( invoice );
        createdById = invoiceCreatedById( invoice );
        createdByName = invoiceCreatedByFirstName( invoice );
        submittedById = invoiceSubmittedById( invoice );
        submittedByName = invoiceSubmittedByFirstName( invoice );
        approvedById = invoiceApprovedById( invoice );
        approvedByName = invoiceApprovedByFirstName( invoice );
        cancelledById = invoiceCancelledById( invoice );
        cancelledByName = invoiceCancelledByFirstName( invoice );
        id = invoice.getId();
        invoiceNumber = invoice.getInvoiceNumber();
        clientReferenceId = invoice.getClientReferenceId();
        invoiceDate = invoice.getInvoiceDate();
        dueDate = invoice.getDueDate();
        subtotalAmount = invoice.getSubtotalAmount();
        taxAmount = invoice.getTaxAmount();
        discountAmount = invoice.getDiscountAmount();
        totalAmount = invoice.getTotalAmount();
        paidAmount = invoice.getPaidAmount();
        outstandingBalance = invoice.getOutstandingBalance();
        status = invoice.getStatus();
        currencyCode = invoice.getCurrencyCode();
        createdAt = invoice.getCreatedAt();
        submittedAt = invoice.getSubmittedAt();
        approvedAt = invoice.getApprovedAt();
        cancelledAt = invoice.getCancelledAt();
        cancellationReason = invoice.getCancellationReason();
        items = customerInvoiceItemListToCustomerInvoiceItemResponseList( invoice.getItems() );
        version = invoice.getVersion();

        CustomerInvoiceResponse customerInvoiceResponse = new CustomerInvoiceResponse( id, companyId, customerId, customerName, customerCode, salesOrderId, salesOrderNumber, invoiceNumber, clientReferenceId, invoiceDate, dueDate, subtotalAmount, taxAmount, discountAmount, totalAmount, paidAmount, outstandingBalance, status, currencyCode, journalEntryId, journalEntryNumber, createdById, createdByName, submittedById, submittedByName, approvedById, approvedByName, cancelledById, cancelledByName, createdAt, submittedAt, approvedAt, cancelledAt, cancellationReason, items, version );

        return customerInvoiceResponse;
    }

    @Override
    public CustomerInvoiceItemResponse toItemResponse(CustomerInvoiceItem item) {
        if ( item == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        String productSku = null;
        Long id = null;
        BigDecimal quantity = null;
        BigDecimal unitPrice = null;
        BigDecimal discountPercentage = null;
        BigDecimal taxPercentage = null;
        BigDecimal netAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal totalAmount = null;
        Long version = null;

        productId = itemProductId( item );
        productName = itemProductName( item );
        productSku = itemProductCode( item );
        id = item.getId();
        quantity = item.getQuantity();
        unitPrice = item.getUnitPrice();
        discountPercentage = item.getDiscountPercentage();
        taxPercentage = item.getTaxPercentage();
        netAmount = item.getNetAmount();
        taxAmount = item.getTaxAmount();
        discountAmount = item.getDiscountAmount();
        totalAmount = item.getTotalAmount();
        version = item.getVersion();

        Long salesOrderItemId = null;
        Long pickListItemId = null;

        CustomerInvoiceItemResponse customerInvoiceItemResponse = new CustomerInvoiceItemResponse( id, salesOrderItemId, pickListItemId, productId, productName, productSku, quantity, unitPrice, discountPercentage, taxPercentage, netAmount, taxAmount, discountAmount, totalAmount, version );

        return customerInvoiceItemResponse;
    }

    private Long invoiceCompanyId(CustomerInvoice customerInvoice) {
        Company company = customerInvoice.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long invoiceCustomerId(CustomerInvoice customerInvoice) {
        Customer customer = customerInvoice.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String invoiceCustomerName(CustomerInvoice customerInvoice) {
        Customer customer = customerInvoice.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    private String invoiceCustomerCode(CustomerInvoice customerInvoice) {
        Customer customer = customerInvoice.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getCode();
    }

    private Long invoiceSalesOrderId(CustomerInvoice customerInvoice) {
        SalesOrder salesOrder = customerInvoice.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getId();
    }

    private String invoiceSalesOrderOrderNumber(CustomerInvoice customerInvoice) {
        SalesOrder salesOrder = customerInvoice.getSalesOrder();
        if ( salesOrder == null ) {
            return null;
        }
        return salesOrder.getOrderNumber();
    }

    private Long invoiceJournalEntryId(CustomerInvoice customerInvoice) {
        JournalEntry journalEntry = customerInvoice.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getId();
    }

    private String invoiceJournalEntryEntryNumber(CustomerInvoice customerInvoice) {
        JournalEntry journalEntry = customerInvoice.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getEntryNumber();
    }

    private Long invoiceCreatedById(CustomerInvoice customerInvoice) {
        User createdBy = customerInvoice.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String invoiceCreatedByFirstName(CustomerInvoice customerInvoice) {
        User createdBy = customerInvoice.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getFirstName();
    }

    private Long invoiceSubmittedById(CustomerInvoice customerInvoice) {
        User submittedBy = customerInvoice.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getId();
    }

    private String invoiceSubmittedByFirstName(CustomerInvoice customerInvoice) {
        User submittedBy = customerInvoice.getSubmittedBy();
        if ( submittedBy == null ) {
            return null;
        }
        return submittedBy.getFirstName();
    }

    private Long invoiceApprovedById(CustomerInvoice customerInvoice) {
        User approvedBy = customerInvoice.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private String invoiceApprovedByFirstName(CustomerInvoice customerInvoice) {
        User approvedBy = customerInvoice.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getFirstName();
    }

    private Long invoiceCancelledById(CustomerInvoice customerInvoice) {
        User cancelledBy = customerInvoice.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String invoiceCancelledByFirstName(CustomerInvoice customerInvoice) {
        User cancelledBy = customerInvoice.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    protected List<CustomerInvoiceItemResponse> customerInvoiceItemListToCustomerInvoiceItemResponseList(List<CustomerInvoiceItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CustomerInvoiceItemResponse> list1 = new ArrayList<CustomerInvoiceItemResponse>( list.size() );
        for ( CustomerInvoiceItem customerInvoiceItem : list ) {
            list1.add( toItemResponse( customerInvoiceItem ) );
        }

        return list1;
    }

    private Long itemProductId(CustomerInvoiceItem customerInvoiceItem) {
        Product product = customerInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String itemProductName(CustomerInvoiceItem customerInvoiceItem) {
        Product product = customerInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String itemProductCode(CustomerInvoiceItem customerInvoiceItem) {
        Product product = customerInvoiceItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }
}
