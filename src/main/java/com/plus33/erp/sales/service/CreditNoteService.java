package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import org.springframework.data.domain.Pageable;

public interface CreditNoteService {
    CreditNoteResponse getCreditNoteById(Long id);
    PageResponse<CreditNoteResponse> searchCreditNotes(Long companyId, Long customerId, String creditNoteNumber, String status, Pageable pageable);
}
