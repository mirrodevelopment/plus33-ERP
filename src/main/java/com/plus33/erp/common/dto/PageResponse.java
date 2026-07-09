/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * Package           : com.plus33.erp.common.dto
 * File              : PageResponse.java
 * Purpose           : Data Transfer Object for request/response in Common Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PageController
 * Related Service   : PageService, PageServiceImpl
 * Related Repository: PageRepository
 * Related Entity    : Page
 * Related DTO       : PageResponse
 * Related Mapper    : PageMapper
 * Related DB Table  : pages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PageController, PageService, PageServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Common Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
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
