/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : MrpController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpController
 * Related Service   : MrpControllerService, MrpControllerServiceImpl
 * Related Repository: MrpControllerRepository
 * Related Entity    : MrpController
 * Related DTO       : ApiResponse, MrpRunRequest, MrpSuggestionDto
 * Related Mapper    : MrpControllerMapper
 * Related DB Table  : mrp_controllers
 * Related REST APIs : POST /api/v1/manufacturing/mrp/runs, GET /api/v1/manufacturing/mrp/suggestions/company/{companyId}
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/mrp/runs, GET /api/v1/manufacturing/mrp/suggestions/company/{companyId}
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to MrpService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> MrpController.endpoint()
 *   --> MrpService.method()
 *   --> MrpRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/mrp/runs, GET /api/v1/manufacturing/mrp/suggestions/company/{companyId}</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/mrp")
@Tag(name = "Material Requirements Planning (MRP)", description = "REST APIs for executing MRP engine runs and retrieving material/production supply suggestions")
public class MrpController {

    private final MrpService mrpService;

    public MrpController(MrpService mrpService) {
        this.mrpService = mrpService;
    }

    /**
     * Performs the runMrp operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @PostMapping("/runs")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Execute MRP Calculation Run", description = "Explodes BOMs against open sales demand and inventory stock to generate purchase and work order planned suggestions")
    public ResponseEntity<ApiResponse<List<MrpSuggestionDto>>> runMrp(@Valid @RequestBody MrpRunRequest request) {
        List<MrpSuggestionDto> suggestions = mrpService.runMrp(request);
        return ResponseEntity.ok(ApiResponse.success("MRP execution completed successfully", suggestions));
    }

    /**
     * Retrieves mrp suggestions by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/suggestions/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get MRP Planned Suggestions", description = "Retrieves active planned order suggestions for a company")
    public ResponseEntity<ApiResponse<List<MrpSuggestionDto>>> getMrpSuggestionsByCompany(@PathVariable Long companyId) {
        List<MrpSuggestionDto> suggestions = mrpService.getMrpSuggestionsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("MRP suggestions retrieved successfully", suggestions));
    }
}