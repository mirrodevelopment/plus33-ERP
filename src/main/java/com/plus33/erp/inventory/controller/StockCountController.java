/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : StockCountController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountControllerService, StockCountControllerServiceImpl
 * Related Repository: StockCountControllerRepository
 * Related Entity    : StockCountController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, searchRequest, StockCountRequest
 * Related Mapper    : StockCountControllerMapper
 * Related DB Table  : stock_count_controllers
 * Related REST APIs : POST /api/v1/stock-counts, PUT /api/v1/stock-counts/{id}, GET /api/v1/stock-counts/{id}, GET /api/v1/stock-counts
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/stock-counts, PUT /api/v1/stock-counts/{id}, GET /api/v1/stock-counts/{id}, GET /api/v1/stock-counts
 ******************************************************************************/
package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.StockCountStatus;
import com.plus33.erp.inventory.entity.StockCountType;
import com.plus33.erp.inventory.service.StockCountService;
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
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to StockCountService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> StockCountController.endpoint()
 *   --> StockCountService.method()
 *   --> StockCountRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/stock-counts, PUT /api/v1/stock-counts/{id}, GET /api/v1/stock-counts/{id}, GET /api/v1/stock-counts, POST /api/v1/stock-counts/{id}/assign</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/stock-counts")
@Tag(name = "Stock Counts", description = "REST APIs for managing stock counts and cycle counts")
public class StockCountController {

    private final StockCountService stockCountService;

    public StockCountController(StockCountService stockCountService) {
        this.stockCountService = stockCountService;
    }

    /**
     * Creates a new count and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('STOCK_COUNT_CREATE')")
    @Operation(summary = "Create stock count session", description = "Creates a count session in DRAFT status. Supports client_reference_id for idempotency.")
    public ResponseEntity<ApiResponse<StockCountResponse>> createCount(
            @Valid @RequestBody StockCountRequest request
    ) {
        IdempotentCreateResult<StockCountResponse> result = stockCountService.createCount(request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        String msg = result.created() ? "Stock count created successfully" : "Stock count replayed successfully";
        return new ResponseEntity<>(ApiResponse.success(msg, result.data()), status);
    }

    /**
     * Updates an existing count record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCK_COUNT_UPDATE')")
    @Operation(summary = "Update stock count session", description = "Updates a stock count session in DRAFT status.")
    public ResponseEntity<ApiResponse<StockCountResponse>> updateCount(
            @PathVariable Long id,
            @Valid @RequestBody StockCountUpdateRequest request
    ) {
        StockCountResponse response = stockCountService.updateCount(id, request);
        return ResponseEntity.ok(ApiResponse.success("Stock count updated successfully", response));
    }

    /**
     * Retrieves a single count by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STOCK_COUNT_VIEW')")
    @Operation(summary = "Get stock count by ID")
    public ResponseEntity<ApiResponse<StockCountResponse>> getCountById(@PathVariable Long id) {
        StockCountResponse response = stockCountService.getCountById(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of counts records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STOCK_COUNT_VIEW')")
    @Operation(summary = "Search stock counts")
    public ResponseEntity<ApiResponse<PageResponse<StockCountResponse>>> searchCounts(
            @RequestParam(required = false) StockCountStatus status,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) StockCountType countType,
            @RequestParam(required = false) String countNumber,
            @RequestParam(required = false) UUID clientReferenceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtTo,
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        StockCountSearchRequest searchRequest = StockCountSearchRequest.builder()
                .status(status)
                .companyId(companyId)
                .warehouseId(warehouseId)
                .storeId(storeId)
                .countType(countType)
                .countNumber(countNumber)
                .clientReferenceId(clientReferenceId)
                .createdAtFrom(createdAtFrom)
                .createdAtTo(createdAtTo)
                .createdBy(createdBy)
                .assignedTo(assignedTo)
                .productId(productId)
                .build();

        PageResponse<StockCountResponse> response = stockCountService.searchCounts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Stock counts retrieved successfully", response));
    }

    /**
     * Performs the assignCount operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasAuthority('STOCK_COUNT_ASSIGN')")
    @Operation(summary = "Assign stock count to an employee")
    public ResponseEntity<ApiResponse<StockCountResponse>> assignCount(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        StockCountResponse response = stockCountService.assignCount(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Stock count assigned successfully", response));
    }

    /**
     * Performs the startCount operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/start")
    @PreAuthorize("hasAuthority('STOCK_COUNT_UPDATE')")
    @Operation(summary = "Start stock count physical counting")
    public ResponseEntity<ApiResponse<StockCountResponse>> startCount(@PathVariable Long id) {
        StockCountResponse response = stockCountService.startCount(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count started successfully", response));
    }

    /**
     * Submits the count for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('STOCK_COUNT_SUBMIT')")
    @Operation(summary = "Submit completed physical count")
    public ResponseEntity<ApiResponse<StockCountResponse>> submitCount(
            @PathVariable Long id,
            @Valid @RequestBody StockCountSubmitRequest request
    ) {
        StockCountResponse response = stockCountService.submitCount(id, request);
        return ResponseEntity.ok(ApiResponse.success("Stock count submitted successfully", response));
    }

    /**
     * Performs the rejectCount operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('STOCK_COUNT_APPROVE')")
    @Operation(summary = "Reject submitted count and trigger recount")
    public ResponseEntity<ApiResponse<StockCountResponse>> rejectCount(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        StockCountResponse response = stockCountService.rejectCount(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Stock count rejected successfully", response));
    }

    /**
     * Performs the reopenCount operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/reopen")
    @PreAuthorize("hasAuthority('STOCK_COUNT_APPROVE')")
    @Operation(summary = "Reopen rejected count session for recount")
    public ResponseEntity<ApiResponse<StockCountResponse>> reopenCount(@PathVariable Long id) {
        StockCountResponse response = stockCountService.reopenCount(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count reopened successfully", response));
    }

    /**
     * Approves the count, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('STOCK_COUNT_APPROVE')")
    @Operation(summary = "Approve submitted stock count")
    public ResponseEntity<ApiResponse<StockCountResponse>> approveCount(@PathVariable Long id) {
        StockCountResponse response = stockCountService.approveCount(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count approved successfully", response));
    }

    /**
     * Posts count entries to the General Ledger and updates financial balances.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('STOCK_COUNT_POST')")
    @Operation(summary = "Post approved stock count and apply inventory adjustments")
    public ResponseEntity<ApiResponse<StockCountResponse>> postCount(@PathVariable Long id) {
        StockCountResponse response = stockCountService.postCount(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count posted successfully", response));
    }

    /**
     * Completes the count workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/close")
    @PreAuthorize("hasAuthority('STOCK_COUNT_POST')")
    @Operation(summary = "Administratively close the stock count session")
    public ResponseEntity<ApiResponse<StockCountResponse>> closeCount(@PathVariable Long id) {
        StockCountResponse response = stockCountService.closeCount(id);
        return ResponseEntity.ok(ApiResponse.success("Stock count closed successfully", response));
    }
}