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

@RestController
@RequestMapping("/api/v1/manufacturing/boms")
@Tag(name = "Bill of Materials Management", description = "REST APIs for managing BOM structures and components")
public class BomController {

    private final BomService bomService;

    public BomController(BomService bomService) {
        this.bomService = bomService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Bill of Materials", description = "Creates a new BOM header with component lines")
    public ResponseEntity<ApiResponse<BomHeaderDto>> createBom(@Valid @RequestBody CreateBomRequest request) {
        BomHeaderDto dto = bomService.createBom(request);
        return new ResponseEntity<>(ApiResponse.success("BOM created successfully", dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get BOM by ID", description = "Retrieves BOM structure and lines by ID")
    public ResponseEntity<ApiResponse<BomHeaderDto>> getBomById(@PathVariable Long id) {
        BomHeaderDto dto = bomService.getBomById(id);
        return ResponseEntity.ok(ApiResponse.success("BOM retrieved successfully", dto));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get BOMs by Company", description = "Retrieves all BOMs for a given company")
    public ResponseEntity<ApiResponse<List<BomHeaderDto>>> getBomsByCompany(@PathVariable Long companyId) {
        List<BomHeaderDto> dtoList = bomService.getBomsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("BOMs retrieved successfully", dtoList));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Approve BOM", description = "Approves a BOM version for active production use")
    public ResponseEntity<ApiResponse<BomHeaderDto>> approveBom(@PathVariable Long id, @RequestParam String approvedBy) {
        BomHeaderDto dto = bomService.approveBom(id, approvedBy);
        return ResponseEntity.ok(ApiResponse.success("BOM approved successfully", dto));
    }
}
