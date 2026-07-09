/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : QualityInspectionService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionController
 * Related Service   : QualityInspectionService, QualityInspectionServiceImpl
 * Related Repository: QualityInspectionRepository
 * Related Entity    : QualityInspection
 * Related DTO       : CreateQualityInspectionRequest, QualityInspectionDto, RecordInspectionResultRequest
 * Related Mapper    : QualityInspectionMapper
 * Related DB Table  : quality_inspections
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
 * <p><b>Class  :</b> {@code QualityInspectionService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
