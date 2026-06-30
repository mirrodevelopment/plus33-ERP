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

    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getInspectionsByProductionOrder(Long companyId, Long productionOrderId) {
        return qualityInspectionRepository.findByProductionOrderId(productionOrderId).stream()
                .filter(qi -> qi.getCompanyId().equals(companyId))
                .map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getInspectionsByDateRange(Long companyId, LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return qualityInspectionRepository.findByDateRange(companyId, start, end).stream()
                .map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QualityInspectionDto> getPendingInspections(Long companyId) {
        return qualityInspectionRepository.findByCompanyIdAndStatus(companyId, InspectionStatus.PENDING).stream()
                .map(this::mapToDto).toList();
    }

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
