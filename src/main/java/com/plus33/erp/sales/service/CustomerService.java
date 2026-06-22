package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.dto.CustomerSearchRequest;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
    CustomerResponse getCustomerById(Long id);
    PageResponse<CustomerResponse> searchCustomers(CustomerSearchRequest searchRequest, Pageable pageable);
    CustomerResponse activateCustomer(Long id);
    CustomerResponse deactivateCustomer(Long id);
    void deleteCustomer(Long id);
}
