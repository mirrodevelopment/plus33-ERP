package com.plus33.erp.finance.dto;

import com.plus33.erp.finance.entity.SupplierInvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierInvoiceSearchRequest {

    private String invoiceNumber;
    private Long companyId;
    private Long supplierId;
    private Long purchaseOrderId;
    private SupplierInvoiceStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate invoiceDateTo;
}
