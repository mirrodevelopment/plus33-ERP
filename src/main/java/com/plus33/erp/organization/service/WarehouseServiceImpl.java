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

    @Override
    public WarehouseResponse getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
        return organizationMapper.toResponse(warehouse);
    }

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

    @Override
    @Transactional
    public WarehouseResponse activateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + id));
        warehouse.setActive(true);
        Warehouse saved = warehouseRepository.save(warehouse);
        return organizationMapper.toResponse(saved);
    }

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
