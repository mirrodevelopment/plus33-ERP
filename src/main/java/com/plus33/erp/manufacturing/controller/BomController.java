/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : BomController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomController
 * Related Service   : BomControllerService, BomControllerServiceImpl
 * Related Repository: BomControllerRepository
 * Related Entity    : BomController
 * Related DTO       : ApiResponse, BomHeaderDto, CreateBomRequest
 * Related Mapper    : BomControllerMapper
 * Related DB Table  : bom_controllers
 * Related REST APIs : POST /api/v1/manufacturing/boms, GET /api/v1/manufacturing/boms/{id}, GET /api/v1/manufacturing/boms/company/{companyId}, PUT /api/v1/manufacturing/boms/{id}/approve
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/boms, GET /api/v1/manufacturing/boms/{id}, GET /api/v1/manufacturing/boms/company/{companyId}, PUT /api/v1/manufacturing/boms/{id}/approve
 ******************************************************************************/
package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.BomHeaderDto;
import com.plus33.erp.manufacturing.dto.CreateBomRequest;
import com.plus33.erp.manufacturing.service.BomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BomService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BomController.endpoint()
 *   --> BomService.method()
 *   --> BomRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/boms, GET /api/v1/manufacturing/boms/{id}, GET /api/v1/manufacturing/boms/company/{companyId}, PUT /api/v1/manufacturing/boms/{id}/approve</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/boms")
@Tag(name = "Bill of Materials Management", description = "REST APIs for managing BOM structures and components")
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    /**
     * Creates a new bom and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Bill of Materials", description = "Creates a new BOM header with component lines")
    public ResponseEntity<ApiResponse<BomHeaderDto>> createBom(@Valid @RequestBody CreateBomRequest request) {
        BomHeaderDto dto = bomService.createBom(request);
        return new ResponseEntity<>(ApiResponse.success("BOM created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single bom by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get BOM by ID", description = "Retrieves BOM structure and lines by ID")
    public ResponseEntity<ApiResponse<BomHeaderDto>> getBomById(@PathVariable Long id) {
        BomHeaderDto dto = bomService.getBomById(id);
        return ResponseEntity.ok(ApiResponse.success("BOM retrieved successfully", dto));
    }

    /**
     * Retrieves boms by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get BOMs by Company", description = "Retrieves all BOMs for a given company")
    public ResponseEntity<ApiResponse<List<BomHeaderDto>>> getBomsByCompany(@PathVariable Long companyId) {
        List<BomHeaderDto> dtoList = bomService.getBomsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("BOMs retrieved successfully", dtoList));
    }

    /**
     * Approves the bom, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param approvedBy the approvedBy input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Approve BOM", description = "Approves a BOM version for active production use")
    public ResponseEntity<ApiResponse<BomHeaderDto>> approveBom(@PathVariable Long id, @RequestParam String approvedBy) {
        BomHeaderDto dto = bomService.approveBom(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success("BOM approved successfully", dto));
    }
}