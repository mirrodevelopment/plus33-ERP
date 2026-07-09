/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : WorkCenterController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkCenterController
 * Related Service   : WorkCenterControllerService, WorkCenterControllerServiceImpl
 * Related Repository: WorkCenterControllerRepository
 * Related Entity    : WorkCenterController
 * Related DTO       : ApiResponse, CreateWorkCenterRequest, WorkCenterDto
 * Related Mapper    : WorkCenterControllerMapper
 * Related DB Table  : work_center_controllers
 * Related REST APIs : POST /api/v1/manufacturing/work-centers, GET /api/v1/manufacturing/work-centers/{id}, GET /api/v1/manufacturing/work-centers/company/{companyId}
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/work-centers, GET /api/v1/manufacturing/work-centers/{id}, GET /api/v1/manufacturing/work-centers/company/{companyId}
 ******************************************************************************/
package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.CreateWorkCenterRequest;
import com.plus33.erp.manufacturing.dto.WorkCenterDto;
import com.plus33.erp.manufacturing.service.WorkCenterService;
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
 * <p><b>Class  :</b> {@code WorkCenterController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to WorkCenterService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> WorkCenterController.endpoint()
 *   --> WorkCenterService.method()
 *   --> WorkCenterRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/work-centers, GET /api/v1/manufacturing/work-centers/{id}, GET /api/v1/manufacturing/work-centers/company/{companyId}</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/work-centers")
@Tag(name = "Work Center Management", description = "REST APIs for managing manufacturing plant work centers and capacities")
public class WorkCenterController {

    private final WorkCenterService workCenterService;

    public WorkCenterController(WorkCenterService workCenterService) {
        this.workCenterService = workCenterService;
    }

    /**
     * Creates a new work center and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Work Center", description = "Creates a new plant work center with hourly rates and capacity")
    public ResponseEntity<ApiResponse<WorkCenterDto>> createWorkCenter(@Valid @RequestBody CreateWorkCenterRequest request) {
        WorkCenterDto dto = workCenterService.createWorkCenter(request);
        return new ResponseEntity<>(ApiResponse.success("Work center created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single work center by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Work Center by ID", description = "Retrieves work center details by ID")
    public ResponseEntity<ApiResponse<WorkCenterDto>> getWorkCenterById(@PathVariable Long id) {
        WorkCenterDto dto = workCenterService.getWorkCenterById(id);
        return ResponseEntity.ok(ApiResponse.success("Work center retrieved successfully", dto));
    }

    /**
     * Retrieves work centers by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Work Centers by Company", description = "Retrieves all work centers for a given company")
    public ResponseEntity<ApiResponse<List<WorkCenterDto>>> getWorkCentersByCompany(@PathVariable Long companyId) {
        List<WorkCenterDto> dtoList = workCenterService.getWorkCentersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Work centers retrieved successfully", dtoList));
    }
}