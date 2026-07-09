/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : CarrierController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarrierController
 * Related Service   : CarrierControllerService, CarrierControllerServiceImpl
 * Related Repository: CarrierRepository
 * Related Entity    : CarrierController
 * Related DTO       : ApiResponse
 * Related Mapper    : CarrierControllerMapper
 * Related DB Table  : carrier_controllers
 * Related REST APIs : GET /api/v1/wms/carriers, POST /api/v1/wms/carriers, GET /api/v1/wms/carriers/providers, POST /api/v1/wms/carriers/book
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/wms/carriers, POST /api/v1/wms/carriers, GET /api/v1/wms/carriers/providers, POST /api/v1/wms/carriers/book
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CarrierController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CarrierService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CarrierController.endpoint()
 *   --> CarrierService.method()
 *   --> CarrierRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/wms/carriers, POST /api/v1/wms/carriers, GET /api/v1/wms/carriers/providers, POST /api/v1/wms/carriers/book, GET /api/v1/wms/carriers/track</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves carriers data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get registered carriers", description = "Retrieves active carrier records for company")
    public ResponseEntity<ApiResponse<List<Carrier>>> getCarriers(@RequestParam Long companyId) {
        List<Carrier> carriers = carrierRepo.findByCompanyIdAndActiveTrue(companyId);
        return ResponseEntity.ok(ApiResponse.success("Carriers retrieved", carriers));
    }

    /**
     * Creates a new carrier and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param carrier the carrier input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create carrier", description = "Registers a new carrier profile")
    public ResponseEntity<ApiResponse<Carrier>> createCarrier(@RequestBody Carrier carrier) {
        Carrier saved = carrierRepo.save(carrier);
        return new ResponseEntity<>(ApiResponse.success("Carrier registered", saved), HttpStatus.CREATED);
    }

    /**
     * Retrieves provider keys data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/providers")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "List provider keys", description = "Lists built-in carrier provider keys")
    public ResponseEntity<ApiResponse<Set<String>>> getProviderKeys() {
        return ResponseEntity.ok(ApiResponse.success("Provider keys retrieved", carrierRegistry.availableProviders()));
    }

    /**
     * Performs the bookShipment operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     */
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

    /**
     * Performs the trackShipment operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data the result string value
     */
    @GetMapping("/track")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Track shipment", description = "Queries carrier API for tracking status")
    public ResponseEntity<ApiResponse<String>> trackShipment(@RequestParam String providerKey,
                                                              @RequestParam String trackingNumber,
                                                              @RequestParam String accountNumber) {
        String status = carrierRegistry.track(providerKey, trackingNumber, accountNumber);
        return ResponseEntity.ok(ApiResponse.success("Shipment status retrieved", status));
    }

    /**
     * Performs the estimateRate operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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