package com.plus33.erp.finance.mapper;

import com.plus33.erp.finance.dto.PaymentAllocationRequest;
import com.plus33.erp.finance.dto.PaymentAllocationResponse;
import com.plus33.erp.finance.dto.PaymentRequest;
import com.plus33.erp.finance.dto.PaymentResponse;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.PaymentAllocation;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerInvoice;
import com.plus33.erp.security.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public Payment toEntity(PaymentRequest request) {
        if ( request == null ) {
            return null;
        }

        Payment.PaymentBuilder payment = Payment.builder();

        payment.paymentDate( request.getPaymentDate() );
        payment.paymentMethod( request.getPaymentMethod() );
        payment.amount( request.getAmount() );
        payment.referenceNumber( request.getReferenceNumber() );
        payment.currencyCode( request.getCurrencyCode() );

        return payment.build();
    }

    @Override
    public PaymentResponse toResponse(Payment entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentResponse.PaymentResponseBuilder paymentResponse = PaymentResponse.builder();

        paymentResponse.companyId( entityCompanyId( entity ) );
        paymentResponse.companyName( entityCompanyName( entity ) );
        paymentResponse.supplierId( entitySupplierId( entity ) );
        paymentResponse.supplierName( entitySupplierName( entity ) );
        paymentResponse.customerId( entityCustomerId( entity ) );
        paymentResponse.customerName( entityCustomerName( entity ) );
        paymentResponse.journalEntryId( entityJournalEntryId( entity ) );
        paymentResponse.journalEntryNumber( entityJournalEntryEntryNumber( entity ) );
        paymentResponse.cancelledByUserId( entityCancelledById( entity ) );
        paymentResponse.cancelledByUserName( entityCancelledByFirstName( entity ) );
        paymentResponse.createdByUserId( entityCreatedById( entity ) );
        paymentResponse.createdByUserName( entityCreatedByFirstName( entity ) );
        paymentResponse.id( entity.getId() );
        paymentResponse.paymentNumber( entity.getPaymentNumber() );
        paymentResponse.paymentDate( entity.getPaymentDate() );
        paymentResponse.paymentMethod( entity.getPaymentMethod() );
        paymentResponse.paymentType( entity.getPaymentType() );
        paymentResponse.amount( entity.getAmount() );
        paymentResponse.referenceNumber( entity.getReferenceNumber() );
        paymentResponse.currencyCode( entity.getCurrencyCode() );
        paymentResponse.status( entity.getStatus() );
        paymentResponse.cancelledAt( entity.getCancelledAt() );
        paymentResponse.cancellationReason( entity.getCancellationReason() );
        paymentResponse.createdAt( entity.getCreatedAt() );
        paymentResponse.updatedAt( entity.getUpdatedAt() );
        paymentResponse.allocations( toAllocationResponseList( entity.getAllocations() ) );

        return paymentResponse.build();
    }

    @Override
    public List<PaymentResponse> toResponseList(List<Payment> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentResponse> list1 = new ArrayList<PaymentResponse>( list.size() );
        for ( Payment payment : list ) {
            list1.add( toResponse( payment ) );
        }

        return list1;
    }

    @Override
    public PaymentAllocation toAllocationEntity(PaymentAllocationRequest request) {
        if ( request == null ) {
            return null;
        }

        PaymentAllocation.PaymentAllocationBuilder paymentAllocation = PaymentAllocation.builder();

        paymentAllocation.allocatedAmount( request.getAllocatedAmount() );

        return paymentAllocation.build();
    }

    @Override
    public PaymentAllocationResponse toAllocationResponse(PaymentAllocation entity) {
        if ( entity == null ) {
            return null;
        }

        PaymentAllocationResponse.PaymentAllocationResponseBuilder paymentAllocationResponse = PaymentAllocationResponse.builder();

        paymentAllocationResponse.supplierInvoiceId( entitySupplierInvoiceId( entity ) );
        paymentAllocationResponse.supplierInvoiceNumber( entitySupplierInvoiceInvoiceNumber( entity ) );
        paymentAllocationResponse.customerInvoiceId( entityCustomerInvoiceId( entity ) );
        paymentAllocationResponse.customerInvoiceNumber( entityCustomerInvoiceInvoiceNumber( entity ) );
        paymentAllocationResponse.id( entity.getId() );
        paymentAllocationResponse.allocatedAmount( entity.getAllocatedAmount() );

        return paymentAllocationResponse.build();
    }

    @Override
    public List<PaymentAllocationResponse> toAllocationResponseList(List<PaymentAllocation> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentAllocationResponse> list1 = new ArrayList<PaymentAllocationResponse>( list.size() );
        for ( PaymentAllocation paymentAllocation : list ) {
            list1.add( toAllocationResponse( paymentAllocation ) );
        }

        return list1;
    }

    private Long entityCompanyId(Payment payment) {
        Company company = payment.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(Payment payment) {
        Company company = payment.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }

    private Long entitySupplierId(Payment payment) {
        Supplier supplier = payment.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierName(Payment payment) {
        Supplier supplier = payment.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private Long entityCustomerId(Payment payment) {
        Customer customer = payment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String entityCustomerName(Payment payment) {
        Customer customer = payment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    private Long entityJournalEntryId(Payment payment) {
        JournalEntry journalEntry = payment.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getId();
    }

    private String entityJournalEntryEntryNumber(Payment payment) {
        JournalEntry journalEntry = payment.getJournalEntry();
        if ( journalEntry == null ) {
            return null;
        }
        return journalEntry.getEntryNumber();
    }

    private Long entityCancelledById(Payment payment) {
        User cancelledBy = payment.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getId();
    }

    private String entityCancelledByFirstName(Payment payment) {
        User cancelledBy = payment.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getFirstName();
    }

    private Long entityCreatedById(Payment payment) {
        User createdBy = payment.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getId();
    }

    private String entityCreatedByFirstName(Payment payment) {
        User createdBy = payment.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getFirstName();
    }

    private Long entitySupplierInvoiceId(PaymentAllocation paymentAllocation) {
        SupplierInvoice supplierInvoice = paymentAllocation.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        return supplierInvoice.getId();
    }

    private String entitySupplierInvoiceInvoiceNumber(PaymentAllocation paymentAllocation) {
        SupplierInvoice supplierInvoice = paymentAllocation.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        return supplierInvoice.getInvoiceNumber();
    }

    private Long entityCustomerInvoiceId(PaymentAllocation paymentAllocation) {
        CustomerInvoice customerInvoice = paymentAllocation.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getId();
    }

    private String entityCustomerInvoiceInvoiceNumber(PaymentAllocation paymentAllocation) {
        CustomerInvoice customerInvoice = paymentAllocation.getCustomerInvoice();
        if ( customerInvoice == null ) {
            return null;
        }
        return customerInvoice.getInvoiceNumber();
    }
}
