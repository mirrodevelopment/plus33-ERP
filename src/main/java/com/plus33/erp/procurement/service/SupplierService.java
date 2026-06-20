package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.dto.SupplierSearchRequest;
import org.springframework.data.domain.Pageable;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);
    SupplierResponse updateSupplier(Long id, SupplierRequest request);
    SupplierResponse getSupplierById(Long id);
    PageResponse<SupplierResponse> searchSuppliers(SupplierSearchRequest searchRequest, Pageable pageable);
    SupplierResponse activateSupplier(Long id);
    SupplierResponse deactivateSupplier(Long id);
    void deleteSupplier(Long id);
}
