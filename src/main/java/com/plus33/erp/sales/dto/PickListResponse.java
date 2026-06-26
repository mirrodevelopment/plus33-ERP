package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.PickListStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PickListResponse(
    Long id,
    Long companyId,
    String companyName,
    Long salesOrderId,
    String salesOrderNumber,
    String pickNumber,
    UUID clientReferenceId,
    PickListStatus status,
    Long warehouseId,
    String warehouseName,
    Long storeId,
    String storeName,

    // Audit trail
    Long createdByUserId,
    String createdByUserName,
    Long releasedByUserId,
    String releasedByUserName,
    Long pickedByUserId,
    String pickedByUserName,
    Long packedByUserId,
    String packedByUserName,
    Long shippedByUserId,
    String shippedByUserName,
    Long cancelledByUserId,
    String cancelledByUserName,

    LocalDateTime createdAt,
    LocalDateTime releasedAt,
    LocalDateTime pickedAt,
    LocalDateTime packedAt,
    LocalDateTime shippedAt,
    LocalDateTime cancelledAt,
    String cancellationReason,
    Long version,
    List<PickListItemResponse> items
) {}
