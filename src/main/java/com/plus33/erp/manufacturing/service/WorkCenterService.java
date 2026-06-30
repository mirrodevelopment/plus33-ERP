package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.util.List;

public interface WorkCenterService {
    WorkCenterDto createWorkCenter(CreateWorkCenterRequest request);
    WorkCenterDto getWorkCenterById(Long id);
    List<WorkCenterDto> getWorkCentersByCompany(Long companyId);
}
