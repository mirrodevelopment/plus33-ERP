/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : StoreServiceImpl.java
 * Purpose           : Business logic service layer for Organization Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreServiceImpl
 * Related Repository: StoreRepository, RegionRepository, WarehouseRepository
 * Related Entity    : Store
 * Related DTO       : PageResponse, searchRequest, StoreRequest, StoreResponse, StoreSearchRequest
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : stores
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : StoreController, StoreServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Organization Module. Implements StoreService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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
import com.plus33.erp.organization.entity.StoreType;
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
 * <p><b>Class  :</b> {@code StoreServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Organization Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * StoreController
 *   --> StoreServiceImpl (this)
 *   --> Validate business rules
 *   --> StoreRepository (read/write 'stores')
 *   --> StoreMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code stores}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final RegionRepository regionRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrganizationMapper organizationMapper;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;
    private final com.plus33.erp.organization.repository.StoreSettingRepository storeSettingRepository;

    public StoreServiceImpl(StoreRepository storeRepository,
                            RegionRepository regionRepository,
                            WarehouseRepository warehouseRepository,
                            OrganizationMapper organizationMapper,
                            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate,
                            com.plus33.erp.organization.repository.StoreSettingRepository storeSettingRepository) {
        this.storeRepository = storeRepository;
        this.regionRepository = regionRepository;
        this.warehouseRepository = warehouseRepository;
        this.organizationMapper = organizationMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.storeSettingRepository = storeSettingRepository;
    }

    /**
     * Creates a new store and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the StoreResponse result
     * @throws BusinessException if a business rule is violated
     */
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
        if (request.type() != null) {
            try {
                store.setType(StoreType.valueOf(request.type()));
            } catch (IllegalArgumentException e) {
                store.setType(StoreType.COMPACT_CAFE);
            }
        } else {
            store.setType(StoreType.COMPACT_CAFE);
        }

        Store saved = storeRepository.save(store);
        return enrichStoreResponse(organizationMapper.toResponse(saved), saved);
    }

    /**
     * Retrieves a single store by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the StoreResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single store by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the StoreResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        return enrichStoreResponse(organizationMapper.toResponse(store), store);
    }

    /**
     * Returns a filtered paginated list of stores records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of stores records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
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
                .map(s -> enrichStoreResponse(organizationMapper.toResponse(s), s))
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
     * Updates an existing store record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the StoreResponse result
     * @throws BusinessException if a business rule is violated
     */
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
        if (request.type() != null) {
            try {
                store.setType(StoreType.valueOf(request.type()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid updates
            }
        }

        Store saved = storeRepository.save(store);
        return enrichStoreResponse(organizationMapper.toResponse(saved), saved);
    }

    /**
     * Permanently deletes the store from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(false);
        storeRepository.save(store);
    }

    /**
     * Performs the activateStore operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the StoreResponse result
     */
    @Override
    @Transactional
    public StoreResponse activateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(true);
        Store saved = storeRepository.save(store);
        return enrichStoreResponse(organizationMapper.toResponse(saved), saved);
    }

    /**
     * Performs the deactivateStore operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the StoreResponse result
     */
    @Override
    @Transactional
    public StoreResponse deactivateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + id));
        store.setActive(false);
        Store saved = storeRepository.save(store);
        return enrichStoreResponse(organizationMapper.toResponse(saved), saved);
    }

    private void validateStoreWarehouseRelationship(Region region, Warehouse warehouse) {
        if (!warehouse.getActive()) {
            throw new BusinessException("Cannot assign warehouse: referenced warehouse is inactive");
        }
        if (!warehouse.getRegion().getId().equals(region.getId())) {
            throw new BusinessException("Cannot assign warehouse: warehouse region does not match store region");
        }
    }

    private StoreResponse enrichStoreResponse(StoreResponse response, Store store) {
        if (response == null) return null;
        
        // 1. Calculate assigned employee count
        Integer empCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(DISTINCT user_id) FROM user_stores WHERE store_id = ?",
            Integer.class,
            response.id()
        );
        
        // 2. Calculate linked warehouse stock valuation
        Double stockValue = 0.0;
        if (response.warehouseId() != null) {
            stockValue = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(ls.quantity * COALESCE(ls.unit_cost, 1.50)), 0) " +
                "FROM location_stock ls " +
                "JOIN warehouse_locations wl ON wl.id = ls.location_id " +
                "WHERE wl.warehouse_id = ?",
                Double.class,
                response.warehouseId()
            );
        }
        
        return new StoreResponse(
            response.id(),
            response.code(),
            response.name(),
            response.address(),
            response.phone(),
            response.email(),
            response.timezone(),
            response.openingDate(),
            response.regionId(),
            response.regionCode(),
            response.warehouseId(),
            response.warehouseCode(),
            response.active(),
            response.createdAt(),
            response.updatedAt(),
            empCount != null ? empCount : 0,
            stockValue != null ? stockValue : 0.0,
            response.type()
        );
    }

    @Override
    public com.plus33.erp.organization.dto.StoreSettingResponse getStoreSettings(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + storeId));
        
        com.plus33.erp.organization.entity.StoreSetting setting = storeSettingRepository.findByStoreId(storeId)
                .orElseGet(() -> {
                    com.plus33.erp.organization.entity.StoreSetting newSetting = new com.plus33.erp.organization.entity.StoreSetting();
                    newSetting.setStore(store);
                    return storeSettingRepository.save(newSetting);
                });
        
        return organizationMapper.toResponse(setting);
    }

    @Override
    @Transactional
    public com.plus33.erp.organization.dto.StoreSettingResponse updateStoreSettings(Long storeId, com.plus33.erp.organization.dto.StoreSettingRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + storeId));
        
        com.plus33.erp.organization.entity.StoreSetting setting = storeSettingRepository.findByStoreId(storeId)
                .orElseGet(() -> {
                    com.plus33.erp.organization.entity.StoreSetting newSetting = new com.plus33.erp.organization.entity.StoreSetting();
                    newSetting.setStore(store);
                    return newSetting;
                });
        
        organizationMapper.updateEntity(request, setting);
        com.plus33.erp.organization.entity.StoreSetting saved = storeSettingRepository.save(setting);
        return organizationMapper.toResponse(saved);
    }
}