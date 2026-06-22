package com.plus33.erp.finance.dto;

import com.plus33.erp.finance.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String paymentNumber;
    private Long companyId;
    private String companyName;
    private Long supplierId;
    private String supplierName;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String paymentType;
    private BigDecimal amount;
    private String referenceNumber;
    private Long journalEntryId;
    private String journalEntryNumber;
    private String currencyCode;
    private PaymentStatus status;
    private LocalDateTime cancelledAt;
    private Long cancelledByUserId;
    private String cancelledByUserName;
    private String cancellationReason;
    private Long createdByUserId;
    private String createdByUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaymentAllocationResponse> allocations;
}
