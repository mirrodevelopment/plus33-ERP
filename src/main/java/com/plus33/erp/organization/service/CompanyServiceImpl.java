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

@Service
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final OrganizationMapper organizationMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, OrganizationMapper organizationMapper) {
        this.companyRepository = companyRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        return organizationMapper.toResponse(company);
    }

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

    @Override
    @Transactional
    public CompanyResponse activateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + id));
        company.setActive(true);
        Company saved = companyRepository.save(company);
        return organizationMapper.toResponse(saved);
    }

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
