/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : CostCenterService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterService, CostCenterServiceImpl
 * Related Repository: CostCenterRepository
 * Related Entity    : CostCenter
 * Related DTO       : CostCenterRequest, CostCenterResponse
 * Related Mapper    : CostCenterMapper
 * Related DB Table  : cost_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.CostCenterRequest;
import com.plus33.erp.finance.budget.dto.CostCenterResponse;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CostCenterService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CostCenterService {
    CostCenterResponse createCostCenter(Long companyId, CostCenterRequest request);
    CostCenterResponse updateCostCenter(Long id, CostCenterRequest request);
    CostCenterResponse getCostCenter(Long id);
    List<CostCenterResponse> getCostCentersByCompany(Long companyId);
}
