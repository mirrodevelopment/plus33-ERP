package com.plus33.erp.finance.assets.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.assets.dto.*;
import com.plus33.erp.finance.assets.service.FixedAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fixed-assets")
@RequiredArgsConstructor
@Tag(name = "Fixed Asset Management", description = "REST APIs for Fixed Asset categorization, acquisition, assignments, transfers, maintenance, physical audits, and depreciation")
public class FixedAssetController {

    private final FixedAssetService fixedAssetService;

    @PostMapping("/categories")
    @PreAuthorize("hasAuthority('FIXED_ASSET_CREATE')")
    @Operation(summary = "Create Asset Category", description = "Create a new fixed asset category with configured GL account codes.")
    public ResponseEntity<ApiResponse<AssetCategoryResponse>> createCategory(
            @RequestParam Long companyId,
            @RequestBody AssetCategoryRequest request
    ) {
        AssetCategoryResponse response = fixedAssetService.createCategory(companyId, request);
        return ResponseEntity.ok(ApiResponse.success("Asset category created successfully", response));
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Categories", description = "Retrieve all asset categories for a specific company.")
    public ResponseEntity<ApiResponse<List<AssetCategoryResponse>>> getCategories(
            @RequestParam Long companyId
    ) {
        List<AssetCategoryResponse> response = fixedAssetService.getCategories(companyId);
        return ResponseEntity.ok(ApiResponse.success("Asset categories retrieved successfully", response));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('FIXED_ASSET_CREATE')")
    @Operation(summary = "Register Fixed Asset", description = "Register a new fixed asset in DRAFT status. Asset code is automatically generated.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> registerAsset(
            @RequestParam Long companyId,
            @RequestBody FixedAssetRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        FixedAssetResponse response = fixedAssetService.registerAsset(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset registered successfully in DRAFT", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Fixed Asset Details", description = "Retrieve comprehensive details of a fixed asset, including nested active components.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> getAsset(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        FixedAssetResponse response = fixedAssetService.getAsset(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset details retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Search Fixed Assets", description = "Search and page fixed assets with multi-company isolation.")
    public ResponseEntity<ApiResponse<PageResponse<FixedAssetResponse>>> searchAssets(
            @RequestParam Long companyId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            Pageable pageable
    ) {
        PageResponse<FixedAssetResponse> response = fixedAssetService.searchAssets(companyId, name, status, categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Fixed assets searched successfully", response));
    }

    @PostMapping("/{id}/acquire")
    @PreAuthorize("hasAuthority('FIXED_ASSET_ACQUIRE')")
    @Operation(summary = "Acquire/Capitalize Fixed Asset", description = "Capitalize the draft asset, post a balanced General Ledger journal entry, and transition status to ACTIVE.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> acquireAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        FixedAssetResponse response = fixedAssetService.acquireAsset(companyId, id, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset capitalized and posted to GL successfully", response));
    }

    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('FIXED_ASSET_ASSIGN')")
    @Operation(summary = "Assign Fixed Asset", description = "Assign the asset to an employee, department, store, or warehouse and log to history.")
    public ResponseEntity<ApiResponse<AssetAssignmentResponse>> assignAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetAssignmentRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetAssignmentResponse response = fixedAssetService.assignAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset assigned successfully", response));
    }

    @PostMapping("/{id}/transfer")
    @PreAuthorize("hasAuthority('FIXED_ASSET_TRANSFER')")
    @Operation(summary = "Transfer Fixed Asset", description = "Transfer physical location of the asset and log to transfer history.")
    public ResponseEntity<ApiResponse<AssetTransferResponse>> transferAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetTransferRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetTransferResponse response = fixedAssetService.transferAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset transferred successfully", response));
    }

