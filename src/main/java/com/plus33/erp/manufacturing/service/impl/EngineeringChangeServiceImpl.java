package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.*;
import com.plus33.erp.manufacturing.service.EngineeringChangeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class EngineeringChangeServiceImpl implements EngineeringChangeService {

    private final EngineeringChangeOrderRepository ecoRepository;
    private final EngineeringChangeLineRepository ecoLineRepository;
    private final BomHeaderRepository bomHeaderRepository;
    private final RoutingHeaderRepository routingHeaderRepository;

    public EngineeringChangeServiceImpl(EngineeringChangeOrderRepository ecoRepository,
                                         EngineeringChangeLineRepository ecoLineRepository,
                                         BomHeaderRepository bomHeaderRepository,
                                         RoutingHeaderRepository routingHeaderRepository) {
        this.ecoRepository = ecoRepository;
        this.ecoLineRepository = ecoLineRepository;
        this.bomHeaderRepository = bomHeaderRepository;
        this.routingHeaderRepository = routingHeaderRepository;
    }

    @Override
    public EngineeringChangeOrderDto createEco(CreateEcoRequest request) {
        if (ecoRepository.existsByCompanyIdAndEcoNumber(request.getCompanyId(), request.getEcoNumber())) {
            throw new IllegalArgumentException("ECO Number already exists: " + request.getEcoNumber());
        }

        EngineeringChangeOrder eco = new EngineeringChangeOrder();
        eco.setCompanyId(request.getCompanyId());
        eco.setEcoNumber(request.getEcoNumber());
        eco.setTitle(request.getTitle());
        eco.setDescription(request.getDescription());
        eco.setReason(request.getReason());
        eco.setPriority(request.getPriority() != null ? request.getPriority() : "NORMAL");
        eco.setEffectiveDate(request.getEffectiveDate() != null ? request.getEffectiveDate() : LocalDate.now());
        eco.setRequestedBy(request.getRequestedBy());
        eco.setStatus(EcoStatus.DRAFT);

        eco = ecoRepository.save(eco);

        if (request.getLines() != null) {
            for (CreateEcoLineRequest lineReq : request.getLines()) {
                EngineeringChangeLine line = new EngineeringChangeLine();
                line.setEngineeringChangeOrder(eco);
                line.setChangeType(lineReq.getChangeType());
                line.setReferenceType(lineReq.getReferenceType());
                line.setReferenceId(lineReq.getReferenceId());
                line.setBeforeSnapshot(lineReq.getBeforeSnapshot());
                line.setAfterSnapshot(lineReq.getAfterSnapshot());
                line.setEffectiveFrom(lineReq.getEffectiveFrom() != null ? lineReq.getEffectiveFrom() : eco.getEffectiveDate());
                line.setSortSequence(lineReq.getSortSequence() != null ? lineReq.getSortSequence() : 10);
                line.setNotes(lineReq.getNotes());
                ecoLineRepository.save(line);
                eco.getLines().add(line);
            }
        }

        return mapToDto(eco);
    }

    @Override
    @Transactional(readOnly = true)
    public EngineeringChangeOrderDto getEcoById(Long ecoId) {
        EngineeringChangeOrder eco = ecoRepository.findById(ecoId)
                .orElseThrow(() -> new NoSuchElementException("ECO not found with ID: " + ecoId));
        return mapToDto(eco);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EngineeringChangeOrderDto> getEcoByCompany(Long companyId) {
        return ecoRepository.findByCompanyId(companyId)
                .stream().map(this::mapToDto).toList();
    }

    @Override
    public EngineeringChangeOrderDto submitEco(Long ecoId, Long userId) {
        EngineeringChangeOrder eco = ecoRepository.findById(ecoId)
                .orElseThrow(() -> new NoSuchElementException("ECO not found with ID: " + ecoId));
        if (eco.getStatus() != EcoStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT ECOs can be submitted");
        }
        eco.setStatus(EcoStatus.UNDER_REVIEW);
        eco.setReviewedBy(userId);
        eco.setReviewedAt(LocalDateTime.now());
        return mapToDto(ecoRepository.save(eco));
    }

    @Override
    public EngineeringChangeOrderDto approveEco(Long ecoId, Long userId) {
        EngineeringChangeOrder eco = ecoRepository.findById(ecoId)
                .orElseThrow(() -> new NoSuchElementException("ECO not found with ID: " + ecoId));
        if (eco.getStatus() != EcoStatus.UNDER_REVIEW && eco.getStatus() != EcoStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT or UNDER_REVIEW ECOs can be approved");
        }
        eco.setStatus(EcoStatus.APPROVED);
        eco.setApprovedBy(userId);
        eco.setApprovedAt(LocalDateTime.now());
        return mapToDto(ecoRepository.save(eco));
    }

    @Override
    public EngineeringChangeOrderDto implementEco(Long ecoId, Long userId) {
        EngineeringChangeOrder eco = ecoRepository.findById(ecoId)
                .orElseThrow(() -> new NoSuchElementException("ECO not found with ID: " + ecoId));
        if (eco.getStatus() != EcoStatus.APPROVED) {
            throw new IllegalStateException("Only APPROVED ECOs can be implemented");
        }

        // Apply revisions to BOM Headers and Routing Headers referenced in change lines
        for (EngineeringChangeLine line : eco.getLines()) {
            if ("BOM_HEADER".equals(line.getReferenceType())) {
                BomHeader bom = bomHeaderRepository.findById(line.getReferenceId()).orElse(null);
                if (bom != null) {
                    // Supersede existing active BOMs for this product
                    List<BomHeader> activeBoms = bomHeaderRepository
                            .findActiveBomForProduct(bom.getCompanyId(), bom.getProduct().getId(), LocalDate.now());
                    for (BomHeader activeBom : activeBoms) {
                        if (!activeBom.getId().equals(bom.getId())) {
                            activeBom.setStatus(BomStatus.SUPERSEDED);
                            activeBom.setEffectiveTo(eco.getEffectiveDate());
                            bomHeaderRepository.save(activeBom);
                        }
                    }
                    bom.setStatus(BomStatus.ACTIVE);
                    bom.setEffectiveFrom(eco.getEffectiveDate());
                    bomHeaderRepository.save(bom);
                }
            } else if ("ROUTING_HEADER".equals(line.getReferenceType())) {
                RoutingHeader routing = routingHeaderRepository.findById(line.getReferenceId()).orElse(null);
                if (routing != null) {
                    routing.setStatus("ACTIVE");
                    routing.setEffectiveFrom(eco.getEffectiveDate());
                    routingHeaderRepository.save(routing);
                }
            }
        }

        eco.setStatus(EcoStatus.IMPLEMENTED);
        eco.setImplementedAt(LocalDateTime.now());
        return mapToDto(ecoRepository.save(eco));
    }

    @Override
    public EngineeringChangeOrderDto cancelEco(Long ecoId, String reason, Long userId) {
        EngineeringChangeOrder eco = ecoRepository.findById(ecoId)
                .orElseThrow(() -> new NoSuchElementException("ECO not found with ID: " + ecoId));
        if (eco.getStatus() == EcoStatus.IMPLEMENTED) {
            throw new IllegalStateException("Implemented ECOs cannot be cancelled");
        }
        eco.setStatus(EcoStatus.CANCELLED);
        eco.setReason(reason);
        return mapToDto(ecoRepository.save(eco));
    }

    private EngineeringChangeOrderDto mapToDto(EngineeringChangeOrder eco) {
        EngineeringChangeOrderDto dto = new EngineeringChangeOrderDto();
        dto.setId(eco.getId());
        dto.setCompanyId(eco.getCompanyId());
        dto.setEcoNumber(eco.getEcoNumber());
        dto.setTitle(eco.getTitle());
        dto.setDescription(eco.getDescription());
        dto.setReason(eco.getReason());
        dto.setStatus(eco.getStatus() != null ? eco.getStatus().name() : null);
        dto.setPriority(eco.getPriority());
        dto.setEffectiveDate(eco.getEffectiveDate());
        dto.setRequestedBy(eco.getRequestedBy());
        dto.setReviewedBy(eco.getReviewedBy());
        dto.setApprovedBy(eco.getApprovedBy());
        dto.setReviewedAt(eco.getReviewedAt());
        dto.setApprovedAt(eco.getApprovedAt());
        dto.setImplementedAt(eco.getImplementedAt());

        List<EngineeringChangeLineDto> lineDtos = ecoLineRepository.findByEngineeringChangeOrderId(eco.getId())
                .stream().map(line -> {
                    EngineeringChangeLineDto lineDto = new EngineeringChangeLineDto();
                    lineDto.setId(line.getId());
                    lineDto.setEcoId(line.getEngineeringChangeOrder().getId());
                    lineDto.setChangeType(line.getChangeType());
                    lineDto.setReferenceType(line.getReferenceType());
                    lineDto.setReferenceId(line.getReferenceId());
                    lineDto.setBeforeSnapshot(line.getBeforeSnapshot());
                    lineDto.setAfterSnapshot(line.getAfterSnapshot());
                    lineDto.setEffectiveFrom(line.getEffectiveFrom());
                    lineDto.setSortSequence(line.getSortSequence());
                    lineDto.setNotes(line.getNotes());
                    return lineDto;
                }).toList();
        dto.setLines(lineDtos);

        return dto;
    }
}
