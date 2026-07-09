/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerReturnRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnService, CustomerReturnServiceImpl
 * Related Repository: CustomerReturnRepository
 * Related Entity    : CustomerReturn
 * Related DTO       : CustomerReturnItemRequest, CustomerReturnRequest
 * Related Mapper    : CustomerReturnMapper
 * Related DB Table  : customer_returns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerReturnController, CustomerReturnService, CustomerReturnServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.ReturnReason;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
