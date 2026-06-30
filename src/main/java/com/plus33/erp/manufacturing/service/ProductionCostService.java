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
