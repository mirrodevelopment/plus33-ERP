package com.plus33.erp.paymentrun.mapper;

import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.paymentrun.dto.PaymentRunInvoiceResponse;
import com.plus33.erp.paymentrun.dto.PaymentRunResponse;
import com.plus33.erp.paymentrun.dto.PaymentRunSupplierResultResponse;
import com.plus33.erp.paymentrun.entity.PaymentRun;
import com.plus33.erp.paymentrun.entity.PaymentRunInvoice;
import com.plus33.erp.paymentrun.entity.PaymentRunSupplierResult;
import com.plus33.erp.procurement.entity.Supplier;
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
    date = "2026-07-14T10:25:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class PaymentRunMapperImpl implements PaymentRunMapper {

    @Override
    public PaymentRunResponse toResponse(PaymentRun entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        Long filterSupplierId = null;
        String approvedByEmail = null;
        String executedByEmail = null;
        String cancelledByEmail = null;
        String createdByEmail = null;
        List<PaymentRunInvoiceResponse> invoices = null;
        List<PaymentRunSupplierResultResponse> supplierResults = null;
        Long id = null;
        String runNumber = null;
        String status = null;
        LocalDate paymentDate = null;
        String paymentMethod = null;
        String currencyCode = null;
        LocalDate filterDueDate = null;
        String bankAccountCode = null;
        BigDecimal totalAmount = null;
        String exportFormat = null;
        String exportFileName = null;
        String exportStoragePath = null;
        String exportChecksum = null;
        LocalDateTime exportGeneratedAt = null;
        UUID clientReferenceId = null;
        Integer successfulPaymentsCount = null;
        Integer failedPaymentsCount = null;
        Integer processedInvoicesCount = null;
        String failureReason = null;
        LocalDateTime approvedAt = null;
        LocalDateTime executedAt = null;
        LocalDateTime cancelledAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId( entity );
        filterSupplierId = entityFilterSupplierId( entity );
        approvedByEmail = entityApprovedByEmail( entity );
        executedByEmail = entityExecutedByEmail( entity );
        cancelledByEmail = entityCancelledByEmail( entity );
        createdByEmail = entityCreatedByEmail( entity );
        invoices = toInvoiceResponseList( entity.getInvoices() );
        supplierResults = toSupplierResultResponseList( entity.getSupplierResults() );
        id = entity.getId();
        runNumber = entity.getRunNumber();
        if ( entity.getStatus() != null ) {
            status = entity.getStatus().name();
        }
        paymentDate = entity.getPaymentDate();
        paymentMethod = entity.getPaymentMethod();
        currencyCode = entity.getCurrencyCode();
        filterDueDate = entity.getFilterDueDate();
        bankAccountCode = entity.getBankAccountCode();
        totalAmount = entity.getTotalAmount();
        exportFormat = entity.getExportFormat();
        exportFileName = entity.getExportFileName();
        exportStoragePath = entity.getExportStoragePath();
        exportChecksum = entity.getExportChecksum();
        exportGeneratedAt = entity.getExportGeneratedAt();
        clientReferenceId = entity.getClientReferenceId();
        successfulPaymentsCount = entity.getSuccessfulPaymentsCount();
        failedPaymentsCount = entity.getFailedPaymentsCount();
        processedInvoicesCount = entity.getProcessedInvoicesCount();
        failureReason = entity.getFailureReason();
        approvedAt = entity.getApprovedAt();
        executedAt = entity.getExecutedAt();
        cancelledAt = entity.getCancelledAt();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        PaymentRunResponse paymentRunResponse = new PaymentRunResponse( id, runNumber, companyId, status, paymentDate, paymentMethod, currencyCode, filterDueDate, filterSupplierId, bankAccountCode, totalAmount, exportFormat, exportFileName, exportStoragePath, exportChecksum, exportGeneratedAt, clientReferenceId, successfulPaymentsCount, failedPaymentsCount, processedInvoicesCount, failureReason, approvedByEmail, approvedAt, executedByEmail, executedAt, cancelledByEmail, cancelledAt, createdByEmail, createdAt, updatedAt, invoices, supplierResults );

        return paymentRunResponse;
    }

    @Override
    public List<PaymentRunResponse> toResponseList(List<PaymentRun> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentRunResponse> list1 = new ArrayList<PaymentRunResponse>( list.size() );
        for ( PaymentRun paymentRun : list ) {
            list1.add( toResponse( paymentRun ) );
        }

        return list1;
    }

    @Override
    public PaymentRunInvoiceResponse toInvoiceResponse(PaymentRunInvoice entity) {
        if ( entity == null ) {
            return null;
        }

        Long supplierInvoiceId = null;
        String invoiceNumber = null;
        Long supplierId = null;
        String supplierName = null;
        Long id = null;
        BigDecimal invoiceOutstandingBalance = null;
        BigDecimal paymentAmount = null;
        String paymentReference = null;

        supplierInvoiceId = entitySupplierInvoiceId( entity );
        invoiceNumber = entitySupplierInvoiceInvoiceNumber( entity );
        supplierId = entitySupplierInvoiceSupplierId( entity );
        supplierName = entitySupplierInvoiceSupplierName( entity );
        id = entity.getId();
        invoiceOutstandingBalance = entity.getInvoiceOutstandingBalance();
        paymentAmount = entity.getPaymentAmount();
        paymentReference = entity.getPaymentReference();

        PaymentRunInvoiceResponse paymentRunInvoiceResponse = new PaymentRunInvoiceResponse( id, supplierInvoiceId, invoiceNumber, supplierId, supplierName, invoiceOutstandingBalance, paymentAmount, paymentReference );

        return paymentRunInvoiceResponse;
    }

    @Override
    public List<PaymentRunInvoiceResponse> toInvoiceResponseList(List<PaymentRunInvoice> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentRunInvoiceResponse> list1 = new ArrayList<PaymentRunInvoiceResponse>( list.size() );
        for ( PaymentRunInvoice paymentRunInvoice : list ) {
            list1.add( toInvoiceResponse( paymentRunInvoice ) );
        }

        return list1;
    }

    @Override
    public PaymentRunSupplierResultResponse toSupplierResultResponse(PaymentRunSupplierResult entity) {
        if ( entity == null ) {
            return null;
        }

        Long supplierId = null;
        String supplierName = null;
        Long paymentId = null;
        String paymentNumber = null;
        Long id = null;
        String status = null;
        BigDecimal amount = null;
        String errorMessage = null;
        LocalDateTime startedAt = null;
        LocalDateTime completedAt = null;

        supplierId = entitySupplierId( entity );
        supplierName = entitySupplierName( entity );
        paymentId = entityPaymentId( entity );
        paymentNumber = entityPaymentPaymentNumber( entity );
        id = entity.getId();
        status = entity.getStatus();
        amount = entity.getAmount();
        errorMessage = entity.getErrorMessage();
        startedAt = entity.getStartedAt();
        completedAt = entity.getCompletedAt();

        PaymentRunSupplierResultResponse paymentRunSupplierResultResponse = new PaymentRunSupplierResultResponse( id, supplierId, supplierName, paymentId, paymentNumber, status, amount, errorMessage, startedAt, completedAt );

        return paymentRunSupplierResultResponse;
    }

    @Override
    public List<PaymentRunSupplierResultResponse> toSupplierResultResponseList(List<PaymentRunSupplierResult> list) {
        if ( list == null ) {
            return null;
        }

        List<PaymentRunSupplierResultResponse> list1 = new ArrayList<PaymentRunSupplierResultResponse>( list.size() );
        for ( PaymentRunSupplierResult paymentRunSupplierResult : list ) {
            list1.add( toSupplierResultResponse( paymentRunSupplierResult ) );
        }

        return list1;
    }

    private Long entityCompanyId(PaymentRun paymentRun) {
        Company company = paymentRun.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityFilterSupplierId(PaymentRun paymentRun) {
        Supplier filterSupplier = paymentRun.getFilterSupplier();
        if ( filterSupplier == null ) {
            return null;
        }
        return filterSupplier.getId();
    }

    private String entityApprovedByEmail(PaymentRun paymentRun) {
        User approvedBy = paymentRun.getApprovedBy();
        if ( approvedBy == null ) {
            return null;
        }
        return approvedBy.getEmail();
    }

    private String entityExecutedByEmail(PaymentRun paymentRun) {
        User executedBy = paymentRun.getExecutedBy();
        if ( executedBy == null ) {
            return null;
        }
        return executedBy.getEmail();
    }

    private String entityCancelledByEmail(PaymentRun paymentRun) {
        User cancelledBy = paymentRun.getCancelledBy();
        if ( cancelledBy == null ) {
            return null;
        }
        return cancelledBy.getEmail();
    }

    private String entityCreatedByEmail(PaymentRun paymentRun) {
        User createdBy = paymentRun.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        return createdBy.getEmail();
    }

    private Long entitySupplierInvoiceId(PaymentRunInvoice paymentRunInvoice) {
        SupplierInvoice supplierInvoice = paymentRunInvoice.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        return supplierInvoice.getId();
    }

    private String entitySupplierInvoiceInvoiceNumber(PaymentRunInvoice paymentRunInvoice) {
        SupplierInvoice supplierInvoice = paymentRunInvoice.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        return supplierInvoice.getInvoiceNumber();
    }

    private Long entitySupplierInvoiceSupplierId(PaymentRunInvoice paymentRunInvoice) {
        SupplierInvoice supplierInvoice = paymentRunInvoice.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        Supplier supplier = supplierInvoice.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierInvoiceSupplierName(PaymentRunInvoice paymentRunInvoice) {
        SupplierInvoice supplierInvoice = paymentRunInvoice.getSupplierInvoice();
        if ( supplierInvoice == null ) {
            return null;
        }
        Supplier supplier = supplierInvoice.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private Long entitySupplierId(PaymentRunSupplierResult paymentRunSupplierResult) {
        Supplier supplier = paymentRunSupplierResult.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getId();
    }

    private String entitySupplierName(PaymentRunSupplierResult paymentRunSupplierResult) {
        Supplier supplier = paymentRunSupplierResult.getSupplier();
        if ( supplier == null ) {
            return null;
        }
        return supplier.getName();
    }

    private Long entityPaymentId(PaymentRunSupplierResult paymentRunSupplierResult) {
        Payment payment = paymentRunSupplierResult.getPayment();
        if ( payment == null ) {
            return null;
        }
        return payment.getId();
    }

    private String entityPaymentPaymentNumber(PaymentRunSupplierResult paymentRunSupplierResult) {
        Payment payment = paymentRunSupplierResult.getPayment();
        if ( payment == null ) {
            return null;
        }
        return payment.getPaymentNumber();
    }
}
