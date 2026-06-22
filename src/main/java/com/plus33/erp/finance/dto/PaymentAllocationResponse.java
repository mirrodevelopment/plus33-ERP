package com.plus33.erp.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAllocationResponse {
    private Long id;
    private Long supplierInvoiceId;
    private String supplierInvoiceNumber;
    private BigDecimal allocatedAmount;
}
