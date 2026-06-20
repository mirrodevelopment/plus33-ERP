package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.CompanyRequest;
import com.plus33.erp.organization.dto.CompanyResponse;
import com.plus33.erp.organization.dto.CompanySearchRequest;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyResponse getCompanyById(Long id);
    PageResponse<CompanyResponse> searchCompanies(CompanySearchRequest searchRequest, Pageable pageable);
    CompanyResponse updateCompany(Long id, CompanyRequest request);
    CompanyResponse activateCompany(Long id);
    CompanyResponse deactivateCompany(Long id);
}
