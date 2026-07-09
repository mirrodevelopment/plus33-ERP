/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : LedgerBalanceSnapshot.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LedgerBalanceSnapshotController
 * Related Service   : LedgerBalanceSnapshotService, LedgerBalanceSnapshotServiceImpl
 * Related Repository: LedgerBalanceSnapshotRepository
 * Related Entity    : LedgerBalanceSnapshot
 * Related DTO       : N/A
 * Related Mapper    : LedgerBalanceSnapshotMapper
 * Related DB Table  : ledger_balance_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import com.plus33.erp.finance.reporting.entity.ExchangeRateType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code LedgerBalanceSnapshot}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record LedgerBalanceSnapshot(
    Map<Long, BigDecimal> debitBalances,
    Map<Long, BigDecimal> creditBalances,
    LocalDate startDate,
    LocalDate endDate,
    String currency,
    ExchangeRateType rateType
) {}
