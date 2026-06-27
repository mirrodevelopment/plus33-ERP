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
public class SupplierInvoiceItemResponse {

    private Long id;
    private Long purchaseOrderItemId;
    private Long goodsReceiptItemId;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal netAmount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private Long dimensionSetId;
}
