/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : EngineeringChangeService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeController
 * Related Service   : EngineeringChangeService, EngineeringChangeServiceImpl
 * Related Repository: EngineeringChangeRepository
 * Related Entity    : EngineeringChange
 * Related DTO       : CreateEcoRequest, EngineeringChangeOrderDto
 * Related Mapper    : EngineeringChangeMapper
 * Related DB Table  : engineering_changes
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

public interface EngineeringChangeService {
    EngineeringChangeOrderDto createEco(CreateEcoRequest request);
    EngineeringChangeOrderDto getEcoById(Long ecoId);
    List<EngineeringChangeOrderDto> getEcoByCompany(Long companyId);
    EngineeringChangeOrderDto submitEco(Long ecoId, Long userId);
    EngineeringChangeOrderDto approveEco(Long ecoId, Long userId);
    EngineeringChangeOrderDto implementEco(Long ecoId, Long userId);
    EngineeringChangeOrderDto cancelEco(Long ecoId, String reason, Long userId);
}
