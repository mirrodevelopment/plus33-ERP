package com.plus33.erp.ar.service;

import com.plus33.erp.ar.dto.ARWriteOffRequest;
import com.plus33.erp.ar.dto.ARWriteOffResponse;
import com.plus33.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

/**
 * Transactional service for AR write-off operations.
 * Reporting queries are handled by {@link ARService}.
 */
public interface ARWriteOffService {

    /**
     * Records a bad-debt write-off. Reduces the invoice outstanding balance,
     * updates the customer balance, and posts a GL journal entry:
     * DR Bad Debt Expense (5300) / CR Accounts Receivable (1400).
     */
    ARWriteOffResponse createWriteOff(ARWriteOffRequest request);

    /**
     * Retrieves a write-off record by ID.
     */
    ARWriteOffResponse getWriteOffById(Long id);

    /**
     * Searches write-off records with optional company and customer filters.
     */
    PageResponse<ARWriteOffResponse> searchWriteOffs(Long companyId, Long customerId, Pageable pageable);
}
