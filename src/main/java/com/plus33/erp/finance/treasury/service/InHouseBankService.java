package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;

import java.util.List;

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