    @PostMapping("/{id}/maintain")
    @PreAuthorize("hasAuthority('FIXED_ASSET_MAINTAIN')")
    @Operation(summary = "Log Asset Maintenance", description = "Log maintenance details. Optionally capitalize maintenance costs and adjust GL book values.")
    public ResponseEntity<ApiResponse<AssetMaintenanceResponse>> maintainAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetMaintenanceRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetMaintenanceResponse response = fixedAssetService.maintainAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Maintenance logged successfully", response));
    }

    @PostMapping("/{id}/dispose")
    @PreAuthorize("hasAuthority('FIXED_ASSET_DISPOSE')")
    @Operation(summary = "Dispose/Sell Fixed Asset", description = "Sell or write off a fixed asset. Calculates gains/losses and posts balanced double-entry adjustments to the GL.")
    public ResponseEntity<ApiResponse<AssetDisposalResponse>> disposeAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetDisposalRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetDisposalResponse response = fixedAssetService.disposeAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset disposed successfully", response));
    }

    @PostMapping("/depreciate")
    @PreAuthorize("hasAuthority('FIXED_ASSET_DEPRECIATE')")
    @Operation(summary = "Run Monthly Depreciation", description = "Run depreciation calculations. Support dryRun = true for preview and projected GL entries.")
    public ResponseEntity<ApiResponse<DepreciationRunResponse>> runDepreciation(
            @RequestParam Long companyId,
            @RequestBody DepreciationRunRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        DepreciationRunResponse response = fixedAssetService.runDepreciation(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Depreciation run completed successfully", response));
    }

    @PostMapping("/audits")
    @PreAuthorize("hasAuthority('FIXED_ASSET_AUDIT')")
    @Operation(summary = "Submit Physical Verification Audit", description = "Log physical audits. Triggers automatic status transitions for damaged/missing assets.")
    public ResponseEntity<ApiResponse<AssetAuditResponse>> submitAudit(
            @RequestParam Long companyId,
            @RequestBody AssetAuditRequest request
    ) {
        AssetAuditResponse response = fixedAssetService.submitAudit(companyId, request);
        return ResponseEntity.ok(ApiResponse.success("Physical verification audit submitted successfully", response));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Dashboard KPIs", description = "Retrieve high-fidelity operational asset management metrics and charts.")
    public ResponseEntity<ApiResponse<FixedAssetDashboardResponse>> getDashboard(
            @RequestParam Long companyId
    ) {
        FixedAssetDashboardResponse response = fixedAssetService.getDashboard(companyId);
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics retrieved successfully", response));
    }

    // ── Enterprise Endpoints ────────────────────────────────────────────────

    @PostMapping("/{id}/revalue")
    @PreAuthorize("hasAuthority('FIXED_ASSET_REVALUE')")
    @Operation(summary = "Revalue Fixed Asset", description = "Record asset revaluation (IAS 16) and post revaluation equity reserve GL adjustments.")
    public ResponseEntity<ApiResponse<AssetRevaluationResponse>> revalueAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetRevaluationRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetRevaluationResponse response = fixedAssetService.revalueAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset revalued successfully", response));
    }

    @GetMapping("/{id}/revaluations")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Revaluations", description = "Retrieve revaluation history for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetRevaluationResponse>>> getRevaluations(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetRevaluationResponse> response = fixedAssetService.getRevaluations(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset revaluations retrieved successfully", response));
    }

    @PostMapping("/{id}/impair")
    @PreAuthorize("hasAuthority('FIXED_ASSET_IMPAIR')")
    @Operation(summary = "Impair Fixed Asset", description = "Record asset impairment loss (IAS 36) and post GL adjustments.")
    public ResponseEntity<ApiResponse<AssetImpairmentResponse>> impairAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetImpairmentRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetImpairmentResponse response = fixedAssetService.impairAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset impairment recorded successfully", response));
    }

    @GetMapping("/{id}/impairments")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Impairments", description = "Retrieve impairment history for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetImpairmentResponse>>> getImpairments(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetImpairmentResponse> response = fixedAssetService.getImpairments(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset impairments retrieved successfully", response));
    }

    @PostMapping("/{id}/partial-dispose")
    @PreAuthorize("hasAuthority('FIXED_ASSET_DISPOSE')")
    @Operation(summary = "Partially Dispose Fixed Asset", description = "Perform partial asset disposal, adjust book values, and post GL adjustments.")
    public ResponseEntity<ApiResponse<AssetDisposalResponse>> partialDispose(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetPartialDisposalRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetDisposalResponse response = fixedAssetService.partialDispose(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset partial disposal recorded successfully", response));
    }

    @PostMapping("/{id}/capitalize-cwip")
    @PreAuthorize("hasAuthority('FIXED_ASSET_ACQUIRE')")
    @Operation(summary = "Capitalize CWIP", description = "Capitalize a Construction Work-in-Progress asset to ACTIVE status.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> capitalizeCwip(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetCapitalizeCwipRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        FixedAssetResponse response = fixedAssetService.capitalizeCwip(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("CWIP capitalized successfully", response));
    }

    @PostMapping("/{id}/leases")
    @PreAuthorize("hasAuthority('FIXED_ASSET_LEASE')")
    @Operation(summary = "Add Lease Terms", description = "Record lease details (operating or finance) and map lease liability accounts.")
    public ResponseEntity<ApiResponse<AssetLeaseResponse>> createLease(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetLeaseRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetLeaseResponse response = fixedAssetService.createLease(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset lease recorded successfully", response));
    }

    @GetMapping("/{id}/leases")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Leases", description = "Retrieve lease history/details for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetLeaseResponse>>> getLeases(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetLeaseResponse> response = fixedAssetService.getLeases(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset leases retrieved successfully", response));
    }

    @PostMapping("/{id}/split")
    @PreAuthorize("hasAuthority('FIXED_ASSET_SPLIT_MERGE')")
    @Operation(summary = "Split Fixed Asset", description = "Split an asset into multiple components, distributing cost and depreciation.")
    public ResponseEntity<ApiResponse<List<FixedAssetResponse>>> splitAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetSplitRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        List<FixedAssetResponse> response = fixedAssetService.splitAsset(companyId, id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset split successfully", response));
    }

    @PostMapping("/merge")
    @PreAuthorize("hasAuthority('FIXED_ASSET_SPLIT_MERGE')")
    @Operation(summary = "Merge Fixed Assets", description = "Merge multiple component assets into a single target asset.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> mergeAssets(
            @RequestParam Long companyId,
            @RequestBody AssetMergeRequest request,
            Principal principal
      ) {
          String username = principal != null ? principal.getName() : "system";
          FixedAssetResponse response = fixedAssetService.mergeAssets(companyId, request, username);
          return ResponseEntity.ok(ApiResponse.success("Assets merged successfully", response));
      }

    @PostMapping("/{id}/utilization")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Log Utilization", description = "Record usage readings (runtime hours, mileage, production units).")
    public ResponseEntity<ApiResponse<AssetUtilizationResponse>> recordUtilization(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestBody AssetUtilizationRequest request
    ) {
        AssetUtilizationResponse response = fixedAssetService.recordUtilization(companyId, id, request);
        return ResponseEntity.ok(ApiResponse.success("Asset utilization recorded successfully", response));
    }

    @GetMapping("/{id}/utilization")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Utilization History", description = "Retrieve usage readings history for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetUtilizationResponse>>> getUtilization(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetUtilizationResponse> response = fixedAssetService.getUtilization(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset utilization history retrieved successfully", response));
    }

    @GetMapping("/{id}/tco")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get TCO Analysis", description = "Retrieve total cost of ownership (TCO) and operating cost analysis.")
    public ResponseEntity<ApiResponse<AssetTcoResponse>> getTco(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        AssetTcoResponse response = fixedAssetService.getTco(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("TCO analysis retrieved successfully", response));
    }

    @PostMapping("/maintenance-plans")
    @PreAuthorize("hasAuthority('FIXED_ASSET_MAINTAIN')")
    @Operation(summary = "Create Maintenance Plan", description = "Set up preventive maintenance intervals and vendor details.")
    public ResponseEntity<ApiResponse<AssetMaintenancePlanResponse>> createMaintenancePlan(
            @RequestParam Long companyId,
            @RequestBody AssetMaintenancePlanRequest request
    ) {
        AssetMaintenancePlanResponse response = fixedAssetService.createMaintenancePlan(companyId, request);
        return ResponseEntity.ok(ApiResponse.success("Maintenance plan created successfully", response));
    }

    @GetMapping("/{id}/maintenance-plans")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Maintenance Plans", description = "Retrieve scheduled maintenance plans for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetMaintenancePlanResponse>>> getMaintenancePlans(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetMaintenancePlanResponse> response = fixedAssetService.getMaintenancePlans(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Maintenance plans retrieved successfully", response));
    }

    @PostMapping("/reservations")
    @PreAuthorize("hasAuthority('FIXED_ASSET_RESERVE')")
    @Operation(summary = "Reserve Shared Asset", description = "Request checkout / reservation for shared company assets.")
    public ResponseEntity<ApiResponse<AssetReservationResponse>> createReservation(
            @RequestParam Long companyId,
            @RequestBody AssetReservationRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetReservationResponse response = fixedAssetService.createReservation(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Asset reservation created successfully", response));
    }

    @GetMapping("/{id}/reservations")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Reservations", description = "Retrieve reservations history for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetReservationResponse>>> getReservations(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetReservationResponse> response = fixedAssetService.getReservations(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset reservations retrieved successfully", response));
    }

    @PostMapping("/work-orders")
    @PreAuthorize("hasAuthority('FIXED_ASSET_WORK_ORDER')")
    @Operation(summary = "Create Maintenance Work Order", description = "Create a new maintenance work order for technician/vendor assignment.")
    public ResponseEntity<ApiResponse<AssetWorkOrderResponse>> createWorkOrder(
            @RequestParam Long companyId,
            @RequestBody AssetWorkOrderRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetWorkOrderResponse response = fixedAssetService.createWorkOrder(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Work order created successfully", response));
    }

    @PostMapping("/work-orders/{workOrderId}/complete")
    @PreAuthorize("hasAuthority('FIXED_ASSET_WORK_ORDER')")
    @Operation(summary = "Complete Work Order", description = "Mark a maintenance work order as completed with actual labor/parts cost tracking.")
    public ResponseEntity<ApiResponse<AssetWorkOrderResponse>> completeWorkOrder(
            @RequestParam Long companyId,
            @PathVariable Long workOrderId,
            @RequestParam BigDecimal actualCost,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetWorkOrderResponse response = fixedAssetService.completeWorkOrder(companyId, workOrderId, actualCost, username);
        return ResponseEntity.ok(ApiResponse.success("Work order marked complete successfully", response));
    }

    @GetMapping("/{id}/work-orders")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset Work Orders", description = "Retrieve work orders history for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetWorkOrderResponse>>> getWorkOrders(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetWorkOrderResponse> response = fixedAssetService.getWorkOrders(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Work orders retrieved successfully", response));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Asset History Log", description = "Retrieve complete, chronological audit timeline for a specific asset.")
    public ResponseEntity<ApiResponse<List<AssetHistoryResponse>>> getAssetHistory(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        List<AssetHistoryResponse> response = fixedAssetService.getAssetHistory(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Asset history log retrieved successfully", response));
    }

    @PostMapping("/{id}/legal-hold")
    @PreAuthorize("hasAuthority('FIXED_ASSET_COMPLIANCE')")
    @Operation(summary = "Toggle Legal Hold", description = "Toggle legal hold status on an asset to prevent disposals, transfers, or edits.")
    public ResponseEntity<ApiResponse<FixedAssetResponse>> toggleLegalHold(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestParam boolean hold,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        FixedAssetResponse response = fixedAssetService.toggleLegalHold(companyId, id, hold, username);
        return ResponseEntity.ok(ApiResponse.success("Asset legal hold toggled successfully", response));
    }

    @PostMapping("/transfers/{transferId}/approve")
    @PreAuthorize("hasAuthority('FIXED_ASSET_TRANSFER_APPROVE')")
    @Operation(summary = "Approve Transfer Request", description = "Approve a physical asset transfer request.")
    public ResponseEntity<ApiResponse<AssetTransferResponse>> approveTransfer(
            @RequestParam Long companyId,
            @PathVariable Long transferId,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetTransferResponse response = fixedAssetService.approveTransfer(companyId, transferId, username);
        return ResponseEntity.ok(ApiResponse.success("Asset transfer approved successfully", response));
    }

    @PostMapping("/transfers/{transferId}/receive")
    @PreAuthorize("hasAuthority('FIXED_ASSET_TRANSFER')")
    @Operation(summary = "Mark Transfer as Received", description = "Mark an approved asset transfer as physically received at destination.")
    public ResponseEntity<ApiResponse<AssetTransferResponse>> receiveTransfer(
            @RequestParam Long companyId,
            @PathVariable Long transferId,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        AssetTransferResponse response = fixedAssetService.receiveTransfer(companyId, transferId, username);
        return ResponseEntity.ok(ApiResponse.success("Asset transfer received successfully", response));
    }

    @GetMapping("/{id}/rollup")
    @PreAuthorize("hasAuthority('FIXED_ASSET_VIEW')")
    @Operation(summary = "Get Hierarchy Rollup KPIs", description = "Retrieve rolled-up costs, values, and health scores for asset tree.")
    public ResponseEntity<ApiResponse<FixedAssetDashboardResponse>> getHierarchyRollup(
            @RequestParam Long companyId,
            @PathVariable Long id
    ) {
        FixedAssetDashboardResponse response = fixedAssetService.getHierarchyRollup(companyId, id);
        return ResponseEntity.ok(ApiResponse.success("Hierarchy rollup dashboard retrieved successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('FIXED_ASSET_CREATE')")
    @Operation(summary = "Soft Delete Fixed Asset", description = "Soft delete an asset, hiding it from standard queries while keeping it archived.")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(
            @RequestParam Long companyId,
            @PathVariable Long id,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "system";
        fixedAssetService.deleteAsset(companyId, id, username);
        return ResponseEntity.ok(ApiResponse.success("Fixed asset soft deleted successfully", null));
    }
}
