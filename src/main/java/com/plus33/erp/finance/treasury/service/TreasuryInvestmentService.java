/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryInvestmentService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryInvestmentController
 * Related Service   : TreasuryInvestmentService, TreasuryInvestmentServiceImpl
 * Related Repository: TreasuryInvestmentRepository
 * Related Entity    : TreasuryInvestment
 * Related DTO       : TreasuryInvestmentRequest, TreasuryInvestmentResponse
 * Related Mapper    : TreasuryInvestmentMapper
 * Related DB Table  : treasury_investments
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
 * <p><b>Class  :</b> {@code TreasuryInvestmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
