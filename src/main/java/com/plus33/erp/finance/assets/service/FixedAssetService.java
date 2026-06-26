package com.plus33.erp.finance.assets.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.assets.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FixedAssetService {
    AssetCategoryResponse createCategory(Long companyId, AssetCategoryRequest request);
    List<AssetCategoryResponse> getCategories(Long companyId);

    FixedAssetResponse registerAsset(Long companyId, FixedAssetRequest request, String username);
    FixedAssetResponse getAsset(Long companyId, Long id);
    PageResponse<FixedAssetResponse> searchAssets(Long companyId, String name, String status, Long categoryId, Pageable pageable);

    FixedAssetResponse acquireAsset(Long companyId, Long id, String username);
    AssetAssignmentResponse assignAsset(Long companyId, Long id, AssetAssignmentRequest request, String username);
    AssetTransferResponse transferAsset(Long companyId, Long id, AssetTransferRequest request, String username);
    AssetMaintenanceResponse maintainAsset(Long companyId, Long id, AssetMaintenanceRequest request, String username);
    AssetDisposalResponse disposeAsset(Long companyId, Long id, AssetDisposalRequest request, String username);
    DepreciationRunResponse runDepreciation(Long companyId, DepreciationRunRequest request, String username);
    AssetAuditResponse submitAudit(Long companyId, AssetAuditRequest request);
    FixedAssetDashboardResponse getDashboard(Long companyId);

    // ── Enterprise Methods ──────────────────────────────────────────────────

    // Revaluation (IAS 16 / IFRS)
    AssetRevaluationResponse revalueAsset(Long companyId, Long id, AssetRevaluationRequest request, String username);
    List<AssetRevaluationResponse> getRevaluations(Long companyId, Long assetId);

    // Impairment (IAS 36)
    AssetImpairmentResponse impairAsset(Long companyId, Long id, AssetImpairmentRequest request, String username);
    List<AssetImpairmentResponse> getImpairments(Long companyId, Long assetId);

    // Partial Disposal
    AssetDisposalResponse partialDispose(Long companyId, Long id, AssetPartialDisposalRequest request, String username);

    // CWIP Capitalization
    FixedAssetResponse capitalizeCwip(Long companyId, Long id, AssetCapitalizeCwipRequest request, String username);

    // Lease Management
    AssetLeaseResponse createLease(Long companyId, Long assetId, AssetLeaseRequest request, String username);
    List<AssetLeaseResponse> getLeases(Long companyId, Long assetId);

    // Split & Merge
    List<FixedAssetResponse> splitAsset(Long companyId, Long id, AssetSplitRequest request, String username);
    FixedAssetResponse mergeAssets(Long companyId, AssetMergeRequest request, String username);

    // Utilization
    AssetUtilizationResponse recordUtilization(Long companyId, Long assetId, AssetUtilizationRequest request);
    List<AssetUtilizationResponse> getUtilization(Long companyId, Long assetId);

    // TCO Analysis
    AssetTcoResponse getTco(Long companyId, Long assetId);

    // Maintenance Plans
    AssetMaintenancePlanResponse createMaintenancePlan(Long companyId, AssetMaintenancePlanRequest request);
    List<AssetMaintenancePlanResponse> getMaintenancePlans(Long companyId, Long assetId);

    // Reservations
    AssetReservationResponse createReservation(Long companyId, AssetReservationRequest request, String username);
    List<AssetReservationResponse> getReservations(Long companyId, Long assetId);

    // Work Orders
    AssetWorkOrderResponse createWorkOrder(Long companyId, AssetWorkOrderRequest request, String username);
    AssetWorkOrderResponse completeWorkOrder(Long companyId, Long workOrderId, java.math.BigDecimal actualCost, String username);
    List<AssetWorkOrderResponse> getWorkOrders(Long companyId, Long assetId);

    // Asset History
    List<AssetHistoryResponse> getAssetHistory(Long companyId, Long assetId);

    // Legal Hold
    FixedAssetResponse toggleLegalHold(Long companyId, Long id, boolean hold, String username);

    // Transfer Workflow
    AssetTransferResponse approveTransfer(Long companyId, Long transferId, String username);
    AssetTransferResponse receiveTransfer(Long companyId, Long transferId, String username);

    // Hierarchy Roll-up
    FixedAssetDashboardResponse getHierarchyRollup(Long companyId, Long assetId);

    // Soft Delete
    void deleteAsset(Long companyId, Long id, String username);
}
