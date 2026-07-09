/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : PeriodLockResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockController
 * Related Service   : PeriodLockService, PeriodLockServiceImpl
 * Related Repository: PeriodLockRepository
 * Related Entity    : PeriodLock
 * Related DTO       : PeriodLockResponse
 * Related Mapper    : PeriodLockMapper
 * Related DB Table  : period_locks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PeriodLockController, PeriodLockService, PeriodLockServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PeriodLockResponse(
    Long id,
    Long companyId,
    LocalDate lockDate,
    String lockType,
    String lockedBy,
    LocalDateTime lockedAt,
    String reason
) {}
