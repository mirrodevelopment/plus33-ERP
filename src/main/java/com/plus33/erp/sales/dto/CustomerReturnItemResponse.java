package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.InspectionResult;
import java.math.BigDecimal;

public record CustomerReturnItemResponse(
    Long id,
    Long salesOrderItemId,
    Long customerInvoiceItemId,
    Long productId,
    String productName,
    String productCode,
    BigDecimal quantity,
    InspectionResult inspectionResult,
    String inspectionNotes,
    Long lotId,
    String lotNumber,
    Long serialId,
    String serialNumber
) {}
