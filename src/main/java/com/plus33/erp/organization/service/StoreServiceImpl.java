package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.StoreSearchRequest;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
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
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrganizationMapper organizationMapper;

    public StoreServiceImpl(StoreRepository storeRepository,
                            RegionRepository regionRepository,
                            WarehouseRepository warehouseRepository,
                            OrganizationMapper organizationMapper) {
        this.storeRepository = storeRepository;
        this.regionRepository = regionRepository;
        this.warehouseRepository = warehouseRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    @Transactional
    public StoreResponse createStore(StoreRequest request) {
        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));

        Long companyId = region.getCompany().getId();
        if (storeRepository.existsByRegionCompanyIdAndCode(companyId, request.code())) {
            throw new DuplicateResourceException("Store with code " + request.code() + " already exists in company " + region.getCompany().getName());
        }

        Warehouse warehouse = null;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            validateStoreWarehouseRelationship(region, warehouse);
        }

        Store store = organizationMapper.toEntity(request);
        store.setRegion(region);
        store.setWarehouse(warehouse);
        if (request.active() != null) {
            store.setActive(request.active());
        }

        Store saved = storeRepository.save(store);
        return organizationMapper.toResponse(saved);
    }

    @Override
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        return organizationMapper.toResponse(store);
    }

    @Override
    public PageResponse<StoreResponse> searchStores(StoreSearchRequest searchRequest, Pageable pageable) {
        Specification<Store> spec = (root, query, cb) -> {
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

            if (searchRequest.warehouseId() != null) {
                predicates.add(cb.equal(root.get("warehouse").get("id"), searchRequest.warehouseId()));
            }

            Boolean activeFilter = searchRequest.active() != null ? searchRequest.active() : Boolean.TRUE;
            predicates.add(cb.equal(root.get("active"), activeFilter));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Store> page = storeRepository.findAll(spec, pageable);
        List<StoreResponse> content = page.getContent().stream()
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
    public StoreResponse updateStore(Long id, StoreRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));

        if (!store.getActive()) {
            throw new BusinessException("Soft-deleted store cannot be updated");
        }

        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found with ID: " + request.regionId()));

        Long companyId = region.getCompany().getId();
        boolean regionOrCodeChanged = !store.getRegion().getId().equals(request.regionId())
                || !store.getCode().equals(request.code());

        if (regionOrCodeChanged && storeRepository.existsByRegionCompanyIdAndCode(companyId, request.code())) {
            throw new DuplicateResourceException("Store with code " + request.code() + " already exists in company " + region.getCompany().getName());
        }

        Warehouse warehouse = null;
        if (request.warehouseId() != null) {
            warehouse = warehouseRepository.findById(request.warehouseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with ID: " + request.warehouseId()));
            validateStoreWarehouseRelationship(region, warehouse);
        }

        organizationMapper.updateEntity(request, store);
        store.setRegion(region);
        store.setWarehouse(warehouse);
        if (request.active() != null) {
            store.setActive(request.active());
        }

        Store saved = storeRepository.save(store);
        return organizationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(false);
        storeRepository.save(store);
    }

    @Override
    @Transactional
    public StoreResponse activateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(true);
        Store saved = storeRepository.save(store);
        return organizationMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public StoreResponse deactivateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(false);
        Store saved = storeRepository.save(store);
        return organizationMapper.toResponse(saved);
    }

    private void validateStoreWarehouseRelationship(Region region, Warehouse warehouse) {
        if (!warehouse.getActive()) {
            throw new BusinessException("Cannot assign warehouse: referenced warehouse is inactive");
        }
        if (!warehouse.getRegion().getId().equals(region.getId())) {
            throw new BusinessException("Cannot assign warehouse: warehouse region does not match store region");
        }
    }
}
