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

        Region saved = regionRepository.save(region);
        return organizationMapper.toResponse(saved);
    }

    @Override
    public RegionResponse getRegionById(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));
        return organizationMapper.toResponse(region);
    }

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

        Region saved = regionRepository.save(region);
        return organizationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + id));

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
