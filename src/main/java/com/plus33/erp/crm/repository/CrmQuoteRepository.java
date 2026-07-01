package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CrmQuoteRepository extends JpaRepository<CrmQuote, Long> {
    Optional<CrmQuote> findByQuoteNumber(String quoteNumber);
    List<CrmQuote> findByCompanyId(Long companyId);
}
