package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.MrpRunRequest;
import com.plus33.erp.manufacturing.dto.MrpSuggestionDto;
import com.plus33.erp.manufacturing.service.MrpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturing/mrp")
@Tag(name = "Material Requirements Planning (MRP)", description = "REST APIs for executing MRP engine runs and retrieving material/production supply suggestions")
public class MrpController {

    private final MrpService mrpService;

    public MrpController(MrpService mrpService) {
        this.mrpService = mrpService;
    }

    @PostMapping("/runs")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Execute MRP Calculation Run", description = "Explodes BOMs against open sales demand and inventory stock to generate purchase and work order planned suggestions")
    public ResponseEntity<ApiResponse<List<MrpSuggestionDto>>> runMrp(@Valid @RequestBody MrpRunRequest request) {
        List<MrpSuggestionDto> suggestions = mrpService.runMrp(request);
        return ResponseEntity.ok(ApiResponse.success("MRP execution completed successfully", suggestions));
    }

    @GetMapping("/suggestions/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get MRP Planned Suggestions", description = "Retrieves active planned order suggestions for a company")
    public ResponseEntity<ApiResponse<List<MrpSuggestionDto>>> getMrpSuggestionsByCompany(@PathVariable Long companyId) {
        List<MrpSuggestionDto> suggestions = mrpService.getMrpSuggestionsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("MRP suggestions retrieved successfully", suggestions));
    }
}
