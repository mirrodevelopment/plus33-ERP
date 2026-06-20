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

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        return supplierMapper.toResponse(supplier);
    }

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

    @Override
    @Transactional
    public SupplierResponse activateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(true);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public SupplierResponse deactivateSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(false);
        Supplier saved = supplierRepository.save(supplier);
        return supplierMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with ID: " + id));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}
