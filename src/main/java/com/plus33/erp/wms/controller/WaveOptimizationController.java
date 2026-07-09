/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : WaveOptimizationController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WaveOptimizationController
 * Related Service   : WaveOptimizationControllerService, WaveOptimizationControllerServiceImpl
 * Related Repository: WaveOptimizationControllerRepository
 * Related Entity    : WaveOptimizationController
 * Related DTO       : ApiResponse
 * Related Mapper    : WaveOptimizationControllerMapper
 * Related DB Table  : wave_optimization_controllers
 * Related REST APIs : POST /api/v1/wms/waves, POST /api/v1/wms/waves/{waveId}/release, POST /api/v1/wms/waves/{waveId}/picks, POST /api/v1/wms/waves/picks/{id}/confirm
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/wms/waves, POST /api/v1/wms/waves/{waveId}/release, POST /api/v1/wms/waves/{waveId}/picks, POST /api/v1/wms/waves/picks/{id}/confirm
 ******************************************************************************/
package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.entity.PickingWork;
import com.plus33.erp.wms.entity.Wave;
import com.plus33.erp.wms.service.impl.WaveOptimizationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WaveOptimizationController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to WaveOptimizationService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> WaveOptimizationController.endpoint()
 *   --> WaveOptimizationService.method()
 *   --> WaveOptimizationRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/wms/waves, POST /api/v1/wms/waves/{waveId}/release, POST /api/v1/wms/waves/{waveId}/picks, POST /api/v1/wms/waves/picks/{id}/confirm, GET /api/v1/wms/waves/{waveId}/picks</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/wms/waves")
@Tag(name = "Outbound & Wave Optimization", description = "APIs for wave creation, stock allocation, and pick confirmation")
public class WaveOptimizationController {

    private final WaveOptimizationServiceImpl waveService;

    public WaveOptimizationController(WaveOptimizationServiceImpl waveService) {
        this.waveService = waveService;
    }

    /**
     * Creates a new wave and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param wave the wave input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Create wave", description = "Creates a new wave header")
    public ResponseEntity<ApiResponse<Wave>> createWave(@RequestBody Wave wave) {
        Wave saved = waveService.createWave(wave);
        return new ResponseEntity<>(ApiResponse.success("Wave created", saved), HttpStatus.CREATED);
    }

    /**
     * Releases previously reserved wave resources back to the available pool.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param waveId the waveId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{waveId}/release")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Release wave", description = "Releases wave for picking")
    public ResponseEntity<ApiResponse<Wave>> releaseWave(@PathVariable Long waveId) {
        Wave released = waveService.releaseWave(waveId);
        return ResponseEntity.ok(ApiResponse.success("Wave released", released));
    }

    /**
     * Creates a new picking work and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{waveId}/picks")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Add picking work to wave", description = "Allocates stock and adds pick line to wave")
    public ResponseEntity<ApiResponse<PickingWork>> addPickingWork(@PathVariable Long waveId,
                                                                     @RequestParam Long companyId,
                                                                     @RequestParam String sourceType,
                                                                     @RequestParam Long sourceId,
                                                                     @RequestParam(required = false) Long sourceLineId,
                                                                     @RequestParam Long productId,
                                                                     @RequestParam(required = false) String lotNumber,
                                                                     @RequestParam(required = false) String serialNumber,
                                                                     @RequestParam BigDecimal qty,
                                                                     @RequestParam Long unitId) {
        PickingWork pw = waveService.addPickingWork(waveId, companyId, sourceType, sourceId, sourceLineId,
                productId, lotNumber, serialNumber, qty, unitId);
        return new ResponseEntity<>(ApiResponse.success("Picking work added", pw), HttpStatus.CREATED);
    }

    /**
     * Approves the pick, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/picks/{id}/confirm")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Confirm pick", description = "Confirms bin pick completion, updates stock and ledger")
    public ResponseEntity<ApiResponse<PickingWork>> confirmPick(@PathVariable Long id,
                                                                  @RequestParam BigDecimal pickedQty,
                                                                  @RequestParam Long operatorId) {
        PickingWork confirmed = waveService.confirmPick(id, pickedQty, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Pick confirmed", confirmed));
    }

    /**
     * Retrieves open picks data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param waveId the waveId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{waveId}/picks")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get open picks by wave", description = "Retrieves pending pick tasks for wave")
    public ResponseEntity<ApiResponse<List<PickingWork>>> getOpenPicks(@PathVariable Long waveId) {
        List<PickingWork> openPicks = waveService.getOpenPicksByWave(waveId);
        return ResponseEntity.ok(ApiResponse.success("Open picks retrieved", openPicks));
    }
}