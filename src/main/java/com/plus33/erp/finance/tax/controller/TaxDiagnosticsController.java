/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.controller
 * File              : TaxDiagnosticsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxDiagnosticsController
 * Related Service   : TaxDiagnosticsControllerService, TaxDiagnosticsControllerServiceImpl
 * Related Repository: TaxDiagnosticsControllerRepository
 * Related Entity    : TaxDiagnosticsController
 * Related DTO       : N/A
 * Related Mapper    : TaxDiagnosticsControllerMapper
 * Related DB Table  : tax_diagnostics_controllers
 * Related REST APIs : GET /api/v1/tax/diagnostics/metrics
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/tax/diagnostics/metrics
 ******************************************************************************/
package com.plus33.erp.finance.tax.controller;

import com.plus33.erp.finance.tax.service.TaxMetricsRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxDiagnosticsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to TaxDiagnosticsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> TaxDiagnosticsController.endpoint()
 *   --> TaxDiagnosticsService.method()
 *   --> TaxDiagnosticsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/tax/diagnostics/metrics</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/tax/diagnostics")
@RequiredArgsConstructor
public class TaxDiagnosticsController {

    private final TaxMetricsRegistry metricsRegistry;

    /**
     * Retrieves metrics data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/metrics")
    @PreAuthorize("hasAuthority('TAX_VIEW')")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(metricsRegistry.getDiagnostics());
    }
}