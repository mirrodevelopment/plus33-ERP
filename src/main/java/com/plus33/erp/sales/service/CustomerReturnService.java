package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import org.springframework.data.domain.Pageable;

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
