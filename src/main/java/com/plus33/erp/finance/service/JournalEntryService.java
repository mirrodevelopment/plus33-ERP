package com.plus33.erp.finance.service;

import com.plus33.erp.finance.dto.JournalEntryRequest;
import com.plus33.erp.finance.dto.JournalEntryResponse;

import java.util.List;

public interface JournalEntryService {
    JournalEntryResponse createJournalEntry(JournalEntryRequest request);
    JournalEntryResponse postJournalEntry(Long id);
    JournalEntryResponse getJournalEntryById(Long id);
    List<JournalEntryResponse> getJournalEntriesByCompany(Long companyId);
}
