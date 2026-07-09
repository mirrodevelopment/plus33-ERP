/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : RegionServiceImpl.java
 * Purpose           : Business logic service layer for Organization Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionServiceImpl
 * Related Repository: RegionRepository, CompanyRepository, WarehouseRepository, StoreRepository, UserRegionRepository
 * Related Entity    : Region
 * Related DTO       : PageResponse, RegionRequest, RegionResponse, RegionSearchRequest, searchRequest
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : regions
 * Related REST APIs : N/A
 * Depends On        : Common Module, Workforce Module
 * Used By           : RegionController, RegionServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Organization Module. Implements RegionService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.RegionSearchRequest;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.mapper.OrganizationMapper;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.workforce.repository.UserRegionRepository;
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
 * <p><b>Class  :</b> {@code RegionServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Organization Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RegionController
 *   --> RegionServiceImpl (this)
 *   --> Validate business rules
 *   --> RegionRepository (read/write 'regions')
 *   --> RegionMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code regions}</p>
 * <p><b>Module Deps      :</b> Common, Organization, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final UserRegionRepository userRegionRepository;
    private final OrganizationMapper organizationMapper;

    public RegionServiceImpl(RegionRepository regionRepository,
                             CompanyRepository companyRepository,
                             WarehouseRepository warehouseRepository,
                             StoreRepository storeRepository,
                             UserRegionRepository userRegionRepository,
                             OrganizationMapper organizationMapper) {
        this.regionRepository = regionRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.storeRepository = storeRepository;
        this.userRegionRepository = userRegionRepository;
        this.organizationMapper = organizationMapper;
    }

    /**
     * Creates a new region and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the RegionResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public RegionResponse createRegion(RegionRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        if (regionRepository.existsByCompanyIdAndCode(request.companyId(), request.code())) {
            throw new DuplicateResourceException("Region with code " + request.code() + " already exists in company " + company.getName());
        }

        Region region = organizationMapper.toEntity(request);
        region.setCompany(company);
        if (request.parentId() != null) {
            Region parentRegion = regionRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent region not found with ID: " + request.parentId()));
            region.setParent(parentRegion);
        }

        Region saved = regionRepository.save(region);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Retrieves a single region by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the RegionResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single region by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the RegionResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public RegionResponse getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));
        return organizationMapper.toResponse(region);
    }

    /**
     * Returns a filtered paginated list of regions records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of regions records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<RegionResponse> searchRegions(RegionSearchRequest searchRequest, Pageable pageable) {
        Specification<Region> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern)
                ));
            }

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Region> page = regionRepository.findAll(spec, pageable);
        List<RegionResponse> content = page.getContent().stream()
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
     * Updates an existing region record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the RegionResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public RegionResponse updateRegion(Long id, RegionRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        boolean companyOrCodeChanged = !region.getCompany().getId().equals(request.companyId())
                || !region.getCode().equals(request.code());

        if (companyOrCodeChanged && regionRepository.existsByCompanyIdAndCode(request.companyId(), request.code())) {
            throw new DuplicateResourceException("Region with code " + request.code() + " already exists in company " + company.getName());
        }

        organizationMapper.updateEntity(request, region);
        region.setCompany(company);
        if (request.parentId() != null) {
            Region parentRegion = regionRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent region not found with ID: " + request.parentId()));
            region.setParent(parentRegion);
        } else {
            region.setParent(null);
        }

        Region saved = regionRepository.save(region);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Permanently deletes the region from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));

        if (regionRepository.existsByParentId(id)) {
            throw new BusinessException("Cannot delete country: active sub-regions are mapped to this country");
        }

        if (warehouseRepository.existsByRegionIdAndActiveTrue(id)) {
            throw new BusinessException("Cannot delete region: active warehouses exist in this region");
        }

        if (storeRepository.existsByRegionIdAndActiveTrue(id)) {
            throw new BusinessException("Cannot delete region: active stores exist in this region");
        }

        if (userRegionRepository.existsByIdRegionId(id)) {
            throw new BusinessException("Cannot delete region: employees are assigned to this region");
        }

        regionRepository.delete(region);
    }
}