package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.InHouseBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InHouseBankAccountRepository extends JpaRepository<InHouseBankAccount, Long> {
    Optional<InHouseBankAccount> findByAccountNumber(String accountNumber);
    Optional<InHouseBankAccount> findBySubsidiaryCompanyId(Long subsidiaryCompanyId);
}
