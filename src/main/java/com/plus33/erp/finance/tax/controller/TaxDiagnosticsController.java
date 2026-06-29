package com.plus33.erp.finance.tax.controller;

import com.plus33.erp.finance.tax.service.TaxMetricsRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tax/diagnostics")
@RequiredArgsConstructor
public class TaxDiagnosticsController {

    private final TaxMetricsRegistry metricsRegistry;

    @GetMapping("/metrics")
    @PreAuthorize("hasAuthority('TAX_VIEW')")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(metricsRegistry.getDiagnostics());
    }
}
