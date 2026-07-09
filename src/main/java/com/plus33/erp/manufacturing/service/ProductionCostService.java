/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : ProductionCostService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionCostController
 * Related Service   : ProductionCostService, ProductionCostServiceImpl
 * Related Repository: ProductionCostRepository
 * Related Entity    : ProductionCost
 * Related DTO       : ProductionCostDto, ProductionCostSummaryDto
 * Related Mapper    : ProductionCostMapper
 * Related DB Table  : production_costs
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

public interface ProductionCostService {

    ProductionCostDto getCostByOrder(Long companyId, Long productionOrderId);

    ProductionCostDto finalizeProductionCosts(Long companyId, Long productionOrderId, Long userId);

    ProductionCostDto reverseProductionCosts(Long companyId, Long productionOrderId, String reversalReason, Long userId);

    ProductionCostSummaryDto getCostVarianceSummary(Long companyId, Long productionOrderId);

    List<ProductionCostDto> getOpenWipCosts(Long companyId);
}
