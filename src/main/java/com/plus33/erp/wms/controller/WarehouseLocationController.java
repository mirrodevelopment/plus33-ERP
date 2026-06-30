package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.entity.WarehouseLocation;
import com.plus33.erp.wms.entity.WarehouseZone;
import com.plus33.erp.wms.repository.WarehouseLocationRepository;
import com.plus33.erp.wms.repository.WarehouseZoneRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wms/locations")
@Tag(name = "Warehouse Location Management", description = "APIs for managing warehouse zones and bin locations")
public class WarehouseLocationController {

    private final WarehouseLocationRepository locationRepo;
    private final WarehouseZoneRepository zoneRepo;

    public WarehouseLocationController(WarehouseLocationRepository locationRepo,
                                       WarehouseZoneRepository zoneRepo) {
        this.locationRepo = locationRepo;
        this.zoneRepo = zoneRepo;
    }

    @GetMapping("/zones")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get zones by warehouse", description = "Retrieves active warehouse zones")
    public ResponseEntity<ApiResponse<List<WarehouseZone>>> getZones(@RequestParam Long companyId,
                                                                      @RequestParam Long warehouseId) {
        List<WarehouseZone> zones = zoneRepo.findByCompanyIdAndWarehouseIdAndActiveTrue(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Zones retrieved", zones));
    }

    @PostMapping("/zones")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create warehouse zone", description = "Creates a new warehouse zone")
    public ResponseEntity<ApiResponse<WarehouseZone>> createZone(@RequestBody WarehouseZone zone) {
        WarehouseZone saved = zoneRepo.save(zone);
        return new ResponseEntity<>(ApiResponse.success("Zone created", saved), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get locations by warehouse", description = "Retrieves active warehouse locations")
    public ResponseEntity<ApiResponse<List<WarehouseLocation>>> getLocations(@RequestParam Long companyId,
                                                                              @RequestParam Long warehouseId) {
        List<WarehouseLocation> locations = locationRepo.findByCompanyIdAndWarehouseIdAndActiveTrue(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Locations retrieved", locations));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create location", description = "Creates a new warehouse bin location")
    public ResponseEntity<ApiResponse<WarehouseLocation>> createLocation(@RequestBody WarehouseLocation location) {
        WarehouseLocation saved = locationRepo.save(location);
        return new ResponseEntity<>(ApiResponse.success("Location created", saved), HttpStatus.CREATED);
    }
}
