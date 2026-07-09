/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.service
 * File              : CompanyService.java
 * Purpose           : Service interface contract defining the API for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyService, CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : CompanyRequest, CompanyResponse, CompanySearchRequest, PageResponse, searchRequest
 * Related Mapper    : CompanyMapper
 * Related DB Table  : companys
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Organization Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.organization.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.CompanyRequest;
import com.plus33.erp.organization.dto.CompanyResponse;
import com.plus33.erp.organization.dto.CompanySearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code CompanyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CompanyService {
    CompanyResponse getCompanyById(Long id);
    PageResponse<CompanyResponse> searchCompanies(CompanySearchRequest searchRequest, Pageable pageable);
    CompanyResponse updateCompany(Long id, CompanyRequest request);
    CompanyResponse activateCompany(Long id);
    CompanyResponse deactivateCompany(Long id);
}
