/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CustomerService.java
 * Purpose           : Service interface contract defining the API for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerService, CustomerServiceImpl
 * Related Repository: CustomerRepository
 * Related Entity    : Customer
 * Related DTO       : CustomerRequest, CustomerResponse, CustomerSearchRequest, PageResponse, searchRequest
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
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
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.dto.CustomerSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest searchRequest, Pageable pageable);
    CustomerResponse activateCustomer(Long id);
    CustomerResponse deactivateCustomer(Long id);
    void deleteCustomer(Long id);
}
