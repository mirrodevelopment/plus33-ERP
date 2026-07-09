/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : WorkCenterService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterController
 * Related Service   : WorkCenterService, WorkCenterServiceImpl
 * Related Repository: WorkCenterRepository
 * Related Entity    : WorkCenter
 * Related DTO       : CreateWorkCenterRequest, WorkCenterDto
 * Related Mapper    : WorkCenterMapper
 * Related DB Table  : work_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.util.List;

public interface WorkCenterService {
    WorkCenterDto createWorkCenter(CreateWorkCenterRequest request);
    WorkCenterDto getWorkCenterById(Long id);
    List<WorkCenterDto> getWorkCentersByCompany(Long companyId);
}
