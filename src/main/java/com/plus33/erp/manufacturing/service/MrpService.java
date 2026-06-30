package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.util.List;

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
