/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.controller
 * File              : JournalEntryController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryControllerService, JournalEntryControllerServiceImpl
 * Related Repository: JournalEntryControllerRepository
 * Related Entity    : JournalEntryController
 * Related DTO       : ApiResponse, JournalEntryRequest, JournalEntryResponse
 * Related Mapper    : JournalEntryControllerMapper
 * Related DB Table  : journal_entry_controllers
 * Related REST APIs : POST /api/v1/journal-entries, POST /api/v1/journal-entries/{id}/post, GET /api/v1/journal-entries/{id}, GET /api/v1/journal-entries
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/journal-entries, POST /api/v1/journal-entries/{id}/post, GET /api/v1/journal-entries/{id}, GET /api/v1/journal-entries
 ******************************************************************************/
package com.plus33.erp.finance.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.dto.JournalEntryRequest;
import com.plus33.erp.finance.dto.JournalEntryResponse;
import com.plus33.erp.finance.service.JournalEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to JournalEntryService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> JournalEntryController.endpoint()
 *   --> JournalEntryService.method()
 *   --> JournalEntryRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/journal-entries, POST /api/v1/journal-entries/{id}/post, GET /api/v1/journal-entries/{id}, GET /api/v1/journal-entries</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/journal-entries")
@RequiredArgsConstructor
@Tag(name = "General Ledger", description = "API endpoints for manual journal entries and GL postings.")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    /**
     * Creates a new journal entry and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('FINANCE_WRITE')")
    @Operation(summary = "Create Journal Entry", description = "Create a draft manual journal entry.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> createJournalEntry(@Valid @RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = journalEntryService.createJournalEntry(request);
        return new ResponseEntity<>(ApiResponse.success("Journal entry created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Posts journal entry entries to the General Ledger and updates financial balances.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('FINANCE_WRITE')")
    @Operation(summary = "Post Journal Entry", description = "Post the journal entry to the ledger and execute budget checks.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> postJournalEntry(@PathVariable Long id) {
        JournalEntryResponse response = journalEntryService.postJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success("Journal entry posted to ledger successfully", response));
    }

    /**
     * Retrieves a single journal entry by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FINANCE_READ')")
    @Operation(summary = "Get Journal Entry", description = "Retrieve a journal entry by its unique ID.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> getJournalEntryById(@PathVariable Long id) {
        JournalEntryResponse response = journalEntryService.getJournalEntryById(id);
        return ResponseEntity.ok(ApiResponse.success("Journal entry retrieved successfully", response));
    }

    /**
     * Retrieves journal entries by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('FINANCE_READ')")
    @Operation(summary = "List Journal Entries", description = "List all journal entries for a company.")
    public ResponseEntity<ApiResponse<List<JournalEntryResponse>>> getJournalEntriesByCompany(@RequestParam Long companyId) {
        List<JournalEntryResponse> response = journalEntryService.getJournalEntriesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Journal entries retrieved successfully", response));
    }
}