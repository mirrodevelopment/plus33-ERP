package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.carrier.CarrierRegistry;
import com.plus33.erp.wms.entity.Carrier;
import com.plus33.erp.wms.repository.CarrierRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/wms/carriers")
@Tag(name = "Carrier Registry & TMS", description = "APIs for 3PL carrier integration, rate estimation, booking, and tracking")
public class CarrierController {

    private final CarrierRepository carrierRepo;
    private final CarrierRegistry carrierRegistry;

    public CarrierController(CarrierRepository carrierRepo, CarrierRegistry carrierRegistry) {
        this.carrierRepo = carrierRepo;
        this.carrierRegistry = carrierRegistry;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get registered carriers", description = "Retrieves active carrier records for company")
    public ResponseEntity<ApiResponse<List<Carrier>>> getCarriers(@RequestParam Long companyId) {
        List<Carrier> carriers = carrierRepo.findByCompanyIdAndActiveTrue(companyId);
        return ResponseEntity.ok(ApiResponse.success("Carriers retrieved", carriers));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create carrier", description = "Registers a new carrier profile")
    public ResponseEntity<ApiResponse<Carrier>> createCarrier(@RequestBody Carrier carrier) {
        Carrier saved = carrierRepo.save(carrier);
        return new ResponseEntity<>(ApiResponse.success("Carrier registered", saved), HttpStatus.CREATED);
    }

    @GetMapping("/providers")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "List provider keys", description = "Lists built-in carrier provider keys")
    public ResponseEntity<ApiResponse<Set<String>>> getProviderKeys() {
        return ResponseEntity.ok(ApiResponse.success("Provider keys retrieved", carrierRegistry.availableProviders()));
    }

    @PostMapping("/book")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Book shipment with carrier", description = "Books shipment and generates tracking reference")
    public ResponseEntity<ApiResponse<String>> bookShipment(@RequestParam String providerKey,
                                                             @RequestParam String accountNumber,
                                                             @RequestParam Long warehouseId,
                                                             @RequestBody Map<String, Object> details) {
        String tracking = carrierRegistry.book(providerKey, accountNumber, warehouseId, details);
        return ResponseEntity.ok(ApiResponse.success("Shipment booked", tracking));
    }

    @GetMapping("/track")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Track shipment", description = "Queries carrier API for tracking status")
    public ResponseEntity<ApiResponse<String>> trackShipment(@RequestParam String providerKey,
                                                              @RequestParam String trackingNumber,
                                                              @RequestParam String accountNumber) {
        String status = carrierRegistry.track(providerKey, trackingNumber, accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Shipment status retrieved", status));
    }

    @PostMapping("/rate-estimate")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Estimate freight rate", description = "Queries carrier for freight rate estimate")
    public ResponseEntity<ApiResponse<BigDecimal>> estimateRate(@RequestParam String providerKey,
                                                                 @RequestParam String accountNumber,
                                                                 @RequestBody Map<String, Object> request) {
        BigDecimal rate = carrierRegistry.estimateRate(providerKey, accountNumber, request);
        return ResponseEntity.ok(ApiResponse.success("Freight rate estimated", rate));
    }
}
