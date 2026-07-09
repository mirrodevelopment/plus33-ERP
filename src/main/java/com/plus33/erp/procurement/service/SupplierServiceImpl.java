/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : SupplierServiceImpl.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierServiceImpl
 * Related Repository: SupplierRepository, CompanyRepository
 * Related Entity    : Supplier
 * Related DTO       : PageResponse, searchRequest, SupplierRequest, SupplierResponse, SupplierSearchRequest
 * Related Mapper    : SupplierMapper
 * Related DB Table  : suppliers
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : SupplierController, SupplierServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements SupplierService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.dto.SupplierSearchRequest;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.procurement.mapper.SupplierMapper;
import com.plus33.erp.procurement.repository.SupplierRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SupplierController
 *   --> SupplierServiceImpl (this)
 *   --> Validate business rules
 *   --> SupplierRepository (read/write 'suppliers')
 *   --> SupplierMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code suppliers}</p>
 * <p><b>Module Deps      :</b> Common, Organization, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final CompanyRepository companyRepository;
    private final SupplierMapper supplierMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository,
                               CompanyRepository companyRepository,
                               SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.companyRepository = companyRepository;
        this.supplierMapper = supplierMapper;
    }

    /**
     * Creates a new supplier and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the SupplierResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierResponse createSupplier(SupplierRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));

        if (supplierRepository.existsByCompanyIdAndCode(request.companyId(), request.code())) {
            throw new DuplicateResourceException("Supplier with code " + request.code() + " already exists in this company");
        }

        if (request.email() != null && !request.email().isBlank() &&
                supplierRepository.existsByCompanyIdAndEmail(request.companyId(), request.email())) {
            throw new DuplicateResourceException("Supplier with email " + request.email() + " already exists in this company");
        }

        Supplier supplier = supplierMapper.toEntity(request);
        supplier.setCompany(company);
        if (request.active() != null) {
            supplier.setActive(request.active());
        }

        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    /**
     * Updates an existing supplier record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the SupplierResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));

        if (!supplier.getActive()) {
            throw new BusinessException("Soft-deleted suppliers cannot be updated");
        }

        if (!supplier.getCompany().getId().equals(request.companyId())) {
            throw new BusinessException("Cannot transfer supplier to a different company");
        }

        if (!supplier.getCode().equals(request.code()) &&
                supplierRepository.existsByCompanyIdAndCode(request.companyId(), request.code())) {
            throw new DuplicateResourceException("Supplier with code " + request.code() + " already exists in this company");
        }

        if (request.email() != null && !request.email().isBlank() &&
                !request.email().equalsIgnoreCase(supplier.getEmail()) &&
                supplierRepository.existsByCompanyIdAndEmail(request.companyId(), request.email())) {
            throw new DuplicateResourceException("Supplier with email " + request.email() + " already exists in this company");
        }

        supplierMapper.updateEntity(request, supplier);
        if (request.active() != null) {
            supplier.setActive(request.active());
        }

        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    /**
     * Retrieves a single supplier by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the SupplierResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        return supplierMapper.toResponse(supplier);
    }

    /**
     * Returns a filtered paginated list of suppliers records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<SupplierResponse> searchSuppliers(SupplierSearchRequest searchRequest, Pageable pageable) {
        Specification<Supplier> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), searchRequest.companyId()));
            }

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("contactPerson")), searchPattern)
                ));
            }

            Boolean activeFilter = searchRequest.active() != null ? searchRequest.active() : Boolean.TRUE;
            predicates.add(cb.equal(root.get("active"), activeFilter));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Supplier> supplierPage = supplierRepository.findAll(spec, pageable);
        List<SupplierResponse> content = supplierPage.getContent().stream()
                .map(supplierMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                supplierPage.getNumber(),
                supplierPage.getSize(),
                supplierPage.getTotalElements(),
                supplierPage.getTotalPages()
        );
    }

    /**
     * Performs the activateSupplier operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the SupplierResponse result
     */
    @Override
    @Transactional
    public SupplierResponse activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(true);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    /**
     * Performs the deactivateSupplier operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the SupplierResponse result
     */
    @Override
    @Transactional
    public SupplierResponse deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(false);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    /**
     * Permanently deletes the supplier from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}