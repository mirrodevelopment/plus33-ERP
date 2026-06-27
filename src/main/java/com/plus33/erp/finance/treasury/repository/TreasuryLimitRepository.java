package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreasuryLimitRepository extends JpaRepository<TreasuryLimit, Long> {
    List<TreasuryLimit> findByCompanyId(Long companyId);
    Optional<TreasuryLimit> findByCompanyIdAndLimitTypeAndCurrencyCodeAndCountryCodeAndTargetBankId(
            Long companyId, String limitType, String currencyCode, String countryCode, Long targetBankId);
}
