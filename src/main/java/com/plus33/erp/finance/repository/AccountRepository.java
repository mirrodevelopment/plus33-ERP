package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCompanyIdAndAccountCode(Long companyId, String accountCode);
    List<Account> findByCompanyId(Long companyId);
}
