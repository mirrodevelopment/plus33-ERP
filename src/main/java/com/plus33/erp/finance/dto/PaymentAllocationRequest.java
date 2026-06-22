package com.plus33.erp.finance.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationRequest {

    @NotNull(message = "Supplier invoice ID is required")
    private Long supplierInvoiceId;

    @NotNull(message = "Allocated amount is required")
    @Positive(message = "Allocated amount must be greater than zero")
    private BigDecimal allocatedAmount;
}
