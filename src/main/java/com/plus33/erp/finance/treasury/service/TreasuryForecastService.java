package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;

import java.util.List;

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
