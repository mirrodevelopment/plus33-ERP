/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : EngineeringChangeController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EngineeringChangeController
 * Related Service   : EngineeringChangeControllerService, EngineeringChangeControllerServiceImpl
 * Related Repository: EngineeringChangeControllerRepository
 * Related Entity    : EngineeringChangeController
 * Related DTO       : ApiResponse, CreateEcoRequest, EngineeringChangeOrderDto
 * Related Mapper    : EngineeringChangeControllerMapper
 * Related DB Table  : engineering_change_controllers
 * Related REST APIs : POST /api/v1/manufacturing/ecos, GET /api/v1/manufacturing/ecos/{id}, GET /api/v1/manufacturing/ecos/company/{companyId}, PUT /api/v1/manufacturing/ecos/{id}/submit
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/ecos, GET /api/v1/manufacturing/ecos/{id}, GET /api/v1/manufacturing/ecos/company/{companyId}, PUT /api/v1/manufacturing/ecos/{id}/submit
 ******************************************************************************/
package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.service.EngineeringChangeService;
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
 * <p><b>Class  :</b> {@code EngineeringChangeController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EngineeringChangeService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EngineeringChangeController.endpoint()
 *   --> EngineeringChangeService.method()
 *   --> EngineeringChangeRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/ecos, GET /api/v1/manufacturing/ecos/{id}, GET /api/v1/manufacturing/ecos/company/{companyId}, PUT /api/v1/manufacturing/ecos/{id}/submit, PUT /api/v1/manufacturing/ecos/{id}/approve</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/ecos")
@Tag(name = "Engineering Change Management (ECM)", description = "REST APIs for creating, approving, and implementing BOM and Routing revisions")
public class EngineeringChangeController {

    private final EngineeringChangeService engineeringChangeService;

    public EngineeringChangeController(EngineeringChangeService engineeringChangeService) {
        this.engineeringChangeService = engineeringChangeService;
    }

    /**
     * Creates a new eco and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Create Engineering Change Order (ECO)", description = "Creates a draft ECO with change items")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> createEco(@Valid @RequestBody CreateEcoRequest request) {
        EngineeringChangeOrderDto dto = engineeringChangeService.createEco(request);
        return new ResponseEntity<>(ApiResponse.success("ECO created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single eco by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get ECO by ID", description = "Retrieves details of an ECO")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> getEcoById(@PathVariable Long id) {
        EngineeringChangeOrderDto dto = engineeringChangeService.getEcoById(id);
        return ResponseEntity.ok(ApiResponse.success("ECO retrieved successfully", dto));
    }

    /**
     * Retrieves eco by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get ECOs by Company", description = "Retrieves all ECOs for a given company")
    public ResponseEntity<ApiResponse<List<EngineeringChangeOrderDto>>> getEcoByCompany(@PathVariable Long companyId) {
        List<EngineeringChangeOrderDto> dtoList = engineeringChangeService.getEcoByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("ECOs retrieved successfully", dtoList));
    }

    /**
     * Submits the eco for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param userId authenticated user identifier
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Submit ECO for Review", description = "Submits a draft ECO for reviewing workflow")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> submitEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.submitEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO submitted successfully", dto));
    }

    /**
     * Approves the eco, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param userId authenticated user identifier
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Approve ECO", description = "Approves reviewed ECO and marks it ready for implementation")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> approveEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.approveEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO approved successfully", dto));
    }

    /**
     * Performs the implementEco operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param userId authenticated user identifier
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PutMapping("/{id}/implement")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Implement ECO", description = "Implements approved ECO and rolls revisions into active BOMs and Routings")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> implementEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.implementEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO implemented successfully", dto));
    }

    /**
     * Cancels the eco and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Cancel ECO", description = "Cancels a draft/review ECO and logs cancellation reason")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> cancelEco(
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.cancelEco(id, reason, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO cancelled successfully", dto));
    }
}