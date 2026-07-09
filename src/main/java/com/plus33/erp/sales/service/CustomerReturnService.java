/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CustomerReturnService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnService, CustomerReturnServiceImpl
 * Related Repository: CustomerReturnRepository
 * Related Entity    : CustomerReturn
 * Related DTO       : CustomerReturnRequest, CustomerReturnResponse, CustomerReturnSearchRequest, InspectionRequest, PageResponse
 * Related Mapper    : CustomerReturnMapper
 * Related DB Table  : customer_returns
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CustomerReturnService {
    CustomerReturnResponse createReturn(CustomerReturnRequest request);
    CustomerReturnResponse updateReturn(Long id, CustomerReturnRequest request);
    CustomerReturnResponse getReturnById(Long id);
    PageResponse<CustomerReturnResponse> searchReturns(CustomerReturnSearchRequest searchRequest, Pageable pageable);
    CustomerReturnResponse approveReturn(Long id, ReturnApprovalRequest request);
    CustomerReturnResponse receiveReturn(Long id);
    CustomerReturnResponse inspectReturn(Long id, InspectionRequest request);
    CustomerReturnResponse closeReturn(Long id, ReturnCloseRequest request);
    CustomerReturnResponse cancelReturn(Long id, String reason);
}
