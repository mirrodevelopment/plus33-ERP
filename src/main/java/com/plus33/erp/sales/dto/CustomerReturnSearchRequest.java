/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerReturnSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnSearchController
 * Related Service   : CustomerReturnSearchService, CustomerReturnSearchServiceImpl
 * Related Repository: CustomerReturnSearchRepository
 * Related Entity    : CustomerReturnSearch
 * Related DTO       : CustomerReturnSearchRequest
 * Related Mapper    : CustomerReturnSearchMapper
 * Related DB Table  : customer_return_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerReturnSearchController, CustomerReturnSearchService, CustomerReturnSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerReturnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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