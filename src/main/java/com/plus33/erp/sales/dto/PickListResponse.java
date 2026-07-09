/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : PickListResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : PickListItemResponse, PickListResponse
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickListController, PickListService, PickListServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.PickListStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
