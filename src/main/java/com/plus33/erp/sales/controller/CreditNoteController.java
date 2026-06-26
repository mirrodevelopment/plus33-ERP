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

@RestController
@RequestMapping("/api/v1/credit-notes")
@Tag(name = "Credit Note Management", description = "REST APIs for querying and viewing customer credit notes")
public class CreditNoteController {

    private final CreditNoteService creditNoteService;

    public CreditNoteController(CreditNoteService creditNoteService) {
        this.creditNoteService = creditNoteService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CREDIT_NOTE_VIEW')")
    @Operation(summary = "Get a credit note by ID")
    public ResponseEntity<ApiResponse<CreditNoteResponse>> getCreditNoteById(@PathVariable Long id) {
        CreditNoteResponse response = creditNoteService.getCreditNoteById(id);
        return ResponseEntity.ok(ApiResponse.success("Credit note retrieved successfully", response));
    }

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
