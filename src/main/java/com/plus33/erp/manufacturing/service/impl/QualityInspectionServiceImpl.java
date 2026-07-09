/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : QualityInspectionServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionController
 * Related Service   : QualityInspectionServiceImpl
 * Related Repository: QualityInspectionRepository, ProductionOrderRepository, ProductRepository
 * Related Entity    : QualityInspection
 * Related DTO       : CreateQualityInspectionRequest, mapToDto, QualityInspectionDto, RecordInspectionResultRequest
 * Related Mapper    : QualityInspectionMapper
 * Related DB Table  : quality_inspections
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module
 * Used By           : QualityInspectionController, QualityInspectionServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements QualityInspectionService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.ProductionOrderRepository;
import com.plus33.erp.manufacturing.repository.QualityInspectionRepository;
import com.plus33.erp.manufacturing.service.QualityInspectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code QualityInspectionServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * QualityInspectionController
 *   --> QualityInspectionServiceImpl (this)
 *   --> Validate business rules
 *   --> QualityInspectionRepository (read/write 'quality_inspections')
 *   --> QualityInspectionMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code quality_inspections}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class QualityInspectionServiceImpl implements QualityInspectionService {

    private final QualityInspectionRepository qualityInspectionRepository;
    private final ProductionOrderRepository productionOrderRepository;
    private final ProductRepository productRepository;

    public QualityInspectionServiceImpl(QualityInspectionRepository qualityInspectionRepository,
                                         ProductionOrderRepository productionOrderRepository,
                                         ProductRepository productRepository) {
        this.qualityInspectionRepository = qualityInspectionRepository;
        this.productionOrderRepository = productionOrderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Creates a new inspection and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the QualityInspectionDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new inspection and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the QualityInspectionDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public QualityInspectionDto createInspection(CreateQualityInspectionRequest request) {
        if (qualityInspectionRepository.findByCompanyIdAndInspectionNumber(request.getCompanyId(), request.getInspectionNumber()).isPresent()) {
            throw new IllegalArgumentException("Inspection Number already exists: " + request.getInspectionNumber());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + request.getProductId()));

        ProductionOrder po = null;
        if (request.getProductionOrderId() != null) {
            po = productionOrderRepository.findById(request.getProductionOrderId())
                    .orElseThrow(() -> new NoSuchElementException("Production Order not found with ID: " + request.getProductionOrderId()));
        }

        QualityInspection qi = new QualityInspection();
        qi.setCompanyId(request.getCompanyId());
        qi.setInspectionNumber(request.getInspectionNumber());
        qi.setProduct(product);
        qi.setProductionOrder(po);
        qi.setInspectionType(request.getInspectionType());
        qi.setSampleSize(request.getSampleSize());
        qi.setInspectedQuantity(request.getSampleSize());
        qi.setStatus(InspectionStatus.PENDING);
        qi.setHoldProduction(false);
        qi.setNotes(request.getNotes());

        qi = qualityInspectionRepository.save(qi);

        if (po != null && "FINAL".equalsIgnoreCase(request.getInspectionType())) {
            po.setStatus(ProductionOrderStatus.QUALITY_PENDING);
            productionOrderRepository.save(po);
        }

        return mapToDto(qi);
    }

    /**
     * Retrieves a single inspection by id by its identifier.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @return the QualityInspectionDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public QualityInspectionDto getInspectionById(Long companyId, Long inspectionId) {
        QualityInspection qi = qualityInspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NoSuchElementException("Inspection not found with ID: " + inspectionId));
        if (!qi.getCompanyId().equals(companyId)) {
            throw new BusinessException("Inspection does not belong to company: " + companyId);
        }
        return mapToDto(qi);
    }

    /**
     * Retrieves inspections by production order data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param productionOrderId the productionOrderId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getInspectionsByProductionOrder(Long companyId, Long productionOrderId) {
        return qualityInspectionRepository.findByProductionOrderId(productionOrderId).stream()
                .filter(qi -> qi.getCompanyId().equals(companyId))
                .map(this::mapToDto).toList();
    }

    /**
     * Retrieves inspections by date range data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param from the from input value
     * @param to the to input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getInspectionsByDateRange(Long companyId, LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return qualityInspectionRepository.findByDateRange(companyId, start, end).stream()
                .map(this::mapToDto).toList();
    }

    /**
     * Retrieves pending inspections data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getPendingInspections(Long companyId) {
        return qualityInspectionRepository.findByCompanyIdAndStatus(companyId, InspectionStatus.PENDING).stream()
                .map(this::mapToDto).toList();
    }

    /**
     * Performs the recordInspectionResult operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param request the validated request DTO containing input data
     * @return the QualityInspectionDto result
     */
    /**
     * Performs the recordInspectionResult operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param request the validated request DTO containing input data
     * @return the QualityInspectionDto result
     */
    @Override
    public QualityInspectionDto recordInspectionResult(Long companyId, Long inspectionId, RecordInspectionResultRequest request) {
        QualityInspection qi = qualityInspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NoSuchElementException("Inspection not found with ID: " + inspectionId));
        if (!qi.getCompanyId().equals(companyId)) {
            throw new BusinessException("Inspection does not belong to company: " + companyId);
        }

        qi.setPassedQuantity(request.getPassedQuantity());
        qi.setFailedQuantity(request.getFailedQuantity());
        qi.setInspectedQuantity(request.getPassedQuantity().add(request.getFailedQuantity()));
        qi.setInspectedBy(request.getInspectorUserId());
        qi.setInspectedAt(LocalDateTime.now());
        qi.setDisposition(request.getDisposition());
        qi.setNotes(request.getNotes());

        if (request.getFailedQuantity().compareTo(java.math.BigDecimal.ZERO) > 0) {
            qi.setHoldProduction(true);
            qi.setNonConformanceReport("Defect Code: " + request.getDefectCode() + ". Details: " + request.getDefectDescription());
            qi.setCorrectiveAction("Trigger standard CAPA workflow.");
        } else {
            qi.setHoldProduction(false);
        }

        return mapToDto(qualityInspectionRepository.save(qi));
    }

    /**
     * Approves the inspection, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param approvedBy the approvedBy input value
     * @param notes the notes input value
     * @return the QualityInspectionDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Approves the inspection, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param approvedBy the approvedBy input value
     * @param notes the notes input value
     * @return the QualityInspectionDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public QualityInspectionDto approveInspection(Long companyId, Long inspectionId, Long approvedBy, String notes) {
        QualityInspection qi = qualityInspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NoSuchElementException("Inspection not found with ID: " + inspectionId));
        if (!qi.getCompanyId().equals(companyId)) {
            throw new BusinessException("Inspection does not belong to company: " + companyId);
        }

        qi.setStatus(InspectionStatus.PASSED);
        qi.setApprovedBy(approvedBy);
        qi.setApprovedAt(LocalDateTime.now());
        qi.setHoldProduction(false);
        qi.setNotes(notes);
        qi.setDisposition("ACCEPT");

        qi = qualityInspectionRepository.save(qi);

        ProductionOrder po = qi.getProductionOrder();
        if (po != null && "FINAL".equalsIgnoreCase(qi.getInspectionType())) {
            // Check if there are other pending final inspections
            boolean otherPending = qualityInspectionRepository.findByProductionOrderId(po.getId()).stream()
                    .anyMatch(i -> "FINAL".equalsIgnoreCase(i.getInspectionType()) && i.getStatus() == InspectionStatus.PENDING);
            if (!otherPending) {
                po.setStatus(ProductionOrderStatus.COMPLETED);
                productionOrderRepository.save(po);
            }
        }

        return mapToDto(qi);
    }

    /**
     * Performs the rejectInspection operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param rejectedBy the rejectedBy input value
     * @param defectCode the defectCode input value
     * @param defectDescription the defectDescription input value
     * @param disposition the disposition input value
     * @return the QualityInspectionDto result
     */
    /**
     * Performs the rejectInspection operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param inspectionId the inspectionId input value
     * @param rejectedBy the rejectedBy input value
     * @param defectCode the defectCode input value
     * @param defectDescription the defectDescription input value
     * @param disposition the disposition input value
     * @return the QualityInspectionDto result
     */
    @Override
    public QualityInspectionDto rejectInspection(Long companyId, Long inspectionId, Long rejectedBy, String defectCode, String defectDescription, String disposition) {
        QualityInspection qi = qualityInspectionRepository.findById(inspectionId)
                .orElseThrow(() -> new NoSuchElementException("Inspection not found with ID: " + inspectionId));
        if (!qi.getCompanyId().equals(companyId)) {
            throw new BusinessException("Inspection does not belong to company: " + companyId);
        }

        qi.setStatus(InspectionStatus.FAILED);
        qi.setApprovedBy(rejectedBy);
        qi.setApprovedAt(LocalDateTime.now());
        qi.setHoldProduction(true);
        qi.setDisposition(disposition);
        qi.setNonConformanceReport("Defect: " + defectCode + " - " + defectDescription);

        qi = qualityInspectionRepository.save(qi);

        ProductionOrder po = qi.getProductionOrder();
        if (po != null) {
            if ("REWORK".equalsIgnoreCase(disposition)) {
                po.setStatus(ProductionOrderStatus.REWORK);
            } else if ("SCRAP".equalsIgnoreCase(disposition)) {
                po.setStatus(ProductionOrderStatus.SCRAPPED);
            } else {
                po.setStatus(ProductionOrderStatus.HOLD);
            }
            productionOrderRepository.save(po);
        }

        return mapToDto(qi);
    }

    /**
     * Retrieves rejected inspections data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getRejectedInspections(Long companyId) {
        return qualityInspectionRepository.findRejectedInspections(companyId).stream()
                .map(this::mapToDto).toList();
    }

    private QualityInspectionDto mapToDto(QualityInspection qi) {
        QualityInspectionDto dto = new QualityInspectionDto();
        dto.setId(qi.getId());
        dto.setCompanyId(qi.getCompanyId());
        dto.setInspectionNumber(qi.getInspectionNumber());
        if (qi.getProductionOrder() != null) {
            dto.setProductionOrderId(qi.getProductionOrder().getId());
            dto.setProductionOrderNumber(qi.getProductionOrder().getOrderNumber());
        }
        dto.setProductId(qi.getProduct().getId());
        dto.setProductCode(qi.getProduct().getCode());
        dto.setProductName(qi.getProduct().getName());
        dto.setInspectionType(qi.getInspectionType());
        dto.setStatus(qi.getStatus().name());
        dto.setSampleSize(qi.getSampleSize());
        dto.setInspectedQuantity(qi.getInspectedQuantity());
        dto.setPassedQuantity(qi.getPassedQuantity());
        dto.setFailedQuantity(qi.getFailedQuantity());
        dto.setSamplingPlan(qi.getSamplingPlan());
        dto.setDisposition(qi.getDisposition());
        dto.setHoldProduction(qi.getHoldProduction());
        dto.setNonConformanceReport(qi.getNonConformanceReport());
        dto.setCorrectiveAction(qi.getCorrectiveAction());
        dto.setInspectedBy(qi.getInspectedBy());
        dto.setApprovedBy(qi.getApprovedBy());
        dto.setInspectedAt(qi.getInspectedAt());
        dto.setApprovedAt(qi.getApprovedAt());
        dto.setNotes(qi.getNotes());
        dto.setCreatedAt(qi.getCreatedAt());
        dto.setUpdatedAt(qi.getUpdatedAt());
        return dto;
    }
}