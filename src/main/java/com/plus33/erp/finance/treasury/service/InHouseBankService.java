/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : InHouseBankService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InHouseBankController
 * Related Service   : InHouseBankService, InHouseBankServiceImpl
 * Related Repository: InHouseBankRepository
 * Related Entity    : InHouseBank
 * Related DTO       : InHouseBankAccountRequest, InHouseBankAccountResponse, IntercompanyLoanRequest, IntercompanyLoanResponse, InternalSettlementRequest
 * Related Mapper    : InHouseBankMapper
 * Related DB Table  : in_house_banks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code InHouseBankService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InHouseBankService {
    InHouseBankAccountResponse createInHouseAccount(InHouseBankAccountRequest request);
    InHouseBankAccountResponse getInHouseAccountById(Long id);
    InHouseBankAccountResponse getInHouseAccountBySubsidiary(Long subsidiaryCompanyId);

    IntercompanyLoanResponse createLoan(IntercompanyLoanRequest request);
    IntercompanyLoanResponse getLoanById(Long id);
    List<IntercompanyLoanResponse> getLoansByLender(Long lenderCompanyId);
    List<IntercompanyLoanResponse> getLoansByBorrower(Long borrowerCompanyId);
    void accrueIntercompanyLoanInterest(Long loanId);

    InternalSettlementResponse createInternalSettlement(InternalSettlementRequest request);
    List<InternalSettlementResponse> getInternalSettlements(Long companyId);
}
