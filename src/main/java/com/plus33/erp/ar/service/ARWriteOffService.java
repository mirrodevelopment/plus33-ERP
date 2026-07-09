/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.service
 * File              : ARWriteOffService.java
 * Purpose           : Service interface contract defining the API for Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARWriteOffController
 * Related Service   : ARWriteOffService, ARWriteOffServiceImpl
 * Related Repository: ARWriteOffRepository
 * Related Entity    : ARWriteOff
 * Related DTO       : ARWriteOffRequest, ARWriteOffResponse, PageResponse
 * Related Mapper    : ARWriteOffMapper
 * Related DB Table  : a_r_write_offs
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Ar Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ar Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
