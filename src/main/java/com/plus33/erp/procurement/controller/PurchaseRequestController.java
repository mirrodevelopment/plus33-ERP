package com.plus33.erp.procurement.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import com.plus33.erp.procurement.service.PurchaseRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/purchase-requests")
@Tag(name = "Purchase Request Management", description = "REST APIs for managing purchase requests and approval workflow")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CREATE')")
    @Operation(summary = "Create a new purchase request", description = "Initiates a purchase request in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> createPurchaseRequest(
            @Valid @RequestBody PurchaseRequestRequest request
    ) {
        PurchaseRequestResponse response = purchaseRequestService.createPurchaseRequest(request);
        return new ResponseEntity<>(ApiResponse.success("Purchase request created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_VIEW')")
    @Operation(summary = "Get purchase request by ID", description = "Retrieves details of a purchase request by primary key.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> getPurchaseRequestById(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.getPurchaseRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_VIEW')")
    @Operation(summary = "Search purchase requests", description = "Performs dynamic searches and pagination filters for purchase requests.")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseRequestResponse>>> searchPurchaseRequests(
            @RequestParam(required = false) String requestNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) PurchaseRequestStatus status,
            @RequestParam(required = false) Long requestedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requiredDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requiredDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PurchaseRequestSearchRequest searchRequest = new PurchaseRequestSearchRequest(
                requestNumber, companyId, supplierId, warehouseId, storeId, status, requestedBy,
                requestDateFrom, requestDateTo, requiredDateFrom, requiredDateTo);
        PageResponse<PurchaseRequestResponse> response = purchaseRequestService.searchPurchaseRequests(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Purchase requests retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_UPDATE')")
    @Operation(summary = "Update purchase request details", description = "Modifies details of a purchase request while it is in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> updatePurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequestRequest request
    ) {
        PurchaseRequestResponse response = purchaseRequestService.updatePurchaseRequest(id, request);
        return ResponseEntity.ok(ApiResponse.success("Purchase request updated successfully", response));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_SUBMIT')")
    @Operation(summary = "Submit purchase request", description = "Submits a purchase request in DRAFT status for approval.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> submitPurchaseRequest(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.submitPurchaseRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request submitted successfully", response));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_APPROVE')")
    @Operation(summary = "Approve purchase request", description = "Approves a submitted purchase request.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> approvePurchaseRequest(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.approvePurchaseRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request approved successfully", response));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_REJECT')")
    @Operation(summary = "Reject purchase request", description = "Rejects a submitted purchase request with a reason.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> rejectPurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        PurchaseRequestResponse response = purchaseRequestService.rejectPurchaseRequest(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Purchase request rejected successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CANCEL')")
    @Operation(summary = "Cancel purchase request", description = "Cancels a submitted purchase request with a reason.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> cancelPurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        PurchaseRequestResponse response = purchaseRequestService.cancelPurchaseRequest(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Purchase request cancelled successfully", response));
    }

    @PostMapping("/{id}/convert")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CONVERT')")
    @Operation(summary = "Convert approved purchase request to PO", description = "Converts an approved purchase request to a Purchase Order draft.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> convertPurchaseRequestToPo(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.convertPurchaseRequestToPo(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request converted to PO successfully", response));
    }
}
