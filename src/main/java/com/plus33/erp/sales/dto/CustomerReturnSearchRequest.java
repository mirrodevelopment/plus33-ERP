package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerReturnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReturnSearchRequest {
    private String returnNumber;
    private Long companyId;
    private Long customerId;
    private Long salesOrderId;
    private Long customerInvoiceId;
    private CustomerReturnStatus status;
    private LocalDate returnDateFrom;
    private LocalDate returnDateTo;
}
