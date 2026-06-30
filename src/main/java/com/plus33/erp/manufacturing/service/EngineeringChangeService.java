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
