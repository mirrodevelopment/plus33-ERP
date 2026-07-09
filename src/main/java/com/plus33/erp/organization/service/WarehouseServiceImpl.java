/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : WarehouseServiceImpl.java
 * Purpose           : Business logic service layer for Organization Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseServiceImpl
 * Related Repository: WarehouseRepository, RegionRepository, StoreRepository
 * Related Entity    : Warehouse
 * Related DTO       : PageResponse, searchRequest, toResponse, WarehouseRequest, WarehouseResponse
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : warehouses
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : WarehouseController, WarehouseServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Organization Module. Implements WarehouseService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.dto.WarehouseSearchRequest;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.mapper.OrganizationMapper;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Organization Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WarehouseController
 *   --> WarehouseServiceImpl (this)
 *   --> Validate business rules
 *   --> WarehouseRepository (read/write 'warehouses')
 *   --> WarehouseMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code warehouses}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final RegionRepository regionRepository;
    private final StoreRepository storeRepository;
    private final OrganizationMapper organizationMapper;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                RegionRepository regionRepository,
                                StoreRepository storeRepository,
                                OrganizationMapper organizationMapper) {
        this.warehouseRepository = warehouseRepository;
        this.regionRepository = regionRepository;
        this.storeRepository = storeRepository;
        this.organizationMapper = organizationMapper;
    }

    /**
     * Creates a new warehouse and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the WarehouseResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));

        if (warehouseRepository.existsByRegionIdAndCode(request.regionId(), request.code())) {
            throw new DuplicateResourceException("Warehouse with code " + request.code() + " already exists in region " + region.getName());
        }

        Warehouse warehouse = organizationMapper.toEntity(request);
        warehouse.setRegion(region);
        if (request.active() != null) {
            warehouse.setActive(request.active());
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Retrieves a single warehouse by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the WarehouseResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single warehouse by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the WarehouseResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
        return organizationMapper.toResponse(warehouse);
    }

    /**
     * Returns a filtered paginated list of warehouses records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of warehouses records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<WarehouseResponse> searchWarehouses(WarehouseSearchRequest searchRequest, Pageable pageable) {
        Specification<Warehouse> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern)
                ));
            }

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("region").get("company").get("id"), searchRequest.companyId()));
            }

            if (searchRequest.regionId() != null) {
                predicates.add(cb.equal(root.get("region").get("id"), searchRequest.regionId()));
            }

            Boolean activeFilter = searchRequest.active() != null ? searchRequest.active() : Boolean.TRUE;
            predicates.add(cb.equal(root.get("active"), activeFilter));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Warehouse> page = warehouseRepository.findAll(spec, pageable);
        List<WarehouseResponse> content = page.getContent().stream()
                .map(organizationMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    /**
     * Updates an existing warehouse record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the WarehouseResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(Long id, WarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));

        if (!warehouse.getActive()) {
            throw new BusinessException("Soft-deleted warehouse cannot be updated");
        }

        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));

        boolean regionOrCodeChanged = !warehouse.getRegion().getId().equals(request.regionId())
                || !warehouse.getCode().equals(request.code());

        if (regionOrCodeChanged && warehouseRepository.existsByRegionIdAndCode(request.regionId(), request.code())) {
            throw new DuplicateResourceException("Warehouse with code " + request.code() + " already exists in region " + region.getName());
        }

        organizationMapper.updateEntity(request, warehouse);
        warehouse.setRegion(region);
        if (request.active() != null) {
            warehouse.setActive(request.active());
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Permanently deletes the warehouse from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));

        if (storeRepository.existsByWarehouseIdAndActiveTrue(id)) {
            throw new BusinessException("Cannot delete warehouse: active stores depend on it as their default warehouse");
        }

        warehouse.setActive(false);
        warehouseRepository.save(warehouse);
    }

    /**
     * Performs the activateWarehouse operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the WarehouseResponse result
     */
    @Override
    @Transactional
    public WarehouseResponse activateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
        warehouse.setActive(true);
        Warehouse saved = warehouseRepository.save(warehouse);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Performs the deactivateWarehouse operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the WarehouseResponse result
     */
    @Override
    @Transactional
    public WarehouseResponse deactivateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));

        if (storeRepository.existsByWarehouseIdAndActiveTrue(id)) {
            throw new BusinessException("Cannot deactivate warehouse: active stores depend on it as their default warehouse");
        }

        warehouse.setActive(false);
        Warehouse saved = warehouseRepository.save(warehouse);
        return organizationMapper.toResponse(saved);
    }
}