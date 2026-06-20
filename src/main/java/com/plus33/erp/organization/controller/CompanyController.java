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

@RestController
@RequestMapping("/api/v1/companies")
@Tag(name = "Company Management", description = "REST APIs for managing organization companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    @Operation(summary = "Get company by ID", description = "Retrieves details of a company by its primary key.")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompanyById(@PathVariable Long id) {
        CompanyResponse response = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success("Company retrieved successfully", response));
    }

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

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    @Operation(summary = "Activate company", description = "Activates an inactive company.")
    public ResponseEntity<ApiResponse<CompanyResponse>> activateCompany(@PathVariable Long id) {
        CompanyResponse response = companyService.activateCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company activated successfully", response));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    @Operation(summary = "Deactivate company", description = "Deactivates a company.")
    public ResponseEntity<ApiResponse<CompanyResponse>> deactivateCompany(@PathVariable Long id) {
        CompanyResponse response = companyService.deactivateCompany(id);
        return ResponseEntity.ok(ApiResponse.success("Company deactivated successfully", response));
    }
}
