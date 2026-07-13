package com.plus33.erp.sales.mapper;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.sales.dto.CreditNoteItemResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import com.plus33.erp.sales.entity.CreditNote;
import com.plus33.erp.sales.entity.CreditNoteItem;
import com.plus33.erp.sales.entity.CreditNoteStatus;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.sales.entity.CustomerReturn;
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
public class CreditNoteMapperImpl implements CreditNoteMapper {

    @Override
    public CreditNoteResponse toResponse(CreditNote creditNote) {
        if ( creditNote == null ) {
            return null;
        }

        Long companyId = null;
        Long customerId = null;
        String customerName = null;
        String customerCode = null;
        Long customerReturnId = null;
        String customerReturnNumber = null;
        Long customerInvoiceId = null;
        String customerInvoiceNumber = null;
        Long journalEntryId = null;
        String journalEntryNumber = null;
        Long createdById = null;
        String createdByName = null;
        Long approvedById = null;
        String approvedByName = null;
        Long cancelledById = null;
        String cancelledByName = null;
        Long id = null;
        String creditNoteNumber = null;
        UUID clientReferenceId = null;
        CreditNoteStatus status = null;
        BigDecimal subtotalAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal totalAmount = null;
        String remarks = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        LocalDateTime approvedAt = null;
        LocalDateTime cancelledAt = null;
        String cancellationReason = null;
        List<CreditNoteItemResponse> items = null;
        Long version = null;

        companyId = creditNoteCompanyId( creditNote );
        customerId = creditNoteCustomerId( creditNote );
        customerName = creditNoteCustomerName( creditNote );
        customerCode = creditNoteCustomerCode( creditNote );
        customerReturnId = creditNoteCustomerReturnId( creditNote );
        customerReturnNumber = creditNoteCustomerReturnReturnNumber( creditNote );
        customerInvoiceId = creditNoteCustomerInvoiceId( creditNote );
        customerInvoiceNumber = creditNoteCustomerInvoiceInvoiceNumber( creditNote );
        journalEntryId = creditNoteJournalEntryId( creditNote );
        journalEntryNumber = creditNoteJournalEntryEntryNumber( creditNote );
        createdById = creditNoteCreatedById( creditNote );
        createdByName = creditNoteCreatedByFirstName( creditNote );
        approvedById = creditNoteApprovedById( creditNote );
        approvedByName = creditNoteApprovedByFirstName( creditNote );
        cancelledById = creditNoteCancelledById( creditNote );
        cancelledByName = creditNoteCancelledByFirstName( creditNote );
        id = creditNote.getId();
        creditNoteNumber = creditNote.getCreditNoteNumber();
        clientReferenceId = creditNote.getClientReferenceId();
        status = creditNote.getStatus();
        subtotalAmount = creditNote.getSubtotalAmount();
        taxAmount = creditNote.getTaxAmount();
        discountAmount = creditNote.getDiscountAmount();
        totalAmount = creditNote.getTotalAmount();
        remarks = creditNote.getRemarks();
        createdAt = creditNote.getCreatedAt();
        updatedAt = creditNote.getUpdatedAt();
        approvedAt = creditNote.getApprovedAt();
        cancelledAt = creditNote.getCancelledAt();
        cancellationReason = creditNote.getCancellationReason();
        items = creditNoteItemListToCreditNoteItemResponseList( creditNote.getItems() );
        version = creditNote.getVersion();

        CreditNoteResponse creditNoteResponse = new CreditNoteResponse( id, companyId, customerId, customerName, customerCode, customerReturnId, customerReturnNumber, customerInvoiceId, customerInvoiceNumber, creditNoteNumber, clientReferenceId, status, subtotalAmount, taxAmount, discountAmount, totalAmount, journalEntryId, journalEntryNumber, remarks, createdById, createdByName, approvedById, approvedByName, cancelledById, cancelledByName, createdAt, updatedAt, approvedAt, cancelledAt, cancellationReason, items, version );

        return creditNoteResponse;
    }

