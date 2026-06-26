package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CustomerInvoiceSearchRequest {
    private String invoiceNumber;
    private Long companyId;
    private Long customerId;
    private Long salesOrderId;
    private CustomerInvoiceStatus status;
    private LocalDate invoiceDateFrom;
    private LocalDate invoiceDateTo;
}
