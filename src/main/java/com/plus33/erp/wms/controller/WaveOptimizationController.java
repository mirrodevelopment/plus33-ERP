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

@RestController
@RequestMapping("/api/v1/wms/waves")
@Tag(name = "Outbound & Wave Optimization", description = "APIs for wave creation, stock allocation, and pick confirmation")
public class WaveOptimizationController {

    private final WaveOptimizationServiceImpl waveService;

    public WaveOptimizationController(WaveOptimizationServiceImpl waveService) {
        this.waveService = waveService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Create wave", description = "Creates a new wave header")
    public ResponseEntity<ApiResponse<Wave>> createWave(@RequestBody Wave wave) {
        Wave saved = waveService.createWave(wave);
        return new ResponseEntity<>(ApiResponse.success("Wave created", saved), HttpStatus.CREATED);
    }

    @PostMapping("/{waveId}/release")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Release wave", description = "Releases wave for picking")
    public ResponseEntity<ApiResponse<Wave>> releaseWave(@PathVariable Long waveId) {
        Wave released = waveService.releaseWave(waveId);
        return ResponseEntity.ok(ApiResponse.success("Wave released", released));
    }

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

    @PostMapping("/picks/{id}/confirm")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Confirm pick", description = "Confirms bin pick completion, updates stock and ledger")
    public ResponseEntity<ApiResponse<PickingWork>> confirmPick(@PathVariable Long id,
                                                                  @RequestParam BigDecimal pickedQty,
                                                                  @RequestParam Long operatorId) {
        PickingWork confirmed = waveService.confirmPick(id, pickedQty, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Pick confirmed", confirmed));
    }

    @GetMapping("/{waveId}/picks")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get open picks by wave", description = "Retrieves pending pick tasks for wave")
    public ResponseEntity<ApiResponse<List<PickingWork>>> getOpenPicks(@PathVariable Long waveId) {
        List<PickingWork> openPicks = waveService.getOpenPicksByWave(waveId);
        return ResponseEntity.ok(ApiResponse.success("Open picks retrieved", openPicks));
    }
}
