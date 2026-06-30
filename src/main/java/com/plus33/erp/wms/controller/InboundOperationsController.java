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

@RestController
@RequestMapping("/api/v1/wms/inbound")
@Tag(name = "Inbound Operations", description = "APIs for ASNs, gate check-in, and directed put-away")
public class InboundOperationsController {

    private final InboundOperationsServiceImpl inboundService;

    public InboundOperationsController(InboundOperationsServiceImpl inboundService) {
        this.inboundService = inboundService;
    }

    @PostMapping("/asn")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Create ASN", description = "Creates a new advance shipping notice")
    public ResponseEntity<ApiResponse<AdvanceShippingNotice>> createAsn(@RequestBody AdvanceShippingNotice asn) {
        AdvanceShippingNotice saved = inboundService.createAsn(asn);
        return new ResponseEntity<>(ApiResponse.success("ASN created", saved), HttpStatus.CREATED);
    }

    @PostMapping("/asn/{asnId}/check-in")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Gate check-in", description = "Records vehicle arrival and assigns dock door")
    public ResponseEntity<ApiResponse<AdvanceShippingNotice>> checkIn(@PathVariable Long asnId,
                                                                        @RequestParam(required = false) Long dockDoorId,
                                                                        @RequestParam(required = false) Long checkinId) {
        AdvanceShippingNotice checkedIn = inboundService.checkIn(asnId, dockDoorId, checkinId);
        return ResponseEntity.ok(ApiResponse.success("ASN checked in", checkedIn));
    }

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

    @PostMapping("/put-away-tasks/{id}/complete")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Complete put-away", description = "Confirms bin put-away and records movement")
    public ResponseEntity<ApiResponse<PutAwayWork>> completePutAway(@PathVariable Long id,
                                                                      @RequestParam Long operatorId) {
        PutAwayWork completed = inboundService.completePutAway(id, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Put-away task completed", completed));
    }

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
