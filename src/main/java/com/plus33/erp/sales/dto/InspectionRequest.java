/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : InspectionRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InspectionController
 * Related Service   : InspectionService, InspectionServiceImpl
 * Related Repository: InspectionRepository
 * Related Entity    : Inspection
 * Related DTO       : InspectionRequest
 * Related Mapper    : InspectionMapper
 * Related DB Table  : inspections
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InspectionController, InspectionService, InspectionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.InspectionResult;
import java.util.List;

public record InspectionRequest(
    List<ItemInspection> items,
    String remarks
) {
    public record ItemInspection(
        Long productId,
        InspectionResult result,
        String notes
    ) {}
}