    @Override
    public CreditNoteItemResponse toItemResponse(CreditNoteItem item) {
        if ( item == null ) {
            return null;
        }

        Long productId = null;
        String productName = null;
        String productCode = null;
        Long id = null;
        BigDecimal quantity = null;
        BigDecimal unitPrice = null;
        BigDecimal discountPercentage = null;
        BigDecimal taxPercentage = null;
        BigDecimal netAmount = null;
        BigDecimal taxAmount = null;
        BigDecimal discountAmount = null;
        BigDecimal totalAmount = null;

        productId = itemProductId( item );
        productName = itemProductName( item );
        productCode = itemProductCode( item );
        id = item.getId();
        quantity = item.getQuantity();
        unitPrice = item.getUnitPrice();
        discountPercentage = item.getDiscountPercentage();
        taxPercentage = item.getTaxPercentage();
        netAmount = item.getNetAmount();
        taxAmount = item.getTaxAmount();
        discountAmount = item.getDiscountAmount();
        totalAmount = item.getTotalAmount();

        CreditNoteItemResponse creditNoteItemResponse = new CreditNoteItemResponse( id, productId, productName, productCode, quantity, unitPrice, discountPercentage, taxPercentage, netAmount, taxAmount, discountAmount, totalAmount );

        return creditNoteItemResponse;
    }

    private Long creditNoteCompanyId(CreditNote creditNote) {
        Company company = creditNote.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long creditNoteCustomerId(CreditNote creditNote) {
        Customer customer = creditNote.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String creditNoteCustomerName(CreditNote creditNote) {
        Customer customer = creditNote.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    private String creditNoteCustomerCode(CreditNote creditNote) {
        Customer customer = creditNote.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getCode();
    }

    private Long creditNoteCustomerReturnId(CreditNote creditNote) {
        CustomerReturn customerReturn = creditNote.getCustomerReturn();
        if ( customerReturn == null ) {
            return null;
        }
        return customerReturn.getId();
    }

    private String creditNoteCustomerReturnReturnNumber(CreditNote creditNote) {
        CustomerReturn customerReturn = creditNote.getCustomerReturn();
        if ( customerReturn == null ) {
            return null;
        }
        return customerReturn.getReturnNumber();
    }

    private Long creditNoteCustomerInvoiceId(CreditNote creditNote) {
        CustomerInvoice customerInvoice = creditNote.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getId();
    }

    private String creditNoteCustomerInvoiceInvoiceNumber(CreditNote creditNote) {
        CustomerInvoice customerInvoice = creditNote.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getInvoiceNumber();
    }

    private Long creditNoteJournalEntryId(CreditNote creditNote) {
        JournalEntry journalEntry = creditNote.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getId();
    }

    private String creditNoteJournalEntryEntryNumber(CreditNote creditNote) {
        JournalEntry journalEntry = creditNote.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getEntryNumber();
    }

    private Long creditNoteCreatedById(CreditNote creditNote) {
        User createdBy = creditNote.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String creditNoteCreatedByFirstName(CreditNote creditNote) {
        User createdBy = creditNote.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getFirstName();
    }

    private Long creditNoteApprovedById(CreditNote creditNote) {
        User approvedBy = creditNote.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getId();
    }

    private String creditNoteApprovedByFirstName(CreditNote creditNote) {
        User approvedBy = creditNote.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getFirstName();
    }

    private Long creditNoteCancelledById(CreditNote creditNote) {
        User cancelledBy = creditNote.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String creditNoteCancelledByFirstName(CreditNote creditNote) {
        User cancelledBy = creditNote.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    protected List<CreditNoteItemResponse> creditNoteItemListToCreditNoteItemResponseList(List<CreditNoteItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CreditNoteItemResponse> list1 = new ArrayList<CreditNoteItemResponse>( list.size() );
        for ( CreditNoteItem creditNoteItem : list ) {
            list1.add( toItemResponse( creditNoteItem ) );
        }

        return list1;
    }

    private Long itemProductId(CreditNoteItem creditNoteItem) {
        Product product = creditNoteItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String itemProductName(CreditNoteItem creditNoteItem) {
        Product product = creditNoteItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private String itemProductCode(CreditNoteItem creditNoteItem) {
        Product product = creditNoteItem.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getCode();
    }
}
