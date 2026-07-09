/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : MrpService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpController
 * Related Service   : MrpService, MrpServiceImpl
 * Related Repository: MrpRepository
 * Related Entity    : Mrp
 * Related DTO       : ExecuteMrpRunRequest, MrpPlannedOrderDto, MrpRunDto, MrpRunRequest, MrpSuggestionDto
 * Related Mapper    : MrpMapper
 * Related DB Table  : mrps
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
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface MrpService {
    List<MrpSuggestionDto> runMrp(MrpRunRequest request);
    List<MrpSuggestionDto> getMrpSuggestionsByCompany(Long companyId);
    MrpRunDto executeMrpRun(ExecuteMrpRunRequest request);
    MrpRunDto getMrpRun(Long companyId, Long runId);
    List<MrpRunDto> getMrpRunHistory(Long companyId);
    List<MrpPlannedOrderDto> getPlannedOrders(Long companyId, Long runId);
    List<MrpPlannedOrderDto> getActionablePlannedOrders(Long companyId, LocalDate from, LocalDate to);
    MrpPlannedOrderDto firmPlannedOrder(Long companyId, Long plannedOrderId, Long userId);
    ProductionOrderDto releasePlannedOrder(Long companyId, Long plannedOrderId, Long userId);
    void cancelMrpRun(Long companyId, Long runId, Long userId);
}
