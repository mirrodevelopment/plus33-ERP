/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : SupplierService.java
 * Purpose           : Service interface contract defining the API for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : Supplier
 * Related DTO       : PageResponse, searchRequest, SupplierRequest, SupplierResponse, SupplierSearchRequest
 * Related Mapper    : SupplierMapper
 * Related DB Table  : suppliers
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Procurement Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.dto.SupplierSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierService {
    SupplierResponse createSupplier(SupplierRequest request);
    SupplierResponse updateSupplier(Long id, SupplierRequest request);
    SupplierResponse getSupplierById(Long id);
    PageResponse<SupplierResponse> searchSuppliers(SupplierSearchRequest searchRequest, Pageable pageable);
    SupplierResponse activateSupplier(Long id);
    SupplierResponse deactivateSupplier(Long id);
    void deleteSupplier(Long id);
}
