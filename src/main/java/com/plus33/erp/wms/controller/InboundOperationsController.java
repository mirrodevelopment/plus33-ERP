/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : InboundOperationsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InboundOperationsController
 * Related Service   : InboundOperationsControllerService, InboundOperationsControllerServiceImpl
 * Related Repository: InboundOperationsControllerRepository
 * Related Entity    : InboundOperationsController
 * Related DTO       : ApiResponse
 * Related Mapper    : InboundOperationsControllerMapper
 * Related DB Table  : inbound_operations_controllers
 * Related REST APIs : POST /api/v1/wms/inbound/asn, POST /api/v1/wms/inbound/asn/{asnId}/check-in, POST /api/v1/wms/inbound/asn/{asnId}/put-away-tasks, POST /api/v1/wms/inbound/put-away-tasks/{id}/complete
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/wms/inbound/asn, POST /api/v1/wms/inbound/asn/{asnId}/check-in, POST /api/v1/wms/inbound/asn/{asnId}/put-away-tasks, POST /api/v1/wms/inbound/put-away-tasks/{id}/complete
 ******************************************************************************/
package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.entity.AdvanceShippingNotice;
import com.plus33.erp.wms.entity.PutAwayWork;
import com.plus33.erp.wms.service.impl.InboundOperationsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code InboundOperationsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to InboundOperationsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> InboundOperationsController.endpoint()
 *   --> InboundOperationsService.method()
 *   --> InboundOperationsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/wms/inbound/asn, POST /api/v1/wms/inbound/asn/{asnId}/check-in, POST /api/v1/wms/inbound/asn/{asnId}/put-away-tasks, POST /api/v1/wms/inbound/put-away-tasks/{id}/complete, GET /api/v1/wms/inbound/asn</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/wms/inbound")
@Tag(name = "Inbound Operations", description = "APIs for ASNs, gate check-in, and directed put-away")
public class InboundOperationsController {

    private final InboundOperationsServiceImpl inboundService;

    public InboundOperationsController(InboundOperationsServiceImpl inboundService) {
        this.inboundService = inboundService;
    }

    /**
     * Creates a new asn and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param asn the asn input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/asn")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Create ASN", description = "Creates a new advance shipping notice")
    public ResponseEntity<ApiResponse<AdvanceShippingNotice>> createAsn(@RequestBody AdvanceShippingNotice asn) {
        AdvanceShippingNotice saved = inboundService.createAsn(asn);
        return new ResponseEntity<>(ApiResponse.success("ASN created", saved), HttpStatus.CREATED);
    }

    /**
     * Validates business rules and constraints for in.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/asn/{asnId}/check-in")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Gate check-in", description = "Records vehicle arrival and assigns dock door")
    public ResponseEntity<ApiResponse<AdvanceShippingNotice>> checkIn(@PathVariable Long asnId,
                                                                        @RequestParam(required = false) Long dockDoorId,
                                                                        @RequestParam(required = false) Long checkinId) {
        AdvanceShippingNotice checkedIn = inboundService.checkIn(asnId, dockDoorId, checkinId);
        return ResponseEntity.ok(ApiResponse.success("ASN checked in", checkedIn));
    }

    /**
     * Generates the put away tasks based on input parameters and business rules.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @PostMapping("/asn/{asnId}/put-away-tasks")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Generate put-away tasks", description = "Triggers directed put-away using chosen strategy")
    public ResponseEntity<ApiResponse<List<PutAwayWork>>> generatePutAwayTasks(@PathVariable Long asnId,
                                                                                 @RequestParam(defaultValue = "NEAREST_EMPTY_BIN") String strategyKey,
                                                                                 @RequestParam Long stagingLocationId,
                                                                                 @RequestParam Long receivedByUserId) {
        List<PutAwayWork> tasks = inboundService.generatePutAwayTasks(asnId, strategyKey, stagingLocationId, receivedByUserId);
        return ResponseEntity.ok(ApiResponse.success("Put-away tasks generated", tasks));
    }

    /**
     * Completes the put away workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/put-away-tasks/{id}/complete")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Complete put-away", description = "Confirms bin put-away and records movement")
    public ResponseEntity<ApiResponse<PutAwayWork>> completePutAway(@PathVariable Long id,
                                                                      @RequestParam Long operatorId) {
        PutAwayWork completed = inboundService.completePutAway(id, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Put-away task completed", completed));
    }

    /**
     * Returns a filtered paginated list of asns records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @GetMapping("/asn")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "List ASNs", description = "Finds ASNs by warehouse and status")
    public ResponseEntity<ApiResponse<List<AdvanceShippingNotice>>> listAsns(@RequestParam Long companyId,
                                                                               @RequestParam Long warehouseId,
                                                                               @RequestParam String status) {
        List<AdvanceShippingNotice> list = inboundService.findByWarehouseAndStatus(companyId, warehouseId, status);
        return ResponseEntity.ok(ApiResponse.success("ASNs retrieved", list));
    }
}