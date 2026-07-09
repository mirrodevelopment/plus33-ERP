/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.controller
 * File              : CompanyController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyControllerService, CompanyControllerServiceImpl
 * Related Repository: CompanyControllerRepository
 * Related Entity    : CompanyController
 * Related DTO       : ApiResponse, CompanyRequest, CompanyResponse, CompanySearchRequest, PageRequest
 * Related Mapper    : CompanyControllerMapper
 * Related DB Table  : company_controllers
 * Related REST APIs : GET /api/v1/companies/{id}, GET /api/v1/companies, PUT /api/v1/companies/{id}, PATCH /api/v1/companies/{id}/activate
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Organization Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/companies/{id}, GET /api/v1/companies, PUT /api/v1/companies/{id}, PATCH /api/v1/companies/{id}/activate
 ******************************************************************************/
package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.CompanyRequest;
import com.plus33.erp.organization.dto.CompanyResponse;
import com.plus33.erp.organization.dto.CompanySearchRequest;
import com.plus33.erp.organization.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code CompanyController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CompanyService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CompanyController.endpoint()
 *   --> CompanyService.method()
 *   --> CompanyRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/companies/{id}, GET /api/v1/companies, PUT /api/v1/companies/{id}, PATCH /api/v1/companies/{id}/activate, PATCH /api/v1/companies/{id}/deactivate</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Company Management", description = "REST APIs for managing organization companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Retrieves a single company by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    @Operation(summary = "Get company by ID", description = "Retrieves details of a company by its primary key.")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        CompanyResponse response = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success("Company retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of companies records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    @Operation(summary = "Search companies", description = "Performs dynamic searches and pagination filters for companies.")
    public ResponseEntity<ApiResponse<PageResponse<CompanyResponse>>> searchCompanies(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        CompanySearchRequest searchRequest = new CompanySearchRequest(query, active);
        PageResponse<CompanyResponse> response = companyService.searchCompanies(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Companies retrieved successfully", response));
    }

    /**
     * Updates an existing company record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    @Operation(summary = "Update company details", description = "Modifies details of an active company by ID.")
    public ResponseEntity<ApiResponse<CompanyResponse>> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequest request
    ) {
        CompanyResponse response = companyService.updateCompany(id, request);
        return ResponseEntity.ok(ApiResponse.success("Company updated successfully", response));
    }

    /**
     * Performs the activateCompany operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    @Operation(summary = "Activate company", description = "Activates an inactive company.")
    public ResponseEntity<ApiResponse<CompanyResponse>> activateCompany(@PathVariable Long id) {
        CompanyResponse response = companyService.activateCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company activated successfully", response));
    }

    /**
     * Performs the deactivateCompany operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    @Operation(summary = "Deactivate company", description = "Deactivates a company.")
    public ResponseEntity<ApiResponse<CompanyResponse>> deactivateCompany(@PathVariable Long id) {
        CompanyResponse response = companyService.deactivateCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company deactivated successfully", response));
    }
}