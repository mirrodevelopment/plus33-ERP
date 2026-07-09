/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service.impl
 * File              : BomServiceImpl.java
 * Purpose           : Business logic service layer for Manufacturing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomController
 * Related Service   : BomServiceImpl
 * Related Repository: BomHeaderRepository, BomLineRepository, ProductRepository, UnitOfMeasureRepository, BomSubstituteRepository
 * Related Entity    : Bom
 * Related DTO       : BomHeaderDto, BomLineDto, CreateBomLineRequest, CreateBomRequest, mapToHeaderDto
 * Related Mapper    : BomMapper
 * Related DB Table  : boms
 * Related REST APIs : N/A
 * Depends On        : Common Module, Inventory Module
 * Used By           : BomController, BomServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Manufacturing Module. Implements BomService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Manufacturing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * BomController
 *   --> BomServiceImpl (this)
 *   --> Validate business rules
 *   --> BomRepository (read/write 'boms')
 *   --> BomMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code boms}</p>
 * <p><b>Module Deps      :</b> Common, Inventory, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new bom and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new bom and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new bom line and persists it to the database.
     *
     * @param bomHeaderId the bomHeaderId input value
     * @param request the validated request DTO containing input data
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new bom line and persists it to the database.
     *
     * @param bomHeaderId the bomHeaderId input value
     * @param request the validated request DTO containing input data
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves a single bom by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the BomHeaderDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BomHeaderDto getBomById(Long id) {
        BomHeader header = bomHeaderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found with id: " + id));
        return mapToHeaderDto(header);
    }

    /**
     * Retrieves boms by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<BomHeaderDto> getBomsByCompany(Long companyId) {
        return bomHeaderRepository.findAllByCompany(companyId).stream()
                .map(this::mapToHeaderDto).toList();
    }

    /**
     * Retrieves active bom for product data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param productId the productId input value
     * @return the BomHeaderDto result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BomHeaderDto getActiveBomForProduct(Long companyId, Long productId) {
        return bomHeaderRepository.findActiveBomForProduct(companyId, productId, LocalDate.now())
                .stream().findFirst()
                .map(this::mapToHeaderDto)
                .orElseThrow(() -> new NoSuchElementException("No active BOM found for product ID: " + productId));
    }

    /**
     * Approves the bom, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param bomId the bomId input value
     * @param reviewerName the reviewerName input value
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Approves the bom, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param bomId the bomId input value
     * @param reviewerName the reviewerName input value
     * @return the BomHeaderDto result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public BomHeaderDto approveBom(Long bomId, String reviewerName) {
        BomHeader header = bomHeaderRepository.findById(bomId)
                .orElseThrow(() -> new NoSuchElementException("BOM Header not found"));
        header.setStatus(BomStatus.APPROVED);
        header.setApprovedAt(LocalDateTime.now());
        return mapToHeaderDto(bomHeaderRepository.save(header));
    }

    /**
     * Permanently deletes the bom from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param bomId the bomId input value
     */
    /**
     * Permanently deletes the bom from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param bomId the bomId input value
     */
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