/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryForecastService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryForecastController
 * Related Service   : TreasuryForecastService, TreasuryForecastServiceImpl
 * Related Repository: TreasuryForecastRepository
 * Related Entity    : TreasuryForecast
 * Related DTO       : CashPositionSnapshotResponse, TreasuryComplianceLogRequest, TreasuryComplianceLogResponse, TreasuryLimitRequest, TreasuryLimitResponse
 * Related Mapper    : TreasuryForecastMapper
 * Related DB Table  : treasury_forecasts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryForecastService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TreasuryForecastService {
    CashPositionSnapshotResponse takeCashPositionSnapshot(Long companyId, String snapshotType, String username);
    List<CashPositionSnapshotResponse> getCashPositionSnapshots(Long companyId);
    
    // Limits
    TreasuryLimitResponse createLimit(TreasuryLimitRequest request);
    List<TreasuryLimitResponse> getLimitsByCompany(Long companyId);
    void checkExposureLimits(Long companyId);

    // Logging
    TreasuryComplianceLogResponse logCompliance(TreasuryComplianceLogRequest request);
    List<TreasuryComplianceLogResponse> getComplianceLogs(Long companyId);
}
