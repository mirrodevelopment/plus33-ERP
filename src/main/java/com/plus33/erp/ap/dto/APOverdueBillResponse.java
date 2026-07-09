/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APOverdueBillResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APOverdueBillController
 * Related Service   : APOverdueBillService, APOverdueBillServiceImpl
 * Related Repository: APOverdueBillRepository
 * Related Entity    : APOverdueBill
 * Related DTO       : APOverdueBillResponse
 * Related Mapper    : APOverdueBillMapper
 * Related DB Table  : a_p_overdue_bills
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : APOverdueBillController, APOverdueBillService, APOverdueBillServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record APOverdueBillResponse(
        Long billId,
        String billNumber,
        Long supplierId,
        String supplierName,
        Long companyId,
        LocalDate billDate,
        LocalDate dueDate,
        Long daysOverdue,
        BigDecimal totalAmount,
        BigDecimal outstandingBalance,
        String status
) {}
