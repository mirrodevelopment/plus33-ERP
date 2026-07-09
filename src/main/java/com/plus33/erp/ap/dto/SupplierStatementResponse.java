/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : SupplierStatementResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierStatementController
 * Related Service   : SupplierStatementService, SupplierStatementServiceImpl
 * Related Repository: SupplierStatementRepository
 * Related Entity    : SupplierStatement
 * Related DTO       : SupplierStatementResponse
 * Related Mapper    : SupplierStatementMapper
 * Related DB Table  : supplier_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierStatementController, SupplierStatementService, SupplierStatementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Ap Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierStatementResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ap.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Ap Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record SupplierStatementResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<SupplierStatementEntry> entries
) {}
