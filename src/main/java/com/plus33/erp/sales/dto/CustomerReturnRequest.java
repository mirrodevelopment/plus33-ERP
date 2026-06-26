package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.ReturnReason;
import java.util.List;
import java.util.UUID;

public record CustomerReturnRequest(
    Long companyId,
    Long customerId,
    Long salesOrderId,
    Long customerInvoiceId,
    Long warehouseId,
    Long storeId,
    UUID clientReferenceId,
    ReturnReason reason,
    String remarks,
    List<CustomerReturnItemRequest> items
) {}
