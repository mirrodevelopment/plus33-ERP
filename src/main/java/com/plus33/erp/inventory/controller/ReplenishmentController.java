package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;
import com.plus33.erp.inventory.service.ReplenishmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/replenishment")
@Tag(name = "Replenishment", description = "REST APIs for managing replenishment rules and suggestions")
public class ReplenishmentController {

    private final ReplenishmentService replenishmentService;

    public ReplenishmentController(ReplenishmentService replenishmentService) {
        this.replenishmentService = replenishmentService;
    }

    @PostMapping("/rules")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_CREATE')")
    @Operation(summary = "Create replenishment rule", description = "Creates a replenishment rule. Supports client_reference_id for idempotency.")
    public ResponseEntity<ApiResponse<ReplenishmentRuleResponse>> createRule(
            @Valid @RequestBody ReplenishmentRuleRequest request
    ) {
        IdempotentCreateResult<ReplenishmentRuleResponse> result = replenishmentService.createRule(request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        String msg = result.created() ? "Replenishment rule created successfully" : "Replenishment rule replayed successfully";
        return new ResponseEntity<>(ApiResponse.success(msg, result.data()), status);
    }

    @GetMapping("/rules")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_VIEW')")
    @Operation(summary = "List replenishment rules")
    public ResponseEntity<ApiResponse<PageResponse<ReplenishmentRuleResponse>>> listRules(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PageResponse<ReplenishmentRuleResponse> response = replenishmentService.listRules(companyId, warehouseId, storeId, productId, active, pageable);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rules retrieved successfully", response));
    }

    @GetMapping("/rules/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_VIEW')")
    @Operation(summary = "Get replenishment rule by ID")
    public ResponseEntity<ApiResponse<ReplenishmentRuleResponse>> getRule(@PathVariable Long id) {
        ReplenishmentRuleResponse response = replenishmentService.getRule(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule retrieved successfully", response));
    }

    @PutMapping("/rules/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_UPDATE')")
    @Operation(summary = "Update replenishment rule")
    public ResponseEntity<ApiResponse<ReplenishmentRuleResponse>> updateRule(
            @PathVariable Long id,
            @Valid @RequestBody ReplenishmentRuleUpdateRequest request
    ) {
        ReplenishmentRuleResponse response = replenishmentService.updateRule(id, request);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule updated successfully", response));
    }

    @DeleteMapping("/rules/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_DELETE')")
    @Operation(summary = "Deactivate replenishment rule (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteRule(@PathVariable Long id) {
        replenishmentService.deleteRule(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule deactivated successfully", null));
    }

    @PostMapping("/evaluate")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "Evaluate all replenishment rules for a company")
    public ResponseEntity<ApiResponse<List<ReplenishmentSuggestionResponse>>> evaluateAll(
            @RequestParam Long companyId
    ) {
        List<ReplenishmentSuggestionResponse> responses = replenishmentService.evaluateAll(companyId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rules evaluated successfully", responses));
    }

    @PostMapping("/rules/{id}/evaluate")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "Evaluate a single replenishment rule")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> evaluateRule(
            @PathVariable Long id
    ) {
        ReplenishmentSuggestionResponse response = replenishmentService.evaluateRule(id);
        if (response == null) {
            return ResponseEntity.ok(ApiResponse.success("No suggestion needed. Stock level is sufficient.", null));
        }
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule evaluated successfully", response));
    }

    @GetMapping("/suggestions")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "List replenishment suggestions")
    public ResponseEntity<ApiResponse<PageResponse<ReplenishmentSuggestionResponse>>> listSuggestions(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) ReplenishmentSuggestionStatus status,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PageResponse<ReplenishmentSuggestionResponse> response = replenishmentService.listSuggestions(companyId, status, productId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestions retrieved successfully", response));
    }

    @GetMapping("/suggestions/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "Get replenishment suggestion by ID")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> getSuggestion(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.getSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestion retrieved successfully", response));
    }

    @PostMapping("/suggestions/{id}/acknowledge")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ACKNOWLEDGE')")
    @Operation(summary = "Acknowledge a replenishment suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> acknowledgeSuggestion(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.acknowledgeSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestion acknowledged successfully", response));
    }

    @PostMapping("/suggestions/{id}/cancel")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_CANCEL')")
    @Operation(summary = "Cancel a replenishment suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> cancelSuggestion(
            @PathVariable Long id,
            @Valid @RequestBody ReplenishmentSuggestionCancelRequest request
    ) {
        ReplenishmentSuggestionResponse response = replenishmentService.cancelSuggestion(id, request.notes());
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestion cancelled successfully", response));
    }

    @PostMapping("/suggestions/{id}/create-purchase-request")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ORDER')")
    @Operation(summary = "Auto-create a purchase request from a suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> createPurchaseRequest(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.createPurchaseRequestFromSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request created successfully from suggestion", response));
    }

    @PostMapping("/suggestions/{id}/create-transfer")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ORDER')")
    @Operation(summary = "Auto-create an inventory transfer from a store replenishment suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> createTransfer(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.createTransferFromSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer created successfully from suggestion", response));
    }
}
