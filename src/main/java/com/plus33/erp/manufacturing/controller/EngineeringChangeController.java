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

@RestController
@RequestMapping("/api/v1/manufacturing/ecos")
@Tag(name = "Engineering Change Management (ECM)", description = "REST APIs for creating, approving, and implementing BOM and Routing revisions")
public class EngineeringChangeController {

    private final EngineeringChangeService engineeringChangeService;

    public EngineeringChangeController(EngineeringChangeService engineeringChangeService) {
        this.engineeringChangeService = engineeringChangeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Create Engineering Change Order (ECO)", description = "Creates a draft ECO with change items")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> createEco(@Valid @RequestBody CreateEcoRequest request) {
        EngineeringChangeOrderDto dto = engineeringChangeService.createEco(request);
        return new ResponseEntity<>(ApiResponse.success("ECO created successfully", dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get ECO by ID", description = "Retrieves details of an ECO")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> getEcoById(@PathVariable Long id) {
        EngineeringChangeOrderDto dto = engineeringChangeService.getEcoById(id);
        return ResponseEntity.ok(ApiResponse.success("ECO retrieved successfully", dto));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('BOM_MANAGE') or hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get ECOs by Company", description = "Retrieves all ECOs for a given company")
    public ResponseEntity<ApiResponse<List<EngineeringChangeOrderDto>>> getEcoByCompany(@PathVariable Long companyId) {
        List<EngineeringChangeOrderDto> dtoList = engineeringChangeService.getEcoByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("ECOs retrieved successfully", dtoList));
    }

    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Submit ECO for Review", description = "Submits a draft ECO for reviewing workflow")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> submitEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.submitEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO submitted successfully", dto));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Approve ECO", description = "Approves reviewed ECO and marks it ready for implementation")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> approveEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.approveEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO approved successfully", dto));
    }

    @PutMapping("/{id}/implement")
    @PreAuthorize("hasAuthority('BOM_MANAGE')")
    @Operation(summary = "Implement ECO", description = "Implements approved ECO and rolls revisions into active BOMs and Routings")
    public ResponseEntity<ApiResponse<EngineeringChangeOrderDto>> implementEco(@PathVariable Long id, @RequestParam Long userId) {
        EngineeringChangeOrderDto dto = engineeringChangeService.implementEco(id, userId);
        return ResponseEntity.ok(ApiResponse.success("ECO implemented successfully", dto));
    }

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
