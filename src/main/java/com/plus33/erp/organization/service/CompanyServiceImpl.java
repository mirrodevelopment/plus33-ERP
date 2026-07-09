/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : CompanyServiceImpl.java
 * Purpose           : Business logic service layer for Organization Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : CompanyRequest, CompanyResponse, CompanySearchRequest, PageResponse, searchRequest
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : companys
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : CompanyController, CompanyServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Organization Module. Implements CompanyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.organization.dto.CompanyRequest;
import com.plus33.erp.organization.dto.CompanyResponse;
import com.plus33.erp.organization.dto.CompanySearchRequest;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.mapper.OrganizationMapper;
import com.plus33.erp.organization.repository.CompanyRepository;
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
 * <p><b>Class  :</b> {@code CompanyServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Organization Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CompanyController
 *   --> CompanyServiceImpl (this)
 *   --> Validate business rules
 *   --> CompanyRepository (read/write 'companys')
 *   --> CompanyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code companys}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final OrganizationMapper organizationMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, OrganizationMapper organizationMapper) {
        this.companyRepository = companyRepository;
        this.organizationMapper = organizationMapper;
    }

    /**
     * Retrieves a single company by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CompanyResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single company by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CompanyResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        return organizationMapper.toResponse(company);
    }

    /**
     * Returns a filtered paginated list of companies records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of companies records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<CompanyResponse> searchCompanies(CompanySearchRequest searchRequest, Pageable pageable) {
        Specification<Company> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern),
                        cb.like(cb.lower(root.get("legalName")), searchPattern)
                ));
            }

            if (searchRequest.active() != null) {
                predicates.add(cb.equal(root.get("active"), searchRequest.active()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Company> page = companyRepository.findAll(spec, pageable);
        List<CompanyResponse> content = page.getContent().stream()
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
     * Updates an existing company record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the CompanyResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));

        if (!company.getActive()) {
            throw new BusinessException("Soft-deleted company cannot be updated");
        }

        if (!company.getCode().equals(request.code()) && companyRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Company with code " + request.code() + " already exists");
        }

        organizationMapper.updateEntity(request, company);
        if (request.active() != null) {
            company.setActive(request.active());
        }

        Company saved = companyRepository.save(company);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Performs the activateCompany operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CompanyResponse result
     */
    @Override
    @Transactional
    public CompanyResponse activateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        company.setActive(true);
        Company saved = companyRepository.save(company);
        return organizationMapper.toResponse(saved);
    }

    /**
     * Performs the deactivateCompany operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the CompanyResponse result
     */
    @Override
    @Transactional
    public CompanyResponse deactivateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        company.setActive(false);
        Company saved = companyRepository.save(company);
        return organizationMapper.toResponse(saved);
    }
}