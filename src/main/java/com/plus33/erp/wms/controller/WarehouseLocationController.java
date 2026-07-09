/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : WarehouseLocationController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLocationController
 * Related Service   : WarehouseLocationControllerService, WarehouseLocationControllerServiceImpl
 * Related Repository: WarehouseLocationRepository, WarehouseZoneRepository
 * Related Entity    : WarehouseLocationController
 * Related DTO       : ApiResponse
 * Related Mapper    : WarehouseLocationControllerMapper
 * Related DB Table  : warehouse_location_controllers
 * Related REST APIs : GET /api/v1/wms/locations/zones, POST /api/v1/wms/locations/zones, GET /api/v1/wms/locations, POST /api/v1/wms/locations
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/wms/locations/zones, POST /api/v1/wms/locations/zones, GET /api/v1/wms/locations, POST /api/v1/wms/locations
 ******************************************************************************/
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
import java.util.Map;
import java.util.HashMap;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLocationController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to WarehouseLocationService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> WarehouseLocationController.endpoint()
 *   --> WarehouseLocationService.method()
 *   --> WarehouseLocationRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/wms/locations/zones, POST /api/v1/wms/locations/zones, GET /api/v1/wms/locations, POST /api/v1/wms/locations, GET /api/v1/wms/locations/wms/stock-ledger</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/wms/locations")
@Tag(name = "Warehouse Location Management", description = "APIs for managing warehouse zones and bin locations")
public class WarehouseLocationController {

    private final WarehouseLocationRepository locationRepo;
    private final WarehouseZoneRepository zoneRepo;
    private final org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    public WarehouseLocationController(WarehouseLocationRepository locationRepo,
                                       WarehouseZoneRepository zoneRepo,
                                       org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        this.locationRepo = locationRepo;
        this.zoneRepo = zoneRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves zones data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/zones")
    @PreAuthorize("hasAnyAuthority('WMS_VIEW', 'WMS_ADMIN', 'INVENTORY_VIEW')")
    @Operation(summary = "Get zones by warehouse", description = "Retrieves active warehouse zones")
    public ResponseEntity<ApiResponse<List<WarehouseZone>>> getZones(@RequestParam Long companyId,
                                                                      @RequestParam Long warehouseId) {
        List<WarehouseZone> zones = zoneRepo.findByCompanyIdAndWarehouseIdAndActiveTrue(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Zones retrieved", zones));
    }

    /**
     * Creates a new zone and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param zone the zone input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/zones")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create warehouse zone", description = "Creates a new warehouse zone")
    public ResponseEntity<ApiResponse<WarehouseZone>> createZone(@RequestBody WarehouseZone zone) {
        WarehouseZone saved = zoneRepo.save(zone);
        return new ResponseEntity<>(ApiResponse.success("Zone created", saved), HttpStatus.CREATED);
    }

    /**
     * Retrieves locations data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('WMS_VIEW', 'WMS_ADMIN', 'INVENTORY_VIEW')")
    @Operation(summary = "Get locations by warehouse", description = "Retrieves active warehouse locations")
    public ResponseEntity<ApiResponse<List<WarehouseLocation>>> getLocations(@RequestParam Long companyId,
                                                                              @RequestParam Long warehouseId) {
        List<WarehouseLocation> locations = locationRepo.findByCompanyIdAndWarehouseIdAndActiveTrue(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Locations retrieved", locations));
    }

    /**
     * Creates a new location and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param location the location input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create location", description = "Creates a new warehouse bin location")
    public ResponseEntity<ApiResponse<WarehouseLocation>> createLocation(@RequestBody WarehouseLocation location) {
        WarehouseLocation saved = locationRepo.save(location);
        return new ResponseEntity<>(ApiResponse.success("Location created", saved), HttpStatus.CREATED);
    }

    /**
     * Retrieves stock ledger data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/wms/stock-ledger")
    @PreAuthorize("hasAnyAuthority('WMS_VIEW', 'WMS_ADMIN', 'INVENTORY_VIEW')")
    @Operation(summary = "Get WMS Stock Ledger", description = "Retrieves live WMS stock balance records")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStockLedger() {
        String sql = "SELECT ls.id, p.name, pc.name AS category, ls.quantity AS qty, " +
                     "ls.reserved_quantity AS reserved, uom.code AS uom, ls.lot_number AS lot, " +
                     "ls.expiry_date AS expiry " +
                     "FROM location_stock ls " +
                     "JOIN products p ON p.id = ls.product_id " +
                     "JOIN product_categories pc ON pc.id = p.category_id " +
                     "JOIN units_of_measure uom ON uom.id = p.unit_id " +
                     "ORDER BY ls.id ASC";
        
        List<Map<String, Object>> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", rs.getLong("id"));
            map.put("name", rs.getString("name"));
            map.put("category", rs.getString("category"));
            map.put("qty", rs.getBigDecimal("qty"));
            map.put("reserved", rs.getBigDecimal("reserved"));
            map.put("uom", rs.getString("uom"));
            map.put("lot", rs.getString("lot"));
            map.put("expiry", rs.getDate("expiry") != null ? rs.getDate("expiry").toString() : "");
            return map;
        });
        
        return ResponseEntity.ok(ApiResponse.success("Stock ledger retrieved successfully", list));
    }
}