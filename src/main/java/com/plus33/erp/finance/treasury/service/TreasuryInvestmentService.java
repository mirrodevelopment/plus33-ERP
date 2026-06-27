package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.InvestmentAndRiskDtos.*;

import java.util.List;

public interface TreasuryInvestmentService {
    TreasuryInvestmentResponse createInvestment(TreasuryInvestmentRequest request);
    TreasuryInvestmentResponse getInvestmentById(Long id);
    List<TreasuryInvestmentResponse> getInvestmentsByCompany(Long companyId);
    void accrueInterest(Long investmentId);
    void liquidateInvestment(Long investmentId);

    void executeDailyMaturities(String username);
    void executeDailyAccruals(String username);
    void executeFXRevaluations(Long companyId, String username);
}
