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

@RestController
@RequestMapping("/api/v1/journal-entries")
@RequiredArgsConstructor
@Tag(name = "General Ledger", description = "API endpoints for manual journal entries and GL postings.")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;

    @PostMapping
    @PreAuthorize("hasAuthority('FINANCE_WRITE')")
    @Operation(summary = "Create Journal Entry", description = "Create a draft manual journal entry.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> createJournalEntry(@Valid @RequestBody JournalEntryRequest request) {
        JournalEntryResponse response = journalEntryService.createJournalEntry(request);
        return new ResponseEntity<>(ApiResponse.success("Journal entry created successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('FINANCE_WRITE')")
    @Operation(summary = "Post Journal Entry", description = "Post the journal entry to the ledger and execute budget checks.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> postJournalEntry(@PathVariable Long id) {
        JournalEntryResponse response = journalEntryService.postJournalEntry(id);
        return ResponseEntity.ok(ApiResponse.success("Journal entry posted to ledger successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FINANCE_READ')")
    @Operation(summary = "Get Journal Entry", description = "Retrieve a journal entry by its unique ID.")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> getJournalEntryById(@PathVariable Long id) {
        JournalEntryResponse response = journalEntryService.getJournalEntryById(id);
        return ResponseEntity.ok(ApiResponse.success("Journal entry retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FINANCE_READ')")
    @Operation(summary = "List Journal Entries", description = "List all journal entries for a company.")
    public ResponseEntity<ApiResponse<List<JournalEntryResponse>>> getJournalEntriesByCompany(@RequestParam Long companyId) {
        List<JournalEntryResponse> response = journalEntryService.getJournalEntriesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Journal entries retrieved successfully", response));
    }
}
