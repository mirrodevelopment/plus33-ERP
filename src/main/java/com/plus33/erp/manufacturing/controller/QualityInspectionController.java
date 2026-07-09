/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : QualityInspectionController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: QualityInspectionController
 * Related Service   : QualityInspectionControllerService, QualityInspectionControllerServiceImpl
 * Related Repository: QualityInspectionControllerRepository
 * Related Entity    : QualityInspectionController
 * Related DTO       : ApiResponse, CreateQualityInspectionRequest, QualityInspectionDto, RecordInspectionResultRequest
 * Related Mapper    : QualityInspectionControllerMapper
 * Related DB Table  : quality_inspection_controllers
 * Related REST APIs : POST /api/v1/manufacturing/quality/inspections, GET /api/v1/manufacturing/quality/inspections/{id}, GET /api/v1/manufacturing/quality/inspections/production-order/{poId}, GET /api/v1/manufacturing/quality/inspections/range
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/quality/inspections, GET /api/v1/manufacturing/quality/inspections/{id}, GET /api/v1/manufacturing/quality/inspections/production-order/{poId}, GET /api/v1/manufacturing/quality/inspections/range
 ******************************************************************************/
package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.service.QualityInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code QualityInspectionController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to QualityInspectionService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> QualityInspectionController.endpoint()
 *   --> QualityInspectionService.method()
 *   --> QualityInspectionRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/quality/inspections, GET /api/v1/manufacturing/quality/inspections/{id}, GET /api/v1/manufacturing/quality/inspections/production-order/{poId}, GET /api/v1/manufacturing/quality/inspections/range, GET /api/v1/manufacturing/quality/inspections/pending</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/quality/inspections")
@Tag(name = "Quality Inspection Management", description = "REST APIs for managing shop floor and incoming quality inspections")
public class QualityInspectionController {

    private final QualityInspectionService qualityInspectionService;

    public QualityInspectionController(QualityInspectionService qualityInspectionService) {
        this.qualityInspectionService = qualityInspectionService;
    }

    /**
     * Creates a new inspection and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCTION_EXECUTE') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Create Quality Inspection", description = "Creates a quality inspection log for a product lot/serial or production order")
    public ResponseEntity<ApiResponse<QualityInspectionDto>> createInspection(@Valid @RequestBody CreateQualityInspectionRequest request) {
        QualityInspectionDto dto = qualityInspectionService.createInspection(request);
        return new ResponseEntity<>(ApiResponse.success("Quality inspection log created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single inspection by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Quality Inspection by ID", description = "Retrieves details of a quality inspection log")
    public ResponseEntity<ApiResponse<QualityInspectionDto>> getInspectionById(@RequestParam Long companyId, @PathVariable Long id) {
        QualityInspectionDto dto = qualityInspectionService.getInspectionById(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Quality inspection retrieved successfully", dto));
    }

    /**
     * Retrieves inspections by production order data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param poId the poId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/production-order/{poId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Quality Inspections by Production Order ID", description = "Retrieves all quality inspection logs associated with a production order")
    public ResponseEntity<ApiResponse<List<QualityInspectionDto>>> getInspectionsByProductionOrder(@RequestParam Long companyId, @PathVariable Long poId) {
        List<QualityInspectionDto> dtoList = qualityInspectionService.getInspectionsByProductionOrder(companyId, poId);
        return ResponseEntity.ok(ApiResponse.success("Quality inspections retrieved successfully", dtoList));
    }

    /**
     * Retrieves inspections by date range data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/range")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Quality Inspections by Date Range", description = "Retrieves quality inspection logs within a specified date range")
    public ResponseEntity<ApiResponse<List<QualityInspectionDto>>> getInspectionsByDateRange(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<QualityInspectionDto> dtoList = qualityInspectionService.getInspectionsByDateRange(companyId, from, to);
        return ResponseEntity.ok(ApiResponse.success("Quality inspections retrieved successfully", dtoList));
    }

    /**
     * Retrieves pending inspections data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Pending Quality Inspections", description = "Retrieves all pending quality inspections for a company")
    public ResponseEntity<ApiResponse<List<QualityInspectionDto>>> getPendingInspections(@RequestParam Long companyId) {
        List<QualityInspectionDto> dtoList = qualityInspectionService.getPendingInspections(companyId);
        return ResponseEntity.ok(ApiResponse.success("Pending quality inspections retrieved successfully", dtoList));
    }

    /**
     * Performs the recordInspectionResult operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PutMapping("/{id}/record-result")
    @PreAuthorize("hasAuthority('PRODUCTION_EXECUTE')")
    @Operation(summary = "Record Quality Inspection Results", description = "Records actual inspected, passed, and failed quantities")
    public ResponseEntity<ApiResponse<QualityInspectionDto>> recordInspectionResult(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @Valid @RequestBody RecordInspectionResultRequest request) {
        QualityInspectionDto dto = qualityInspectionService.recordInspectionResult(companyId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Quality inspection results recorded successfully", dto));
    }

    /**
     * Approves the inspection, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('QUALITY_APPROVE')")
    @Operation(summary = "Approve Quality Inspection", description = "Approves the quality inspection and clears any holds on the associated production order")
    public ResponseEntity<ApiResponse<QualityInspectionDto>> approveInspection(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestParam Long approvedBy,
            @RequestParam(required = false) String notes) {
        QualityInspectionDto dto = qualityInspectionService.approveInspection(companyId, id, approvedBy, notes);
        return ResponseEntity.ok(ApiResponse.success("Quality inspection approved successfully", dto));
    }

    /**
     * Performs the rejectInspection operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('QUALITY_APPROVE')")
    @Operation(summary = "Reject Quality Inspection", description = "Rejects the quality inspection, putting the associated production order on Hold, Rework, or Scrap status")
    public ResponseEntity<ApiResponse<QualityInspectionDto>> rejectInspection(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestParam Long rejectedBy,
            @RequestParam String defectCode,
            @RequestParam String defectDescription,
            @RequestParam String disposition) {
        QualityInspectionDto dto = qualityInspectionService.rejectInspection(companyId, id, rejectedBy, defectCode, defectDescription, disposition);
        return ResponseEntity.ok(ApiResponse.success("Quality inspection rejected successfully", dto));
    }

    /**
     * Retrieves rejected inspections data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/rejected")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Rejected Quality Inspections", description = "Retrieves all rejected quality inspections")
    public ResponseEntity<ApiResponse<List<QualityInspectionDto>>> getRejectedInspections(@RequestParam Long companyId) {
        List<QualityInspectionDto> dtoList = qualityInspectionService.getRejectedInspections(companyId);
        return ResponseEntity.ok(ApiResponse.success("Rejected quality inspections retrieved successfully", dtoList));
    }
}