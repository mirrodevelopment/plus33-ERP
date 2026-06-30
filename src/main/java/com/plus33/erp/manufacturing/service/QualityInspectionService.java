package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface QualityInspectionService {

    QualityInspectionDto createInspection(CreateQualityInspectionRequest request);

    QualityInspectionDto getInspectionById(Long companyId, Long inspectionId);

    List<QualityInspectionDto> getInspectionsByProductionOrder(Long companyId, Long productionOrderId);

    List<QualityInspectionDto> getInspectionsByDateRange(Long companyId, LocalDate from, LocalDate to);

    List<QualityInspectionDto> getPendingInspections(Long companyId);

    QualityInspectionDto recordInspectionResult(Long companyId, Long inspectionId,
            RecordInspectionResultRequest request);

    QualityInspectionDto approveInspection(Long companyId, Long inspectionId, Long approvedBy, String notes);

    QualityInspectionDto rejectInspection(Long companyId, Long inspectionId, Long rejectedBy,
            String defectCode, String defectDescription, String disposition);

    List<QualityInspectionDto> getRejectedInspections(Long companyId);
}
