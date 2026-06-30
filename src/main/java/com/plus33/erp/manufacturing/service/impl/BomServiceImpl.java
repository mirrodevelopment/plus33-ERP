package com.plus33.erp.manufacturing.service.impl;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.BomHeaderRepository;
import com.plus33.erp.manufacturing.repository.BomLineRepository;
import com.plus33.erp.manufacturing.repository.BomSubstituteRepository;
import com.plus33.erp.manufacturing.service.BomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class BomServiceImpl implements BomService {

    private final BomHeaderRepository bomHeaderRepository;
    private final BomLineRepository bomLineRepository;
    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final BomSubstituteRepository bomSubstituteRepository;

    public BomServiceImpl(BomHeaderRepository bomHeaderRepository,
                          BomLineRepository bomLineRepository,
                          ProductRepository productRepository,
                          UnitOfMeasureRepository unitOfMeasureRepository,
                          BomSubstituteRepository bomSubstituteRepository) {
        this.bomHeaderRepository = bomHeaderRepository;
        this.bomLineRepository = bomLineRepository;
        this.productRepository = productRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
        this.bomSubstituteRepository = bomSubstituteRepository;
    }

    @Override
    public BomHeaderDto createBom(CreateBomRequest request) {
        if (bomHeaderRepository.existsByCompanyIdAndBomNumber(request.getCompanyId(), request.getBomNumber())) {
            throw new IllegalArgumentException("BOM number already exists in company: " + request.getBomNumber());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        UnitOfMeasure unit = unitOfMeasureRepository.findById(request.getUnitId())
                .orElseThrow(() -> new NoSuchElementException("Unit of measure not found"));

        BomHeader header = new BomHeader();
        header.setCompanyId(request.getCompanyId());
        header.setBomNumber(request.getBomNumber());
        header.setProduct(product);
        header.setBomType(BomType.MANUFACTURING);
        header.setRevision("1.0");
        header.setBaseQuantity(request.getBaseQuantity() != null ? request.getBaseQuantity() : BigDecimal.ONE);
        header.setBaseUnit(unit);
        header.setEffectiveFrom(request.getEffectiveFrom() != null ? request.getEffectiveFrom() : LocalDate.now());
        header.setEffectiveTo(request.getEffectiveTo());
        header.setStatus(BomStatus.DRAFT);
        header.setDescription(request.getNotes());

        header = bomHeaderRepository.save(header);
        return mapToHeaderDto(header);
    }

    @Override
    public BomHeaderDto addBomLine(Long bomHeaderId, CreateBomLineRequest request) {
        BomHeader header = bomHeaderRepository.findById(bomHeaderId)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found with id: " + bomHeaderId));

        Product component = productRepository.findById(request.getComponentProductId())
                .orElseThrow(() -> new NoSuchElementException("Component product not found"));

        UnitOfMeasure unit = unitOfMeasureRepository.findById(request.getUnitId())
                .orElseThrow(() -> new NoSuchElementException("Unit of measure not found"));

        BomLine line = new BomLine();
        line.setBomHeader(header);
        line.setLineNumber(request.getLineNumber() != null ? request.getLineNumber() : header.getLines().size() + 1);
        line.setComponentProduct(component);
        line.setQuantity(request.getQuantity());
        line.setUnit(unit);
        line.setSortSequence(request.getSortSequence() != null ? request.getSortSequence() : (header.getLines().size() + 1) * 10);
        line.setComponentType(request.getComponentType() != null ? request.getComponentType() : "MATERIAL");
        line.setBackflush(request.getPhantom() != null ? request.getPhantom() : false);
        line.setScrapPercentage(request.getScrapPercentage() != null ? request.getScrapPercentage() : BigDecimal.ZERO);
        line.setYieldPercentage(request.getYieldPercentage() != null ? request.getYieldPercentage() : new BigDecimal("100.00"));
        line.setEffectiveFrom(request.getEffectiveFrom() != null ? request.getEffectiveFrom() : header.getEffectiveFrom());
        line.setEffectiveTo(request.getEffectiveTo());
        line.setNotes(request.getNotes());

        line = bomLineRepository.save(line);
        header.getLines().add(line);

        if (request.getSubstituteProductId() != null) {
            Product substituteProduct = productRepository.findById(request.getSubstituteProductId())
                    .orElseThrow(() -> new NoSuchElementException("Substitute product not found"));

            BomSubstitute substitute = new BomSubstitute();
            substitute.setBomLine(line);
            substitute.setSubstituteProduct(substituteProduct);
            substitute.setSubstituteQuantity(line.getQuantity());
            substitute.setUnit(line.getUnit());
            substitute.setPriority(1);
            bomSubstituteRepository.save(substitute);
        }

        header = bomHeaderRepository.save(header);
        return mapToHeaderDto(header);
    }

    @Override
    @Transactional(readOnly = true)
    public BomHeaderDto getBomById(Long id) {
        BomHeader header = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found with id: " + id));
        return mapToHeaderDto(header);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BomHeaderDto> getBomsByCompany(Long companyId) {
        return bomHeaderRepository.findAllByCompany(companyId).stream()
                .map(this::mapToHeaderDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BomHeaderDto getActiveBomForProduct(Long companyId, Long productId) {
        return bomHeaderRepository.findActiveBomForProduct(companyId, productId, LocalDate.now())
                .stream().findFirst()
                .map(this::mapToHeaderDto)
                .orElseThrow(() -> new NoSuchElementException("No active BOM found for product ID: " + productId));
    }

    @Override
    public BomHeaderDto approveBom(Long bomId, String reviewerName) {
        BomHeader header = bomHeaderRepository.findById(bomId)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found"));
        header.setStatus(BomStatus.APPROVED);
        header.setApprovedAt(LocalDateTime.now());
        return mapToHeaderDto(bomHeaderRepository.save(header));
    }

    @Override
    public void deleteBom(Long companyId, Long bomId) {
        BomHeader header = bomHeaderRepository.findById(bomId)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found"));
        bomHeaderRepository.delete(header);
    }

    private BomHeaderDto mapToHeaderDto(BomHeader header) {
        BomHeaderDto dto = new BomHeaderDto();
        dto.setId(header.getId());
        dto.setCompanyId(header.getCompanyId());
        dto.setBomNumber(header.getBomNumber());
        dto.setProductId(header.getProduct().getId());
        dto.setProductCode(header.getProduct().getCode());
        dto.setProductName(header.getProduct().getName());
        dto.setBaseQuantity(header.getBaseQuantity());
        dto.setUnitCode(header.getBaseUnit() != null ? header.getBaseUnit().getCode() : null);
        dto.setStatus(header.getStatus() != null ? header.getStatus().name() : null);
        dto.setVersion(1);
        dto.setEffectiveFrom(header.getEffectiveFrom());
        dto.setEffectiveTo(header.getEffectiveTo());
        dto.setNotes(header.getDescription());
        dto.setCreatedAt(header.getCreatedAt());
        dto.setUpdatedAt(header.getUpdatedAt());

        if (header.getLines() != null) {
            List<BomLineDto> lineDtos = header.getLines().stream().map(line -> {
                BomLineDto ldto = new BomLineDto();
                ldto.setId(line.getId());
                ldto.setComponentProductId(line.getComponentProduct().getId());
                ldto.setLineNumber(line.getLineNumber());
                ldto.setQuantity(line.getQuantity());
                ldto.setUnitId(line.getUnit().getId());
                ldto.setSortSequence(line.getSortSequence());
                ldto.setComponentType(line.getComponentType());
                ldto.setPhantom(line.getBackflush());
                ldto.setScrapPercentage(line.getScrapPercentage());
                ldto.setYieldPercentage(line.getYieldPercentage());
                ldto.setEffectiveFrom(line.getEffectiveFrom());
                ldto.setEffectiveTo(line.getEffectiveTo());
                ldto.setNotes(line.getNotes());

                // Find substitute if exists
                List<BomSubstitute> substitutes = bomSubstituteRepository.findByBomLineId(line.getId());
                if (!substitutes.isEmpty()) {
                    ldto.setSubstituteProductId(substitutes.get(0).getSubstituteProduct().getId());
                }
                return ldto;
            }).toList();
            dto.setLines(lineDtos);
        }
        return dto;
    }
}
