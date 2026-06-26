package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PaymentRunResponse(
        Long id,
        String runNumber,
        Long companyId,
        String status,
        LocalDate paymentDate,
        String paymentMethod,
        String currencyCode,
        LocalDate filterDueDate,
        Long filterSupplierId,
        String bankAccountCode,
        BigDecimal totalAmount,
        String exportFormat,
        String exportFileName,
        String exportStoragePath,
        String exportChecksum,
        LocalDateTime exportGeneratedAt,
        UUID clientReferenceId,
        
        Integer successfulPaymentsCount,
        Integer failedPaymentsCount,
        Integer processedInvoicesCount,
        String failureReason,
        
        String approvedByEmail,
        LocalDateTime approvedAt,
        String executedByEmail,
        LocalDateTime executedAt,
        String cancelledByEmail,
        LocalDateTime cancelledAt,
        
        String createdByEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PaymentRunInvoiceResponse> invoices,
        List<PaymentRunSupplierResultResponse> supplierResults
) {}

