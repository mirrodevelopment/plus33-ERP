/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : ReplenishmentController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentController
 * Related Service   : ReplenishmentControllerService, ReplenishmentControllerServiceImpl
 * Related Repository: ReplenishmentControllerRepository
 * Related Entity    : ReplenishmentController
 * Related DTO       : ApiResponse, createPurchaseRequest, PageRequest, PageResponse, ReplenishmentRuleRequest
 * Related Mapper    : ReplenishmentControllerMapper
 * Related DB Table  : replenishment_controllers
 * Related REST APIs : POST /api/v1/replenishment/rules, GET /api/v1/replenishment/rules, GET /api/v1/replenishment/rules/{id}, PUT /api/v1/replenishment/rules/{id}
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/replenishment/rules, GET /api/v1/replenishment/rules, GET /api/v1/replenishment/rules/{id}, PUT /api/v1/replenishment/rules/{id}
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ReplenishmentService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ReplenishmentController.endpoint()
 *   --> ReplenishmentService.method()
 *   --> ReplenishmentRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/replenishment/rules, GET /api/v1/replenishment/rules, GET /api/v1/replenishment/rules/{id}, PUT /api/v1/replenishment/rules/{id}, DELETE /api/v1/replenishment/rules/{id}</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/replenishment")
@Tag(name = "Replenishment", description = "REST APIs for managing replenishment rules and suggestions")
public class ReplenishmentController {

    private final ReplenishmentService replenishmentService;

    public ReplenishmentController(ReplenishmentService replenishmentService) {
        this.replenishmentService = replenishmentService;
    }

    /**
     * Creates a new rule and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Returns a filtered paginated list of rules records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Retrieves rule data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/rules/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_VIEW')")
    @Operation(summary = "Get replenishment rule by ID")
    public ResponseEntity<ApiResponse<ReplenishmentRuleResponse>> getRule(@PathVariable Long id) {
        ReplenishmentRuleResponse response = replenishmentService.getRule(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule retrieved successfully", response));
    }

    /**
     * Updates an existing rule record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Permanently deletes the rule from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/rules/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_RULE_DELETE')")
    @Operation(summary = "Deactivate replenishment rule (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteRule(@PathVariable Long id) {
        replenishmentService.deleteRule(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rule deactivated successfully", null));
    }

    /**
     * Performs the evaluateAll operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @PostMapping("/evaluate")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "Evaluate all replenishment rules for a company")
    public ResponseEntity<ApiResponse<List<ReplenishmentSuggestionResponse>>> evaluateAll(
            @RequestParam Long companyId
    ) {
        List<ReplenishmentSuggestionResponse> responses = replenishmentService.evaluateAll(companyId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment rules evaluated successfully", responses));
    }

    /**
     * Performs the evaluateRule operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Returns a filtered paginated list of suggestions records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Retrieves suggestion data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/suggestions/{id}")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_VIEW')")
    @Operation(summary = "Get replenishment suggestion by ID")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> getSuggestion(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.getSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestion retrieved successfully", response));
    }

    /**
     * Performs the acknowledgeSuggestion operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/suggestions/{id}/acknowledge")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ACKNOWLEDGE')")
    @Operation(summary = "Acknowledge a replenishment suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> acknowledgeSuggestion(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.acknowledgeSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Replenishment suggestion acknowledged successfully", response));
    }

    /**
     * Cancels the suggestion and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new purchase request and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/suggestions/{id}/create-purchase-request")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ORDER')")
    @Operation(summary = "Auto-create a purchase request from a suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> createPurchaseRequest(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.createPurchaseRequestFromSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request created successfully from suggestion", response));
    }

    /**
     * Creates a new transfer and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/suggestions/{id}/create-transfer")
    @PreAuthorize("hasAuthority('REPLENISHMENT_SUGGESTION_ORDER')")
    @Operation(summary = "Auto-create an inventory transfer from a store replenishment suggestion")
    public ResponseEntity<ApiResponse<ReplenishmentSuggestionResponse>> createTransfer(@PathVariable Long id) {
        ReplenishmentSuggestionResponse response = replenishmentService.createTransferFromSuggestion(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer created successfully from suggestion", response));
    }
}