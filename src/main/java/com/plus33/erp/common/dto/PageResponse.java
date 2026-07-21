/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : PageResponse.java
 * Path              : src/main/java/com/plus33/erp/common/dto/PageResponse.java
 * Purpose           : Generic paginated response wrapper for returning lists of entities
 *                     along with pagination metadata (current page, size, total elements, total pages).
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Generic record representing a paginated result set returned by REST endpoints
 * across the ERP (e.g. employee lists, inventory items, transactions).
 *
 * Fields:
 *   content       — List<T> containing the records for the current page.
 *   page          — 0-indexed current page number.
 *   size          — Number of items per page.
 *   totalElements — Total count of items matching the query across all pages.
 *   totalPages    — Calculated total number of pages available.
 ******************************************************************************/
package com.plus33.erp.common.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
