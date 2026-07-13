/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Dashboard Module
 * Package           : com.plus33.erp.dashboard.controller
 * File              : DashboardOverviewController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Dashboard Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardOverviewController
 * Related Service   : DashboardOverviewControllerService, DashboardOverviewControllerServiceImpl
 * Related Repository: DashboardOverviewControllerRepository
 * Related Entity    : DashboardOverviewController
 * Related DTO       : ApiResponse, DashboardOverviewDTO
 * Related Mapper    : DashboardOverviewControllerMapper
 * Related DB Table  : dashboard_overview_controllers
 * Related REST APIs : GET /api/v1/dashboard/overview
 * Depends On        : Common Module
 * Used By           : Dashboard Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Dashboard Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/dashboard/overview
 ******************************************************************************/
package com.plus33.erp.dashboard.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.dashboard.dto.DashboardOverviewDTO;
import com.plus33.erp.dashboard.service.DashboardOverviewService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Dashboard Module</b>
 *
 * <p><b>Class  :</b> {@code DashboardOverviewController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.dashboard.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to DashboardOverviewService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> DashboardOverviewController.endpoint()
 *   --> DashboardOverviewService.method()
 *   --> DashboardOverviewRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/dashboard/overview</p>
 * <p><b>Module Deps      :</b> Common, Dashboard</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardOverviewController {

    private final DashboardOverviewService dashboardOverviewService;

    public DashboardOverviewController(DashboardOverviewService dashboardOverviewService) {
        this.dashboardOverviewService = dashboardOverviewService;
    }

    /**
     * Retrieves overview data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW') or hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardOverviewDTO>> getOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long storeId) {
        if (from == null) {
            from = LocalDate.now().minusDays(29);
        }
        if (to == null) {
            to = LocalDate.now();
        }

        DashboardOverviewDTO data = dashboardOverviewService.getDashboardOverview(from, to, regionId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Dashboard overview loaded successfully", data));
    }
}