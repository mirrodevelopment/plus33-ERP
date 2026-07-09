/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : CreditNoteController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteControllerService, CreditNoteControllerServiceImpl
 * Related Repository: CreditNoteControllerRepository
 * Related Entity    : CreditNoteController
 * Related DTO       : ApiResponse, CreditNoteResponse, PageRequest, PageResponse
 * Related Mapper    : CreditNoteControllerMapper
 * Related DB Table  : credit_note_controllers
 * Related REST APIs : GET /api/v1/credit-notes/{id}, GET /api/v1/credit-notes
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/credit-notes/{id}, GET /api/v1/credit-notes
 ******************************************************************************/
package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import com.plus33.erp.sales.service.CreditNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CreditNoteService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CreditNoteController.endpoint()
 *   --> CreditNoteService.method()
 *   --> CreditNoteRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/credit-notes/{id}, GET /api/v1/credit-notes</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/credit-notes")
@Tag(name = "Credit Note Management", description = "REST APIs for querying and viewing customer credit notes")
public class CreditNoteController {

    private final CreditNoteService creditNoteService;

    public CreditNoteController(CreditNoteService creditNoteService) {
        this.creditNoteService = creditNoteService;
    }

    /**
     * Retrieves a single credit note by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CREDIT_NOTE_VIEW')")
    @Operation(summary = "Get a credit note by ID")
    public ResponseEntity<ApiResponse<CreditNoteResponse>> getCreditNoteById(@PathVariable Long id) {
        CreditNoteResponse response = creditNoteService.getCreditNoteById(id);
        return ResponseEntity.ok(ApiResponse.success("Credit note retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of credit notes records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CREDIT_NOTE_VIEW')")
    @Operation(summary = "Search credit notes with filtering and pagination")
    public ResponseEntity<ApiResponse<PageResponse<CreditNoteResponse>>> searchCreditNotes(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String creditNoteNumber,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDir = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1]) 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortField));

        PageResponse<CreditNoteResponse> response = creditNoteService.searchCreditNotes(companyId, customerId, creditNoteNumber, status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Credit notes retrieved successfully", response));
    }
}